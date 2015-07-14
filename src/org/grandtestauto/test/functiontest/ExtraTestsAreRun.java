package org.grandtestauto.test.functiontest;

import org.grandtestauto.test.*;
import org.grandtestauto.test.dataconstants.org.grandtestauto.*;

import java.io.*;

/**
 * See the GrandTestAuto test specification.
 */
public class ExtraTestsAreRun extends FTBase {
    public boolean runTest() {
        Helpers.setupForZip( new File( Grandtestauto.test46_zip ), true, true, true ).runAllTests();
        assert testsRun.contains( "a46.test.ATest" );
        return true;
    }
}
