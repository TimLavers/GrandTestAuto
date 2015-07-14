package org.grandtestauto;

import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.text.NumberFormat;
import java.util.logging.*;

/**
 * Handles all results logging.
 *
 * @author Tim Lavers
 */
public class ResultsLogger implements GTALogger {

    /**
     * To which results are written.
     */
    private Logger logger;

    /**
     * Creates a ResultsLogger that logs results to the named file, if not null, and
     * optionally to the console.
     */
    public ResultsLogger( String resultsFileName, boolean logToConsole ) {
        logger = Logger.getAnonymousLogger();
        logger.setUseParentHandlers( false );
        logger.setLevel( Level.ALL );
        //Add handlers as required.
        if (logToConsole) {
            Handler consoleHandler = new ConsoleHandler();
            consoleHandler.setFormatter( new ResultsFormatter() );
            logger.addHandler( consoleHandler );
        }
        if (resultsFileName != null) {
            OutputStream os = null;
            try {
                os = new FileOutputStream( resultsFileName );
            } catch (FileNotFoundException e) {
                //The only useful thing we can do is print the error and assert fail.
                e.printStackTrace();
                System.out.println( Messages.message( Messages.message( Messages.OPK_COULD_NOT_OPEN_RESULTS_FILE, resultsFileName )));
                assert false;
            }
            Handler fileHandler = new StreamHandler( os, new ResultsFormatter() );
            fileHandler.setFormatter( new ResultsFormatter() );
            logger.addHandler( fileHandler );
        }
    }

    public void log( String message, @Nullable Throwable t ) {
        logger.log( Level.SEVERE, message, t );
        Handler[] handlers = logger.getHandlers();
        for (Handler handler : handlers) {
            handler.flush();
        }
    }

    /**
     * Flush and close the handlers..
     */
    public void closeLogger() {
        Handler[] handlers = logger.getHandlers();
        for (Handler handler : handlers) {
            handler.close();
        }
    }

    static String formatTestExecutionTime(long timeInMillis) {
        float timeInSeconds = ((float) timeInMillis) / 1000f;
        NumberFormat testTimeFormatter = NumberFormat.getNumberInstance();
        return testTimeFormatter.format(timeInSeconds) + "s";
    }

    static String memoryUsed() {
        float memoryInMegabytes = Runtime.getRuntime().totalMemory() / (1024 * 1024);
        NumberFormat formatter = NumberFormat.getNumberInstance();
        return formatter.format(memoryInMegabytes) + "M";
    }

}
