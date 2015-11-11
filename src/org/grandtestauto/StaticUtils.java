/****************************************************************************
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

import org.jetbrains.annotations.Nullable;

import java.lang.reflect.AnnotatedElement;
import java.util.concurrent.TimeUnit;

/**
 * Shared static methods.
 *
 * @author Tim Lavers
 */
class StaticUtils {

    static int flakyRepeats(AnnotatedElement element) {
        Flaky flaky = element.getAnnotation(Flaky.class);
        if (flaky != null) {
            return flaky.repeat();
        }
        return 1;
    }

    @Nullable
    static Integer pauseOnException(AnnotatedElement element) {
        Integer secondsToPauseIfTestThrowsException = null;
        PauseOnException pauseOnException = element.getAnnotation(PauseOnException.class);
        if (pauseOnException != null) {
            secondsToPauseIfTestThrowsException = pauseOnException.seconds();
        }
        return secondsToPauseIfTestThrowsException;
    }

    static void pauseOnException(Integer seconds, ResultsLogger logger) {
        if (seconds != null) {
            String pauseMessage = Messages.message(Messages.OPK_PAUSING_TEST_THAT_THREW_ERROR, seconds.toString());
            printAndLog(null, pauseMessage, logger);
        try {
            Thread.sleep(TimeUnit.SECONDS.toMillis(seconds));
        } catch (Exception e2) {
            e2.printStackTrace();
        }
        }
    }

    static void printAndLog(Throwable e, String msg, ResultsLogger resultsLogger) {
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