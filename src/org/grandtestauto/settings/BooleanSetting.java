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
public abstract class BooleanSetting implements Setting {
    boolean value = defaultValue();

    public void buildFrom(Properties properties) {
        value = parseBoolean(properties.getProperty(key()), defaultValue());
    }

    public boolean defaultValue() {
        return false;
    }
}

