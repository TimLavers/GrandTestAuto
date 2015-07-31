package org.grandtestauto.test.functiontest;

import org.grandtestauto.GrandTestAuto;
import org.grandtestauto.assertion.Assert;
import org.grandtestauto.test.Helpers;
import org.grandtestauto.test.dataconstants.org.grandtestauto.Grandtestauto;

import java.io.File;

/**
 * Tests issue #1
 *
 * @author Tim Lavers
 */
public class TestClassesSeparateFromProductionClasses extends FTBase {
    public boolean runTest() throws Exception {
        //In this zip the test and production classes are in separate directories.
        GrandTestAuto gta = Helpers.setupForZipWithSeparateSourceAndTestClassRoots( new File( Grandtestauto.test130_zip ));
        Assert.azzert(gta.runAllTests());

        Assert.azzert(testsRun.contains("a130.a.test.ATest"));
        Assert.azzert(testsRun.contains("a130.a.test.TheExtraUnitTest" ));
        Assert.azzert(testsRun.contains("a130.a.functiontest.TheFunctionTest" ));
        Assert.azzert(testsRun.contains("a130.a.loadtest.TheLoadTest" ));
        Assert.aequals(4, testsRun.size());

        return true;
    }
}