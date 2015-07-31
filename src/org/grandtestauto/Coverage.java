/****************************************************************************
 *
 * Name: Coverage.java
 *
 * Synopsis: See javadoc class comments.
 *
 * Description: See javadoc class comments.
 *
 * Copyright 2002 Timothy Gordon Lavers (Australia)
 *
 *                          The Wide Open License (WOL)
 *
 * Permission to use, copy, modify, distribute and sell this software and its
 * documentation for any purpose is hereby granted without fee, provided that
 * the above copyright notice and this license appear in all source copies.
 * THIS SOFTWARE IS PROVIDED "AS IS" WITHOUT EXPRESS OR IMPLIED WARRANTY OF
 * ANY KIND. See http://www.dspguru.com/wol.htm for more information.
 *
 *****************************************************************************/
package org.grandtestauto;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Base class for testing tools that ensure unit test coverage at the package level.
 *
 * @author Tim Lavers
 */
public abstract class Coverage {

    /**
     * The name of the package that this tests.
     */
    private @NotNull String packageTested;

    /**
     * Untested classes.
     */
    private @NotNull Set<String> untestedClasses = new HashSet<>();

    /**
     * Records which methods are tested.
     */
    private @NotNull Accountant accountant = new Accountant();

    /**
     * Details of classes to be tested.
     */
    private @Nullable PackageInfo pi;

    private @NotNull File testClassesRoot;

    /**
     * Finds all test classes, so that ones that don't correspond to a testable class can be run.
     */
    private @NotNull TestFinder testFinder;

    /**
     * Classes that have already been printed out in the results.
     */
    private @NotNull Set<Class> classesAlreadyPrinted = new HashSet<>();

    /**
     * Creates a <code>CoverageUnitTester</code> that will run unit tests
     * for classes in the parent package to the package in which this
     * is defined.
     */
    public Coverage(@NotNull File testClassesRoot) {
        this.testClassesRoot = testClassesRoot;
    }

    /**
     * Runs the unit tests for all of the accessible concrete classes
     * in the package being tested, plus any other test classes.
     * Records classes and methods not tested.
     */
    public boolean runTests() {
        init();
        boolean result = true;
        if (pi == null) {
            //There was nothing to test.
            return true;
        }
        String testPackageName = packageTested + ".test.";
        //Run the minimal set of tests: those corresponding to public concrete classes in the package under test.
        //Record the names of the actual test classes that are run, so that we don't run them again later.
        Set<String> namesOfTestClassesRunSoFar = new HashSet<>();

        for (String className : pi.classNameToTestability().keySet()) {
            //Curtail tests if we've already failed and failfast is on.
            if (!continueWithTests(result)) break;

            //Skip this test if we're only running selected tests in a package.
            if (classIsToBeSkippedBecauseOfSettings( className )) continue;

            String fullName = packageTested + '.' + className;
            try {
                Class toTest = Class.forName( fullName );
                //Add all method and constructor tests to the accountant as missing.
                //These will be accounted for as the tests are run.
                ClassAnalyser analyser = new ClassAnalyser( toTest );
                for (Method testableMethod : analyser.testableMethods()) {
                    accountant.noTestFound( testableMethod );
                }
                for (Constructor constructor : analyser.testableConstructors()) {
                    accountant.recordAsNeedingTest( constructor );
                }
                String testClassName = testPackageName + className + "Test";
                namesOfTestClassesRunSoFar.add( testClassName );
                result &= runTests( testClassName, className, analyser, pi.classNameToTestability().get( className ) );
            } catch (Exception e) {
                //Most unlikely as the class has already been instantiated once.
                testingError( Messages.message( Messages.SK_BUG_IN_GTA ), e );
            }
        }
        //Now run the extra tests, if there are any.
        for (String className : testFinder.classesInPackage()) {
            //Curtail tests if we've already failed and failfast is on.
            if (!continueWithTests( result )) break;

            //Skip this test if we're only running selected tests in a package.
            if (classIsToBeSkippedBecauseOfSettings( className )) continue;

            try {
                String testClassName = testPackageName + className;
                if (!namesOfTestClassesRunSoFar.contains( testClassName )) {
                    result &= runTests( testClassName, null, null, Testability.NO_TEST_REQUIRED );
                }
            } catch (Exception e) {
                //Most unlikely as the class has already been instantiated once.
                testingError( Messages.message( Messages.SK_BUG_IN_GTA ), e );
            }
        }

        if (!accountant.untestedMethods().isEmpty()) {
            result = false;
        }
        if (!accountant.untestedConstructors().isEmpty()) {
            result = false;
        }
        reportUntested();
        return result;
    }

