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
import org.grandtestauto.settings.RunUnitTests;

import java.util.Properties;

/**
 * @author Tim Lavers
 */
public class RunUnitTestsTest extends SettingTestBase {

    public boolean buildFromTest() {
        init();
        Properties properties = new Properties();
        RunUnitTests rft = new RunUnitTests();
        rft.buildFrom(properties);
        rft.addTo(settings);
        Assert.azzert(settings.runUnitTests());

        settings = new DummySettings();
        properties = new Properties();
        properties.put(RunUnitTests.RUN_UNIT_TESTS, "t");
        rft = new RunUnitTests();
        rft.buildFrom(properties);
        rft.addTo(settings);
        Assert.azzert(settings.runUnitTests());

        settings = new DummySettings();
        properties = new Properties();
        properties.put(RunUnitTests.RUN_UNIT_TESTS, "fAlsE");
        rft = new RunUnitTests();
        rft.buildFrom(properties);
        rft.addTo(settings);
        Assert.azzertFalse(settings.runUnitTests());

        settings = new DummySettings();
        properties = new Properties();
        properties.put(RunUnitTests.RUN_UNIT_TESTS, "f");
        rft = new RunUnitTests();
        rft.buildFrom(properties);
        rft.addTo(settings);
        Assert.azzertFalse(settings.runUnitTests());
        return true;
    }

    public boolean addToTest() {
        init();
        RunUnitTests rft = new RunUnitTests();
        rft.addTo(settings);
        Assert.azzert(settings.runUnitTests());
        return true;
    }

    public boolean defaultValueTest() {
        init();
        RunUnitTests rft = new RunUnitTests();
        Assert.azzert(rft.defaultValue());
        return true;
    }

    public boolean valueInUserExplanationTest() {
        init();
        RunUnitTests rft = new RunUnitTests();
        rft.addTo(settings);
        Assert.aequals(Boolean.TRUE, rft.valueInUserExplanation(settings));

        init();
        rft = new RunUnitTests();
        makeFalse(rft);
        rft.addTo(settings);
        Assert.aequals(Boolean.FALSE, rft.valueInUserExplanation(settings));
        return true;
    }

    public boolean summaryTest() {
        init();
        RunUnitTests rft = new RunUnitTests();
        Assert.aequals(Messages.message(Messages.SK_WILL_RUN_UNIT_TESTS), rft.summary());

        rft = new RunUnitTests();
        makeFalse(rft);
        Assert.aequals(Messages.message(Messages.SK_WILL_NOT_RUN_UNIT_TESTS), rft.summary());

        return true;
    }

    public boolean keyTest() {
        init();
        Assert.aequals(RunUnitTests.RUN_UNIT_TESTS, new RunUnitTests().key());
        return true;
    }

    private void makeFalse(RunUnitTests rft) {
        Properties properties = new Properties();
        properties.put(RunUnitTests.RUN_UNIT_TESTS, "f");
        rft.buildFrom(properties);
    }
}
