/**
 * *************************************************************************
 * <p>
 * Name: Settings.java
 * <p>
 * Synopsis: See javadoc class comments.
 * <p>
 * Description: See javadoc class comments.
 * <p>
 * Copyright 2002 Timothy Gordon Lavers (Australia)
 * <p>
 * The Wide Open License (WOL)
 * <p>
 * Permission to use, copy, modify, distribute and sell this software and its
 * documentation for any purpose is hereby granted without fee, provided that
 * the above copyright notice and this license appear in all source copies.
 * THIS SOFTWARE IS PROVIDED "AS IS" WITHOUT EXPRESS OR IMPLIED WARRANTY OF
 * ANY KIND. See http://www.dspguru.com/wol.htm for more information.
 * <p>
 * ***************************************************************************
 */
package org.grandtestauto.settings;

import org.grandtestauto.*;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.*;
import java.util.stream.Stream;

/**
 * The parameters for the invocation of <code>GrandTestAuto</code>.
 *
 * @author Tim Lavers
 */
public abstract class SettingsSpecification {
    /**
     * The name for the results file, if not set explicitly.
     */
    public static final String DEFAULT_LOG_FILE_NAME = "GTAResults.txt";
    public static String NL = System.getProperty("line.separator");
    String resultsFileName = DEFAULT_LOG_FILE_NAME;
    boolean logToFile = true;
    String initialPackageName = null;
    String finalPackageName = null;
    boolean logToConsole = true;
    boolean runUnitTests = true;
    boolean runFunctionTests = true;
    boolean runLoadTests = true;
    boolean stopAtFirstFailure = false;
    String singlePackageName = null;
    String initialClassWithinSinglePackage = null;
    String finalClassWithinSinglePackage = null;
    String singleClassWithinSinglePackage = null;
    String initialMethod = null;
    String finalMethod = null;
    String singleMethod = null;
    boolean lessVerboseLogging = false;
    private List<Setting> settings = new LinkedList<>();
//    private File classesRoot;
    private File productionClassesRoot;
    private File testClassesRoot;
    private SortedSet<Name> packagesAsNames;
    private NameFilter packageNameFilter = new NameFilter(NameFilter.Type.PACKAGE, null, null, null);
    private NameFilter classNameFilter = new NameFilter(NameFilter.Type.CLASS, null, null, null);
    private NameFilter methodNameFilter = new NameFilter(NameFilter.Type.METHOD, null, null, null);

    public SettingsSpecification() {
        settings.add(new ClassesRoot());
        settings.add(new ProductionClassesRoot());
        settings.add(new LogToFile());
        settings.add(new LogToConsole());
        settings.add(new ResultsFileName());
        settings.add(new FirstPackage());
        settings.add(new LastPackage());
        settings.add(new SinglePackage());
        settings.add(new RunUnitTests());
        settings.add(new RunFunctionTests());
        settings.add(new RunLoadTests());
        settings.add(new StopAtFirstFailure());
        settings.add(new FirstClass());
        settings.add(new LastClass());
        settings.add(new SingleClass());
        settings.add(new FirstMethod());
        settings.add(new LastMethod());
        settings.add(new SingleMethod());
        settings.add(new LessVerboseLogging());
    }

    public static SortedSet<Name> subDirectoriesAsNames(File root) {
        SortedSet<Name> result = new TreeSet<>();
        addSubdirectoriesToDirectoryList(result, null, root);
        return result;
    }

    private static void addSubdirectoriesToDirectoryList(Set<Name> list, final Name base, File baseDir) {
        File[] subDirs = baseDir.listFiles(File::isDirectory);
        if (subDirs == null) return;
        for (File subDir : subDirs) {
            Name newName = base == null ? Name.dotSeparatedName(subDir.getName()) : new Name(base, subDir.getName());
            addSubdirectoriesToDirectoryList(list, newName, subDir);
            list.add(newName);
        }
    }

    void loadFromProperties(Properties properties) {
        settingsStream().forEach(s -> s.buildFrom(properties));
        settingsStream().forEach(s -> s.addTo(this));
    }

    Stream<Setting> settingsStream() {
        return settings.stream();
    }

    /**
     * The directory containing all of the classes in the project being tested.
     */
    File classesDir() {
        return testClassesRoot;
    }

    public File productionClassesDir() {
        return productionClassesRoot;
    }

    public File testClassesDir() {
        return testClassesRoot;
    }

