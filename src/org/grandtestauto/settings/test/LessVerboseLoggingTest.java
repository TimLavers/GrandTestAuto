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
import org.grandtestauto.settings.LessVerboseLogging;

import java.util.Properties;

/**
 * @author Tim Lavers
 */
public class LessVerboseLoggingTest extends SettingTestBase {

    public boolean buildFromTest() {
        init();
        Properties properties = new Properties();
        LessVerboseLogging lvl = new LessVerboseLogging();
        lvl.buildFrom(properties);
        lvl.addTo(settings);
        Assert.azzertFalse(settings.lessVerboseLogging());

        settings = new DummySettings();
        properties = new Properties();
        properties.put(LessVerboseLogging.LESS_VERBOSE, "t");
        lvl = new LessVerboseLogging();
        lvl.buildFrom(properties);
        lvl.addTo(settings);
        Assert.azzert(settings.lessVerboseLogging());

        settings = new DummySettings();
        properties = new Properties();
        properties.put(LessVerboseLogging.LESS_VERBOSE, "fAlsE");
        lvl = new LessVerboseLogging();
        lvl.buildFrom(properties);
        lvl.addTo(settings);
        Assert.azzertFalse(settings.lessVerboseLogging());

        settings = new DummySettings();
        properties = new Properties();
        properties.put(LessVerboseLogging.LESS_VERBOSE, "f");
        lvl = new LessVerboseLogging();
        lvl.buildFrom(properties);
        lvl.addTo(settings);
        Assert.azzertFalse(settings.lessVerboseLogging());
        return true;
    }

    public boolean addToTest() {
        init();
        LessVerboseLogging lvl = new LessVerboseLogging();
        lvl.addTo(settings);
        Assert.azzertFalse(settings.lessVerboseLogging());

        init();
        lvl = new LessVerboseLogging();
        makeTrue(lvl);
        lvl.addTo(settings);
        Assert.azzert(settings.lessVerboseLogging());
        return true;
    }

    public boolean summaryTest() {
        init();
        LessVerboseLogging lvl = new LessVerboseLogging();
        Assert.azzertNull(lvl.summary());

        lvl = new LessVerboseLogging();
        makeTrue(lvl);
        Assert.azzertNull(lvl.summary());

        return true;
    }

    public boolean keyTest() {
        init();
        LessVerboseLogging lvl = new LessVerboseLogging();
        Assert.azzertNull(lvl.key());

        return true;
    }

    public boolean valueInUserExplanationTest() {
        init();
        LessVerboseLogging lvl = new LessVerboseLogging();
        Assert.azzertNull(lvl.valueInUserExplanation(settings));

        return true;
    }

    private void makeTrue(LessVerboseLogging lvl) {
        Properties properties = new Properties();
        properties.put(LessVerboseLogging.LESS_VERBOSE, "t");
        lvl.buildFrom(properties);
    }
}
