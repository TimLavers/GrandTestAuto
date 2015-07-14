package org.grandtestauto.test.tools;

import java.util.*;

/**
 * The methods in this class are discussed in Chapter 14.
 */
public class TestHelper {

    /**
     * Private constructor as we don't want to instantiate this.
     */
    private TestHelper() {
    }

    public static Set<String> namesOfActiveThreads() {
        Set<String> result = new HashSet<String>();
        Thread[] threads = new Thread[Thread.activeCount()];
        Thread.enumerate( threads );
        for (Thread thread : threads) {
            if (thread != null) {
                result.add( thread.getName() );
            }
        }
        return result;
    }

    public static void printActiveThreads() {
        int activeCount = namesOfActiveThreads().size();
        System.out.println( "Number of active Threads: " + activeCount );
        for (String name : namesOfActiveThreads()) {
            System.out.println( name );
        }
    }

    public static boolean waitForNamedThreadToFinish(
            final String threadName, long timeoutMillis ) {
        Waiting.ItHappened ih = new Waiting.ItHappened() {
            public boolean itHappened() {
                return !namesOfActiveThreads().contains( threadName );
            }
        };
        return Waiting.waitFor( ih, timeoutMillis );
    }

    /**
     * Removes leading and trailing whitespace from the given string and
     * replaces multiple whitespaces with the first char of the multiple
     * whitespace substring.
     *
     * @param str The string in which all whitespace is to be compacted
     * @return str.replaceAll( "[\\s]+", " ").trim();
     */
    public static String trimAndCompactWhitespaces( String str ) {
        return str.replaceAll( "[\\s]+", " " ).trim();
    }
}