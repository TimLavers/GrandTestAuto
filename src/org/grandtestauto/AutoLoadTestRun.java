/****************************************************************************
 *
 * Name: Accountant.java
 *
 * Synopsis: See javadoc class comments.
 *
 * Description: See javadoc class comments.
 *
 * Copyright 2002 Timothy Gordon Lavers (Australia)
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
package org.grandtestauto;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.TimeUnit;

/**
 * Runs a single <code>AutoLoadTest</code>.
 *
 * @author Tim Lavers
 */
class AutoLoadTestRun {
    @NotNull
    private String fullClassName;
    @NotNull
    private String testName;

    @Nullable
    private ResultsLogger resultsLogger;

    AutoLoadTestRun(@NotNull String fullClassName, @NotNull String testName, @Nullable ResultsLogger resultsLogger) {
        this.fullClassName = fullClassName;
        this.testName = testName;
        this.resultsLogger = resultsLogger;
    }

    boolean runAutoLoadTest() throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        Class<?> klass = Class.forName(fullClassName);
        int repeats = 1;
        Flaky flaky = klass.getAnnotation(Flaky.class);
        if (flaky != null) {
            repeats = flaky.repeat();
        }
        Integer secondsToPauseIfTestThrowsException = null;
        PauseOnException pauseOnException = klass.getAnnotation(PauseOnException.class);
        if (pauseOnException != null) {
            secondsToPauseIfTestThrowsException = pauseOnException.seconds();
        }
        AutoLoadTest ft = (AutoLoadTest) klass.newInstance();
        //Report the result.
        boolean resultForTest = false;
        long timeJustBeforeTestRun = 0;
        long timeJustAfterTestRun = 0;
        for (int i=1; !resultForTest && i<=repeats; i++) {
            if (i > 1) {
                printAndLog(null, Messages.message(Messages.TPK_RUNNING_TEST_AGAIN, testName, "" + i));
            }
            timeJustBeforeTestRun = System.currentTimeMillis();
            try {
                resultForTest = ft.runTest();
            } catch (Throwable e) {
                String msg = Messages.message(Messages.OPK_ERROR_RUNNING_AUTO_LOAD_TEST, testName);
                printAndLog(e, msg);
                if (secondsToPauseIfTestThrowsException != null) {
                    String pauseMessage = Messages.message(Messages.OPK_PAUSING_TEST_THAT_THREW_ERROR, secondsToPauseIfTestThrowsException.toString());
                    printAndLog(null, pauseMessage);
                    try {
                        Thread.sleep(TimeUnit.SECONDS.toMillis(secondsToPauseIfTestThrowsException));
                    } catch (Exception e2) {
                        e2.printStackTrace();
                    }
                }
            }
            timeJustAfterTestRun = System.currentTimeMillis();
        }
        long timeToRunTest = timeJustAfterTestRun - timeJustBeforeTestRun;
        String msg = testName + " " + Messages.passOrFail(resultForTest) + ", " + ResultsLogger.formatTestExecutionTime(timeToRunTest);
        printAndLog(null, msg);
        return resultForTest;
    }

    private void printAndLog(Throwable e, String msg) {
        if (resultsLogger != null) {
            resultsLogger.log(msg, e);
        } else {
            if (e != null) {
                e.printStackTrace();
            }
            System.out.println(msg);
        }
    }
}