    abstract boolean continueWithTests(boolean resultSoFar);
    abstract boolean classIsToBeSkippedBecauseOfSettings(String className);
    abstract String testPackageName();
    abstract PackageInfo productionPackageInfo();
    abstract boolean doTestsForClass(Class klass, @Nullable ClassAnalyser analyser) throws InvocationTargetException ;
    @NotNull
    abstract ResultsLogger resultsLogger();
    abstract void testingError(String str, Exception e);

    /**
     * Reports the results of running the given test method.
     */
    void reportResult(Method testMethod, boolean result) {
        StringBuilder message = new StringBuilder();
        Class<?> declaringClass = testMethod.getDeclaringClass();
        if (!classesAlreadyPrinted().contains(declaringClass)) {
            String nameToLog = declaringClass.getName().substring(packageTested().length() + 6);
            resultsLogger().log(nameToLog, null);
            classesAlreadyPrinted().add(declaringClass);
        }
        message.append("    ");
        message.append(testMethod.getName());
        message.append(" ");
        message.append(Messages.passOrFail(result));
        resultsLogger().log(message.toString(), null);
    }

    String packageTested() {
        return packageTested;
    }

    @NotNull Accountant accountant() {
        return accountant;
    }

    Set<Class> classesAlreadyPrinted() {
        return classesAlreadyPrinted;
    }

    private boolean runTests( @NotNull String testClassName, @Nullable String nameOfClassUnderTestOrNull, @Nullable ClassAnalyser analyser, @NotNull Testability testability ) throws InvocationTargetException {
        boolean result = true;
        try {
            Class testClass = Class.forName( testClassName );
            result &= doTestsForClass(testClass,analyser);
        } catch (ClassNotFoundException cnfe) {
            if (testability.equals( Testability.TEST_REQUIRED )) {
                //There is no test class defined. Record this and cause failure.
                untestedClasses.add( nameOfClassUnderTestOrNull );
                result = false;
            }
        }
        return result;
    }

    private void init() {
        packageTested = testPackageName().substring(0, testPackageName().length() - 5);
        pi = productionPackageInfo();
        if (pi != null) {
            pi.seek();
        }
        File testClassesDir = new File(testClassesRoot, packageTested.replace( '.', File.separatorChar ) );
        testClassesDir = new File( testClassesDir, "test" );
        testFinder = new TestFinder( testPackageName(), testClassesDir );
        testFinder.seek();
    }

    private void reportUntested() {
        //Are there any untested classes to report?
        reportUntested( untestedClasses, Messages.TPK_CK_CLASSES_NOT_TESTED, Object::toString);
        //Are there any untested constructors?
        reportUntested( accountant.untestedConstructors(), Messages.CK_CONSTRUCTOR_TESTS_EXPECTED, Object::toString);
        //Are there any untested methods?
        reportUntested( accountant.untestedMethods(), Messages.TPK_CK_METHODS_NOT_TESTED, Object::toString);
    }

    private interface ItemFormatter {
        String format(Object o);
    }

    private void reportUntested( Set untested, String key, ItemFormatter formatter ) {
        if (!untested.isEmpty()) {
            StringBuilder msg = new StringBuilder();
            msg.append( Messages.message( key, packageTested, untested.size() ) );
            msg.append( Messages.nl() );
            for (Iterator itor = untested.iterator(); itor.hasNext();) {
                msg.append( formatter.format( itor.next() ) );
                if (itor.hasNext()) {
                    msg.append( Messages.nl() );
                }
            }
            msg.append( "." );
            resultsLogger().log( msg.toString(), null );
        }
    }
}