    public String summary() {
        StringBuilder sb = new StringBuilder();
        sb.append(Messages.message(runUnitTests ? Messages.SK_WILL_RUN_UNIT_TESTS : Messages.SK_WILL_NOT_RUN_UNIT_TESTS));
        sb.append(NL);
        sb.append(Messages.message(runFunctionTests ? Messages.SK_WILL_RUN_FUNCTION_TESTS : Messages.SK_WILL_NOT_RUN_FUNCTION_TESTS));
        sb.append(NL);
        sb.append(Messages.message(runLoadTests ? Messages.SK_WILL_RUN_LOAD_TESTS : Messages.SK_WILL_NOT_RUN_LOAD_TESTS));
        sb.append(NL);
        addToSummary(packageNameFilter.loggingMessage(), sb);
        addToSummary(classNameFilter.loggingMessage(), sb);
        addToSummary(methodNameFilter.loggingMessage(), sb);
        if (stopAtFirstFailure) {
            sb.append(Messages.message(Messages.SK_FAIL_FAST));
            sb.append(NL);
        }
        return sb.toString();
    }

    private void addToSummary(String valueOrEmpty, StringBuilder sb) {
        if (valueOrEmpty.length() > 0) {
            sb.append(valueOrEmpty);
            sb.append(NL);
        }
    }

    void setClassesRoot(File root) {
        checkDirectory(root);
        if (productionClassesRoot == null) {
            productionClassesRoot = root;
        }
        testClassesRoot = root;
        packagesAsNames = subDirectoriesAsNames(root);
    }

    void setProductionClassesRoot(File root) {
        checkDirectory(root);
        productionClassesRoot = root;
    }

    private void checkDirectory(File root) {
        if (!root.isDirectory() || !root.exists()) {
            String errorMessage = Messages.message(Messages.OPK_CLASSES_DIRECTORY_DOES_NOT_EXIST, root.getAbsolutePath());
            throw new IllegalArgumentException(errorMessage);
        }
    }

    void setLogToFile(Boolean value) {
        logToFile = value;
    }

    /**
     * Should results be logged to the console?
     */
    public boolean logToConsole() {
        return logToConsole;
    }

    void setLogToConsole(Boolean value) {
        logToConsole = value;
    }

    void setResultsFileName(String name) {
        resultsFileName = name.trim();
    }

    void setInitialPackageName(String name) {
        initialPackageName = getFirstMatchingPackage(name);
        packageNameFilter = new NameFilter(NameFilter.Type.PACKAGE, initialPackageName, finalPackageName, singlePackageName);
    }

    void setFinalPackageName(String name) {
        finalPackageName = getFirstMatchingPackage(name);
        packageNameFilter = new NameFilter(NameFilter.Type.PACKAGE, initialPackageName, finalPackageName, singlePackageName);
    }

    void setSinglePackageName(String name) {
        singlePackageName = getFirstMatchingPackage(name);
        packageNameFilter = new NameFilter(NameFilter.Type.PACKAGE, initialPackageName, finalPackageName, singlePackageName);
    }

    void setFirstClassName(String name) {
        if (singlePackageName == null) return;
        initialClassWithinSinglePackage = getFirstMatchingClass(name);
        classNameFilter = new NameFilter(NameFilter.Type.CLASS, initialClassWithinSinglePackage, finalClassWithinSinglePackage, singleClassWithinSinglePackage);
    }

    void setLastClassName(String name) {
        if (singlePackageName == null) return;
        finalClassWithinSinglePackage = getFirstMatchingClass(name);
        classNameFilter = new NameFilter(NameFilter.Type.CLASS, initialClassWithinSinglePackage, finalClassWithinSinglePackage, singleClassWithinSinglePackage);
    }

    void setSingleClassName(String name) {
        if (singlePackageName == null) return;
        singleClassWithinSinglePackage = getFirstMatchingClass(name);
        classNameFilter = new NameFilter(NameFilter.Type.CLASS, initialClassWithinSinglePackage, finalClassWithinSinglePackage, singleClassWithinSinglePackage);
    }

    void setFirstMethodName(String initialMethodName) {
        if (singleClassWithinSinglePackage == null) return;
        initialMethod = nullIfBlank(initialMethodName);
        methodNameFilter = new NameFilter(NameFilter.Type.METHOD, initialMethod, finalMethod, singleMethod);
    }

    void setLastMethodName(String finalMethodName) {
        if (singleClassWithinSinglePackage == null) return;
        finalMethod = nullIfBlank(finalMethodName);
        methodNameFilter = new NameFilter(NameFilter.Type.METHOD, initialMethod, finalMethod, singleMethod);
    }

    void setSingleMethodName(String singleMethodName) {
        if (singleClassWithinSinglePackage == null) return;
        singleMethod = nullIfBlank(singleMethodName);
        methodNameFilter = new NameFilter(NameFilter.Type.METHOD, initialMethod, finalMethod, singleMethod);
    }

    void setRunUnitTests(Boolean value) {
        runUnitTests = value;
    }

    void setRunFunctionTests(Boolean value) {
        runFunctionTests = value;
    }

    void setRunLoadTests(Boolean value) {
        runLoadTests = value;
    }

