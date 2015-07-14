package org.grandtestauto.test.functiontest;

import org.grandtestauto.Messages;
import org.grandtestauto.settings.SettingsSpecificationFromFile;
import org.grandtestauto.test.Helpers;
import org.grandtestauto.test.dataconstants.org.grandtestauto.Grandtestauto;

import java.io.File;

/**
 * GTA-1
 */
class AbbreviatedSettings extends MainTestBase {

    public boolean runTest() throws Exception {
        //Setup the test package.
        Helpers.expandZipAndWriteSettingsFile(new File(Grandtestauto.test36_zip), true, true, true, null, false, true, Helpers.defaultLogFile().getPath());

        //Invoke GTA with the single argument "-settings|classes_dir|ft|sp=a36.f".
        runMainWithoutExit("-settings|" + Helpers.tempDirectory().getCanonicalPath() + "|ft|sp=a36.f");

        //Read in the log file contents and check that they show that the single package a36.functiontest was run.
        String logFileContents = Helpers.logFileContents();
        assert logFileContents.contains(new SettingsSpecificationFromFile().summary());
        assert logFileContents.contains(Messages.message(Messages.TPK_UNIT_TEST_PACK_RESULTS, "a36", Messages.passOrFail(true)));
        assert logFileContents.contains(Messages.message(Messages.TPK_FUNCTION_TEST_PACK_RESULTS, "a36.functiontest", Messages.passOrFail(true)));
        assert logFileContents.contains(Messages.message(Messages.OPK_OVERALL_GTA_RESULT, Messages.passOrFail(true)));
        return true;
    }
}
