package org.grandtestauto.test.functiontest;

import org.grandtestauto.*;
import org.grandtestauto.test.*;

import java.io.*;

import org.grandtestauto.test.dataconstants.org.grandtestauto.*;

/**
 * See the GrandTestAuto test specification.
 *
 * @author Tim Lavers
 */
public class ClassNameAbbreviation extends FTBase {
    public boolean runTest() {
        //This first one is not really an abbreviation.
        GrandTestAuto gta = Helpers.setupForZip( new File( Grandtestauto.test100_zip ),
                true, false, false, null, null, "a100",false, true, null, null,
                null, null, "A", null, null, null );
        gta.runAllTests();
        assert testsRun.size() == 1: "Got: " + testsRun;
        assert testsRun.contains( "a100.test.ATest" );

        //A single class.
        testsRun.clear();
        gta = Helpers.setupForZip( new File( Grandtestauto.test100_zip ),
                true, false, false, null, null, "a100",false, true, null, null,
                null, null, "Ap", null, null, null );
        gta.runAllTests();
        assert testsRun.size() == 1: "Got: " + testsRun;
        assert testsRun.contains( "a100.test.AppleOrchardTest" );

        //A single class, again.
        testsRun.clear();
        gta = Helpers.setupForZip( new File( Grandtestauto.test100_zip ),
                true, false, false, null, null, "a100",false, true, null, null,
                null, null, "ApOrTe", null, null, null );
        gta.runAllTests();
        assert testsRun.size() == 1: "Got: " + testsRun;
        assert testsRun.contains( "a100.test.AppleOrchardTest" );

        //Various tests with a single function test class.
        testsRun.clear();
        gta = Helpers.setupForZip( new File( Grandtestauto.test100_zip ),
                false, true, false, null, null, "a100.f",false, true, null, null,
                null, null, "AB", null, null, null );
        gta.runAllTests();
        assert testsRun.size() == 1: "Got: " + testsRun;
        assert testsRun.contains( "a100.functiontest.ABC" );

        testsRun.clear();
        gta = Helpers.setupForZip( new File( Grandtestauto.test100_zip ),
                false, true, false, null, null, "a100.f",false, true, null, null,
                null, null, "AlB", null, null, null );
        gta.runAllTests();
        assert testsRun.size() == 1: "Got: " + testsRun;
        assert testsRun.contains( "a100.functiontest.AlwaysBrilliant" );

        //A final test.
        testsRun.clear();
        gta = Helpers.setupForZip( new File( Grandtestauto.test100_zip ),
                true, false, false, null, null, "a100",false, true, null, null,
                null, "ApOr", null, null, null, null );
        gta.runAllTests();
        assert testsRun.size() == 3: "Got: " + testsRun;
        assert testsRun.contains( "a100.test.ATest" );
        assert testsRun.contains( "a100.test.AppleTest" );
        assert testsRun.contains( "a100.test.AppleOrchardTest" );

        //An initial test.
        testsRun.clear();
        gta = Helpers.setupForZip( new File( Grandtestauto.test100_zip ),
                true, false, false, null, null, "a100",false, true, null, null,
                "AprTTe", null, null, null, null, null );
        gta.runAllTests();
        assert testsRun.size() == 4: "Got: " + testsRun;
        assert testsRun.contains( "a100.test.PearTreeTest" );
        assert testsRun.contains( "a100.test.ApricotTreeTest" );
        assert testsRun.contains( "a100.test.ExtraTest" );
        assert testsRun.contains( "a100.test.XtraTest" );

        //An initial and a final test.
        testsRun.clear();
        gta = Helpers.setupForZip( new File( Grandtestauto.test100_zip ),
                true, false, false, null, null, "a100",false, true, null, null,
                "AprTTe", "ET", null, null, null, null );
        gta.runAllTests();
        assert testsRun.size() == 2: "Got: " + testsRun;
        assert testsRun.contains( "a100.test.ApricotTreeTest" );
        assert testsRun.contains( "a100.test.ExtraTest" );
        return true;
    }
}