    void setStopAtFirstFailure(Boolean value) {
        stopAtFirstFailure = value;
    }

    void setLessVerboseLogging(Boolean value) {
        lessVerboseLogging = value;
    }

    /**
     * Should execution of tests cease after a UnitTester or AutoLoadTest returns
     * false or throws an Exception?
     */
    public boolean stopAtFirstFailure() {
        return stopAtFirstFailure;
    }

    /**
     * Null if the log to file value is false, else either the value for the log file
     * property, or the default value.
     */
    public String resultsFileName() {
        return logToFile ? resultsFileName : null;
    }

    public
    @Nullable
    String singlePackageName() {
        return singlePackageName;
    }

    /**
     * Are the unit tests to be run?
     */
    public boolean runUnitTests() {
        return runUnitTests;
    }

    /**
     * Are the function tests to be run?
     */
    public boolean runFunctionTests() {
        return runFunctionTests;
    }

    /**
     * Are the load tests to be run?
     */
    public boolean runLoadTests() {
        return runLoadTests;
    }

    /**
     * Should the logging exclude the GTA introduction and other verbose productions.
     *
     * @return the value of the key <code>LESS_VERBOSE</code>.
     */
    public boolean lessVerboseLogging() {
        return lessVerboseLogging;
    }

    /**
     * Accepts the names of classes based on the values for <code>INITIAL_CLASS_WITHIN_SINGLE_PACKAGE</CODE>,
     * <code>FINAL_CLASS_WITHIN_SINGLE_PACKAGE</CODE> and <code>SINGLE_CLASS_WITHIN_SINGLE_PACKAGE</code>.
     */
    public NameFilter classNameFilter() {
        return classNameFilter;
    }

    /**
     * Accepts the names of tests based on the values for <code>FIRST_PACKAGE</CODE>,
     * <code>FINAL_PACKAGE</code> and <code>SINGLE_PACKAGE</code>.
     */
    public NameFilter packageNameFilter() {
        return packageNameFilter;
    }

    /**
     * Determines which methods are run, based on, based on the values of <code>INITIAL_METHOD_WITHIN_SINGLE_UNIT_TEST_CLASS</CODE>,
     * <code>FINAL_METHOD_WITHIN_SINGLE_UNIT_TEST_CLASS</code> and <code>SINGLE_METHOD_WITHIN_SINGLE_UNIT_TEST_CLASS</code>,
     * will be null unless  <code>SINGLE_PACKAGE</code> and  <code>SINGLE_CLASS_WITHIN_SINGLE_PACKAGE</code> are set (as well
     * as at least one of the method properties).
     */
    public NameFilter methodNameFilter() {
        return methodNameFilter;
    }

    private String nullIfBlank(String string) {
        if (string == null) return null;
        if (string.trim().isEmpty()) return null;
        return string.trim();
    }

    private String getFirstMatchingPackage(String propertiesValue) {
        if (propertiesValue == null) return null;
        String trimmed = propertiesValue.trim();
        if (trimmed.isEmpty()) return null;
        Name name = Name.dotSeparatedName(trimmed);
        for (Name packageName : packagesAsNames) {
            if (name.matches(packageName)) return packageName.toString();
        }
        return trimmed;
    }

    private String getFirstMatchingClass(String abbreviated) {
        assert singlePackageName != null : "GTA Bug...please report to comments@grandtestauto.org";
//        assert classesRoot != null : "GTA Bug...please report to comments@grandtestauto.org";
        if (abbreviated == null) return null;
        String trimmed = abbreviated.trim();
        if (trimmed.isEmpty()) return null;
        Name name = Name.camelCaseName(trimmed);
        String actualTestPackageName;
        if (PackagesInfo.namesPackageThatMightNeedUnitTests(singlePackageName)) {
            actualTestPackageName = singlePackageName + ".test";
        } else {
            actualTestPackageName = singlePackageName;
        }
        File classesDir = new File(testClassesRoot, actualTestPackageName.replace('.', File.separatorChar));
        //We have to be defensive here, as the classes dir is derived from user input and might not exist.
        if (classesDir.exists()) {
            TestFinder testFinder = new TestFinder(actualTestPackageName, classesDir);
            testFinder.seek();
            for (String className : testFinder.relevantClassNames) {
                if (name.matches(Name.camelCaseName(className))) return className;
            }
        }
        return trimmed;
    }
}

class TestFinder extends ClassFinder {

    SortedSet<String> relevantClassNames = new TreeSet<>();

    public TestFinder(String packageName, File classesDir) {
        super(packageName, classesDir);
    }

    public void processClass(String relativeName) {
        relevantClassNames.add(relativeName);
    }

    public boolean foundSomeClasses() {
        return relevantClassNames.size() > 0;
    }
}