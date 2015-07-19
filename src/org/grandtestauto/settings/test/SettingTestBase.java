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

import org.grandtestauto.settings.SettingsSpecification;
import org.grandtestauto.settings.SettingsSpecificationFromFile;
import org.grandtestauto.test.Helpers;

import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

/**
 * @author Tim Lavers
 */
public class SettingTestBase {

    SettingsSpecification settings;
    ResourceBundle commentsBundle;


    void init() {
        Helpers.cleanTempDirectory();
        settings = new DummySettings();
        commentsBundle = PropertyResourceBundle.getBundle(SettingsSpecificationFromFile.class.getName());
    }
}

class DummySettings extends  SettingsSpecification {

}