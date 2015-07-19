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
public class StopAtFirstFailure extends BooleanSetting {
    public static final String STOP_AT_FIRST_FAILURE = "FAIL_FAST";

    public void addTo(SettingsSpecification settings) {
        settings.setStopAtFirstFailure(value);
    }

    @Override
    public String summary() {
       return value ? Messages.message( Messages.SK_FAIL_FAST ): null;
    }

    @Override
    public String key() {
        return STOP_AT_FIRST_FAILURE;
    }

    @Override
    public Object valueInUserExplanation(SettingsSpecification settings) {
        return settings.stopAtFirstFailure();
    }
}

