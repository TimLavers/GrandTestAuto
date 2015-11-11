/****************************************************************************
 * Copyright 2012 Timothy Gordon Lavers (Australia)
 * <p>
 * The Wide Open License (WOL)
 * <p>
 * Permission to use, copy, modify, distribute and sell this software and its
 * documentation for any purpose is hereby granted without fee, provided that
 * the above copyright notice and this license appear in all source copies.
 * THIS SOFTWARE IS PROVIDED "AS IS" WITHOUT EXPRESS OR IMPLIED WARRANTY OF
 * ANY KIND. See http://www.dspguru.com/wol.htm for more information.
 *****************************************************************************/
package org.grandtestauto.test;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;
import org.grandtestauto.GrandTestAuto;
import org.grandtestauto.Messages;
import org.grandtestauto.PackageChecker;
import org.grandtestauto.ProjectAnalyser;
import org.grandtestauto.settings.*;
import org.grandtestauto.util.ProcessReader;

import java.io.*;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * Helper methods for the tests.
 *
 * @author Tim Lavers
 */
public class Helpers {

    public static String NL = Messages.nl();
    public static String LF = "\n";
    private static PrintStream oldOut;
    private static PrintStream newOut;
    private static ByteArrayOutputStream byteOut;
    private static PrintStream oldErr;
    private static PrintStream newErr;
    private static ByteArrayOutputStream byteErr;

    public static <T> void assertSameContents(List<T> list1, List<T> list2) {
        assert list1.size() == list2.size() : "Sizes are: " + list1.size() + ", " + list2.size() + ".";
        assert list1.containsAll(list2);
    }

    public static void assertEqual(Object o1, Object o2) {
        if (!o1.equals(o2)) {
            System.out.println(" First Object: " + o1);
            System.out.println("Second Object: " + o2);
            assert false : "Objects differ as above.";
        }
    }

    public static void assertEqual(int i, int j) {
        if (i != j) {
            System.out.println(" First int: " + i);
            System.out.println("Second int: " + j);
        }
    }

    /**
     * Writes a file called "TestSettings.txt" into the temp directory
     * that contains the given settings.
     *
     * @return the name of the file
     */
    static String writeSettingsFile(File classRootSetting) {
        return writeSettingsFile(classRootSetting, true, true, true);
    }

    static String writeSettingsFile(File classRootSetting, boolean runUnitTests, boolean runFunctionTests, boolean runLoadTests) {
        return writeSettingsFile(classRootSetting, runUnitTests, runFunctionTests, runLoadTests, null);
    }

    public static File defaultLogFile() {
        return new File(tempDirectory(), "GTATestLog.txt");
    }

    public static String logFileContents() {
        try {
            return FileUtils.readFileToString(defaultLogFile());
        } catch (IOException e) {
            e.printStackTrace();
            throw new AssertionError("Could not read log file, as shown.");
        }
    }

    public static List<String> unitTestResultLogLines() {
        return resultLines("Unit");
    }

    public static List<String> functionTestResultLogLines() {
        return resultLines("Function");
    }

    public static List<String> loadTestResultLogLines() {
        return resultLines("Load");
    }

    private static List<String> resultLines(String type) {
        List<String> result = new LinkedList<String>();
        LineIterator litor = null;
        try {
            litor = new LineIterator(new FileReader(defaultLogFile()));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            assert false : "Could not get file reader, as shown.";
        }
        while (litor.hasNext()) {
            String line = litor.nextLine();
            if (line.contains("Results of " + type + " Tests")) {
                result.add(line);
            }
        }
        LineIterator.closeQuietly(litor);
        return result;
    }

    public static boolean overallUnitTestResultInLogFile() {
        return overallTestResultFromLog(Messages.OPK_OVERALL_UNIT_TEST_RESULT);
    }

    public static boolean overallFunctionTestResultInLogFile() {
        return overallTestResultFromLog(Messages.OPK_OVERALL_FUNCTION_TEST_RESULT);
    }

    public static boolean overallLoadTestResultInLogFile() {
        return overallTestResultFromLog(Messages.OPK_OVERALL_LOAD_TEST_RESULT);
    }

