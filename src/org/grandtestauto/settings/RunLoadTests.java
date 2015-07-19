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
package org.grandtestauto.settings;

import org.grandtestauto.Messages;

/**
 * @author Tim Lavers
 */
public class RunLoadTests extends BooleanSetting {
    public static final String RUN_LOAD_TESTS = "RUN_LOAD_TESTS";

    public void addTo(SettingsSpecification settings) {
        settings.setRunLoadTests(value);
    }

    @Override
    public String summary() {
       return (Messages.message(value ? Messages.SK_WILL_RUN_LOAD_TESTS : Messages.SK_WILL_NOT_RUN_LOAD_TESTS));
    }

    @Override
    public String key() {
        return RUN_LOAD_TESTS;
    }

    @Override
    public Object valueInUserExplanation(SettingsSpecification settings) {
        return settings.runLoadTests();
    }

    @Override
    public boolean defaultValue() {
        return true;
    }
}

