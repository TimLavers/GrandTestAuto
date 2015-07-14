package org.grandtestauto.test.functiontest;

import org.grandtestauto.test.*;
import org.grandtestauto.test.dataconstants.org.grandtestauto.*;

import java.io.*;

/**
 * Attempt to track down a bug.
 */
public class ExtraTestFails extends FTBase {
    public boolean runTest() {
        Helpers.setupForZip( new File( Grandtestauto.test98_zip ), true, true, true ).runAllTests();
        String logFileContents = Helpers.logFileContents();
        assert logFileContents.contains( ">>>> Results of Unit Tests for a98: failed. <<<<");
        return true;
    }
}