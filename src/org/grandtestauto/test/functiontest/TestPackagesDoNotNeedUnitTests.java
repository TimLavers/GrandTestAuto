package org.grandtestauto.test.functiontest;

import org.grandtestauto.*;
import org.grandtestauto.test.*;
import org.grandtestauto.test.dataconstants.org.grandtestauto.*;

import java.io.*;

/**
 * See the GrandTestAuto test specification.
 */
public class TestPackagesDoNotNeedUnitTests extends FTBase {
    public boolean runTest() {
        GrandTestAuto gta = Helpers.setupForZip( new File( Grandtestauto.test70_zip ) );
        assert gta.runAllTests();
        return true;
    }
}
