/****************************************************************************
 * Copyright 2012 Timothy Gordon Lavers (Australia)
 *
 *                          The Wide Open License (WOL)
 *
 * Permission to use, copy, modify, distribute and sell this software and its
 * documentation for any purpose is hereby granted without fee, provided that
 * the above copyright notice and this license appear in all source copies.
 * THIS SOFTWARE IS PROVIDED "AS IS" WITHOUT EXPRESS OR IMPLIED WARRANTY OF
 * ANY KIND. See http://www.dspguru.com/wol.htm for more information.
 *
 *****************************************************************************/
package org.grandtestauto.loganalysis;

import org.apache.commons.io.filefilter.FileFileFilter;
import org.grandtestauto.*;

import java.io.File;
import java.io.FileFilter;
import java.util.*;
import java.util.concurrent.*;

/**
 * Extracts information from a directory of GrandTestAuto log files or printouts.
 *
 * @author Tim Lavers
 */
public class LogDirectoryAnalyser implements DoPackageWork {
    private Map<String, Boolean> unitTestPackageDone = new HashMap<String, Boolean>();
    private Map<String, Boolean> functionTestsDone = new HashMap<String, Boolean>();
    private File logsDirectory;
    private GTALogger resultsLogger;

    public LogDirectoryAnalyser(File logsDirectory, GTALogger resultsLogger) throws Exception {
        this.logsDirectory = logsDirectory;
        this.resultsLogger = resultsLogger;
        File[] files = logsDirectory.listFiles((FileFilter) FileFileFilter.FILE);
        ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        List<Callable<LogFileAnalyser>> tasks = new LinkedList<Callable<LogFileAnalyser>>();
        for (File file : files) {
            tasks.add(new FileAnalysis(file));
        }
        List<Future<LogFileAnalyser>> completedTasks = executor.invokeAll(tasks);
        for (Future<LogFileAnalyser> task : completedTasks) {
            try {
                unitTestPackageDone.putAll(task.get().unitTestPackageResults());
                functionTestsDone.putAll(task.get().functionAndLoadTestResults());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
        executor.shutdown();

    }

    public Map<String, Boolean> unitTestPackageResults() {
        return unitTestPackageDone;
    }

    public PackageResult doUnitTests(String packageName) {
        boolean resultExistsInMap = unitTestPackageDone.containsKey(packageName);
        if (!resultExistsInMap) {
            String message = Messages.message(Messages.OPK_NO_RESULTS_FOUND_FOR, packageName);
            resultsLogger.log(message, null);
            return new PR(message);
        }
        Boolean passed = unitTestPackageDone.get(packageName);
        if (!passed) {
            String errorMessage = Messages.message(Messages.OPK_UNIT_TESTS_FAILED_FOR, packageName);
            resultsLogger.log(errorMessage,  null);
            return new PR(errorMessage);
        }
        return new PR(true);
    }

    public PackageResult runAutoLoadTestPackage(boolean areFunctionTests, Collection<String> classesInPackage, String testPackageName) {
        for (String testName : classesInPackage) {
            String fullClassName = testPackageName + "." + testName;
            if (!functionTestsDone.containsKey(fullClassName)) {
                String errorMessage = Messages.message(Messages.OPK_NO_RESULTS_FOUND_FOR, fullClassName);
                resultsLogger.log(errorMessage,  null);
                return new PR(errorMessage);
            }
            Boolean passed = functionTestsDone.get(fullClassName);
            if (!passed) {
                String errorMessage = Messages.message(Messages.OPK_ONE_OR_MORE_TESTS_FAILED_IN, testPackageName);
                resultsLogger.log(errorMessage,  null);
                return new PR(errorMessage);
            }
        }
        return new PR(true);
    }

    public boolean verbose() {
        return false;
    }

    @Override
    public List<String> preliminaryMessages() {
        List<String> result = new LinkedList<String>();
        String logDirMsg = Messages.message(Messages.OPK_LOG_FILE_DIRECTORY, logsDirectory.getAbsolutePath());
        result.add(logDirMsg);
        result.add(Messages.message(Messages.SK_LOG_FILES));
        SortedSet<String> logFileNames = new TreeSet<String>();
        for (File logFile : logsDirectory.listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                return pathname.isFile();
            }
        })) {
            logFileNames.add(logFile.getName());
        }
        result.addAll(logFileNames);
        return result;
    }

    public String messageForFinalResult(boolean passOrFail) {
        if (passOrFail) return Messages.message(Messages.SK_ALL_OK);
        return "Testing problems, as shown.";
    }

    private class PR implements PackageResult {
        private boolean passed;
        private String errorMessage;

        private PR(String errorMessage) {
            this.errorMessage = errorMessage;
            passed = false;
        }

        private PR(boolean passed) {
            this.passed = passed;
        }

        public boolean passed() {
            return passed;
        }

        public long timeTakenInMillis() {
            return 0;
        }

        public String errorMessage() {
            return errorMessage;
        }
    }

    private class FileAnalysis implements Callable<LogFileAnalyser> {
        private File file;

        private FileAnalysis(File file) {
            this.file = file;
        }

        public LogFileAnalyser call() throws Exception {
            LogFileAnalyser lfa = new LogFileAnalyser(file);
            return lfa;
        }
    }
}
