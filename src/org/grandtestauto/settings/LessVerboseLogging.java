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
package org.grandtestauto.settings;

import java.util.Properties;

/**
 * @author Tim Lavers
 */
public class LessVerboseLogging implements Setting {
    public static final String LESS_VERBOSE = "LESS_VERBOSE";

    boolean value = false;

    public void buildFrom(Properties properties) {
        value = parseBoolean(properties.getProperty(LESS_VERBOSE, "false"), true);
    }

    public void addTo(SettingsSpecification settings) {
        settings.setLessVerboseLogging(value);
    }

    @Override
    public String key() {
        return null;
    }

    @Override
    public Object valueInUserExplanation(SettingsSpecification settings) {
        return null;
    }
}

