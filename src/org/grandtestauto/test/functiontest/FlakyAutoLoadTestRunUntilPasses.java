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
public class FlakyAutoLoadTestRunUntilPasses extends FTBase {

    public boolean runTest() {
        GrandTestAuto gta = Helpers.setupForZip( new File( Grandtestauto.test112_zip ), true, true, true );
        Assert.azzert(gta.runAllTests());//The function test passes after 3 attempts.

        //a112.a.functiontest.A is annotated with as flaky with repeats=5 but succeeds on the third attempt.
        for (int i=0; i<3; i++) {
            Assert.aequals("a112.a.functiontest.A", testsRun.get(i));
        }
        Assert.aequals(3, testsRun.size());
        return true;
    }
}
