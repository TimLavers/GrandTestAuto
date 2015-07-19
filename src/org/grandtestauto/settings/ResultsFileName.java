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

import java.util.Properties;

/**
 * @author Tim Lavers
 */
public class ResultsFileName implements Setting {
    public static final String LOG_FILE_NAME = "LOG_FILE_NAME";
    public static final String DEFAULT_LOG_FILE_NAME = "GTAResults.txt";

    private String value = DEFAULT_LOG_FILE_NAME;

    public void buildFrom(Properties properties) {
        value = properties.getProperty(LOG_FILE_NAME, DEFAULT_LOG_FILE_NAME).trim();
        if (value.isEmpty()) {
            value = DEFAULT_LOG_FILE_NAME;
        }
    }

    public void addTo(SettingsSpecification settings) {
        settings.setResultsFileName(value);
    }

    @Override
    public String key() {
        return LOG_FILE_NAME;
    }

    @Override
    public Object valueInUserExplanation(SettingsSpecification settings) {
        return settings.resultsFileName();
    }
}

