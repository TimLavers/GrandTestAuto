package org.grandtestauto.settings.test;

import org.apache.commons.io.FileUtils;
import org.grandtestauto.Messages;
import org.grandtestauto.Name;
import org.grandtestauto.NameFilter;
import org.grandtestauto.assertion.Assert;
import org.grandtestauto.settings.*;
import org.grandtestauto.test.Helpers;
import org.grandtestauto.test.dataconstants.org.grandtestauto.Grandtestauto;

import java.io.*;
import java.util.*;

/**
 * @author Tim Lavers
 */
public class SettingsSpecificationFromFileTest {

    private File tempDir = Helpers.tempDirectory();
    private SettingsSpecificationFromFile settings;

    public boolean productionClassesRootTest() throws IOException {
        Helpers.cleanTempDirectory();
        File productionClassesRoot = new File(Helpers.tempDirectory(), "pcr");
        Assert.azzert(productionClassesRoot.mkdirs(), "Could not create dir: '" + productionClassesRoot + "'");
        File testClassesRoot = new File(Helpers.tempDirectory(), "tcr");
        Assert.azzert(testClassesRoot.mkdirs(), "Could not create dir: '" + testClassesRoot + "'");
        Properties props = new Properties();
        props.put( ProductionClassesRoot.PROD_ROOT, productionClassesRoot.getAbsolutePath() );
        props.put(ClassesRoot.CLASSES_ROOT, testClassesRoot.getAbsolutePath());
        init(props, false);
        Assert.azzert(settings.unknownKeys().isEmpty());
        Assert.aequals(productionClassesRoot, settings.productionClassesDir());
        Assert.aequals(testClassesRoot, settings.testClassesDir());

        return true;
    }

    public boolean singlePackageNameTest() throws Exception {
        String initialPackageName = null;
        String finalPackageName = null;
        String singlePackageName = null;
        init( true, true, true, true, true, null, initialPackageName, finalPackageName, singlePackageName, null );
        assert settings.singlePackageName() == null;

        initialPackageName = "b";
        finalPackageName = null;
        singlePackageName = null;
        init( true, true, true, true, true, null, initialPackageName, finalPackageName, singlePackageName, null );
        assert settings.singlePackageName() == null;

        initialPackageName = "b";
        finalPackageName = "y";
        singlePackageName = "m";
        init( true, true, true, true, true, null, initialPackageName, finalPackageName, singlePackageName, null );
        assert settings.singlePackageName().equals("m");
        return true;
    }

    public boolean camelCaseClassNamesTest() throws Exception {
        String singlePackageName = "a100";
        String singleClassName = "P";

        Helpers.cleanTempDirectory();
        Helpers.expandZipTo( new File( Grandtestauto.test100_zip ), Helpers.tempDirectory() );
        initWithoutcleanup( true, true, true, true, true, null, null, null, singlePackageName, null, null, null, singleClassName, null, null, null );
        NameFilter nameFilter = settings.classNameFilter();
        assert nameFilter.equals( new NameFilter( NameFilter.Type.CLASS, null, null, "PearTreeTest" ) );

        singleClassName = null;
        String initialClassName = "Ap";
        String finalClassName = null;
        initWithoutcleanup( true, true, true, true, true, null, null, null, singlePackageName, null, initialClassName, finalClassName, singleClassName, null, null, null );
        nameFilter = settings.classNameFilter();
        assert nameFilter.equals( new NameFilter( NameFilter.Type.CLASS, "AppleOrchardTest", null, null ) );

        initialClassName = "AprTrT";
        finalClassName = "PT";
        initWithoutcleanup( true, true, true, true, true, null, null, null, singlePackageName, null, initialClassName, finalClassName, singleClassName, null, null, null );
        nameFilter = settings.classNameFilter();
        assert nameFilter.equals( new NameFilter( NameFilter.Type.CLASS, "ApricotTreeTest", "PearTreeTest", null ) );

        initialClassName = "E";
        finalClassName = "X";
        initWithoutcleanup( true, true, true, true, true, null, null, null, singlePackageName, null, initialClassName, finalClassName, singleClassName, null, null, null );
        nameFilter = settings.classNameFilter();
        assert nameFilter.equals(new NameFilter(NameFilter.Type.CLASS, "ExtraTest", "XtraTest", null));
        return true;
    }

    public boolean constructorTest() throws IOException {
        //Just check that a sensible properties example can be produced.
        settings = new SettingsSpecificationFromFile();
        String value = settings.commentedPropertiesWithTheseValues();
        checkPropertiesString( value );
        return true;
    }

