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
import org.grandtestauto.settings.SinglePackage;

import java.util.Properties;

/**
 * @author Tim Lavers
 */
public class SinglePackageTest extends PackageSpecTest {

    public boolean buildFromTest() {
        init();
        Properties properties = new Properties();
        SinglePackage ip = new SinglePackage();
        ip.buildFrom(properties);
        ip.addTo(settings);
        assert settings.packageNameFilter().accept("a");//Nothing has been filtered out.
        assert settings.packageNameFilter().accept("org.strauss");//Nothing has been filtered out.
        assert settings.packageNameFilter().accept("z");//Nothing has been filtered out.

        properties.put(SinglePackage.SINGLE_PACKAGE, "org.strauss");
        ip = new SinglePackage();
        ip.buildFrom(properties);
        ip.addTo(settings);
        assert !settings.packageNameFilter().accept("a");
        assert settings.packageNameFilter().accept("org.strauss");//Nothing has been filtered out.
        assert !settings.packageNameFilter().accept("z");

        return true;
    }

    public boolean addToTest() {
        //Tested above
        return true;
    }

    public boolean valueInUserExplanationTest() {
        init();
        Properties properties = new Properties();
        SinglePackage ip = new SinglePackage();
        ip.buildFrom(properties);
        ip.addTo(settings);

        Assert.aequals("", ip.valueInUserExplanation(settings));

        //Set the single package properties.
        properties.put(SinglePackage.SINGLE_PACKAGE, "org.animals");
        ip.buildFrom(properties);
        ip.addTo(settings);
        Assert.aequals("org.animals", ip.valueInUserExplanation(settings));
        return true;
    }

    public boolean keyTest() {
        init();
        Assert.aequals(SinglePackage.SINGLE_PACKAGE, new SinglePackage().key());
        return true;
    }
}
