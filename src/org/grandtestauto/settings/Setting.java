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

import java.io.File;
import java.util.HashSet;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.Set;

/**
 * One or more parameters that determine some aspect of the running of the tests.
 *
 * @author Tim Lavers
 */
public interface Setting {
    public static String NL = System.getProperty("line.separator");

    default boolean parseBoolean(String s, boolean defaultValue) {
        if (s == null) return defaultValue;
        String normalised = s.trim().toLowerCase();
        if (normalised.isEmpty()) return defaultValue;
        return normalised.equals( "t" ) || Boolean.valueOf(normalised);
    }

    default String getValueReturnNullIfBlank(String key, Properties properties) {
        String result = properties.getProperty( key, null );
        if (result == null) return null;
        String trimmed = result.trim();
        if (trimmed.isEmpty()) return null;
        return trimmed;
    }

    String key();

    Object valueInUserExplanation(SettingsSpecification settings);

    void buildFrom(Properties properties);

    void addTo(SettingsSpecification settings);

    default String summary() { return null;}
}

