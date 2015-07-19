package org.grandtestauto.test.functiontest;

import org.grandtestauto.test.dataconstants.org.grandtestauto.*;
import org.grandtestauto.test.*;

import java.io.*;
import java.text.*;
import java.util.regex.*;

/**
 * The execution time for each unit test package is logged.
 */
public class TimesForUnitTestPackagesAreLogged extends FTBase {
    public boolean runTest() throws Exception {
        //Don't run the unit tests as the names ATest etc will confuse the algorithm below.
        Helpers.setupForZip( new File( Grandtestauto.test99_zip ), true, true, true ).runAllTests();

        //Find a line of the form ">>>> Results of Unit Tests for a99: passed. <<<< 5.509s.
        Pattern autoLoadTestRecogniser = Pattern.compile( ">>>> Results of Unit Tests for a99: passed. <<<< ([0-9]*[\\.]{0,1}[0-9]*)s\\. ([0-9]*)M\\." );
        NumberFormat numberFormatter = NumberFormat.getNumberInstance();
        BufferedReader br = new BufferedReader( new FileReader( Helpers.defaultLogFile() ) );
        Number secondsRecorded = null;
        String line = br.readLine();
        while (line != null) {
            Matcher matcher = autoLoadTestRecogniser.matcher( line );
            if (matcher.matches()) {
                String timeStr = matcher.group( 1 );
                secondsRecorded = numberFormatter.parse( timeStr );
            }
            line = br.readLine();
        }
        br.close();
        assert secondsRecorded.floatValue() > 5.4;
        assert secondsRecorded.floatValue() < 5.9;
        return true;
    }
}