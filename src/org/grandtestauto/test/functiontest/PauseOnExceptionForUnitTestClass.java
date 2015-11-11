package org.grandtestauto.test.functiontest;

import org.grandtestauto.GrandTestAuto;
import org.grandtestauto.Messages;
import org.grandtestauto.assertion.Assert;
import org.grandtestauto.settings.SettingsSpecificationFromFile;
import org.grandtestauto.test.Helpers;
import org.grandtestauto.test.dataconstants.org.grandtestauto.Grandtestauto;
import org.grandtestauto.test.tools.Stopwatch;

import java.io.File;

/**
 * Issue #5.
 *
 * @author Tim Lavers
 */
public class PauseOnExceptionForUnitTestClass extends FTBase {

    public boolean runTest() throws Exception {
        //Create a GrandTestAuto to run just the unit test configured in tests131.zip.
        File settingsFile = Helpers.expandZipWithSeparateSourceAndClassRootsAndWriteSettingsFileForUnitTestsOnly(new File(Grandtestauto.test131_zip));
        GrandTestAuto gta = new GrandTestAuto(new SettingsSpecificationFromFile(settingsFile.getAbsolutePath()));

        //Record the times for the tests to run.
        Stopwatch timer = new Stopwatch();
        timer.start();
        Assert.azzertFalse(gta.runAllTests());//Sanity check.
        timer.stop();

        //Confirm that just the unit test ran.
        Assert.azzert(testsRun.contains("a131.a.test.ATest"));
        Assert.aequals(1, testsRun.size());

        //Check that the time take was approximately 5 seconds.
        Long timeTaken = timer.times().get(0);
        Assert.azzert( timeTaken > 4900, "Time was: " +timeTaken);
        Assert.azzert( timeTaken < 5500, "Time was: " +timeTaken);

        //Check that the pause was logged.
        String pauseMessage = Messages.message(Messages.OPK_PAUSING_TEST_THAT_THREW_ERROR, "5");
        Assert.azzert(Helpers.logFileContents().contains(pauseMessage));

        return true;
    }
}