    public boolean constructor_String_Test() throws Exception {
        //Pretty cursory test as this is tested elsewhere.
        File settingsFile = new File(Grandtestauto.Settings1_txt);
        SettingsSpecificationFromFile settings = new SettingsSpecificationFromFile(settingsFile.getAbsolutePath());
        Assert.aequals(settings.singlePackageName(), "org.grandtestauto");
        Assert.azzert(settings.runUnitTests());
        Assert.azzertFalse(settings.runFunctionTests());
        Assert.azzertFalse(settings.runLoadTests());
        return true;
    }

    public boolean constructor_File_Test() throws Exception {
        //Pretty cursory test as this is tested elsewhere.
        Helpers.cleanTempDirectory();
        Helpers.expandZipTo(new File(Grandtestauto.test100_zip), Helpers.tempDirectory());
        File classesDir = Helpers.tempDirectory();
        SettingsSpecificationFromFile settings2 = new SettingsSpecificationFromFile(classesDir);
        Assert.aequals(settings2.productionClassesDir(), classesDir);
        return true;
    }

    public boolean subDirectoriesAsNamesTest() {
        Helpers.cleanTempDirectory();
        Helpers.expandZipTo( new File( Grandtestauto.test53_zip ), Helpers.tempDirectory() );
        TreeSet<Name> expected = new TreeSet<Name>();
        expected.add( Name.dotSeparatedName( "a53" ) );
        expected.add( Name.dotSeparatedName( "a53.a0" ) );
        expected.add( Name.dotSeparatedName( "a53.a1" ) );
        expected.add( Name.dotSeparatedName( "a53.a2" ) );
        expected.add( Name.dotSeparatedName( "a53.a3" ) );
        expected.add( Name.dotSeparatedName( "a53.a4" ) );
        expected.add( Name.dotSeparatedName( "a53.a5" ) );
        expected.add( Name.dotSeparatedName( "a53.test" ) );
        expected.add( Name.dotSeparatedName( "a53.functiontest" ) );
        expected.add( Name.dotSeparatedName( "a53.loadtest" ) );
        expected.add( Name.dotSeparatedName( "a53.a0.test" ) );
        expected.add( Name.dotSeparatedName( "a53.a0.functiontest" ) );
        expected.add( Name.dotSeparatedName( "a53.a0.loadtest" ) );
        expected.add( Name.dotSeparatedName( "a53.a1.test" ) );
        expected.add( Name.dotSeparatedName( "a53.a1.functiontest" ) );
        expected.add( Name.dotSeparatedName( "a53.a1.loadtest" ) );
        expected.add( Name.dotSeparatedName( "a53.a2.test" ) );
        expected.add( Name.dotSeparatedName( "a53.a2.functiontest" ) );
        expected.add( Name.dotSeparatedName( "a53.a2.loadtest" ) );
        expected.add( Name.dotSeparatedName( "a53.a3.test" ) );
        expected.add( Name.dotSeparatedName( "a53.a3.functiontest" ) );
        expected.add( Name.dotSeparatedName( "a53.a3.loadtest" ) );
        expected.add( Name.dotSeparatedName( "a53.a4.test" ) );
        expected.add( Name.dotSeparatedName( "a53.a4.functiontest" ) );
        expected.add( Name.dotSeparatedName( "a53.a4.loadtest" ) );
        expected.add( Name.dotSeparatedName( "a53.a5.test" ) );
        expected.add( Name.dotSeparatedName( "a53.a5.functiontest" ) );
        expected.add( Name.dotSeparatedName( "a53.a5.loadtest" ) );
        Set<Name> names = SettingsSpecificationFromFile.subDirectoriesAsNames( Helpers.tempDirectory() );
        assert names.equals( expected ) : "Got: " + names;
        return true;
    }

