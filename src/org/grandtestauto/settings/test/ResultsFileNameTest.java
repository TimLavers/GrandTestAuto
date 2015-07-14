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
package org.grandtestauto.settings.test;

import org.grandtestauto.assertion.Assert;
import org.grandtestauto.settings.ResultsFileName;

import java.util.Properties;

/**
 * @author Tim Lavers
 */
public class ResultsFileNameTest extends SettingTestBase {

    public boolean buildFromTest() {
        init();
        //Use default if nothing in properties.
        Properties properties = new Properties();
        ResultsFileName rfn = new ResultsFileName();
        rfn.buildFrom(properties);
        rfn.addTo(settings);
        Assert.aequals(ResultsFileName.DEFAULT_LOG_FILE_NAME, settings.resultsFileName());

        settings = new DummySettings();
        properties = new Properties();
        String value = "the_file.txt";
        properties.put(ResultsFileName.LOG_FILE_NAME, value);
        rfn = new ResultsFileName();
        rfn.buildFrom(properties);
        rfn.addTo(settings);
        Assert.aequals(value, settings.resultsFileName());

        settings = new DummySettings();
        properties = new Properties();
        properties.put(ResultsFileName.LOG_FILE_NAME, "  " + value + " \t");
        rfn = new ResultsFileName();
        rfn.buildFrom(properties);
        rfn.addTo(settings);
        Assert.aequals(value, settings.resultsFileName());

        //Handle rubbish.
        settings = new DummySettings();
        properties = new Properties();
        value = "";
        properties.put(ResultsFileName.LOG_FILE_NAME, value);
        rfn = new ResultsFileName();
        rfn.buildFrom(properties);
        rfn.addTo(settings);
        Assert.aequals(ResultsFileName.DEFAULT_LOG_FILE_NAME, settings.resultsFileName());

        return true;
    }

    public boolean addToTest() {
        init();
        ResultsFileName lvl = new ResultsFileName();
        lvl.addTo(settings);
        Assert.aequals(ResultsFileName.DEFAULT_LOG_FILE_NAME, settings.resultsFileName());

        return true;
    }

    public boolean keyTest() {
        init();
        Assert.aequals(ResultsFileName.LOG_FILE_NAME, new ResultsFileName().key());
        return true;
    }

    public boolean valueInUserExplanationTest() {
        init();
        ResultsFileName lvl = new ResultsFileName();
        lvl.addTo(settings);
        Assert.aequals(ResultsFileName.DEFAULT_LOG_FILE_NAME, lvl.valueInUserExplanation(settings));
        return true;
    }
}
