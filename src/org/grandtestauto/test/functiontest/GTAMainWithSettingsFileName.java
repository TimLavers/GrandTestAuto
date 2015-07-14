package org.grandtestauto.test.functiontest;

import org.grandtestauto.Messages;
import org.grandtestauto.settings.SettingsSpecificationFromFile;
import org.grandtestauto.test.Helpers;
import org.grandtestauto.test.dataconstants.org.grandtestauto.Grandtestauto;

import java.io.File;

/** See the GrandTestAuto test specification. */
public class GTAMainWithSettingsFileName extends MainTestBase {

    public boolean runTest() {
        //Setup the test package.
        String settingsFileName = Helpers.expandZipAndWriteSettingsFile(new File(Grandtestauto.test36_zip), true, true, true, null, false, true, Helpers.defaultLogFile().getPath());

        //Call GTA.main() with settings file name as argument.
        runMainWithoutExit(settingsFileName);

        //Check that the logged results are as expected.
        String logFileContents = Helpers.logFileContents();
        assert logFileContents.contains(new SettingsSpecificationFromFile().summary());
        assert logFileContents.contains(Messages.message(Messages.TPK_UNIT_TEST_PACK_RESULTS, "a36", Messages.passOrFail(true)));
        assert logFileContents.contains(Messages.message(Messages.TPK_FUNCTION_TEST_PACK_RESULTS, "a36.functiontest", Messages.passOrFail(true)));
        assert logFileContents.contains(Messages.message(Messages.TPK_LOAD_TEST_PACK_RESULTS, "a36.loadtest", Messages.passOrFail(true)));
        assert logFileContents.contains(Messages.message(Messages.OPK_OVERALL_GTA_RESULT, Messages.passOrFail(true)));
        return true;
    }
}
