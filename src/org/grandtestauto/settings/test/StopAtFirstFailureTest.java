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
import org.grandtestauto.settings.StopAtFirstFailure;

import java.util.Properties;

/**
 * @author Tim Lavers
 */
public class StopAtFirstFailureTest extends SettingTestBase {

    public boolean buildFromTest() {
        init();
        Properties properties = new Properties();
        StopAtFirstFailure ff = new StopAtFirstFailure();
        ff.buildFrom(properties);
        ff.addTo(settings);
        Assert.azzertFalse(settings.stopAtFirstFailure());

        settings = new DummySettings();
        properties = new Properties();
        properties.put(StopAtFirstFailure.STOP_AT_FIRST_FAILURE, "t");
        ff = new StopAtFirstFailure();
        ff.buildFrom(properties);
        ff.addTo(settings);
        Assert.azzert(settings.stopAtFirstFailure());

        settings = new DummySettings();
        properties = new Properties();
        properties.put(StopAtFirstFailure.STOP_AT_FIRST_FAILURE, "fAlsE");
        ff = new StopAtFirstFailure();
        ff.buildFrom(properties);
        ff.addTo(settings);
        Assert.azzertFalse(settings.stopAtFirstFailure());

        settings = new DummySettings();
        properties = new Properties();
        properties.put(StopAtFirstFailure.STOP_AT_FIRST_FAILURE, "f");
        ff = new StopAtFirstFailure();
        ff.buildFrom(properties);
        ff.addTo(settings);
        Assert.azzertFalse(settings.stopAtFirstFailure());
        return true;
    }

    public boolean addToTest() {
        init();
        StopAtFirstFailure ff = new StopAtFirstFailure();
        ff.addTo(settings);
        Assert.azzertFalse(settings.stopAtFirstFailure());

        init();
        ff = new StopAtFirstFailure();
        makeTrue(ff);
        ff.addTo(settings);
        Assert.azzert(settings.stopAtFirstFailure());
        return true;
    }

    public boolean summaryTest() {
        init();
        StopAtFirstFailure ff = new StopAtFirstFailure();
        Assert.azzertNull(ff.summary());

        ff = new StopAtFirstFailure();
        makeTrue(ff);
        Assert.aequals(Messages.message(Messages.SK_FAIL_FAST), ff.summary());
        makeTrue(ff);

        return true;
    }
    public boolean defaultValueTest() {
        init();
        StopAtFirstFailure ff = new StopAtFirstFailure();
        Assert.azzertFalse(ff.defaultValue());
        return true;
    }

    public boolean valueInUserExplanationTest() {
        init();
        StopAtFirstFailure ff = new StopAtFirstFailure();
        ff.addTo(settings);
        Assert.aequals(Boolean.FALSE, ff.valueInUserExplanation(settings));

        init();
        ff = new StopAtFirstFailure();
        makeTrue(ff);
        ff.addTo(settings);
        Assert.aequals(Boolean.TRUE, ff.valueInUserExplanation(settings));
        return true;
    }

    public boolean keyTest() {
        init();
        Assert.aequals(StopAtFirstFailure.STOP_AT_FIRST_FAILURE, new StopAtFirstFailure().key());
        return true;
    }

    private void makeTrue(StopAtFirstFailure ff) {
        Properties properties = new Properties();
        properties.put(StopAtFirstFailure.STOP_AT_FIRST_FAILURE, "t");
        ff.buildFrom(properties);
    }
}