    public boolean summaryTest() {
        settings = new SettingsSpecificationFromFile();
        String summary = settings.summary();
        StringBuilder expected = new StringBuilder();
        addKeyValues( expected, Messages.SK_WILL_RUN_UNIT_TESTS, Messages.SK_WILL_RUN_FUNCTION_TESTS, Messages.SK_WILL_RUN_LOAD_TESTS );
        assert expected.toString().equals( summary );

        init( true, true, false, true, true );
        summary = settings.summary();
        expected = new StringBuilder();
        addKeyValues( expected, Messages.SK_WILL_RUN_UNIT_TESTS, Messages.SK_WILL_RUN_FUNCTION_TESTS, Messages.SK_WILL_NOT_RUN_LOAD_TESTS );
        assert expected.toString().equals( summary );

        init( true, false, false, true, true );
        summary = settings.summary();
        expected = new StringBuilder();
        addKeyValues( expected, Messages.SK_WILL_RUN_UNIT_TESTS, Messages.SK_WILL_NOT_RUN_FUNCTION_TESTS, Messages.SK_WILL_NOT_RUN_LOAD_TESTS );
        assert expected.toString().equals( summary );

        init( false, false, false, true, true );
        summary = settings.summary();
        expected = new StringBuilder();
        addKeyValues( expected, Messages.SK_WILL_NOT_RUN_UNIT_TESTS, Messages.SK_WILL_NOT_RUN_FUNCTION_TESTS, Messages.SK_WILL_NOT_RUN_LOAD_TESTS );
        assert expected.toString().equals( summary );

        init( true, true, true, false, false, null, "ip", null, null, null, null, null, null, null, null, null );
        summary = settings.summary();
        expected = new StringBuilder();
        addKeyValues( expected, Messages.SK_WILL_RUN_UNIT_TESTS, Messages.SK_WILL_RUN_FUNCTION_TESTS, Messages.SK_WILL_RUN_LOAD_TESTS );
        expected.append( new NameFilter( NameFilter.Type.PACKAGE, "ip", null, null ).loggingMessage() );
        expected.append( SettingsSpecificationFromFile.NL );
        assert expected.toString().equals( summary ) : "Expected: '" + expected + "'\n but got: '" + summary + "'";

        init( true, true, true, false, false, null, "ip", null, "sp", null, "ic", "fc", null, null, null, null );
        summary = settings.summary();
        expected = new StringBuilder();
        addKeyValues( expected, Messages.SK_WILL_RUN_UNIT_TESTS, Messages.SK_WILL_RUN_FUNCTION_TESTS, Messages.SK_WILL_RUN_LOAD_TESTS );
        expected.append( new NameFilter( NameFilter.Type.PACKAGE, "ip", null, "sp" ).loggingMessage() );
        expected.append( SettingsSpecificationFromFile.NL );
        expected.append( new NameFilter( NameFilter.Type.CLASS, "ic", "fc", null ).loggingMessage() );
        expected.append( SettingsSpecificationFromFile.NL );
        assert expected.toString().equals( summary ) : "Expected: '" + expected + "'\n but got: '" + summary + "'";

        init( true, true, true, false, false, null, null, null, "sp", null, "ic", "fc", "sc", "im", "fm", null );
        summary = settings.summary();
        expected = new StringBuilder();
        addKeyValues( expected, Messages.SK_WILL_RUN_UNIT_TESTS, Messages.SK_WILL_RUN_FUNCTION_TESTS, Messages.SK_WILL_RUN_LOAD_TESTS );
        expected.append( new NameFilter( NameFilter.Type.PACKAGE, null, null, "sp" ).loggingMessage() );
        expected.append( SettingsSpecificationFromFile.NL );
        expected.append( new NameFilter( NameFilter.Type.CLASS, "ic", "fc", "sc" ).loggingMessage() );
        expected.append( SettingsSpecificationFromFile.NL );
        expected.append( new NameFilter( NameFilter.Type.METHOD, "im", "fm", null ).loggingMessage() );
        expected.append( SettingsSpecificationFromFile.NL );
        assert expected.toString().equals( summary ) : "Expected: '" + expected + "'\n but got: '" + summary + "'";

        init( true, true, true, false, false, null, null, null, "sp", true, "ic", "fc", "sc", "im", "fm", null );
        summary = settings.summary();
        expected = new StringBuilder();
        addKeyValues( expected, Messages.SK_WILL_RUN_UNIT_TESTS, Messages.SK_WILL_RUN_FUNCTION_TESTS, Messages.SK_WILL_RUN_LOAD_TESTS );
        expected.append( new NameFilter( NameFilter.Type.PACKAGE, null, null, "sp" ).loggingMessage() );
        expected.append( SettingsSpecificationFromFile.NL );
        expected.append( new NameFilter( NameFilter.Type.CLASS, "ic", "fc", "sc" ).loggingMessage() );
        expected.append( SettingsSpecificationFromFile.NL );
        expected.append( new NameFilter( NameFilter.Type.METHOD, "im", "fm", null ).loggingMessage() );
        expected.append( SettingsSpecificationFromFile.NL );
        expected.append( Messages.message( Messages.SK_FAIL_FAST ) );
        expected.append( SettingsSpecificationFromFile.NL );
        assert expected.toString().equals( summary ) : "Expected: '" + expected + "'\n but got: '" + summary + "'";
        return true;
    }

    private void addKeyValues( StringBuilder sb, String... keys ) {
        for (String key : keys) {
            sb.append( Messages.message( key ) );
            sb.append( SettingsSpecificationFromFile.NL );
        }
    }

    public boolean productionClassesDirTest() throws Exception {
        init( true, true, true, true, true );
        assert settings.productionClassesDir().equals( tempDir );
        return true;
    }

