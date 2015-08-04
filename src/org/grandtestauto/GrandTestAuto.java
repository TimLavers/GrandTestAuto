/****************************************************************************
 *
 * Name: GrandTestAuto.java
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

import org.grandtestauto.settings.SettingsSpecification;
import org.grandtestauto.settings.SettingsSpecificationFromCommandLine;
import org.grandtestauto.settings.SettingsSpecificationFromFile;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * <code>GrandTestAuto</code> is used to run tests for a java project
 * consisting of class files contained in a directory and its subdirectories.
 *
 * @author Tim Lavers
 */
public class GrandTestAuto {

    private static final String VERSION = "GrandTestAuto 5.6";
    public static final String DO_NOT_TERMINATE = "org.grandtestauto.DoNotTerminateAfterTests";

    /**
     * The runtime parameters.
     */
    private SettingsSpecification settings;

    private ResultsLogger resultsLogger;

    /**
     * Information about the packages in the project.
     */
    private PackagesInfo<PackageInfo> productionPackagesInfo;

    private PackagesInfo<PackageInfo> testPackagesInfo;

    /**
     * Packages for which there is no unit tester class.
     */
    private List<String> nonUnitTestedPackageNames = new LinkedList<>();

    private DoPackageWork worker;

    /** Prints out the version. */
    static {
        System.err.println(VERSION);
    }

    /**
     * Create and run a <code>GrandTestAuto</code> from the arguments, which must be
     * either a single argument that names a file from which the settings are taken,
     * or a series of key=value pairs overriding the default settings and those
     * given in the System properties.
     *
     * @param args either single string, specifying the name of the settings file, or a series
     *             of key value pairs overriding the default settings and those in the System properties.
     */
    public static void main(String[] args) {
        boolean gtaResult = false;
        boolean hasRun = false;
        boolean doNotTerminateAfterTests = Boolean.getBoolean(DO_NOT_TERMINATE);//Issue #3.
        if (args.length == 1) {
            //Is it a file?
            File f = new File(args[0]);
            if (f.exists() && f.isFile()) {
                try {
                    SettingsSpecificationFromFile settings = new SettingsSpecificationFromFile(args[0]);
                    if (!settings.unknownKeys().isEmpty()) {
                        File correctedFile = correctedFileFor(f);
                        writeCorrectedSettings(correctedFile, settings);
                        p(Messages.message(Messages.OPK_SETTINGS_FILE_HAS_PROBLEMS, args[0]));
                        p(Messages.message(Messages.OPK_CORRECTED_SETTINGS_FILE_WRITTEN, correctedFile.getAbsolutePath()));
                        p(Messages.message(Messages.SK_GTA_CONTINUING_WITH_SETTINGS_THAT_COULD_BE_READ));
                    }
                    GrandTestAuto gta = new GrandTestAuto(settings);
                    hasRun = true;
                    gtaResult = gta.runAllTests();
                } catch (Throwable e) {
                    p(Messages.message(Messages.SK_GTA_COULD_NOT_RUN));
                    e.printStackTrace();
                }
            }
        }
        if (!hasRun) {
            try {
                SettingsSpecificationFromCommandLine settings = new SettingsSpecificationFromCommandLine(args);
                GrandTestAuto gta = new GrandTestAuto(settings);
                gtaResult = gta.runAllTests();
            } catch (Throwable e) {
                p(Messages.message(Messages.SK_GTA_COULD_NOT_RUN));
                e.printStackTrace();
            }
        }
        int status = gtaResult ? 0 : 1;
        p("GTA Exiting with status " + status + ".");
        if (!doNotTerminateAfterTests) {
            System.exit(status);
        }
    }

    private static void writeCorrectedSettings(File correctedFile, SettingsSpecificationFromFile settings) throws IOException {
        BufferedWriter bw = new BufferedWriter(new FileWriter(correctedFile));
        bw.write(settings.commentedPropertiesWithTheseValues());
        bw.close();
    }

    private static File correctedFileFor(File file) {
        File dir = file.getParentFile();
        String relName = file.getName();
        File corrected = new File(dir, "Corrected_" + relName);
        int i = 1;
        while (corrected.exists()) {
            corrected = new File(dir, "Corrected_" + i + "_" + relName);
            i++;
        }
        return corrected;
    }

