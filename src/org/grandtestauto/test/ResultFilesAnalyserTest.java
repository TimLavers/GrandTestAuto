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
package org.grandtestauto.test;

import org.grandtestauto.Messages;
import org.grandtestauto.ResultFilesAnalyser;
import org.grandtestauto.assertion.Assert;
import org.grandtestauto.test.dataconstants.org.grandtestauto.Grandtestauto;
import org.grandtestauto.test.dataconstants.org.grandtestauto.loganalysis.dir6.Dir6;
import org.grandtestauto.test.dataconstants.org.grandtestauto.loganalysis.dir7.Dir7;
import org.grandtestauto.test.dataconstants.org.grandtestauto.loganalysis.dir8.Dir8;

import java.io.File;

import static org.grandtestauto.test.Helpers.NL;

/**
 * @author Tim Lavers
 */
public class ResultFilesAnalyserTest {

    public boolean mainTest() throws Exception {
        init(Grandtestauto.test36_zip);
        String[] args = {Helpers.tempDirectory().getAbsolutePath(), Dir6.PATH};
        Helpers.startRecordingSout();
        ResultFilesAnalyser.main(args);
        String got = Helpers.stopRecordingSout();
        String expected = "Analysing results files and classes..." + NL +
                "Classes directory: " + Helpers.tempDirectory().getAbsolutePath() + NL +
                "Log file directory: " + new File(Dir6.PATH).getAbsolutePath() + "." + NL +
                "Log files:" + NL +
                "TestLog1.txt" + NL +
                "TestLog2.log" + NL +
                "TestLog3.txt" + NL +
                "All tests present and correct." + NL;
        Assert.aequals(expected, got);
        return true;
    }

    public boolean mainWithUnitTestFailureTest() throws Exception {
        init(Grandtestauto.test36_zip);
        String[] args = {Helpers.tempDirectory().getAbsolutePath(), Dir7.PATH};
        Helpers.startRecordingSout();
        ResultFilesAnalyser.main(args);
        String got = Helpers.stopRecordingSout();
        String expected = "Analysing results files and classes..." + NL +
                "Classes directory: " + Helpers.tempDirectory().getAbsolutePath() + NL +
                "Log file directory: " + new File(Dir7.PATH).getAbsolutePath() + "." + NL +
                "Log files:" + NL +
                "TestLog1.txt" + NL +
                "TestLog2.log" + NL +
                "TestLog3.txt" + NL +
                "Unit tests failed for a36." + NL +
                "Testing problems, as shown." + NL;
        Assert.aequals(expected, got);
        return true;
    }

    public boolean mainWithFunctionTestFailureTest() throws Exception {
        init(Grandtestauto.test36_zip);
        String[] args = {Helpers.tempDirectory().getAbsolutePath(), Dir8.PATH};
        Helpers.startRecordingSout();
        ResultFilesAnalyser.main(args);
        String got = Helpers.stopRecordingSout();
        String failedPackageMessage = Messages.message(Messages.OPK_ONE_OR_MORE_TESTS_FAILED_IN, "a36.functiontest");
        String expected = "Analysing results files and classes..." + NL +
                "Classes directory: " + Helpers.tempDirectory().getAbsolutePath() + NL +
                "Log file directory: " + new File(Dir8.PATH).getAbsolutePath() + "." + NL +
                "Log files:" + NL +
                "TestLog1.txt" + NL +
                "TestLog2.log" + NL +
                "TestLog3.txt" + NL +
                failedPackageMessage + NL +
                "Testing problems, as shown." + NL;
        Assert.aequals(expected, got);
        return true;
    }

    private void init(String archiveName) {
        Helpers.cleanTempDirectory();
        File zip = new File(archiveName);
        Helpers.expandZipTo(zip, Helpers.tempDirectory());
    }
}