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
import org.grandtestauto.settings.ProductionClassesRoot;
import org.grandtestauto.test.Helpers;

import java.util.Properties;

/**
 * @author Tim Lavers
 */
public class ProductionClassesRootTest extends SettingTestBase {

    public boolean addToTest() {
        init();
        Properties properties = new Properties();
        properties.put(ProductionClassesRoot.PROD_ROOT, Helpers.tempDirectory().getAbsolutePath());
        ProductionClassesRoot pcr = new ProductionClassesRoot();
        pcr.buildFrom(properties);
        pcr.addTo(settings);
        Assert.aequals(Helpers.tempDirectory(), settings.productionClassesDir());
        return true;
    }

    public boolean buildFromTest() {
        init();
        Properties properties = new Properties();
        properties.put(ClassesRoot.CLASSES_ROOT, Helpers.tempDirectory().getAbsolutePath());
        ProductionClassesRoot pcr = new ProductionClassesRoot();
        pcr.buildFrom(properties);
        pcr.addTo(settings);
        Assert.azzertNull(settings.productionClassesDir());

        return true;
    }

    public boolean keyTest() {
        init();
        Assert.aequals(ProductionClassesRoot.PROD_ROOT, new ProductionClassesRoot().key());
        return true;
    }
}
