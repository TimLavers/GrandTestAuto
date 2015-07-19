package org.grandtestauto.test.functiontest;

import org.grandtestauto.GrandTestAuto;
import org.grandtestauto.assertion.Assert;
import org.grandtestauto.test.Helpers;
import org.grandtestauto.test.dataconstants.org.grandtestauto.Grandtestauto;

import java.io.File;

/**
 * See the GrandTestAuto test specification.
 *
 * @author Tim Lavers
 */
public class FlakyFunctionTestRunRepeatTimes extends FTBase {

    public boolean runTest() {
        GrandTestAuto gta = Helpers.setupForZip( new File( Grandtestauto.test110_zip ), true, true, true );
        Assert.azzertFalse(gta.runAllTests());//The function and load tests fail.

        //a110.a.functiontest.A and a110.a.loadtest.B should each have been run 5 times and 10 times respectively.
        for (int i=0; i<5; i++) {
            Assert.aequals("a110.a.functiontest.A", testsRun.get(i));
        }
        for (int i=5; i<15; i++) {
            Assert.aequals("a110.a.loadtest.B", testsRun.get(i));
        }
        return true;
    }
}
