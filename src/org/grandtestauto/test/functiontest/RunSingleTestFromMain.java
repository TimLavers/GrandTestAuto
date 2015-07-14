package org.grandtestauto.test.functiontest;

import org.grandtestauto.test.Helpers;
import org.grandtestauto.test.dataconstants.org.grandtestauto.Grandtestauto;

import java.io.File;

/** See the GrandTestAuto test specification. */
public class RunSingleTestFromMain extends FTBase {

    public boolean runTest() {
        //Setup the test package.
        Helpers.expandZipAndWriteSettingsFile(new File(Grandtestauto.test36_zip), true, true, true, null, false, true, Helpers.defaultLogFile().getPath());

        //Call main with -run option. As the logging is only to the console,
        //we need to run this in a separate JVM and read in stdout.
        String sout = Helpers.runGTAForSingleTestInSeparateJVMAndReadSystemErr(new File(Grandtestauto.test36_zip), true, true, true, "a36.functiontest.FT");
        assert sout.contains("a36.functiontest.FT passed,") : "got: '" + sout + "'";
        return true;
    }
}
