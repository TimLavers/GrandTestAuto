/****************************************************************************
 * The Wide Open License (WOL)
 * <p>
 * Permission to use, copy, modify, distribute and sell this software and its
 * documentation for any purpose is hereby granted without fee, provided that
 * the above copyright notice and this license appear in all source copies.
 * THIS SOFTWARE IS PROVIDED "AS IS" WITHOUT EXPRESS OR IMPLIED WARRANTY OF
 * ANY KIND. See http://www.dspguru.com/wol.htm for more information.
 *****************************************************************************/
package org.grandtestauto;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * For running the test methods in a particular test class.
 *
 * @author Tim Lavers
 */
class TestRunner {

    /**
     * The class that is the test.
     */
    private
    @NotNull
    Class<?> testClass;
    /**
     * The methods in the test class that are to be run.
     */
    private
    @NotNull
    SortedSet<Method> methodsToRun;
    private MethodInvoker invoker;

    private @Nullable Integer pauseOnExceptionForClass;

    TestRunner(@NotNull Class<?> testClass, @Nullable NameFilter testMethodNameFilter, @NotNull MethodInvoker invoker) {
        this.testClass = testClass;
        this.invoker = invoker;
        //Compare methodsToRun based on their name alone, as no two
        //test methods can have the same name.
        methodsToRun = new TreeSet<>((m1, m2) -> {
            return m1.getName().compareTo(m2.getName());
        });
        //Get all test methods.
        Method[] allDeclared = testClass.getDeclaredMethods();
        for (Method declared : allDeclared) {
            if (isTestMethod(declared)) {
                if (testMethodNameFilter == null || testMethodNameFilter.accept(declared.getName())) {
                    methodsToRun.add(declared);
                }
            }
        }
        pauseOnExceptionForClass = StaticUtils.pauseOnException(testClass);
    }

    boolean runTestMethods(@NotNull Coverage cut, @Nullable ClassAnalyser analyser) {
        boolean result = true;
        //Object to call the test methods on.
        Object testObj = null;
        try {
            testObj = testClass.newInstance();
        } catch (InstantiationException | IllegalAccessException ie) {
            //The test class is badly written.
            cut.testingError(Messages.message(Messages.OPK_COULD_NOT_CREATE_TEST_CLASS, testClass.getName()), ie);
            result = false;
        }
        //Empty params array for invoking test methods.
        for (Method testMethod : methodsToRun) {
            //Check that the method returns a boolean.
            if (!testMethod.getReturnType().equals(boolean.class)) {
                String msg = Messages.message(Messages.OPK_TEST_METHOD_DOES_NOT_RETURN_BOOLEAN, testMethod.getName());
                cut.resultsLogger().log(msg, null);
                result = false;
                //Record m as untested.
                cut.accountant().noTestFound(testMethod);
                //Don't try to run it.
                continue;
            }
            //Run the test method. If it is annotated as Flaky, run multiple times.
            int repeats = StaticUtils.flakyRepeats(testMethod);
            Integer pauseOnException = StaticUtils.pauseOnException(testMethod);
            boolean resultForMethod = false;
            for (int i = 1; !resultForMethod && i <= repeats; i++) {
                if (i > 1) {
                    cut.resultsLogger().log(Messages.message(Messages.TPK_RUNNING_TEST_AGAIN, testMethod.getName(), "" + i), null);
                }
                try {
                    resultForMethod = invoker.invoke(testMethod, testObj);
                    //Record the method as tested.
                    if (analyser != null) {
                        analyser.recordTestDone(testMethod.getName(), cut.accountant());
                    }
                    reportTestResult(cut, testMethod, resultForMethod, i, repeats);
                } catch (IllegalAccessException iae) {
                    //This is an error in the Unit  test.
                    cut.testingError(Messages.message(Messages.OPK_COULD_NOT_RUN_TEST_METHOD, testMethod.getName()), iae);
                    result = false;
                } catch (InvocationTargetException ita) {
                    //The test threw an exception. Print out the underlying exception (it might contain valuable information)
                    //and count this as the test failing.
                    ita.getCause().printStackTrace();
//                    result = false;
                    reportTestResult(cut, testMethod, false, i, repeats);
                    //If the class or the test method is annotated with PauseOnException, pause.
                    StaticUtils.pauseOnException(pauseOnExceptionForClass, cut.resultsLogger());
                    StaticUtils.pauseOnException(pauseOnException, cut.resultsLogger());
                    //If the class contains a method that is annotated as a cleanup method, call it.
                    for (Method m : testClass.getMethods()) {
                        Cleanup isCleanup = m.getAnnotation(Cleanup.class);
                        if (isCleanup != null) {
                            try {
                                m.invoke(testObj);
                            } catch (IllegalAccessException e) {
                                //Should not happen as we only searched through the public methods.
                                //Leave this here anyhow, in case we change the search procedure.
                                cut.testingError(Messages.message(Messages.SK_CLEANUP_NOT_PUBLIC), null);
                            } catch (IllegalArgumentException e) {
                                cut.testingError(Messages.message(Messages.SK_CLEANUP_NOT_NO_ARGS), null);
                            } catch (InvocationTargetException e) {
                                cut.testingError(Messages.message(Messages.OPK_CLEANUP_THREW_THROWABLE, e.getCause().getMessage()), e);
                            }
                        }
                    }
                }
                //Record that there is a test for the method (even if it failed).
                cut.accountant().testFound(testMethod);
            }
            result &= resultForMethod;
        }
        return result;
    }

    private void reportTestResult(Coverage cut, Method testMethod, boolean resultForMethod, int i, int limit) {
        //Report it if it is a positive result....
        if (resultForMethod) {
            cut.reportResult(testMethod, resultForMethod);
            return;
        }

        //....or if we're about to give up.
        if (i == limit) cut.reportResult(testMethod, resultForMethod);
    }

    private boolean isTestMethod(Method m) {
        boolean isTesty = m.getName().endsWith("Test");
        isTesty &= Modifier.isPublic(m.getModifiers());
        isTesty &= !Modifier.isAbstract(m.getModifiers());
        isTesty &= m.getReturnType().equals(boolean.class);
        isTesty &= m.getParameterTypes().length == 0;
        return isTesty;
    }

    public interface MethodInvoker {
        boolean invoke(Method method, Object testClassInstance) throws InvocationTargetException, IllegalAccessException;
    }
}