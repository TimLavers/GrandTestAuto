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
public class ClassesRootTest extends SettingTestBase {

    public boolean buildFromTest() {
        init();
        //The default value is that of "user.dir" in the system properties.
        File junk = new File(Helpers.tempDirectory(), "junk");
        Assert.azzert(junk.mkdirs());
        String originalUserDir = System.getProperty("user.dir");
        System.setProperty("user.dir", junk.getAbsolutePath());
        Properties properties = new Properties();
        ClassesRoot lvl = new ClassesRoot();
        lvl.buildFrom(properties);
        lvl.addTo(settings);
        Assert.aequals(junk, settings.productionClassesDir());
        Assert.aequals(junk, settings.testClassesDir());
        System.setProperty("user.dir", originalUserDir);

        settings = new DummySettings();
        properties = new Properties();
        properties.put(ClassesRoot.CLASSES_ROOT, Helpers.tempDirectory().getAbsolutePath());
        lvl = new ClassesRoot();
        lvl.buildFrom(properties);
        lvl.addTo(settings);
        Assert.aequals(Helpers.tempDirectory(), settings.productionClassesDir());
        Assert.aequals(Helpers.tempDirectory(), settings.testClassesDir());

        return true;
    }

    public boolean addToTest() {
        //Tested above
        return true;
    }

    public boolean keyTest() {
        init();
        Assert.aequals(ClassesRoot.CLASSES_ROOT, new ClassesRoot().key());
        return true;
    }

    public boolean valueInUserExplanationTest() throws Exception {
        init();
        Properties properties = new Properties();
        properties.put(ClassesRoot.CLASSES_ROOT, Helpers.tempDirectory().getAbsolutePath());
        ClassesRoot lvl = new ClassesRoot();
        lvl.buildFrom(properties);
        lvl.addTo(settings);
        Assert.aequals(Helpers.tempDirectory().getCanonicalPath().replaceAll("\\\\", "/"), lvl.valueInUserExplanation(settings));
        return true;
    }
}
