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
public class FlakyAutoLoadTestRepeatedEvenIfExceptionsThrown extends FTBase {

    public boolean runTest() {
        GrandTestAuto gta = Helpers.setupForZip( new File( Grandtestauto.test116_zip ), true, true, true );
        Assert.azzertFalse(gta.runAllTests());//The function test fails with an NPE 12 times.

        for (int i=0; i<12; i++) {
            Assert.aequals("a116.a.functiontest.A", testsRun.get(i));
        }
        Assert.aequals(12, testsRun.size());
        return true;
    }
}
