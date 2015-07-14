package org.grandtestauto.test.functiontest;

import org.grandtestauto.test.dataconstants.org.grandtestauto.*;
import org.grandtestauto.*;
import org.grandtestauto.test.*;

import java.io.*;

/**
 * See the GrandTestAuto test specification.
 */
class MissingUnitTestsRecorded extends FTBase { //Not applicable since v6, when UnitTesters are no longer needed.
    public boolean runTest() {
        GrandTestAuto gta = Helpers.setupForZip( new File( Grandtestauto.test45_zip ) );
        assert !gta.runAllTests();
        assert Helpers.overallUnitTestResultInLogFile();
        assert Helpers.overallFunctionTestResultInLogFile();
        assert Helpers.overallLoadTestResultInLogFile();
        String errorMessage = "The following packages are not unit-tested:" + Helpers.NL + "a45.a" + Helpers.NL + "a45.a.b";
        assert Helpers.logFileContents().contains( errorMessage );
        return true;
    }
}