    public boolean testClassesDirTest() throws Exception {
        init(true, true, true, true, true );
        assert settings.testClassesDir().equals( tempDir );
        return true;
    }

    public boolean valuesAreTrimmedTest() throws Exception {
        init( true, true, true, true, true, " log file  ", "initial package   ", " final package ", null, null );
        assert settings.resultsFileName().equals( "log file" );
        checkEqual(settings.packageNameFilter(), nf( NameFilter.Type.PACKAGE, "initial package", "final package", null ));
        init( true, true, true, true, true, " log file  ", "initial package   ", " final package ", "single package  ", null );
        assert settings.packageNameFilter().equals( new NameFilter( NameFilter.Type.PACKAGE, "initial package", "final package", "single package" ) );
        return true;
    }

    public boolean packageNameFilterTest() throws Exception {
        String initialPackageName = null;
        String finalPackageName = null;
        String singlePackageName = null;
        init( true, true, true, true, true, null, initialPackageName, finalPackageName, singlePackageName, null );
        assert settings.packageNameFilter().accept( "a" );
        assert settings.packageNameFilter().accept( "b" );
        assert settings.packageNameFilter().accept( "m" );
        assert settings.packageNameFilter().accept( "y" );
        assert settings.packageNameFilter().accept( "z" );

        initialPackageName = "b";
        finalPackageName = null;
        singlePackageName = null;
        init( true, true, true, true, true, null, initialPackageName, finalPackageName, singlePackageName, null );
        assert !settings.packageNameFilter().accept( "a" );
        assert settings.packageNameFilter().accept( "b" );
        assert settings.packageNameFilter().accept( "m" );
        assert settings.packageNameFilter().accept( "y" );
        assert settings.packageNameFilter().accept( "z" );

        initialPackageName = null;
        finalPackageName = "y";
        singlePackageName = null;
        init( true, true, true, true, true, null, initialPackageName, finalPackageName, singlePackageName, null );
        assert settings.packageNameFilter().accept( "a" );
        assert settings.packageNameFilter().accept( "b" );
        assert settings.packageNameFilter().accept( "m" );
        assert settings.packageNameFilter().accept( "y" );
        assert !settings.packageNameFilter().accept( "z" );

        initialPackageName = "b";
        finalPackageName = "y";
        singlePackageName = null;
        init( true, true, true, true, true, null, initialPackageName, finalPackageName, singlePackageName, null );
        assert !settings.packageNameFilter().accept( "a" );
        assert settings.packageNameFilter().accept( "b" );
        assert settings.packageNameFilter().accept( "m" );
        assert settings.packageNameFilter().accept( "y" );
        assert !settings.packageNameFilter().accept( "z" );

        initialPackageName = "b";
        finalPackageName = "y";
        singlePackageName = "m";
        init( true, true, true, true, true, null, initialPackageName, finalPackageName, singlePackageName, null );
        assert !settings.packageNameFilter().accept( "a" );
        assert !settings.packageNameFilter().accept( "b" );
        assert settings.packageNameFilter().accept( "m" );
        assert !settings.packageNameFilter().accept( "y" );
        assert !settings.packageNameFilter().accept( "z" );

        //Blanks are like null.
        initialPackageName = "";
        finalPackageName = "";
        singlePackageName = "";
        init( true, true, true, true, true, null, initialPackageName, finalPackageName, singlePackageName, null );
        assert settings.packageNameFilter().accept( "a" );
        assert settings.packageNameFilter().accept( "b" );
        assert settings.packageNameFilter().accept( "m" );
        assert settings.packageNameFilter().accept( "y" );
        assert settings.packageNameFilter().accept( "z" );
        return true;
    }