    static boolean overallTestResultFromLog(String key) {
        String log = logFileContents();
        //First look for true.
        String pf = Messages.passOrFail(true);
        if (log.contains(Messages.message(key, pf))) return true;
        pf = Messages.passOrFail(false);
        //Now look for false.
        if (log.contains(Messages.message(key, pf))) return false;
        //Neither. This is an error.
        assert false : "Overall result not written.";
        return false;
    }

    static String writeSettingsFile(File classRootSetting, boolean runUnitTests, boolean runFunctionTests, boolean runLoadTests, String initialPackageName) {
        return writeSettingsFile(classRootSetting, runUnitTests, runFunctionTests, runLoadTests, initialPackageName, false);
    }

    static String writeSettingsFile(File classRootSetting, boolean runUnitTests, boolean runFunctionTests, boolean runLoadTests, String initialPackageName, boolean logToConsole) {
        return writeSettingsFile(classRootSetting, runUnitTests, runFunctionTests, runLoadTests, initialPackageName, null, null, logToConsole, true, defaultLogFile().getAbsolutePath(), null, null, null, null, null, null, null);
    }

    public static String writeSettingsFile(File classRootSetting,
                                           boolean runUnitTests,
                                           boolean runFunctionTests,
                                           boolean runLoadTests,
                                           String initialPackageName,
                                           String finalPackageName,
                                           String singlePackageName,
                                           boolean logToConsole,
                                           boolean logToFile,
                                           String logFileName,
                                           Boolean failFast,
                                           String initialTestWithinPackage,
                                           String finalTestWithinPackage,
                                           String singleTestWithinSinglePackage,
                                           String initialMethod,
                                           String finalMethod,
                                           String singleMethod) {
        return writeSettingsFile(classRootSetting, runUnitTests, runFunctionTests, runLoadTests,
                initialPackageName, finalPackageName, singlePackageName,
                logToConsole, logToFile, logFileName, failFast,
                initialTestWithinPackage, finalTestWithinPackage, singleTestWithinSinglePackage,
                initialMethod, finalMethod, singleMethod, null);
    }

    public static String writeSettingsFile(File classRootSetting,
                                           boolean runUnitTests,
                                           boolean runFunctionTests,
                                           boolean runLoadTests,
                                           String initialPackageName,
                                           String finalPackageName,
                                           String singlePackageName,
                                           boolean logToConsole,
                                           boolean logToFile,
                                           String logFileName,
                                           Boolean failFast,
                                           String initialTestWithinPackage,
                                           String finalTestWithinPackage,
                                           String singleTestWithinSinglePackage,
                                           String initialMethod,
                                           String finalMethod,
                                           String singleMethod,
                                           String collatedResultsFileName) {
        String clsRoot = classRootSetting.getPath();
        //For windows need to replace '\' in clsRoot by '/' else
        //the settings file won't be parsed properly.
        clsRoot = clsRoot.replace('\\', '/');
        Properties props = new Properties();
        props.setProperty(ClassesRoot.CLASSES_ROOT, clsRoot);
        props.setProperty(LogToFile.LOG_TO_FILE, "true");
        if (logFileName != null) {
            props.setProperty(ResultsFileName.LOG_FILE_NAME, logFileName);
        }
        props.setProperty(LogToFile.LOG_TO_FILE, "" + logToFile);
        props.setProperty(LogToConsole.LOG_TO_CONSOLE, "" + logToConsole);
        props.setProperty(RunUnitTests.RUN_UNIT_TESTS, "" + runUnitTests);
        props.setProperty(RunFunctionTests.RUN_FUNCTION_TESTS, "" + runFunctionTests);
        props.setProperty(RunLoadTests.RUN_LOAD_TESTS, "" + runLoadTests);
        if (initialPackageName != null) {
            props.setProperty(FirstPackage.FIRST_PACKAGE, initialPackageName);
        }
        if (finalPackageName != null) {
            props.setProperty(LastPackage.LAST_PACKAGE, finalPackageName);
        }
        if (singlePackageName != null) {
            props.setProperty(SinglePackage.SINGLE_PACKAGE, singlePackageName);
        }
        if (failFast != null) {
            props.setProperty(StopAtFirstFailure.STOP_AT_FIRST_FAILURE, failFast.toString());
        }
        if (initialTestWithinPackage != null) {
            props.setProperty(FirstClass.FIRST_CLASS, initialTestWithinPackage);
        }
        if (finalTestWithinPackage != null) {
            props.setProperty(LastClass.LAST_CLASS, finalTestWithinPackage);
        }
        if (singleTestWithinSinglePackage != null) {
            props.setProperty(SingleClass.SINGLE_CLASS, singleTestWithinSinglePackage);
        }
        if (initialMethod != null) {
            props.setProperty(FirstMethod.FIRST_METHOD, initialMethod);
        }
        if (finalMethod != null) {
            props.setProperty(LastMethod.LAST_METHOD, finalMethod);
        }
        if (singleMethod != null) {
            props.setProperty(SingleMethod.SINGLE_METHOD, singleMethod);
        }
        if (collatedResultsFileName != null) {
            props.setProperty(ResultsFileName.LOG_FILE_NAME, collatedResultsFileName);
        }
        //noinspection ConstantConditions
        return writeSettingsFile(props).getPath();
    }

