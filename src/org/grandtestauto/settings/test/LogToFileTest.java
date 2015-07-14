/****************************************************************************
 *
 * Name: Settings.java
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
package org.grandtestauto.settings.test;

import org.grandtestauto.assertion.Assert;
import org.grandtestauto.settings.LogToFile;

import java.util.Properties;

/**
 * @author Tim Lavers
 */
public class LogToFileTest extends SettingTestBase {

    public boolean buildFromTest() {
        init();
        Properties properties = new Properties();
        LogToFile lvl = new LogToFile();
        lvl.buildFrom(properties);
        lvl.addTo(settings);
        Assert.azzertNotNull(settings.resultsFileName());

        settings = new DummySettings();
        properties = new Properties();
        properties.put(LogToFile.LOG_TO_FILE, "t");
        lvl = new LogToFile();
        lvl.buildFrom(properties);
        lvl.addTo(settings);
        Assert.azzertNotNull(settings.resultsFileName());

        settings = new DummySettings();
        properties = new Properties();
        properties.put(LogToFile.LOG_TO_FILE, "fAlsE");
        lvl = new LogToFile();
        lvl.buildFrom(properties);
        lvl.addTo(settings);
        Assert.azzertNull(settings.resultsFileName());

        settings = new DummySettings();
        properties = new Properties();
        properties.put(LogToFile.LOG_TO_FILE, "f");
        lvl = new LogToFile();
        lvl.buildFrom(properties);
        lvl.addTo(settings);
        Assert.azzertNull(settings.resultsFileName());
        return true;
    }

    public boolean addToTest() {
        init();
        LogToFile lvl = new LogToFile();
        lvl.addTo(settings);
        Assert.azzertNotNull(settings.resultsFileName());

        init();
        lvl = new LogToFile();
        makeFalse(lvl);
        lvl.addTo(settings);
        Assert.azzertNull(settings.resultsFileName());
         return true;
    }

    public boolean valueInUserExplanationTest() {
        init();
        LogToFile lvl = new LogToFile();
        lvl.addTo(settings);
        Assert.aequals(Boolean.TRUE, lvl.valueInUserExplanation(settings));

        init();
        lvl = new LogToFile();
        makeFalse(lvl);
        lvl.addTo(settings);
        Assert.aequals(Boolean.FALSE, lvl.valueInUserExplanation(settings));
        return true;
    }

    public boolean defaultValueTest() {
        init();
        LogToFile lvl = new LogToFile();
        Assert.azzert(lvl.defaultValue());
        return true;
    }

    public boolean keyTest() {
        init();
        Assert.aequals(LogToFile.LOG_TO_FILE, new LogToFile().key());
        return true;
    }

    private void makeFalse(LogToFile lvl) {
        Properties properties = new Properties();
        properties.put(LogToFile.LOG_TO_FILE, "f");
        lvl.buildFrom(properties);
    }
}