    public boolean classNameFilterTest() throws Exception {
        String singlePackageName = null;
        String initialClassName = null;
        String finalClassName = null;
        String singleClassName = null;

        //If the single package property is null, the class name filter is null.
        init( true, true, true, true, true, null, null, null, singlePackageName, null, initialClassName, finalClassName, singleClassName, null, null, null );
        assert settings.classNameFilter().equals( new NameFilter( NameFilter.Type.CLASS, null, null, null ) );

        initialClassName = "M";
        finalClassName = "R";
        singleClassName = null;
        init( true, true, true, true, true, null, null, null, singlePackageName, null, initialClassName, finalClassName, singleClassName, null, null, null );
        assert settings.classNameFilter().equals( new NameFilter( NameFilter.Type.CLASS, null, null, null ) );

        singlePackageName = "x.y";
        init( true, true, true, true, true, null, null, null, singlePackageName, null, initialClassName, finalClassName, singleClassName, null, null, null );
        assert !settings.classNameFilter().accept( "L" );
        assert settings.classNameFilter().accept( "M" );
        assert settings.classNameFilter().accept( "N" );
        assert settings.classNameFilter().accept( "R" );
        assert !settings.classNameFilter().accept( "S" );

        //Initial value only.
        initialClassName = "M";
        finalClassName = null;
        init( true, true, true, true, true, null, null, null, singlePackageName, null, initialClassName, finalClassName, singleClassName, null, null, null );
        assert !settings.classNameFilter().accept( "L" );
        assert settings.classNameFilter().accept( "M" );
        assert settings.classNameFilter().accept( "N" );
        assert settings.classNameFilter().accept( "R" );
        assert settings.classNameFilter().accept( "S" );
        assert settings.classNameFilter().accept( "Z" );

        //Final value only.
        initialClassName = null;
        finalClassName = "R";
        init( true, true, true, true, true, null, null, null, singlePackageName, null, initialClassName, finalClassName, singleClassName, null, null, null );
        assert settings.classNameFilter().accept( "L" );
        assert settings.classNameFilter().accept( "A" );
        assert settings.classNameFilter().accept( "M" );
        assert settings.classNameFilter().accept( "N" );
        assert settings.classNameFilter().accept( "R" );
        assert !settings.classNameFilter().accept( "S" );
        assert !settings.classNameFilter().accept( "Z" );

        //Single class name over-rides the initial and final values.
        initialClassName = "M";
        finalClassName = "R";
        singleClassName = "Z";
        init( true, true, true, true, true, null, null, null, singlePackageName, null, initialClassName, finalClassName, singleClassName, null, null, null );
        assert !settings.classNameFilter().accept( "L" );
        assert !settings.classNameFilter().accept( "M" );
        assert !settings.classNameFilter().accept( "N" );
        assert !settings.classNameFilter().accept( "R" );
        assert !settings.classNameFilter().accept( "S" );
        assert settings.classNameFilter().accept( "Z" );
        return true;
    }

    public boolean methodNameFilterTest() throws Exception {
        String singlePackageName = null;
        String singleClassName = null;
        String initialMethodName = null;
        String finalMethodName = null;
        String singleMethodName = null;

        //If the single package property is null, or the single class property is null, the method name filter is built of nulls..
        init( true, true, true, true, true, null, null, null, singlePackageName, null, null, null, singleClassName, initialMethodName, finalMethodName, singleMethodName );
        assert settings.methodNameFilter().equals( new NameFilter( NameFilter.Type.METHOD, null, null, null ) );

        initialMethodName = "M";
        finalMethodName = "R";
        init( true, true, true, true, true, null, null, null, singlePackageName, null, null, null, singleClassName, initialMethodName, finalMethodName, singleMethodName );
        assert settings.methodNameFilter().equals( new NameFilter( NameFilter.Type.METHOD, null, null, null ) );

        singlePackageName = "x.y";
        init( true, true, true, true, true, null, null, null, singlePackageName, null, null, null, singleClassName, initialMethodName, finalMethodName, singleMethodName );
        assert settings.methodNameFilter().equals( new NameFilter( NameFilter.Type.METHOD, null, null, null ) );

        singleClassName = "A";
        init( true, true, true, true, true, null, null, null, singlePackageName, null, null, null, singleClassName, initialMethodName, finalMethodName, singleMethodName );
        assert !settings.methodNameFilter().accept( "L" );
        assert settings.methodNameFilter().accept( "M" );
        assert settings.methodNameFilter().accept( "N" );
        assert settings.methodNameFilter().accept( "R" );
        assert !settings.methodNameFilter().accept( "S" );

        //Initial value only.
        initialMethodName = "M";
        finalMethodName = null;
        init( true, true, true, true, true, null, null, null, singlePackageName, null, null, null, singleClassName, initialMethodName, finalMethodName, singleMethodName );
        assert !settings.methodNameFilter().accept( "L" );
        assert settings.methodNameFilter().accept( "M" );
        assert settings.methodNameFilter().accept( "N" );
        assert settings.methodNameFilter().accept( "R" );
        assert settings.methodNameFilter().accept( "S" );
        assert settings.methodNameFilter().accept( "Z" );

        //Final value only.
        initialMethodName = null;
        finalMethodName = "R";
        init( true, true, true, true, true, null, null, null, singlePackageName, null, null, null, singleClassName, initialMethodName, finalMethodName, singleMethodName );
        assert settings.methodNameFilter().accept( "A" );
        assert settings.methodNameFilter().accept( "L" );
        assert settings.methodNameFilter().accept( "M" );
        assert settings.methodNameFilter().accept( "N" );
        assert settings.methodNameFilter().accept( "R" );
        assert !settings.methodNameFilter().accept( "S" );
        assert !settings.methodNameFilter().accept( "Z" );

        //Single method name over-rides the initial and final values.
        initialMethodName = "M";
        finalMethodName = "R";
        singleMethodName = "R";
        init( true, true, true, true, true, null, null, null, singlePackageName, null, null, null, singleClassName, initialMethodName, finalMethodName, singleMethodName );
        assert !settings.methodNameFilter().accept( "L" );
        assert !settings.methodNameFilter().accept( "M" );
        assert !settings.methodNameFilter().accept( "N" );
        assert settings.methodNameFilter().accept( "R" );
        assert !settings.methodNameFilter().accept( "S" );
        return true;
    }

