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
import org.grandtestauto.settings.LogToConsole;

import java.util.Properties;

/**
 * @author Tim Lavers
 */
public class LogToConsoleTest extends SettingTestBase {

    public boolean buildFromTest() {
        init();
        Properties properties = new Properties();
        LogToConsole lvl = new LogToConsole();
        lvl.buildFrom(properties);
        lvl.addTo(settings);
        Assert.azzert(settings.logToConsole());

        settings = new DummySettings();
        properties = new Properties();
        properties.put(LogToConsole.LOG_TO_CONSOLE, "t");
        lvl = new LogToConsole();
        lvl.buildFrom(properties);
        lvl.addTo(settings);
        Assert.azzert(settings.logToConsole());

        settings = new DummySettings();
        properties = new Properties();
        properties.put(LogToConsole.LOG_TO_CONSOLE, "fAlsE");
        lvl = new LogToConsole();
        lvl.buildFrom(properties);
        lvl.addTo(settings);
        Assert.azzertFalse(settings.logToConsole());

        settings = new DummySettings();
        properties = new Properties();
        properties.put(LogToConsole.LOG_TO_CONSOLE, "f");
        lvl = new LogToConsole();
        lvl.buildFrom(properties);
        lvl.addTo(settings);
        Assert.azzertFalse(settings.logToConsole());
        return true;
    }

    public boolean addToTest() {
        init();
        LogToConsole lvl = new LogToConsole();
        lvl.addTo(settings);
        Assert.azzert(settings.logToConsole());

        init();
        lvl = new LogToConsole();
        makeFalse(lvl);
        lvl.addTo(settings);
        Assert.azzertFalse(settings.logToConsole());
        return true;
    }

    public boolean valueInUserExplanationTest() {
        init();
        LogToConsole lvl = new LogToConsole();
        lvl.addTo(settings);
        Assert.aequals(Boolean.TRUE, lvl.valueInUserExplanation(settings));

        init();
        lvl = new LogToConsole();
        makeFalse(lvl);
        lvl.addTo(settings);
        Assert.aequals(Boolean.FALSE, lvl.valueInUserExplanation(settings));
        return true;
    }

    public boolean keyTest() {
        init();
        Assert.aequals(LogToConsole.LOG_TO_CONSOLE, new LogToConsole().key());
        return true;
    }

    public boolean defaultValueTest() {
        init();
        LogToConsole lvl = new LogToConsole();
        Assert.azzert(lvl.defaultValue());
        return true;
    }

    private void makeFalse(LogToConsole lvl) {
        Properties properties = new Properties();
        properties.put(LogToConsole.LOG_TO_CONSOLE, "f");
        lvl.buildFrom(properties);
    }
}
