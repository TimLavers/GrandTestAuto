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
import org.grandtestauto.settings.SingleClass;
import org.grandtestauto.settings.SinglePackage;

import java.util.Properties;

/**
 * @author Tim Lavers
 */
public class SingleClassTest extends PackageSpecTest {

    public boolean buildFromTest() {
        init();
        Properties properties = new Properties();
        SingleClass ip = new SingleClass();
        ip.buildFrom(properties);
        ip.addTo(settings);

        assert settings.classNameFilter().accept("Aardvark");//Nothing has been filtered out.
        assert settings.classNameFilter().accept("Lion");
        assert settings.classNameFilter().accept("Zebra");

        //Set the single package properties.
        properties.put(SinglePackage.SINGLE_PACKAGE, "org.animals");
        SinglePackage sp = new SinglePackage();
        sp.buildFrom(properties);
        sp.addTo(settings);

        properties.put(SingleClass.SINGLE_CLASS, "Lion");
        ip = new SingleClass();
        ip.buildFrom(properties);
        ip.addTo(settings);

        assert !settings.classNameFilter().accept("Aardvark");
        assert settings.classNameFilter().accept("Lion");
        assert !settings.classNameFilter().accept("Zebra");
        return true;
    }

    public boolean addToTest() {
        //Tested above
        return true;
    }

    public boolean valueInUserExplanationTest() {
        init();
        Properties properties = new Properties();
        SingleClass ip = new SingleClass();
        ip.buildFrom(properties);
        ip.addTo(settings);

        Assert.aequals("", ip.valueInUserExplanation(settings));

        //Set the single package properties.
        properties.put(SinglePackage.SINGLE_PACKAGE, "org.animals");
        SinglePackage sp = new SinglePackage();
        sp.buildFrom(properties);
        sp.addTo(settings);

        properties.put(SingleClass.SINGLE_CLASS, "Aardwolf");
        ip = new SingleClass();
        ip.buildFrom(properties);
        ip.addTo(settings);
        Assert.aequals("Aardwolf", ip.valueInUserExplanation(settings));
        return true;
    }

    public boolean keyTest() {
        init();
        Assert.aequals(SingleClass.SINGLE_CLASS, new SingleClass().key());
        return true;
    }
}