    public boolean runFunctionTestsTest() throws Exception {
        init( true, true, true, true, true );
        assert settings.runFunctionTests();
        init( true, false, true, true, true );
        assert !settings.runFunctionTests();
        return true;
    }

    public boolean runLoadTestsTest() throws Exception {
        init( true, true, true, true, true );
        assert settings.runLoadTests();
        init( true, true, false, true, true );
        assert !settings.runLoadTests();
        return true;
    }

    public boolean runUnitTestsTest() throws Exception {
        init( true, true, true, true, true );
        assert settings.runUnitTests();
        init( false, true, true, true, true );
        assert !settings.runUnitTests();
        return true;
    }

    public boolean logToConsoleTest() throws Exception {
        init( true, true, true, false, true );
        assert !settings.logToConsole();
        init( false, true, true, true, true );
        assert settings.logToConsole();
        return true;
    }

    public boolean abbreviationsTest() throws IOException {
        Properties props = new Properties();
        props.put( ClassesRoot.CLASSES_ROOT, Helpers.tempDirectory().getAbsolutePath() );
        init( props );
        assert settings.unknownKeys().isEmpty();
        props = new Properties();
        props.put( LogToConsole.LOG_TO_CONSOLE, "t" );
        props.put( StopAtFirstFailure.STOP_AT_FIRST_FAILURE, "T" );
        props.put( RunFunctionTests.RUN_FUNCTION_TESTS, "f" );
        props.put( RunLoadTests.RUN_LOAD_TESTS, "F" );
        init( props );
        assert settings.logToConsole();
        assert settings.stopAtFirstFailure();
        assert !settings.runFunctionTests();
        assert !settings.runLoadTests();

        return true;
    }

    public boolean resultsFileNameTest() {
        //logToFile is false and no log file specified.
        init( true, true, true, true, false );
        assert settings.resultsFileName() == null;

        //logToFile is true, no log file specified. Use default.
        init( true, true, true, true, true );
        File expected = new File( ResultsFileName.DEFAULT_LOG_FILE_NAME );
        Helpers.assertEqual( new File( settings.resultsFileName() ).getAbsolutePath(), expected.getAbsolutePath() );

        //logToFile is true, and a file is specified.
        File resultsFile = new File( tempDir, "ResultsFile.txt" );
        init( true, true, true, true, true, resultsFile.getAbsolutePath() );
        assert new File( settings.resultsFileName() ).getAbsolutePath().equals( resultsFile.getAbsolutePath() );
//@todo add a test for when the settings file is blank
        return true;
    }

    public boolean lessVerboseLoggingTest() throws Exception {
        //This file has no value set.
        Properties properties = new Properties();
        SettingsSpecificationFromFile s = new SettingsSpecificationFromFile( writeSettingsFile(properties));
        assert !s.lessVerboseLogging();

        //This file has value set to true.
        properties.setProperty( LessVerboseLogging.LESS_VERBOSE, "true" );
        s = new SettingsSpecificationFromFile( writeSettingsFile(properties) );
        assert s.lessVerboseLogging();

        //This file has value set to false.
        properties.setProperty( LessVerboseLogging.LESS_VERBOSE, "false" );
        s = new SettingsSpecificationFromFile( writeSettingsFile(properties) );
        assert !s.lessVerboseLogging();
        return true;
    }

    private String writeSettingsFile( Properties properties ) throws Exception {
        File settingsFile = new File( Helpers.tempDirectory(), "Settings.txt" );
        if (!properties.containsKey( ClassesRoot.CLASSES_ROOT )) {
            properties.put( ClassesRoot.CLASSES_ROOT, Helpers.tempDirectory().getCanonicalPath() );
        }
        properties.store( new BufferedWriter( new FileWriter( settingsFile ) ), "Settings written in test" );
        return settingsFile.getAbsolutePath();
    }

    public boolean stopAtFirstFailureTest() {
        init( true, true, true, true, true, null, null, null, "singlePackage", Boolean.FALSE );
        assert !settings.stopAtFirstFailure();
        init( true, true, true, true, true, null, null, null, "singlePackage", Boolean.TRUE );
        assert settings.stopAtFirstFailure();
        return true;
    }