    public static File writeSettingsFile(Properties values) {
        try {
            File f = new File(tempDirectory(), "TestSettings.txt");
            OutputStream os = new BufferedOutputStream(new FileOutputStream(f));
            values.store(os, "Test properties");
            return f;
        } catch (IOException e) {
            e.printStackTrace();
            assert false : "Could not write settings file, as shown";
            return null;
        }
    }

    /**
     * A directory for use by the tests.
     */
    public static File tempDirectory() {
        File f = new File(System.getProperty("user.dir"), "testtemp");
        checkCreateDir(f);
        return f;
    }

    public static File temp2Directory() {
        File f = new File(System.getProperty("user.dir"), "testtemp2");
        checkCreateDir(f);
        return f;
    }

    public static File classesDirClassic() {
        File f = new File(tempDirectory(), "classesroot");
        checkCreateDir(f);
        return f;
    }

    public static File productionClassesRoot() {
        File f = new File(temp2Directory(), "prod");
        checkCreateDir(f);
        return f;
    }

    public static File testClassesRoot() {
        File f = new File(temp2Directory(), "tests");
        checkCreateDir(f);
        return f;
    }

    private static void checkCreateDir(File f) {
        f.mkdirs();
        assert f.exists();
        assert f.isDirectory();
    }

    /**
     * Cleans the temp directory.
     */
    public static void cleanTempDirectory() {
        clean(temp2Directory(), true);
        clean(tempDirectory(), true);
    }

    /**
     * Deletes all files from the given directory.
     */
    public static void clean(File dir, boolean recursive) {
        assert dir.isDirectory();
        assert dir.exists();
        File[] files = dir.listFiles();
        for (File f : files) {
            if (f.isDirectory() && recursive) {
                clean(f, recursive);
                f.delete();
            } else {
                f.delete();
            }
        }
    }

    /**
     * Expands the given archive into the given directory.
     */
    public static void expandZipTo(File zip, File destination) {
        try {
            ZipFile z = new ZipFile(zip);
            for (Enumeration e = z.entries(); e.hasMoreElements(); ) {
                ZipEntry entry = (ZipEntry) e.nextElement();
                String name = entry.getName();
                File f = new File(destination, name);
                if (entry.isDirectory()) {
                    f.mkdirs();
                } else {
                    BufferedInputStream input = new BufferedInputStream(z.getInputStream(entry));
                    BufferedOutputStream os = new BufferedOutputStream(new FileOutputStream(f));
                    byte[] buff = new byte[1024];
                    int r = 0;
                    while (r > -1) {
                        os.write(buff, 0, r);
                        r = input.read(buff, 0, buff.length);
                    }
                    os.close();
                    input.close();
                }
            }
            z.close();
        } catch (Exception e) {
            e.printStackTrace();
            assert false : "Could not expand " + zip.getPath() + " to " + destination.getPath();
        }
    }

