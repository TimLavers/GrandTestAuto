package org.grandtestauto.test.functiontest;

import org.grandtestauto.GrandTestAuto;
import org.grandtestauto.test.Helpers;
import org.grandtestauto.test.dataconstants.org.grandtestauto.Grandtestauto;

import java.io.File;

/**
 * @author Tim Lavers
 */
public class TestClassesSeparateFromProductionClasses extends FTBase {
    public boolean runTest() {
        //In this zip the test and production classes are in separate directories.
        GrandTestAuto gta = Helpers.setupForZip( new File( Grandtestauto.test130_zip ));
        gta.runAllTests();

        return true;
    }
}