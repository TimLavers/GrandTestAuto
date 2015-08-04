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
import org.grandtestauto.assertion.Assert;
import org.grandtestauto.settings.ClassesRoot;
import org.grandtestauto.settings.ProductionClassesRoot;
import org.grandtestauto.settings.SinglePackage;
import org.grandtestauto.test.dataconstants.org.grandtestauto.Grandtestauto;

import java.io.File;
import java.util.Properties;

/**
 * @author Tim Lavers
 */
public class PackageCheckerTest {

    //Issue #4.
    public boolean testClassesSeparateFromProductionClassesTest() throws Exception {
        Helpers.cleanTempDirectory();
        Helpers.expandZipTo(new File(Grandtestauto.test130_zip), Helpers.temp2Directory());
        Properties properties = new Properties();
        Helpers.addAsProperty(properties, ProductionClassesRoot.PROD_ROOT, Helpers.productionClassesRoot());
        Helpers.addAsProperty(properties, ClassesRoot.CLASSES_ROOT, Helpers.testClassesRoot());
        properties.setProperty(SinglePackage.SINGLE_PACKAGE, "a130.a");
        File propertiesFile = Helpers.writeSettingsFile(properties);

        String[] args = {propertiesFile.getAbsolutePath()};
        Helpers.startRecordingSout();
        PackageChecker.main(args);
        String printedOut = Helpers.stopRecordingSout();
        Assert.azzert(printedOut.contains(Messages.message(Messages.OPK_UNIT_TEST_COVERAGE_COMPLETE, "a130.a")), "Got: '" + printedOut + "'");
        return true;
    }

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