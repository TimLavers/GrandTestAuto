package org.grandtestauto.test.functiontest;

import org.grandtestauto.GrandTestAuto;
import org.grandtestauto.settings.*;
import org.grandtestauto.test.Helpers;
import org.grandtestauto.test.dataconstants.org.grandtestauto.Grandtestauto;

import java.io.*;
import java.util.Properties;

/** See the GrandTestAuto test specification. */
public class BooleanAbbreviation extends FTBase {
    public boolean runTest() throws IOException {
        init();

        //Write a settingsFile file.
        File settingsFile = new File(Helpers.tempDirectory(), "BadSettings.txt");
        String clsRoot = Helpers.classesDirClassic().getAbsolutePath().replace('\\', '/');
        Properties props = new Properties();
        props.setProperty(ClassesRoot.CLASSES_ROOT, clsRoot);
        props.setProperty(RunUnitTests.RUN_UNIT_TESTS, "t");
        props.setProperty(RunFunctionTests.RUN_FUNCTION_TESTS, "f");
        props.setProperty(RunLoadTests.RUN_LOAD_TESTS, "T");
        OutputStream os = new BufferedOutputStream(new FileOutputStream(settingsFile));
        props.store(os, getClass().getName());
        Helpers.expandZipTo(new File(Grandtestauto.test36_zip), Helpers.classesDirClassic());

        SettingsSpecification settings = new SettingsSpecificationFromFile(settingsFile.getAbsolutePath());
        new GrandTestAuto(settings).runAllTests();

        assert testsRun.size() == 2;
        assert testsRun.get(0).equals("a36.test.UnitTester");
        assert testsRun.get(1).equals("a36.loadtest.LT");

        return true;
    }
}