    /**
     * Creates a <code>GrandTestAuto</code> using the given settings.
     */
    public GrandTestAuto(final SettingsSpecification settings) {
        this(settings, null);
    }

    GrandTestAuto(final SettingsSpecification settings, DoPackageWork dpw) {
        this(settings, dpw, new ResultsLogger(settings.resultsFileName(), settings.logToConsole()));
    }
    
    GrandTestAuto(final SettingsSpecification settingsSpecification, DoPackageWork dpw, ResultsLogger resultsLogger) {
        settings = settingsSpecification;
        this.resultsLogger = resultsLogger;
        productionPackagesInfo = new PackagesInfo<PackageInfo>(packageName -> settings.packageNameFilter().accept(packageName) && PackagesInfo.namesPackageThatMightNeedUnitTests(packageName), settings.productionClassesDir()) {
            public PackageInfo createClassFinder(String packageName, File baseDir) {
                return new PackageInfo(packageName, baseDir);
            }
        };
        testPackagesInfo = new PackagesInfo<PackageInfo>(packageName -> settings.packageNameFilter().accept(packageName), settings.testClassesDir()) {
            public PackageInfo createClassFinder(String packageName, File baseDir) {
                return new PackageInfo(packageName, baseDir);
            }
        };
        if (dpw == null) {
            DPWImpl dpwImpl = new RunsTestsInPackages();
            dpwImpl.setGTA(this);
            this.worker = dpwImpl;
        } else {
            worker = dpw;
        }
    }

    /**
     * Runs the <code>UnitTester</code> classes in all
     * packages in the classes directory, then the function tests,
     * then the load tests. Prints result details
     * to a results file (defined in the <code>Settings</code>)
     * and also returns a <code>boolean</code> as a summary of all tests.
     *
     * @return <code>true</code> if all tests passed (which also
     *         implies no exceptions were thrown).
     */
    public boolean runAllTests() {
        for (String message : worker.preliminaryMessages()) {
            resultsLogger().log(message, null);
        }
        boolean result = true;
        if (settings.runUnitTests()) {
            result = runUnitTests();
        }
        if (settings.runFunctionTests()) {
            if (continueWithTests(result)) {
                result &= runAutoLoadTests(true);
            } else {
                resultsLogger.log(Messages.message(Messages.SK_FAIL_FAST_SKIP_FUNCTION_TESTS), null);
            }
        }
        if (settings.runLoadTests()) {
            if (continueWithTests(result)) {
                result &= runAutoLoadTests(false);
            } else {
                resultsLogger.log(Messages.message(Messages.SK_FAIL_FAST_SKIP_LOAD_TESTS), null);
            }
        }
        //Record overall results.
        resultsLogger.log(worker.messageForFinalResult(result), null);
        resultsLogger.closeLogger();
        return result;
    }

    public ResultsLogger resultsLogger() {
        return resultsLogger;
    }

    private boolean runAutoLoadTests(final boolean areFunctionTests) {
        String key;
        if (worker.verbose() && !settings.lessVerboseLogging()) {
            key = areFunctionTests ? Messages.SK_RUNNING_FUNCTION_TESTS : Messages.SK_RUNNING_LOAD_TESTS;
            resultsLogger.log(Messages.message(key), null);
        }
        boolean result = true;
        PackagesInfo<AutoLoadTestFinder> testPackageFinder = createAutoLoadTestPackagesInfo(areFunctionTests);
        for (String testPackageName : testPackageFinder.testablePackageNames()) {
            //Shortcut these tests if a test has failed.
            if (!continueWithTests(result)) {
                key = areFunctionTests ? Messages.SK_FAIL_FAST_FUNCTION_TESTS : Messages.SK_FAIL_FAST_LOAD_TESTS;
                resultsLogger.log(Messages.message(key), null);
                break;
            }
            AutoLoadTestFinder cf = testPackageFinder.packageInfo(testPackageName);
            PackageResult packageResult = worker.runAutoLoadTestPackage(areFunctionTests, cf.classesInPackage(), testPackageName);
            //Report the result for this package.
            if (worker.verbose()) {
                String pf = Messages.passOrFail(packageResult.passed());
                key = areFunctionTests ? Messages.TPK_FUNCTION_TEST_PACK_RESULTS : Messages.TPK_LOAD_TEST_PACK_RESULTS;
                StringBuilder sb = new StringBuilder();
                sb.append(Messages.message(key, testPackageName, pf));
                sb.append(" ").append(ResultsLogger.formatTestExecutionTime(packageResult.timeTakenInMillis()));
                sb.append(". ").append(ResultsLogger.memoryUsed()).append(".");
                resultsLogger.log(sb.toString(), null);
            }
            result &= packageResult.passed();
        }
        //Report the overall result, if applicable.
        if (worker.verbose()) {
            if (areFunctionTests && settings.runFunctionTests() || !areFunctionTests && settings.runLoadTests()) {
                key = areFunctionTests ? Messages.OPK_OVERALL_FUNCTION_TEST_RESULT : Messages.OPK_OVERALL_LOAD_TEST_RESULT;
                reportOverallResult(result, key);
            }
        }
        return result;
    }

