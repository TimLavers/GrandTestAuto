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

import org.grandtestauto.Messages;
import org.grandtestauto.assertion.Assert;
import org.grandtestauto.settings.RunLoadTests;

import java.util.Properties;

/**
 * @author Tim Lavers
 */
public class RunLoadTestsTest extends SettingTestBase {

    public boolean buildFromTest() {
        init();
        Properties properties = new Properties();
        RunLoadTests rft = new RunLoadTests();
        rft.buildFrom(properties);
        rft.addTo(settings);
        Assert.azzert(settings.runLoadTests());

        settings = new DummySettings();
        properties = new Properties();
        properties.put(RunLoadTests.RUN_LOAD_TESTS, "t");
        rft = new RunLoadTests();
        rft.buildFrom(properties);
        rft.addTo(settings);
        Assert.azzert(settings.runLoadTests());

        settings = new DummySettings();
        properties = new Properties();
        properties.put(RunLoadTests.RUN_LOAD_TESTS, "fAlsE");
        rft = new RunLoadTests();
        rft.buildFrom(properties);
        rft.addTo(settings);
        Assert.azzertFalse(settings.runLoadTests());

        settings = new DummySettings();
        properties = new Properties();
        properties.put(RunLoadTests.RUN_LOAD_TESTS, "f");
        rft = new RunLoadTests();
        rft.buildFrom(properties);
        rft.addTo(settings);
        Assert.azzertFalse(settings.runLoadTests());
        return true;
    }

    public boolean addToTest() {
        init();
        RunLoadTests rft = new RunLoadTests();
        rft.addTo(settings);
        Assert.azzert(settings.runLoadTests());
        return true;
    }

    public boolean defaultValueTest() {
        init();
        RunLoadTests rft = new RunLoadTests();
        Assert.azzert(rft.defaultValue());
        return true;
    }

    public boolean keyTest() {
        init();
        Assert.aequals(RunLoadTests.RUN_LOAD_TESTS, new RunLoadTests().key());
        return true;
    }

    public boolean valueInUserExplanationTest() {
        init();
        RunLoadTests rft = new RunLoadTests();
        rft.addTo(settings);
        Assert.aequals(Boolean.TRUE, rft.valueInUserExplanation(settings));

        init();
        rft = new RunLoadTests();
        makeFalse(rft);
        rft.addTo(settings);
        Assert.aequals(Boolean.FALSE, rft.valueInUserExplanation(settings));
        return true;
    }

    public boolean summaryTest() {
        init();
        RunLoadTests rft = new RunLoadTests();
        Assert.aequals(Messages.message(Messages.SK_WILL_RUN_LOAD_TESTS), rft.summary());

        rft = new RunLoadTests();
        makeFalse(rft);
        Assert.aequals(Messages.message(Messages.SK_WILL_NOT_RUN_LOAD_TESTS), rft.summary());

        return true;
    }

    private void makeFalse(RunLoadTests rft) {
        Properties properties = new Properties();
        properties.put(RunLoadTests.RUN_LOAD_TESTS, "f");
        rft.buildFrom(properties);
    }
}
