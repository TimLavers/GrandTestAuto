package org.grandtestauto.settings.test;

//import org.grandtestauto.settings.SingleLineSettings;
import org.grandtestauto.test.Helpers;

        import java.io.*;

/**
 * @author Tim Lavers
 */
public class SingleLineSettingsTest {
    private File tempDir;

    public boolean constructorTest() {
//        SingleLineSettings settings = new SingleLineSettings(line("ft"));
//        Assert.azzert(settings.runFunctionTests());
//        Assert.azzertFalse(settings.runUnitTests());
//        Assert.azzertFalse(settings.runLoadTests());
//
//        Assert.aequals(tempDir, settings.classesDir());
//        Assert.azzertFalse(settings.stopAtFirstFailure());
//        Assert.azzert(settings.logToConsole());
//        Assert.azzert(settings.logToConsole());
        return true;
    }

//    private String line(String ... components) {
//        StringJoiner joiner = new StringJoiner(SingleLineSettings.DELIMITER, SingleLineSettings.INDICATOR,"");
//        for (String component : components) joiner.add(component);
//        return joiner.toString();
//    }

    private void init() {
        Helpers.cleanTempDirectory();
        tempDir = Helpers.tempDirectory();
    }
}