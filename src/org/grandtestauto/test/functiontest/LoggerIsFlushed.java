package org.grandtestauto.test.functiontest;

import org.apache.commons.io.*;
import org.grandtestauto.*;
import org.grandtestauto.settings.ResultsFileName;
import org.grandtestauto.test.*;
import org.grandtestauto.test.dataconstants.org.grandtestauto.*;

import java.io.*;

/**
 * Proves that the results are being written to the log file immediately so that if one of the tests
 * causes the JVM to exit unexpectedly, the interim results are in the log file.
 */
public class LoggerIsFlushed extends FTBase {
    public boolean runTest() throws Exception {
        testsRun.clear();
        //Run package 81 which has 5 unit, 5 function
        //and 5 load tests (same as package 80)
        //but the function test a80.functiontest.D
        //has a System.exit( ) in it.
        Helpers.runGTAInSeparateJVMAndReadSystemErr(
                new File( Grandtestauto.test81_zip ),
                true, true, true, null );
        String loggedToFile = FileUtils.readFileToString( new File( ResultsFileName.DEFAULT_LOG_FILE_NAME ) );

        //Check that certain key phrases are in the log file.
//        assert loggedToFile.contains( "public a81.E()");
//        assert loggedToFile.contains( "public a81.C()");
        assert loggedToFile.contains( "a81.functiontest");
        assert loggedToFile.contains( "A " + Messages.passOrFail( true ));
        assert loggedToFile.contains( "B " + Messages.passOrFail( true ));
        assert loggedToFile.contains( "C " + Messages.passOrFail( true ));

        return true;
    }
}
