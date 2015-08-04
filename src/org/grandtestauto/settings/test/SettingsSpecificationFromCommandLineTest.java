package org.grandtestauto.settings.test;

import org.grandtestauto.Name;
import org.grandtestauto.NameFilter;
import org.grandtestauto.assertion.Assert;
import org.grandtestauto.settings.*;
import org.grandtestauto.test.Helpers;
import org.grandtestauto.test.dataconstants.org.grandtestauto.Grandtestauto;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;

/**
 * @author Tim Lavers
 */
public class SettingsSpecificationFromCommandLineTest {

    private File tempDir = Helpers.tempDirectory();
    private SettingsSpecificationFromCommandLine settings;

    public boolean readSettingsFromSystemPropertiesTest() {
        Helpers.cleanTempDirectory();
        File tcr = new File(Helpers.tempDirectory(), "tcr");
        Assert.azzert(tcr.mkdirs());
        File pcr = new File(Helpers.tempDirectory(), "pcr");
        Assert.azzert(pcr.mkdirs());

        System.setProperty(ClassesRoot.CLASSES_ROOT, tcr.getAbsolutePath().replace('\\', '/'));
        System.setProperty(ProductionClassesRoot.PROD_ROOT, pcr.getAbsolutePath().replace('\\', '/'));
        System.setProperty(RunUnitTests.RUN_UNIT_TESTS, "true");
        System.setProperty(RunFunctionTests.RUN_FUNCTION_TESTS, "false");
        System.setProperty(RunLoadTests.RUN_LOAD_TESTS, "false");
        settings = new SettingsSpecificationFromCommandLine(new String[]{});
        Assert.aequals(tcr, settings.testClassesDir());
        Assert.aequals(pcr, settings.productionClassesDir());
        Assert.azzert(settings.runUnitTests());
        Assert.azzertFalse(settings.runFunctionTests());
        Assert.azzertFalse(settings.runLoadTests());
        //Clean up the System properties.
        System.getProperties().remove(ClassesRoot.CLASSES_ROOT);
        System.getProperties().remove(ProductionClassesRoot.PROD_ROOT);
        System.getProperties().remove(RunUnitTests.RUN_UNIT_TESTS);
        System.getProperties().remove(RunFunctionTests.RUN_FUNCTION_TESTS);
        System.getProperties().remove(RunLoadTests.RUN_LOAD_TESTS);
        return true;
    }

    public boolean settingsFromCommandLineOverrideArgumentsSystemPropertiesTest() {
        Helpers.cleanTempDirectory();
        File tcr = new File(Helpers.tempDirectory(), "tcr");
        Assert.azzert(tcr.mkdirs());
        File pcr = new File(Helpers.tempDirectory(), "pcr");
        Assert.azzert(pcr.mkdirs());

        System.setProperty(ClassesRoot.CLASSES_ROOT, tcr.getAbsolutePath().replace('\\', '/'));
        System.setProperty(ProductionClassesRoot.PROD_ROOT, pcr.getAbsolutePath().replace('\\', '/'));
        System.setProperty(RunUnitTests.RUN_UNIT_TESTS, "true");
        System.setProperty(RunFunctionTests.RUN_FUNCTION_TESTS, "false");
        System.setProperty(RunLoadTests.RUN_LOAD_TESTS, "false");

        File tcr2 = new File(Helpers.tempDirectory(), "tcr2");
        Assert.azzert(tcr2.mkdirs());
        StringBuilder sb = new StringBuilder();
        sb.append(" ").append(ClassesRoot.CLASSES_ROOT).append("=").append(tcr2.getAbsolutePath().replace('\\', '/'));
        sb.append(" ").append(RunUnitTests.RUN_UNIT_TESTS).append("=").append(false);
        sb.append(" ").append(RunFunctionTests.RUN_FUNCTION_TESTS).append("=").append(true);
        sb.append(" ").append(RunLoadTests.RUN_LOAD_TESTS).append("=").append(false);
        sb.append(" ").append("junk").append("=").append(true);
        sb.append(" ").append("more_junk").append("=").append(false);
        sb.append(" ").append("rubbish_with_no_value");
        settings = new SettingsSpecificationFromCommandLine(sb.toString().split(" "));
        Assert.aequals(tcr2, settings.testClassesDir());
        Assert.aequals(pcr, settings.productionClassesDir());
        Assert.azzertFalse(settings.runUnitTests());
        Assert.azzert(settings.runFunctionTests());
        Assert.azzertFalse(settings.runLoadTests());
        //Clean up the System properties.
        System.getProperties().remove(ClassesRoot.CLASSES_ROOT);
        System.getProperties().remove(ProductionClassesRoot.PROD_ROOT);
        System.getProperties().remove(RunUnitTests.RUN_UNIT_TESTS);
        System.getProperties().remove(RunFunctionTests.RUN_FUNCTION_TESTS);
        System.getProperties().remove(RunLoadTests.RUN_LOAD_TESTS);
        return true;
    }

