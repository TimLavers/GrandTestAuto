package org.grandtestauto.test.functiontest;

import org.grandtestauto.*;
import org.grandtestauto.test.*;
import org.grandtestauto.test.dataconstants.org.grandtestauto.*;

import java.io.*;

/**
 * See the GrandTestAuto test specification.
 */
public class NamingOfUnitTestPackages extends FTBase {
    public boolean runTest() {
        GrandTestAuto gta = Helpers.setupForZip( new File( Grandtestauto.test39_zip ), true, false, false );//Unit tests only.
        gta.runAllTests();
        assert testsRun.size() == 1;
        assert testsRun.get( 0 ).equals( "a39.test.UnitTester" );
        return true;
    }
}
