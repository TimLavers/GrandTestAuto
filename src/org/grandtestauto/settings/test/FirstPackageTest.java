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
import org.grandtestauto.settings.FirstPackage;

import java.util.Properties;

/**
 * @author Tim Lavers
 */
public class FirstPackageTest extends PackageSpecTest {

    public boolean keyTest() {
        init();
        Assert.aequals(FirstPackage.FIRST_PACKAGE, new FirstPackage().key());
        return true;
    }

    public boolean buildFromTest() {
        init();
        Properties properties = new Properties();
        FirstPackage ip = new FirstPackage();
        ip.buildFrom(properties);
        ip.addTo(settings);
        assert settings.packageNameFilter().accept("a");//Nothing has been filtered out.

        properties.put(FirstPackage.FIRST_PACKAGE, "org.strauss");
        ip = new FirstPackage();
        ip.buildFrom(properties);
        ip.addTo(settings);
        assert !settings.packageNameFilter().accept("a");

        return true;
    }

    public boolean addToTest() {
        //Tested above
        return true;
    }

    public boolean valueInUserExplanationTest() {
        init();
        Properties properties = new Properties();
        FirstPackage ip = new FirstPackage();
        ip.buildFrom(properties);
        ip.addTo(settings);
        Assert.aequals("", ip.valueInUserExplanation(settings));

        properties.put(FirstPackage.FIRST_PACKAGE, "org.strauss");
        ip = new FirstPackage();
        ip.buildFrom(properties);
        ip.addTo(settings);
        Assert.aequals("org.strauss", ip.valueInUserExplanation(settings));

        return true;
    }
}
