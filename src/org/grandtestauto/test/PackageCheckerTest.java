/****************************************************************************
 *
 * Name: CoverageUnitTester.java
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
package org.grandtestauto.test;

import org.grandtestauto.Messages;
import org.grandtestauto.PackageChecker;
import org.grandtestauto.test.dataconstants.org.grandtestauto.Grandtestauto;

import java.io.File;

/**
 * @author Tim Lavers
 */
public class PackageCheckerTest {

    public boolean mainTest() throws Exception {
        //See test spec Quick Coverage Report Positive
        String settingsFileName = Helpers.expandZipAndWriteSettingsFile(new File(Grandtestauto.test33_zip), false, false, false, null, null, "a33", false, false, null, false, null, null, null, null, null, null);
        Helpers.startRecordingSout();
        String[] args = {settingsFileName};
        PackageChecker.main(args);
        String printedOut = Helpers.stopRecordingSout();
        assert printedOut.contains(Messages.message(Messages.OPK_UNIT_TEST_COVERAGE_COMPLETE, "a33"));
        return true;
    }

    public boolean reportNegativeTest() throws Exception {
        //See test spec Quick Coverage Report Negative
        String settingsFileName = Helpers.expandZipAndWriteSettingsFile(new File(Grandtestauto.test27_zip), false, false, false, null, null, "a27", false, false, null, false, null, null, null, null, null, null);
        Helpers.startRecordingSout();
        String[] args = {settingsFileName};
        PackageChecker.main(args);
        String printedOut = Helpers.stopRecordingSout();
        assert printedOut.contains(Messages.message(Messages.OPK_PROBLEMS_WITH_UNIT_TEST_COVERAGE, "a27"));
        assert printedOut.contains( "protected void a27.A.a");//The name of the untested method.
        return true;
    }

    public boolean lenientPackageNameTest() throws Exception {
        //See test spec Quick Coverage Lenient About Package Name
        String settingsFileName = Helpers.expandZipAndWriteSettingsFile(new File(Grandtestauto.test33_zip), false, false, false, null, null, "a33.test", false, false, null, false, null, null, null, null, null, null);
        Helpers.startRecordingSout();
        String[] args = {settingsFileName};
        PackageChecker.main(args);
        String printedOut = Helpers.stopRecordingSout();
        assert printedOut.contains(Messages.message(Messages.OPK_UNIT_TEST_COVERAGE_COMPLETE, "a33"));
        return true;
    }

    public boolean reportMissingUnitTesterTest() throws Exception {
        //See test spec Quick Coverage Reports Missing UnitTester
        //In package a45.a.b.test, there is no UnitTester class.
        String settingsFileName = Helpers.expandZipAndWriteSettingsFile(new File(Grandtestauto.test45_zip), false, false, false, null, null, "a45.a.b", false, false, null, false, null, null, null, null, null, null);
        Helpers.startRecordingSout();
        String[] args = {settingsFileName};
        PackageChecker.main(args);
        String printedOut = Helpers.stopRecordingSout();
        assert printedOut.contains(Messages.message(Messages.OPK_COULD_NOT_CREATE_UNIT_TESTER, "a45.a.b"));
        return true;
    }

    public boolean constructorTest() throws Exception {
        PackageChecker checker = Helpers.setupPackageChecker(new File(Grandtestauto.test33_zip), "a33");
        assert checker.runTests();

        return true;
    }

    public boolean runTestsTest() throws Exception {
        PackageChecker checker = Helpers.setupPackageChecker(new File(Grandtestauto.test32_zip), "a32");
        assert !checker.runTests();
        return true;
    }
}