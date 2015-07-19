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
package org.grandtestauto.settings.test;

import org.grandtestauto.assertion.Assert;
import org.grandtestauto.settings.ClassesRoot;
import org.grandtestauto.test.Helpers;

import java.io.File;
import java.util.Properties;

/**
 * @author Tim Lavers
 */
public class PackageSpecTest extends SettingTestBase {

    void init() {
        super.init();
        File junk = new File(Helpers.tempDirectory(), "junk");
        Assert.azzert(junk.mkdirs());
        Properties properties = new Properties();
        ClassesRoot lvl = new ClassesRoot();
        lvl.buildFrom(properties);
        lvl.addTo(settings);
    }
}