    public boolean ignoreIrrelevantArgumentsTest() {
        StringBuilder sb = new StringBuilder();
        sb.append(" ").append(ClassesRoot.CLASSES_ROOT).append("=").append(tempDir.getAbsolutePath().replace('\\', '/'));
        sb.append(" ").append(RunUnitTests.RUN_UNIT_TESTS).append("=").append(true);
        sb.append(" ").append("junk").append("=").append(true);
        sb.append(" ").append("more_junk").append("=").append(false);
        sb.append(" ").append("rubbish_with_no_value");
        settings = new SettingsSpecificationFromCommandLine(sb.toString().split(" "));
        Assert.azzert(settings.runUnitTests());
        Assert.aequals(tempDir, settings.testClassesDir());
        return true;
    }

    public boolean unknownKeysTest() {
        init(true, true, true, true, true);
        Assert.azzert(settings.unknownKeys().isEmpty());
        return true;
    }

    public boolean singlePackageNameTest() throws Exception {
        String initialPackageName;
        String finalPackageName;
        String singlePackageName;
        init( true, true, true, true, true, null, null, null, null, null );
        assert settings.singlePackageName() == null;

        initialPackageName = "b";
        init( true, true, true, true, true, null, initialPackageName, null, null, null );
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
        Helpers.expandZipTo(new File(Grandtestauto.test100_zip), Helpers.tempDirectory());
        initWithoutcleanup(true, true, true, true, true, null, null, null, singlePackageName, null, null, null, singleClassName, null, null, null);
        NameFilter nameFilter = settings.classNameFilter();
        assert nameFilter.equals( new NameFilter( NameFilter.Type.CLASS, null, null, "PearTreeTest" ) );

        String initialClassName = "Ap";
        initWithoutcleanup(true, true, true, true, true, null, null, null, singlePackageName, null, initialClassName, null, null, null, null, null);
        nameFilter = settings.classNameFilter();
        assert nameFilter.equals( new NameFilter( NameFilter.Type.CLASS, "AppleOrchardTest", null, null ) );

        initialClassName = "AprTrT";
        String finalClassName = "PT";
        initWithoutcleanup(true, true, true, true, true, null, null, null, singlePackageName, null, initialClassName, finalClassName, null, null, null, null);
        nameFilter = settings.classNameFilter();
        assert nameFilter.equals( new NameFilter( NameFilter.Type.CLASS, "ApricotTreeTest", "PearTreeTest", null ) );

        initialClassName = "E";
        finalClassName = "X";
        initWithoutcleanup(true, true, true, true, true, null, null, null, singlePackageName, null, initialClassName, finalClassName, null, null, null, null);
        nameFilter = settings.classNameFilter();
        assert nameFilter.equals(new NameFilter(NameFilter.Type.CLASS, "ExtraTest", "XtraTest", null));
        return true;
    }

