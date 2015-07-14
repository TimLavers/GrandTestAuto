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

import org.grandtestauto.assertion.Assert;
import org.grandtestauto.settings.BooleanSetting;
import org.grandtestauto.settings.RunFunctionTests;
import org.grandtestauto.settings.Setting;
import org.grandtestauto.settings.SettingsSpecification;

import java.util.Properties;

/**
 * @author Tim Lavers
 */
public class SettingTest {

    private Setting setting;

    public boolean parseBooleanTest() {
        init();
        Assert.azzert(setting.parseBoolean(null, true));
        Assert.azzert(setting.parseBoolean(" ", true));
        Assert.azzert(setting.parseBoolean(" t", true));
        Assert.azzert(setting.parseBoolean(" tRUE", true));
        Assert.azzert(setting.parseBoolean(" tRUE ", true));
        Assert.azzertFalse(setting.parseBoolean(" f ", true));
        Assert.azzertFalse(setting.parseBoolean(" faLSE ", true));
        return true;
    }

    public boolean summaryTest() {
        Assert.azzertNull(new BooleanSetting() {

            @Override
            public void addTo(SettingsSpecification settings) {

            }

            @Override
            public String key() {
                return null;
            }

            @Override
            public Object valueInUserExplanation(SettingsSpecification settings) {
                return null;
            }
        }.summary());
        return true;
    }

    public boolean getValueReturnNullIfBlankTest() {
        init();
        Properties properties = new Properties();
        String key = "junk";
        Assert.azzertNull(setting.getValueReturnNullIfBlank(key, properties));
        properties.put(key, "   ");
        Assert.azzertNull(setting.getValueReturnNullIfBlank(key, properties));
        properties.put(key, "");
        Assert.azzertNull(setting.getValueReturnNullIfBlank(key, properties));
        properties.put(key, "junk");
        Assert.aequals("junk", setting.getValueReturnNullIfBlank(key, properties));
        properties.put(key, "  junk \t");
        Assert.aequals("junk", setting.getValueReturnNullIfBlank(key, properties));

        return true;
    }

    private void init() {
        setting = new RunFunctionTests();
    }
}