    public boolean unknownKeysTest() throws IOException {
        Properties props = new Properties();
        props.put( ClassesRoot.CLASSES_ROOT, Helpers.tempDirectory().getAbsolutePath() );
        init( props );
        assert settings.unknownKeys().isEmpty();
        props = new Properties();
        props.put( LastClass.LAST_CLASS, "" );
        props.put( LastMethod.LAST_METHOD, "" );
        props.put( LastPackage.LAST_PACKAGE, "" );
        props.put( FirstClass.FIRST_CLASS, "" );
        props.put( FirstMethod.FIRST_METHOD, "" );
        props.put( FirstPackage.FIRST_PACKAGE, "" );
        props.put( ResultsFileName.LOG_FILE_NAME, "" );
        props.put( LogToConsole.LOG_TO_CONSOLE, "" );
        props.put( LogToFile.LOG_TO_FILE, "" );
        props.put( RunFunctionTests.RUN_FUNCTION_TESTS, "" );
        props.put( RunLoadTests.RUN_LOAD_TESTS, "" );
        props.put( RunUnitTests.RUN_UNIT_TESTS, "" );
        props.put( SingleClass.SINGLE_CLASS, "" );
        props.put( SingleMethod.SINGLE_METHOD, "" );
        props.put( SinglePackage.SINGLE_PACKAGE, "" );
        props.put( StopAtFirstFailure.STOP_AT_FIRST_FAILURE, "" );
        init( props );
        assert settings.unknownKeys().isEmpty();

        HashSet<String> expected = new HashSet<String>();
        expected.add( "junk" );
        props.put( "junk", "junk" );
        init( props );
        assert settings.unknownKeys().equals( expected ) : "Got: " + settings.unknownKeys();

        //The DEFAULT_... constant is not a key.
        expected.add( ResultsFileName.DEFAULT_LOG_FILE_NAME );
        props.put( ResultsFileName.DEFAULT_LOG_FILE_NAME, "jrwejri" );
        init( props );
        assert settings.unknownKeys().equals( expected ) : "Got: " + settings.unknownKeys();
        return true;
    }

    public boolean commentedPropertiesWithTheseValuesTest() throws IOException {
        Properties props = new Properties();
        String tempPath = Helpers.tempDirectory().getAbsolutePath().replaceAll("\\\\", "/");
        props.put( ClassesRoot.CLASSES_ROOT, tempPath);
        init( props );
        String value = settings.commentedPropertiesWithTheseValues();
        checkPropertiesString( value );

        props.put( LastClass.LAST_CLASS, "aaa" );
        props.put( LastMethod.LAST_METHOD, "aaa" );
        props.put( LastPackage.LAST_PACKAGE, "sddsd" );
        props.put( FirstClass.FIRST_CLASS, "sda" );
        props.put( FirstMethod.FIRST_METHOD, "vvvv" );
        props.put( FirstPackage.FIRST_PACKAGE, "lsdfs" );
        props.put( ResultsFileName.LOG_FILE_NAME, "ppmsf.txt" );
        props.put( LogToConsole.LOG_TO_CONSOLE, "false" );
        props.put( LogToFile.LOG_TO_FILE, "false" );
        props.put( RunFunctionTests.RUN_FUNCTION_TESTS, "false" );
        props.put( RunLoadTests.RUN_LOAD_TESTS, "false" );
        props.put( RunUnitTests.RUN_UNIT_TESTS, "false" );
        props.put( SingleClass.SINGLE_CLASS, "erwer" );
        props.put( SingleMethod.SINGLE_METHOD, "wwerew" );
        props.put( SinglePackage.SINGLE_PACKAGE, "werwe" );
        props.put( StopAtFirstFailure.STOP_AT_FIRST_FAILURE, "true" );
        init( props );
        value = settings.commentedPropertiesWithTheseValues();
        checkPropertiesString( value );
        return true;
    }

