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

import org.grandtestauto.loganalysis.LogDirectoryAnalyser;
import org.grandtestauto.settings.SettingsSpecification;
import org.grandtestauto.settings.SettingsSpecificationFromFile;

import java.io.File;

/**
 * @author Tim Lavers
 */
public class ResultFilesAnalyser {

    public static void main(String[] args) throws Exception {
        File classesDir = new File(args[0]);
        File resultsDir = new File(args[1]);
        new ResultFilesAnalyser(classesDir,  resultsDir);
    }

    private ResultFilesAnalyser(File classesDirectory, File resultFilesDirectory) throws Exception {
        SettingsSpecification settings = new SettingsSpecificationFromFile(classesDirectory);
        ResultsLogger resultsLogger = new ResultsLogger(settings.resultsFileName(), true);
        LogDirectoryAnalyser lda = new LogDirectoryAnalyser(resultFilesDirectory, resultsLogger);
        GrandTestAuto gta = new GrandTestAuto( settings, lda );
        gta.resultsLogger().log( "Analysing results files and classes...", null );
        gta.resultsLogger().log( "Classes directory: " + classesDirectory.getAbsolutePath(), null);
        gta.runAllTests();
    }
}