    /**
     * The root directory for configured test data.
     */
    static File testDataRoot() {
        File userDir = new File(System.getProperty("user.dir"));
        return new File(userDir, "testdata");
    }

    /**
     * The file into which test data for the given class are stored.
     */
    static File testDataDir(Class clazz) {
        String relName = clazz.getPackage().getName();
        relName = relName.replace('.', File.separatorChar);
        //The relname ends with "\test" which must be stripped off.
        relName = relName.substring(0, relName.length() - 5);
        return new File(testDataRoot(), relName);
    }

    /**
     * Clean the temp dir, expand the appropriate zip there,
     * write a settings file, and create a GrandTestAuto that
     * will use the settings file and test the unzipped package hierarchy.
     */
    public static GrandTestAuto setupForZip(Class testClass, String zipName) {
        File zip = new File(Helpers.testDataDir(testClass), zipName + ".zip");
        return setupForZip(zip);
    }

    public static GrandTestAuto setupForZip(String zipName) {
        return setupForZip(new File(zipName));
    }

    public static GrandTestAuto setupForZipWithSeparateSourceAndTestClassRoots(File zip) throws Exception {
        File propertiesFile = expandZipWithSeparateSourceAndClassRootsAndWriteSettingsFile(zip);
        return new GrandTestAuto(new SettingsSpecificationFromFile(propertiesFile.getAbsolutePath()));
    }

    public static File expandZipWithSeparateSourceAndClassRootsAndWriteSettingsFile(File zip) {
        return expandZipWithSeparateSourceAndClassRootsAndWriteSettingsFile(zip, true, true, true);
    }

    public static File expandZipWithSeparateSourceAndClassRootsAndWriteSettingsFileForFunctionTestsOnly(File zip) {
        return expandZipWithSeparateSourceAndClassRootsAndWriteSettingsFile(zip, false, true, false);
    }

    public static File expandZipWithSeparateSourceAndClassRootsAndWriteSettingsFileForUnitTestsOnly(File zip) {
        return expandZipWithSeparateSourceAndClassRootsAndWriteSettingsFile(zip, true, false, false);
    }

    private static File expandZipWithSeparateSourceAndClassRootsAndWriteSettingsFile(File zip, boolean runUnitTests, boolean runFunctionTests, boolean runLoadTests) {
        Properties properties = new Properties();
        addAsProperty(properties, ProductionClassesRoot.PROD_ROOT, productionClassesRoot());
        addAsProperty(properties, ClassesRoot.CLASSES_ROOT, testClassesRoot());
        properties.setProperty(LogToFile.LOG_TO_FILE, "true");
        properties.setProperty(ResultsFileName.LOG_FILE_NAME, defaultLogFile().getAbsolutePath());
        properties.setProperty(RunUnitTests.RUN_UNIT_TESTS, Boolean.toString(runUnitTests));
        properties.setProperty(RunFunctionTests.RUN_FUNCTION_TESTS, Boolean.toString(runFunctionTests));
        properties.setProperty(RunLoadTests.RUN_LOAD_TESTS, Boolean.toString(runLoadTests));
        File propertiesFile = writeSettingsFile(properties);
        expandZipTo(zip, temp2Directory());
        return propertiesFile;
    }

    public  static void addAsProperty(Properties properties, String key, File file) {
        //For windows need to replace '\' in clsRoot by '/' else
        //the settings file won't be parsed properly.
        String property = file.getAbsolutePath().replace('\\', '/');
        properties.setProperty(key, property);
    }

    public static GrandTestAuto setupForZip(File zip) {
        return setupForZip(zip, true, true, true);
    }

    public static GrandTestAuto setupForZip(File zip, boolean runUnitTests, boolean runFunctionTests, boolean runLoadTests) {
        return setupForZip(zip, runUnitTests, runFunctionTests, runLoadTests, null, null);
    }

    public static GrandTestAuto setupForZipWithFailFast(File zip, boolean runUnitTests, boolean runFunctionTests, boolean runLoadTests) {
        return setupForZip(zip, runUnitTests, runFunctionTests, runLoadTests, null, null, null, false, true, defaultLogFile().getPath(), Boolean.TRUE, null, null, null, null, null, null);
    }

