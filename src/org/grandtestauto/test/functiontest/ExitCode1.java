package org.grandtestauto.test.functiontest;

import org.grandtestauto.test.Helpers;
import org.grandtestauto.test.dataconstants.org.grandtestauto.Grandtestauto;

import java.io.File;

/**
 * Check that System.exit(1) is called when tests fail.
 *
 * @author Tim Lavers
 */
public class ExitCode1 extends FTBase {
    public boolean runTest() throws Exception {
        testsRun.clear();
        //Run package 95.
        Integer exitValue = Helpers.runGTAInSeparateJVMAndReadExitValue(
                new File(Grandtestauto.test95_zip),
                true, true, true, null);
        assert exitValue == 1;

        return true;
    }
}