    private PackagesInfo<AutoLoadTestFinder> createAutoLoadTestPackagesInfo(final boolean areFunctionTests) {
        return new PackagesInfo<AutoLoadTestFinder>(packageName -> settings.packageNameFilter().accept(packageName) && (areFunctionTests ? PackagesInfo.namesFunctionTestPackage(packageName) : PackagesInfo.namesLoadTestPackage(packageName)), settings.testClassesDir()) {
            public AutoLoadTestFinder createClassFinder(String packageName, File baseDir) {
                return new AutoLoadTestFinder(packageName, baseDir);
            }
        };
    }

    boolean classIsToBeSkippedBecauseOfSettings(String className) {
        return settings.classNameFilter() != null && !settings.classNameFilter().accept(className);
    }

    private boolean runUnitTests() {
        if (worker.verbose() && !settings.lessVerboseLogging()) {
            resultsLogger.log(Messages.message(Messages.SK_RUNNING_UNIT_TESTS), null);
        }
        List<String> packageNames = productionPackagesInfo().testablePackageNames();
        boolean result = true;
        for (String packageName : packageNames) {
            //Shortcut these tests if a test has failed.
            if (!continueWithTests(result)) {
                resultsLogger.log(Messages.message(Messages.SK_FAIL_FAST_UNIT_TESTS), null);
                break;
            }
            result &= worker.doUnitTests(packageName).passed();
        }
        reportUntestedPackages();
        if (worker.verbose()) reportOverallResult(result, Messages.OPK_OVERALL_UNIT_TEST_RESULT);
        return result;
    }

    PackagesInfo<PackageInfo> productionPackagesInfo() {
        return productionPackagesInfo;
    }

    PackagesInfo<PackageInfo> testPackagesInfo() {
        return testPackagesInfo;
    }

    /**
     * The parameters for the running of the tests.
     */
    SettingsSpecification settings() {
        return settings;
    }

    private static void p(String msg) {
        System.out.println(msg);
    }

    /**
     * This method is for handling errors in the setup of the tests
     * that compromise the running of the tests. For example, if a
     * <code>UnitTester</code> could not be run.
     */
    void testingError(String message, Throwable t) {
        resultsLogger.log(message, t);
    }

    void reportUTResult(String packName, PackageResult result) {
        //@todo Move to results logger
        String pf = Messages.passOrFail(result.passed());
        StringBuilder toPrint = new StringBuilder();
        toPrint.append(Messages.message(Messages.TPK_UNIT_TEST_PACK_RESULTS, packName, pf));
        toPrint.append(" ").append(ResultsLogger.formatTestExecutionTime(result.timeTakenInMillis())).append(". ");
        toPrint.append(ResultsLogger.memoryUsed()).append(".");
        resultsLogger.log(toPrint.toString(), null);
    }

    private void reportUntestedPackages() {
        if (!nonUnitTestedPackageNames.isEmpty()) {
            StringBuilder buff = new StringBuilder();
            buff.append(Messages.message(Messages.CK_PACKAGE_NOT_UNIT_TESTED, nonUnitTestedPackageNames.size()));

            for (String nonUnitTestedPackageName : nonUnitTestedPackageNames) {
                buff.append(Messages.nl());
                buff.append(nonUnitTestedPackageName);
            }
            resultsLogger.log(buff.toString(), null);
        }
    }

    private void reportOverallResult(boolean result, String key) {
        String pf = Messages.passOrFail(result);
        resultsLogger.log(Messages.message(key, pf), null);
    }

    boolean continueWithTests(boolean resultSoFar) {
        return !settings.stopAtFirstFailure() || resultSoFar;
    }
}