    public static GrandTestAuto setupForZip(File zip, boolean runUnitTests, boolean runFunctionTests, boolean runLoadTests, String initialPackage, String finalPackage) {
        return setupForZip(zip, runUnitTests, runFunctionTests, runLoadTests, initialPackage, finalPackage, null, false, true, defaultLogFile().getPath(), null, null, null, null, null, null, null);
    }

    public static GrandTestAuto setupForZip(File zip, boolean runUnitTests, boolean runFunctionTests, boolean runLoadTests, String initialPackage, boolean logToConsole, boolean logToFile, String logFileName) {
        return setupForZip(zip, runUnitTests, runFunctionTests, runLoadTests, initialPackage, null, null, logToConsole, logToFile, logFileName, null, null, null, null, null, null, null);
    }

    public static GrandTestAuto setupForZip(File zip, boolean runUnitTests, boolean runFunctionTests, boolean runLoadTests, String initialPackage, String finalPackage, String singlePackage, boolean logToConsole, boolean logToFile, String logFileName, Boolean failFast, String initialTestWithinPackage, String finalTestWithinPackage, String singleTestWithinSinglePackage, String initialTestMethod, String finalTestMethod, String singleTestMethod) {
        cleanTempDirectory();
        String settingsFileName = expandZipAndWriteSettingsFile(zip, runUnitTests, runFunctionTests, runLoadTests, initialPackage, finalPackage, singlePackage, logToConsole, logToFile, logFileName, failFast, initialTestWithinPackage, finalTestWithinPackage, singleTestWithinSinglePackage, initialTestMethod, finalTestMethod, singleTestMethod);
        SettingsSpecification settings;
        try {
            settings = new SettingsSpecificationFromFile(settingsFileName);
            return new GrandTestAuto(settings);
        } catch (IOException e) {
            assert false : "Could not create or read settings: " + e;
        }
        return null;
    }

    public static ProjectAnalyser setupProjectAnalyser(File zip) {
        cleanTempDirectory();
        String settingsFileName = expandZipAndWriteSettingsFile(zip, true, true, true, null, null, null, true, true, null, false, null, null, null, null, null, null);
        SettingsSpecification settings;
        try {
            settings = new SettingsSpecificationFromFile(settingsFileName);
            return new ProjectAnalyser(settings);
        } catch (IOException e) {
            assert false : "Could not create or read settings: " + e;
        }
        return null;
    }

    public static PackageChecker setupPackageChecker(File classesZip, String packageName) {
        cleanTempDirectory();
        expandZipTo(classesZip, classesDirClassic());
        return new PackageChecker(classesDirClassic(), packageName);
    }

    public static String expandZipAndWriteSettingsFile(File zip, boolean runUnitTests, boolean runFunctionTests, boolean runLoadTests, String initialPackage, boolean logToConsole) {
        return expandZipAndWriteSettingsFile(zip, runUnitTests, runFunctionTests, runLoadTests, initialPackage, logToConsole, true, null);
    }

    public static String expandZipAndWriteSettingsFile(File zip, boolean runUnitTests, boolean runFunctionTests, boolean runLoadTests, String initialPackage, boolean logToConsole, boolean logToFile, String logFileName) {
        return expandZipAndWriteSettingsFile(zip, runUnitTests, runFunctionTests, runLoadTests, initialPackage, null, null, logToConsole, logToFile, logFileName, null, null, null, null, null, null, null);
    }

    public static String expandZipAndWriteSettingsFile(File zip, boolean runUnitTests, boolean runFunctionTests, boolean runLoadTests, String initialPackage, String finalPackage, String singlePackage, boolean logToConsole, boolean logToFile, String logFileName, Boolean failFast, String initialTestWithinPackage, String finalTestWithinPackage, String singleTestWithinSinglePackage, String initialMethod, String finalMethod, String singleMethod) {
        expandZipTo(zip, classesDirClassic());
        //Write the settings file into the temp dir.
        return writeSettingsFile(classesDirClassic(), runUnitTests, runFunctionTests, runLoadTests, initialPackage, finalPackage, singlePackage, logToConsole, logToFile, logFileName, failFast, initialTestWithinPackage, finalTestWithinPackage, singleTestWithinSinglePackage, initialMethod, finalMethod, singleMethod);
    }