    public boolean subDirectoriesAsNamesTest() {
        Helpers.cleanTempDirectory();
        Helpers.expandZipTo( new File( Grandtestauto.test53_zip ), Helpers.tempDirectory() );
        TreeSet<Name> expected = new TreeSet<>();
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

    public boolean productionClassesDirTest() throws Exception {
        init( true, true, true, true, true );
        assert settings.productionClassesDir().equals( tempDir );
        return true;
    }

    public boolean testClassesDirTest() throws Exception {
        init(true, true, true, true, true);
        Assert.aequals(tempDir, settings.testClassesDir());
        return true;
    }

    public boolean packageNameFilterTest() throws Exception {
        String initialPackageName;
        String finalPackageName;
        String singlePackageName;
        init( true, true, true, true, true, null, null, null, null, null );
        assert settings.packageNameFilter().accept( "a" );
        assert settings.packageNameFilter().accept( "b" );
        assert settings.packageNameFilter().accept( "m" );
        assert settings.packageNameFilter().accept( "y" );
        assert settings.packageNameFilter().accept( "z" );

        initialPackageName = "b";
        init( true, true, true, true, true, null, initialPackageName, null, null, null );
        assert !settings.packageNameFilter().accept( "a" );
        assert settings.packageNameFilter().accept( "b" );
        assert settings.packageNameFilter().accept( "m" );
        assert settings.packageNameFilter().accept("y");
        assert settings.packageNameFilter().accept( "z" );

        finalPackageName = "y";
        init( true, true, true, true, true, null, null, finalPackageName, null, null );
        assert settings.packageNameFilter().accept( "a" );
        assert settings.packageNameFilter().accept( "b" );
        assert settings.packageNameFilter().accept( "m" );
        assert settings.packageNameFilter().accept( "y" );
        assert !settings.packageNameFilter().accept( "z" );

        initialPackageName = "b";
        finalPackageName = "y";
        init( true, true, true, true, true, null, initialPackageName, finalPackageName, null, null );
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
        String singlePackageName;
        String initialClassName;
        String finalClassName;
        String singleClassName;

        //If the single package property is null, the class name filter is null.
        init( true, true, true, true, true, null, null, null, null, null, null, null, null, null, null, null );
        assert settings.classNameFilter().equals(new NameFilter(NameFilter.Type.CLASS, null, null, null));

        initialClassName = "M";
        finalClassName = "R";
        init( true, true, true, true, true, null, null, null, null, null, initialClassName, finalClassName, null, null, null, null );
        assert settings.classNameFilter().equals(new NameFilter(NameFilter.Type.CLASS, null, null, null));

        singlePackageName = "x.y";
        init( true, true, true, true, true, null, null, null, singlePackageName, null, initialClassName, finalClassName, null, null, null, null );
        assert !settings.classNameFilter().accept( "L" );
        assert settings.classNameFilter().accept( "M" );
        assert settings.classNameFilter().accept( "N" );
        assert settings.classNameFilter().accept( "R" );
        assert !settings.classNameFilter().accept( "S" );

        //Initial value only.
        initialClassName = "M";
        init( true, true, true, true, true, null, null, null, singlePackageName, null, initialClassName, null, null, null, null, null );
        assert !settings.classNameFilter().accept( "L" );
        assert settings.classNameFilter().accept( "M" );
        assert settings.classNameFilter().accept( "N" );
        assert settings.classNameFilter().accept( "R" );
        assert settings.classNameFilter().accept( "S" );
        assert settings.classNameFilter().accept( "Z" );

        //Final value only.
        finalClassName = "R";
        init( true, true, true, true, true, null, null, null, singlePackageName, null, null, finalClassName, null, null, null, null );
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
        String singlePackageName;
        String singleClassName;
        String initialMethodName;
        String finalMethodName;
        String singleMethodName;

        //If the single package property is null, or the single class property is null, the method name filter is built of nulls..
        init( true, true, true, true, true, null, null, null, null, null, null, null, null, null, null, null);
        assert settings.methodNameFilter().equals( new NameFilter( NameFilter.Type.METHOD, null, null, null ) );

        initialMethodName = "M";
        finalMethodName = "R";
        init( true, true, true, true, true, null, null, null, null, null, null, null, null, initialMethodName, finalMethodName, null);
        assert settings.methodNameFilter().equals( new NameFilter( NameFilter.Type.METHOD, null, null, null ) );

        singlePackageName = "x.y";
        init( true, true, true, true, true, null, null, null, singlePackageName, null, null, null, null, initialMethodName, finalMethodName, null);
        assert settings.methodNameFilter().equals( new NameFilter( NameFilter.Type.METHOD, null, null, null ) );

        singleClassName = "A";
        init( true, true, true, true, true, null, null, null, singlePackageName, null, null, null, singleClassName, initialMethodName, finalMethodName, null);
        assert !settings.methodNameFilter().accept( "L" );
        assert settings.methodNameFilter().accept( "M" );
        assert settings.methodNameFilter().accept( "N" );
        assert settings.methodNameFilter().accept( "R" );
        assert !settings.methodNameFilter().accept( "S" );

        //Initial value only.
        initialMethodName = "M";
        init( true, true, true, true, true, null, null, null, singlePackageName, null, null, null, singleClassName, initialMethodName, null, null);
        assert !settings.methodNameFilter().accept( "L" );
        assert settings.methodNameFilter().accept( "M" );
        assert settings.methodNameFilter().accept( "N" );
        assert settings.methodNameFilter().accept( "R" );
        assert settings.methodNameFilter().accept( "S" );
        assert settings.methodNameFilter().accept( "Z" );

        //Final value only.
        finalMethodName = "R";
        init( true, true, true, true, true, null, null, null, singlePackageName, null, null, null, singleClassName, null, finalMethodName, null);
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
        init(true, true, true, false, true);
        assert !settings.logToConsole();
        init( false, true, true, true, true );
        assert settings.logToConsole();
        return true;
    }

    public boolean resultsFileNameTest() {
        //logToFile is false and no log file specified.
        init( true, true, true, true, false );
        assert settings.resultsFileName() == null;

        //logToFile is true, no log file specified. Use default.
        init(true, true, true, true, true);
        File expected = new File( ResultsFileName.DEFAULT_LOG_FILE_NAME );
        Helpers.assertEqual(new File(settings.resultsFileName()).getAbsolutePath(), expected.getAbsolutePath());

        //logToFile is true, and a file is specified.
        File resultsFile = new File( tempDir, "ResultsFile.txt" );
        init( true, true, true, true, true, resultsFile.getAbsolutePath() );
        assert new File( settings.resultsFileName() ).getAbsolutePath().equals(resultsFile.getAbsolutePath());
        return true;
    }

    public boolean lessVerboseLoggingTest() throws Exception {
        //This file has no value set.
        Properties properties = new Properties();
        SettingsSpecificationFromFile s = new SettingsSpecificationFromFile( writeSettingsFile(properties));
        assert !s.lessVerboseLogging();

        //This file has value set to true.
        properties.setProperty(LessVerboseLogging.LESS_VERBOSE, "true");
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
        properties.store(new BufferedWriter(new FileWriter(settingsFile)), "Settings written in test");
        return settingsFile.getAbsolutePath();
    }

    public boolean stopAtFirstFailureTest() {
        init( true, true, true, true, true, null, null, null, "singlePackage", Boolean.FALSE );
        assert !settings.stopAtFirstFailure();
        init(true, true, true, true, true, null, null, null, "singlePackage", Boolean.TRUE);
        assert settings.stopAtFirstFailure();
        return true;
    }

    private void checkEqual( Object o1, Object o2 ) {
        if (o1 == null) {
            assert o2 == null : "Got: null, " + o2;
        } else {
            assert o1.equals( o2 ) : "Got: " + o1 + ", " + o2;
        }
    }

    private void init( boolean runUnitTests, boolean runFunctionTests, boolean runLoadTests, boolean logToConsole, boolean logToFile ) {
        init(runUnitTests, runFunctionTests, runLoadTests, logToConsole, logToFile, null);
    }

    private void init( boolean runUnitTests, boolean runFunctionTests, boolean runLoadTests, boolean logToConsole, boolean logToFile, String logFileName ) {
        init( runUnitTests, runFunctionTests, runLoadTests, logToConsole, logToFile, logFileName, null, null, null, null );
    }

    private void init( boolean runUnitTests, boolean runFunctionTests, boolean runLoadTests, boolean logToConsole, boolean logToFile, String logFileName, String initialPackageName, String finalPackageName, String singlePackageName, Boolean failFast ) {
        init( runUnitTests, runFunctionTests, runLoadTests, logToConsole, logToFile, logFileName, initialPackageName, finalPackageName, singlePackageName, failFast, null, null, null, null, null, null );
    }

    private void init( boolean runUnitTests, boolean runFunctionTests, boolean runLoadTests, boolean logToConsole, boolean logToFile, String logFileName, String initialPackageName, String finalPackageName, String singlePackageName, Boolean failFast, String initialClassName, String finalClassName, String singleClassName, String initialMethod, String finalMethod, String singleMethod ) {
        Helpers.cleanTempDirectory();
        initWithoutcleanup(runUnitTests, runFunctionTests, runLoadTests, logToConsole, logToFile, logFileName, initialPackageName, finalPackageName, singlePackageName, failFast, initialClassName, finalClassName, singleClassName, initialMethod, finalMethod, singleMethod);
    }

    private void initWithoutcleanup( boolean runUnitTests, boolean runFunctionTests, boolean runLoadTests,
                                     boolean logToConsole, boolean logToFile, String logFileName,
                                     String initialPackageName, String finalPackageName, String singlePackageName,
                                     Boolean failFast, String initialClassName, String finalClassName, String singleClassName,
                                     String initialMethod, String finalMethod, String singleMethod ) {
        StringBuilder sb = new StringBuilder();
        sb.append(" ").append(ClassesRoot.CLASSES_ROOT).append("=").append(tempDir.getAbsolutePath().replace('\\', '/'));
        sb.append(" ").append(RunUnitTests.RUN_UNIT_TESTS).append("=").append(runUnitTests);
        sb.append(" ").append(RunFunctionTests.RUN_FUNCTION_TESTS).append("=").append(runFunctionTests);
        sb.append(" ").append(RunLoadTests.RUN_LOAD_TESTS).append("=").append(runLoadTests);
        sb.append(" ").append(LogToConsole.LOG_TO_CONSOLE).append("=").append(logToConsole);
        sb.append(" ").append(LogToFile.LOG_TO_FILE).append("=").append(logToFile);
        appendSetting(sb, ResultsFileName.LOG_FILE_NAME, logFileName);
        appendSetting(sb, FirstPackage.FIRST_PACKAGE, initialPackageName);
        appendSetting(sb, LastPackage.LAST_PACKAGE, finalPackageName);
        appendSetting(sb, SinglePackage.SINGLE_PACKAGE, singlePackageName);
        sb.append(" ").append(StopAtFirstFailure.STOP_AT_FIRST_FAILURE).append("=").append(failFast);
        appendSetting(sb, FirstClass.FIRST_CLASS, initialClassName);
        appendSetting(sb, LastClass.LAST_CLASS, finalClassName);
        appendSetting(sb, SingleClass.SINGLE_CLASS, singleClassName);
        appendSetting(sb, FirstMethod.FIRST_METHOD, initialMethod);
        appendSetting(sb, SingleMethod.SINGLE_METHOD, singleMethod);
        appendSetting(sb, LastMethod.LAST_METHOD, finalMethod);
        settings = new SettingsSpecificationFromCommandLine(sb.toString().split(" "));
    }

    public static void appendSetting(StringBuilder sb, String key, String value) {
        if (value != null && !value.trim().isEmpty()) {
            sb.append(" ").append(key).append("=").append(value.trim());
        }
    }

    private static NameFilter nf( NameFilter.Type type, String lower, String upper, String exact ) {
        return new NameFilter( type, lower, upper, exact );
    }
}