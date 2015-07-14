package org.grandtestauto.test.functiontest;

import org.grandtestauto.GrandTestAuto;
import org.grandtestauto.assertion.Assert;
import org.grandtestauto.test.Helpers;
import org.grandtestauto.test.dataconstants.org.grandtestauto.Grandtestauto;

import java.io.File;

/**
 * See the GrandTestAuto test specification.
 */
public class UseCoverageUnitTesterForMissingUnitTester extends FTBase {
    public boolean runTest() {
        GrandTestAuto gta = Helpers.setupForZip( new File( Grandtestauto.test111_zip ), true, false, false );
        gta.runAllTests();
        Assert.aequals(1, testsRun.size());
        Assert.aequals("a111.test.ATest", testsRun.get(0));
        return true;
    }
}
