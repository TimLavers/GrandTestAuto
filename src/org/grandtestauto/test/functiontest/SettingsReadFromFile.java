package org.grandtestauto.test.functiontest;

import org.grandtestauto.Messages;
import org.grandtestauto.settings.ClassesRoot;
import org.grandtestauto.settings.SettingsSpecificationFromFile;
import org.grandtestauto.test.Helpers;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Properties;

/**
 * See the GrandTestAuto test specification.
 */
public class SettingsReadFromFile extends FTBase {
    public boolean runTest() throws Exception {
        init();
        //Write a dodgy settings file.
        File dodgySettings = new File( Helpers.tempDirectory(), "BadSettings.txt" );
        String clsRoot = Helpers.tempDirectory().getAbsolutePath().replace( '\\', '/' );
        Properties props = new Properties();
        props.setProperty( ClassesRoot.CLASSES_ROOT, clsRoot );
        props.setProperty( "NOT_REALLY_A_KEY", "true" );
        OutputStream os = new BufferedOutputStream( new FileOutputStream( dodgySettings ) );
        props.store( os, "SettingsReadFromFile" );

        String sout = Helpers.runGTAInSeparateJVM( dodgySettings.getAbsolutePath() )[0];
        assert sout.contains( Messages.message( Messages.OPK_SETTINGS_FILE_HAS_PROBLEMS,dodgySettings.getAbsolutePath() ));
        File correctedSettingsFile = new File( Helpers.tempDirectory(), "Corrected_BadSettings.txt" );
        assert sout.contains( Messages.message( Messages.OPK_CORRECTED_SETTINGS_FILE_WRITTEN, correctedSettingsFile.getAbsolutePath() ));
        assert sout.contains( Messages.message( Messages.SK_GTA_CONTINUING_WITH_SETTINGS_THAT_COULD_BE_READ ) );
        SettingsSpecificationFromFile corrected = new SettingsSpecificationFromFile( correctedSettingsFile.getAbsolutePath() );
        assert corrected.unknownKeys().isEmpty();

        return true;
    }
}
