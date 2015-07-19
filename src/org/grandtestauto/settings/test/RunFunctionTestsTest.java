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
import org.grandtestauto.settings.RunFunctionTests;

import java.util.Properties;

/**
 * @author Tim Lavers
 */
public class RunFunctionTestsTest extends SettingTestBase {

    public boolean buildFromTest() {
        init();
        Properties properties = new Properties();
        RunFunctionTests rft = new RunFunctionTests();
        rft.buildFrom(properties);
        rft.addTo(settings);
        Assert.azzert(settings.runFunctionTests());

        settings = new DummySettings();
        properties = new Properties();
        properties.put(RunFunctionTests.RUN_FUNCTION_TESTS, "t");
        rft = new RunFunctionTests();
        rft.buildFrom(properties);
        rft.addTo(settings);
        Assert.azzert(settings.runFunctionTests());

        settings = new DummySettings();
        properties = new Properties();
        properties.put(RunFunctionTests.RUN_FUNCTION_TESTS, "fAlsE");
        rft = new RunFunctionTests();
        rft.buildFrom(properties);
        rft.addTo(settings);
        Assert.azzertFalse(settings.runFunctionTests());

        settings = new DummySettings();
        properties = new Properties();
        properties.put(RunFunctionTests.RUN_FUNCTION_TESTS, "f");
        rft = new RunFunctionTests();
        rft.buildFrom(properties);
        rft.addTo(settings);
        Assert.azzertFalse(settings.runFunctionTests());
        return true;
    }

    public boolean addToTest() {
        init();
        RunFunctionTests rft = new RunFunctionTests();
        rft.addTo(settings);
        Assert.azzert(settings.runFunctionTests());
        return true;
    }

    public boolean defaultValueTest() {
        init();
        RunFunctionTests rft = new RunFunctionTests();
        Assert.azzert(rft.defaultValue());
        return true;
    }

    public boolean summaryTest() {
        init();
        RunFunctionTests rft = new RunFunctionTests();
        Assert.aequals(Messages.message(Messages.SK_WILL_RUN_FUNCTION_TESTS), rft.summary());

        rft = new RunFunctionTests();
        makeFalse(rft);
        Assert.aequals(Messages.message(Messages.SK_WILL_NOT_RUN_FUNCTION_TESTS), rft.summary());

        return true;
    }

    public boolean valueInUserExplanationTest() {
        init();
        RunFunctionTests rft = new RunFunctionTests();
        rft.addTo(settings);
        Assert.aequals(Boolean.TRUE, rft.valueInUserExplanation(settings));

        init();
        rft = new RunFunctionTests();
        makeFalse(rft);
        rft.addTo(settings);
        Assert.aequals(Boolean.FALSE, rft.valueInUserExplanation(settings));
        return true;
    }

    public boolean keyTest() {
        init();
        Assert.aequals(RunFunctionTests.RUN_FUNCTION_TESTS, new RunFunctionTests().key());
        return true;
    }

    private void makeFalse(RunFunctionTests rft) {
        Properties properties = new Properties();
        properties.put(RunFunctionTests.RUN_FUNCTION_TESTS, "f");
        rft.buildFrom(properties);
    }
}