    public static String runGTAInSeparateJVMAndReadSystemErr(File zip, boolean runUnitTests, boolean runFunctionTests, boolean runLoadTests, String initialPackage) {
        String settingsFileName = expandZipAndWriteSettingsFile(zip, runUnitTests, runFunctionTests, runLoadTests, initialPackage, true);
        return runGTAInSeparateJVM(settingsFileName)[1];
    }

    public static String runGTAInSeparateJVMAndReadSystemOut(File zip, boolean runUnitTests, boolean runFunctionTests, boolean runLoadTests, String initialPackage) {
        String settingsFileName = expandZipAndWriteSettingsFile(zip, runUnitTests, runFunctionTests, runLoadTests, initialPackage, true);
        return runGTAInSeparateJVM(settingsFileName)[0];
    }

    public static Integer runGTAInSeparateJVMAndReadExitValue(File zip, boolean runUnitTests, boolean runFunctionTests, boolean runLoadTests, String initialPackage) {
        String settingsFileName = expandZipAndWriteSettingsFile(zip, runUnitTests, runFunctionTests, runLoadTests, initialPackage, true);
        return runGTAInSeparateJVMAndReadExitValue(settingsFileName);
    }

    public static String runGTAForSingleTestInSeparateJVMAndReadSystemErr(File zip, boolean runUnitTests, boolean runFunctionTests, boolean runLoadTests, String testName) {
        expandZipAndWriteSettingsFile(zip, runUnitTests, runFunctionTests, runLoadTests, null, true);
        return runGTAInSeparateJVM("-run", testName)[0];
    }

    public static String[] runGTAInSeparateJVM(String... args) {
        StringBuilder cmd = new StringBuilder();
        cmd.append("java.exe ");
        cmd.append("-Duser.dir=\"");
        cmd.append(System.getProperty("user.dir"));
        cmd.append("\"");
        cmd.append(" -cp ");
        cmd.append(System.getProperty("java.class.path"));
        cmd.append(" org.grandtestauto.GrandTestAuto");
        for (String arg : args) {
            cmd.append(" ");
            cmd.append(arg);
        }
        try {
            Process p = Runtime.getRuntime().exec(cmd.toString());
            return ProcessReader.readProcess(p);
        } catch (IOException e) {
            e.printStackTrace();
            assert false : "Could not run process, as shown.";
            return null;
        }
    }

    public static Integer runGTAInSeparateJVMAndReadExitValue(String... args) {//todo refactor bigtime
        StringBuilder cmd = new StringBuilder();
        cmd.append("java.exe ");
        cmd.append("-Duser.dir=\"");
        cmd.append(System.getProperty("user.dir"));
        cmd.append("\"");
        cmd.append(" -cp ");
        cmd.append(System.getProperty("java.class.path"));
        cmd.append(" org.grandtestauto.GrandTestAuto");
        for (String arg : args) {
            cmd.append(" ");
            cmd.append(arg);
        }
        try {
            Process p = Runtime.getRuntime().exec(cmd.toString());
            ProcessReader.readProcess(p);
            return p.exitValue();
        } catch (IOException e) {
            e.printStackTrace();
            assert false : "Could not run process, as shown.";
            return null;
        }
    }

    public static void startRecordingSout() {
        System.out.println("** About to switch System.out and System.err. **");
        byteOut = new ByteArrayOutputStream();
        byteErr = new ByteArrayOutputStream();
        newOut = new PrintStream(byteOut);
        newErr = new PrintStream(byteErr);
        oldOut = System.out;
        oldErr = System.err;
        System.setOut(newOut);
        System.setErr(newErr);
    }

    public static String stopRecordingSout() {
        StringBuilder sb = new StringBuilder();
        newOut.flush();
        newErr.flush();
        sb.append(byteOut.toString());
        sb.append(byteErr.toString());
        byteOut.reset();
        byteErr.reset();
        System.setOut(oldOut);
        System.setErr(oldErr);
        System.out.println("** System.out,err switched back. **");
        return sb.toString();
    }
}
