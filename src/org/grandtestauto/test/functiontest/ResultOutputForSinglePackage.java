package org.grandtestauto.test.functiontest;

import org.apache.commons.io.FileUtils;
import org.grandtestauto.GrandTestAuto;
import org.grandtestauto.Messages;
import org.grandtestauto.test.Helpers;
import org.grandtestauto.test.dataconstants.org.grandtestauto.Grandtestauto;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;

/** Check that the overall test result message is given when we only set one package to run. */
public class ResultOutputForSinglePackage extends FTBase {

    public boolean runTest() throws IOException {
        ExceptionHandling.classNameToErrorKeyToThrow = new HashMap<String, String>();
        testsRun = new LinkedList<String>();
        File defaultLogFile = Helpers.defaultLogFile();
        GrandTestAuto gta = Helpers.setupForZip(new File(Grandtestauto.test44_zip), false, true, false, null, null, "a44.c.functiontest", true, true, defaultLogFile.getPath(), null, null, null, null, null, null, null);
        assert gta.runAllTests();
        String actualResults = FileUtils.readFileToString(defaultLogFile);
        String passed = Messages.message(Messages.SK_PASSED);
        String expectedContains = Messages.message(Messages.OPK_OVERALL_GTA_RESULT, passed);
        assert actualResults.contains(expectedContains) : "Got: '" + actualResults + "'";
        return true;
    }
}
