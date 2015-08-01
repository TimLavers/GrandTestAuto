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

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.InvocationTargetException;

/**
 * A <code>CoverageUnitTester</code> runs the unit tests
 * for all of the concrete accessible classes in its parent package.
 * It checks that each testable class <code>X</code> has a unit test
 * called <code>XTest</code>,
 * and that each method of a class is tested, including
 * inherited methods.
 * <p/>
 * For example, consider a package containing three
 * public classes <code>A</code>, <code>B</code> and <code>C</code>.
 * Suppose that <code>A</code> is an abstract class
 * with testable methods <code>a()</code>, <code>b()</code>, <code>c()</code>.
 * Suppose that <code>B</code> and <code>C</code> both extend
 * <code>A</code> and that <code>B</code> re-defines <code>b()</code>
 * and <code>C</code> re-defines <code>c()</code> as well as
 * defining a new method <code>e()</code>.
 * To ensure coverage, the test classes for this hierarchy
 * must be called <code>BTest</code> and <code>CTest</code>.
 * <code>BTest</code> must contain a test for <code>b()</code>.
 * <code>CTest</code> must contain tests for <code>c()</code>
 * and <code>e()</code>. Either <code>BTest</code>
 * or <code>CTest</code> may contain a test for <code>a()</code>,
 * but one of these classes must contain such a test.
 *
 * @author Tim Lavers
 */
public class CoverageUnitTester extends Coverage implements UnitTesterIF {

    /**
     * Controls the tests.
     */
    private
    @NotNull
    GrandTestAuto gta;

    /**
     * For recording results.
     */
    private
    @NotNull
    ResultsLogger resultsLogger;

    /**
     * Creates a <code>CoverageUnitTester</code> that will run unit tests
     * for classes in the parent package to the package in which this
     * is defined.
     */
    public CoverageUnitTester(@NotNull GrandTestAuto gta) {
        super(gta.testPackagesInfo().classesRoot());
        this.gta = gta;
        resultsLogger = gta.resultsLogger();
    }

    @NotNull
    ResultsLogger resultsLogger() {
        return resultsLogger;
    }

    @Override
    boolean continueWithTests(boolean resultSoFar) {
        return gta.continueWithTests(resultSoFar);
    }

    @Override
    boolean classIsToBeSkippedBecauseOfSettings(String className) {
        return gta.classIsToBeSkippedBecauseOfSettings(className);
    }

    @Override
    String testPackageName() {
        return getClass().getPackage().getName();
    }

    @Override
    PackageInfo productionPackageInfo() {
        return gta.productionPackagesInfo().packageInfo(packageTested());
    }

    @Override
    boolean doTestsForClass(Class testClass, @Nullable ClassAnalyser analyser) throws InvocationTargetException {
        NameFilter testMethodNameFilter = gta.settings().methodNameFilter();
        TestRunner runner = new TestRunner(testClass, testMethodNameFilter, (method, testClassInstance) -> (Boolean) method.invoke(testClassInstance));
        //The TestRunner runs the tests and the accountant ticks them off.
        return runner.runTestMethods(this, analyser);
    }

    @Override
    void testingError(String str, Exception e) {
        gta.testingError(str, e);
    }
}