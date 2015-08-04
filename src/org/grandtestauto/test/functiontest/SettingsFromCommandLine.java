package org.grandtestauto.test.functiontest;

import org.grandtestauto.GrandTestAuto;
import org.grandtestauto.assertion.Assert;
import org.grandtestauto.settings.ClassesRoot;
import org.grandtestauto.settings.ProductionClassesRoot;
import org.grandtestauto.settings.RunLoadTests;
import org.grandtestauto.settings.RunUnitTests;
import org.grandtestauto.test.Helpers;
import org.grandtestauto.test.dataconstants.org.grandtestauto.Grandtestauto;

import java.io.File;

import static org.grandtestauto.settings.test.SettingsSpecificationFromCommandLineTest.appendSetting;
import static org.grandtestauto.test.Helpers.*;

/**
 * Tests issue #2
 *
 * @author Tim Lavers
 */
public class SettingsFromCommandLine extends FTBase {
    public boolean runTest() throws Exception {
        //Set the non-termination flag, so that we can safely call GrandTestAuto.main().
        System.getProperties().setProperty(GrandTestAuto.DO_NOT_TERMINATE, "true");
        init();
        //Expand a zip that has the test and production classes in different directories
        //and has two unit tests, one function test and one load test.
        Helpers.expandZipTo(new File( Grandtestauto.test130_zip ),temp2Directory() );

        //Build a command line that specifies the test and production classes directories
        //and that only function tests should be run.
        StringBuilder sb = new StringBuilder();
        appendSetting(sb, ProductionClassesRoot.PROD_ROOT, productionClassesRoot().getAbsolutePath().replace('\\', '/'));
        appendSetting(sb, ClassesRoot.CLASSES_ROOT, testClassesRoot().getAbsolutePath().replace('\\', '/'));
        appendSetting(sb, RunUnitTests.RUN_UNIT_TESTS, "f");
        appendSetting(sb, RunLoadTests.RUN_LOAD_TESTS, "f");

        //Invoke GTA with the command line.
        GrandTestAuto.main(sb.toString().split(" "));
        Assert.azzert(testsRun.contains("a130.a.functiontest.TheFunctionTest"));
        Assert.aequals(1, testsRun.size());

        //Reset the termination property.
        System.getProperties().remove(GrandTestAuto.DO_NOT_TERMINATE);
        return true;
    }
}