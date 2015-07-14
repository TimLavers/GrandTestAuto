/****************************************************************************
 * Copyright 2012 Timothy Gordon Lavers (Australia)
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
package org.grandtestauto.loganalysis.test;

import org.grandtestauto.assertion.Assert;
import org.grandtestauto.loganalysis.LogFileAnalyser;
import org.grandtestauto.test.dataconstants.org.grandtestauto.loganalysis.Loganalysis;

import java.io.File;

/**
 * @author Tim Lavers
 */
public class LogFileAnalyserTest {
    private LogFileAnalyser lfa;

    public boolean handle0sTest() throws Exception {
        init(Loganalysis.LT2_txt);
        Assert.aequals(1, lfa.functionAndLoadTestResults().size());
        Assert.azzert(lfa.functionAndLoadTestResults().get("rippledown.bmd.loadtest.ExportPatientsTest"));
        return true;
    }

    public boolean unitTestPackageResultsTest() throws Exception {
        init(Loganalysis.Log0_txt);
        Assert.azzert(lfa.unitTestPackageResults().isEmpty());

        init(Loganalysis.Log1_txt);
        Assert.aequals(1, lfa.unitTestPackageResults().size());
        Assert.azzert(lfa.unitTestPackageResults().get("org.grandtestauto"));

        init(Loganalysis.Log2_txt);
        Assert.aequals(5, lfa.unitTestPackageResults().size());
        Assert.azzert(lfa.unitTestPackageResults().get("fruit.apple"));
        Assert.azzert(lfa.unitTestPackageResults().get("fruit.apple.core"));
        Assert.azzert(lfa.unitTestPackageResults().get("fruit.apple.seed"));
        Assert.azzert(lfa.unitTestPackageResults().get("fruit.apple.skin"));
        Assert.azzert(lfa.unitTestPackageResults().get("fruit.apple.validate"));

        init(Loganalysis.Log3_txt);
        Assert.aequals(5, lfa.unitTestPackageResults().size());
        Assert.azzert(lfa.unitTestPackageResults().get("fruit.apple"));
        Assert.azzertFalse(lfa.unitTestPackageResults().get("fruit.apple.core"));
        Assert.azzert(lfa.unitTestPackageResults().get("fruit.apple.seed"));
        Assert.azzertFalse(lfa.unitTestPackageResults().get("fruit.apple.skin"));
        Assert.azzert(lfa.unitTestPackageResults().get("fruit.apple.validate"));

        return true;
    }

    public boolean functionAndLoadTestResultsTest() throws Exception {
        init(Loganalysis.Log0_txt);
        Assert.azzert(lfa.functionAndLoadTestResults().isEmpty());

        init(Loganalysis.FT0_log);
        Assert.aequals(13, lfa.functionAndLoadTestResults().size());
        Assert.azzert(lfa.functionAndLoadTestResults().get("rippledown.admin.functiontest.AddOnlineFormsProject"));
        Assert.azzert(lfa.functionAndLoadTestResults().get("rippledown.admin.functiontest.DefaultUsersFunctionTest"));
        Assert.azzert(lfa.functionAndLoadTestResults().get("rippledown.admin.functiontest.EditServerSetting"));
        Assert.azzert(lfa.functionAndLoadTestResults().get("rippledown.admin.functiontest.ExportOnlineProject"));
        Assert.azzert(lfa.functionAndLoadTestResults().get("rippledown.admin.functiontest.ExportOperationOverwrites"));
        Assert.azzert(lfa.functionAndLoadTestResults().get("rippledown.admin.functiontest.ExportProject"));
        Assert.azzert(lfa.functionAndLoadTestResults().get("rippledown.admin.functiontest.ImportPrimaryAttributesForAdmini"));
        Assert.azzert(lfa.functionAndLoadTestResults().get("rippledown.admin.functiontest.PerformHouseKeepingOnDemand"));
        Assert.azzert(lfa.functionAndLoadTestResults().get("rippledown.admin.functiontest.PersistEmailComment"));
        Assert.azzert(lfa.functionAndLoadTestResults().get("rippledown.admin.functiontest.ProjectCanBeExportedWhileEdited"));
        Assert.azzert(lfa.functionAndLoadTestResults().get("rippledown.admin.functiontest.PurgeEmailComment"));
        Assert.azzert(lfa.functionAndLoadTestResults().get("rippledown.admin.functiontest.RetryEmailComment"));
        Assert.azzert(lfa.functionAndLoadTestResults().get("rippledown.admin.functiontest.UpdateDefaultGroupAndUserFunctionTest"));

        init(Loganalysis.LT0_txt);
        Assert.aequals(3, lfa.functionAndLoadTestResults().size());
        Assert.azzert(lfa.functionAndLoadTestResults().get("rippledown.attribute.loadtest.TextCondenserNormalisationLoadTest"));
        Assert.azzert(lfa.functionAndLoadTestResults().get("rippledown.bmd.loadtest.ExportPatientsTest"));
        Assert.azzertFalse(lfa.functionAndLoadTestResults().get("rippledown.bmd.loadtest.OpenBigDatabaseTest"));

        return true;
    }

    private void  init(String logFileName) throws Exception {
        lfa = new LogFileAnalyser(new File(logFileName));
    }
}
