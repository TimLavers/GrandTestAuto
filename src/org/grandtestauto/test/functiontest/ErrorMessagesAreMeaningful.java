package org.grandtestauto.test.functiontest;

import org.grandtestauto.test.Helpers;
import org.grandtestauto.test.dataconstants.org.grandtestauto.Grandtestauto;

import java.io.File;

/**
 * See the GrandTestAuto test specification.
 */
public class ErrorMessagesAreMeaningful extends FTBase {
    public boolean runTest() {
        //Setup a settings file that points to a non-existent classes directory.
        Helpers.cleanTempDirectory();
        File classesDir = new File( Helpers.tempDirectory(), "classesDir/non/existent" );
        assert !classesDir.exists();
        String settingsFileName = Helpers.writeSettingsFile( classesDir, true, true, true, null, null, null, true, true, null, false, null, null, null, null, null, null );
        //Invoke GTA in a separate JVM and read in the standard error stream.
        String serr = Helpers.runGTAInSeparateJVM( settingsFileName )[1];
        //Check that the error stream indicates that the classes directory setting is wrong.
        String expected = "The specified classes directory " + classesDir.getAbsolutePath() + " does not exist!";
        assert serr.contains( expected ) : "Got: " + serr;

        //Now a test for when the unit tester class for a unit test package is not public.
        Helpers.cleanTempDirectory();
        String sout = Helpers.runGTAInSeparateJVMAndReadSystemErr(new File( Grandtestauto.test106_zip ), true, true, true, null );
        //Check that the output indicates that the unit tester class was not found.
        expected = "The UnitTester for a106 is not public.";
        assert sout.contains( expected ) : "Got: " + sout;

        return true;
    }
}