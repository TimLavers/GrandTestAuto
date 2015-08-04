/****************************************************************************
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

import org.grandtestauto.settings.SettingsSpecification;
import org.grandtestauto.settings.SettingsSpecificationFromFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * A <code>PackageChecker</code> checks unit test coverage for a package, without actually running any tests.
 *
 * @author Tim Lavers
 */
public class PackageChecker extends Coverage implements UnitTesterIF {

    public static void main(String[] args) throws IOException {
        SettingsSpecification settings = new SettingsSpecificationFromFile(args[0]);
        String packageName = settings.singlePackageName();
        if (packageName == null) {
            System.out.println(Messages.message( Messages.SK_SINGLE_PACKAGE_NOT_SPECIFIED));
            return;
        }
        PackageChecker checker = new PackageChecker(settings.productionClassesDir(), packageName);
        checker.runTests();
    }

    private
    @NotNull
    PackageInfo pi;

    private
    @NotNull
    ResultsLogger resultsLogger;

    private
    @NotNull
    String nameOfPackageToCheck;

    public PackageChecker(@NotNull File classesRoot, @NotNull String packageName) {
        super(classesRoot);
        resultsLogger = new ResultsLogger(null, true);
        nameOfPackageToCheck = packageName;
        if (nameOfPackageToCheck.endsWith(".test")) {
            nameOfPackageToCheck = nameOfPackageToCheck.substring(0, nameOfPackageToCheck.length() - 5);
        }
        File classesDirForPackageBeingChecked = new File(classesRoot,  nameOfPackageToCheck.replace('.',File.separatorChar));
        pi = new PackageInfo(nameOfPackageToCheck, classesDirForPackageBeingChecked );
    }

    @NotNull
    ResultsLogger resultsLogger() {
        return resultsLogger;
    }

    @Override
    boolean continueWithTests(boolean resultSoFar) {
        return true;
    }

    @Override
    boolean classIsToBeSkippedBecauseOfSettings(String className) {
        return false;
    }

    @Override
    String testPackageName() {
        return nameOfPackageToCheck + ".test";
    }

    @Override
    PackageInfo productionPackageInfo() {
        return pi;
    }

    @Override
    boolean doTestsForClass(Class testClass, @Nullable ClassAnalyser analyser) throws InvocationTargetException {
        TestRunner runner = new TestRunner(testClass, null, (method, testClassInstance) -> true);
        //The TestRunner runs the tests and the accountant ticks them off.
        return runner.runTestMethods(this, analyser);
    }

    @Override
    void testingError(String str, Exception e) {
        resultsLogger.log(str, e);
    }

    @Override
    void reportResult(Method testMethod, boolean result) {
        //Don't print anything.
    }

    @Override
    public boolean runTests() {
        //First check that there is a UnitTester.
//        String utClassName = testPackageName()  + ".UnitTester";
//        try {
//            Class.forName(utClassName);
//        } catch (ClassNotFoundException e) {
//            resultsLogger.log(Messages.message(Messages.OPK_COULD_NOT_CREATE_UNIT_TESTER, nameOfPackageToCheck),null);
//            return false;
//        }
        boolean result =  super.runTests();
        if (result) {
            resultsLogger.log(Messages.message( Messages.OPK_UNIT_TEST_COVERAGE_COMPLETE, nameOfPackageToCheck), null);
        } else {
            resultsLogger.log(Messages.message(Messages.OPK_PROBLEMS_WITH_UNIT_TEST_COVERAGE, nameOfPackageToCheck ), null);
        }
        return result;
    }
}