    private void checkPropertiesString( String value ) throws IOException {
        //Check that a SettingsSpecificationFromFile created from this value has the same values as our settings instance variable.
        File file = new File( Helpers.tempDirectory(), "SettingsSpecificationFromFile" + System.currentTimeMillis() + ".txt" );
        FileUtils.writeStringToFile( file, value );
        SettingsSpecificationFromFile other = new SettingsSpecificationFromFile( file.getAbsolutePath() );
        checkEqual( settings.productionClassesDir(), other.productionClassesDir() );
        checkEqual( settings.classNameFilter(), other.classNameFilter() );
        checkEqual( settings.logToConsole(), other.logToConsole() );
        checkEqual( settings.methodNameFilter(), other.methodNameFilter() );
        checkEqual( settings.packageNameFilter(), other.packageNameFilter() );
        checkEqual( settings.resultsFileName(), other.resultsFileName() );
        checkEqual( settings.runFunctionTests(), other.runFunctionTests() );
        checkEqual( settings.runLoadTests(), other.runLoadTests() );
        checkEqual( settings.stopAtFirstFailure(), other.stopAtFirstFailure() );

        //Check that each the key-value pair is preceded by the correct comment.
        ResourceBundle commentsRB = PropertyResourceBundle.getBundle( SettingsSpecificationFromFile.class.getName() );
        String[] asLines = value.split( "\\n" );
        checkCommentPrecedesLineContaining( asLines, ClassesRoot.CLASSES_ROOT, commentsRB );
        checkCommentPrecedesLineContaining( asLines, LastClass.LAST_CLASS, commentsRB );

        //Check that all of the keys are there.
    }

    private void checkEqual( Object o1, Object o2 ) {
        if (o1 == null) {
            assert o2 == null : "Got: null, " + o2;
        } else {
            assert o1.equals( o2 ) : "Got: " + o1 + ", " + o2;
        }
    }

    private void checkCommentPrecedesLineContaining( String[] lines, String key, ResourceBundle commentsRB ) {
        String comment = "#" + commentsRB.getString( key );
        int pos = 0;
        for (String line : lines) {
            if (line.trim().equals( comment )) break;
            pos++;
        }
        assert lines[pos + 1].trim().startsWith( key );
    }

    private void init(Properties p) throws IOException {
        init(p, true);
    }

    private void init(Properties p, boolean cleanTemp) throws IOException {
        if (cleanTemp) {
            Helpers.cleanTempDirectory();
        }
        File propsFile = new File( Helpers.tempDirectory(), "SettingsTest.txt" );
        BufferedOutputStream bos = new BufferedOutputStream( new FileOutputStream( propsFile ) );
        p.store( bos, "SettingsSpecificationFromFile test, " + new Date() );
        bos.close();
        settings = new SettingsSpecificationFromFile( propsFile.getAbsolutePath() );
    }

    private void init( boolean runUnitTests, boolean runFunctionTests, boolean runLoadTests, boolean logToConsole, boolean logToFile ) {
        init( runUnitTests, runFunctionTests, runLoadTests, logToConsole, logToFile, null );
    }

    private void init( boolean runUnitTests, boolean runFunctionTests, boolean runLoadTests, boolean logToConsole, boolean logToFile, String logFileName ) {
        init( runUnitTests, runFunctionTests, runLoadTests, logToConsole, logToFile, logFileName, null, null, null, null );
    }

    private void init( boolean runUnitTests, boolean runFunctionTests, boolean runLoadTests, boolean logToConsole, boolean logToFile, String logFileName, String initialPackageName, String finalPackageName, String singlePackageName, Boolean failFast ) {
        init( runUnitTests, runFunctionTests, runLoadTests, logToConsole, logToFile, logFileName, initialPackageName, finalPackageName, singlePackageName, failFast, null, null, null, null, null, null );
    }

    private void init( boolean runUnitTests, boolean runFunctionTests, boolean runLoadTests, boolean logToConsole, boolean logToFile, String logFileName, String initialPackageName, String finalPackageName, String singlePackageName, Boolean failFast, String initialClassName, String finalClassName, String singleClassName, String initialMethod, String finalMethod, String singleMethod ) {
        Helpers.cleanTempDirectory();
        initWithoutcleanup( runUnitTests, runFunctionTests, runLoadTests, logToConsole, logToFile, logFileName, initialPackageName, finalPackageName, singlePackageName, failFast, initialClassName, finalClassName, singleClassName, initialMethod, finalMethod, singleMethod );
    }

    private void initWithoutcleanup( boolean runUnitTests, boolean runFunctionTests, boolean runLoadTests, boolean logToConsole, boolean logToFile, String logFileName, String initialPackageName, String finalPackageName, String singlePackageName, Boolean failFast, String initialClassName, String finalClassName, String singleClassName, String initialMethod, String finalMethod, String singleMethod ) {
        try {
            settings = new SettingsSpecificationFromFile( Helpers.writeSettingsFile(tempDir, runUnitTests, runFunctionTests, runLoadTests, initialPackageName, finalPackageName, singlePackageName, logToConsole, logToFile, logFileName, failFast, initialClassName, finalClassName, singleClassName, initialMethod, finalMethod, singleMethod) );
        } catch (IOException e) {
            e.printStackTrace();
            assert false : "Could not write settings file, see exception above.";
        }
    }

    private static NameFilter nf( NameFilter.Type type, String lower, String upper, String exact ) {
        return new NameFilter( type, lower, upper, exact );
    }
}