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
import org.grandtestauto.settings.FirstMethod;
import org.grandtestauto.settings.SingleClass;
import org.grandtestauto.settings.SinglePackage;

import java.util.Properties;

/**
 * @author Tim Lavers
 */
public class FirstMethodTest extends PackageSpecTest {

    public boolean keyTest() {
        init();
        Assert.aequals(FirstMethod.FIRST_METHOD, new FirstMethod().key());
        return true;
    }

    public boolean buildFromTest() {
        init();
        Properties properties = new Properties();
        FirstMethod ip = new FirstMethod();
        ip.buildFrom(properties);
        ip.addTo(settings);

        assert settings.methodNameFilter().accept("aardvark");//Nothing has been filtered out.
        assert settings.methodNameFilter().accept("lion");
        assert settings.methodNameFilter().accept("zebra");

        //Set the single package properties.
        properties.put(SinglePackage.SINGLE_PACKAGE, "org.animals");
        properties.put(SingleClass.SINGLE_CLASS, "Mammals");
        SinglePackage sp = new SinglePackage();
        sp.buildFrom(properties);
        sp.addTo(settings);
        SingleClass sc = new SingleClass();
        sc.buildFrom(properties);
        sc.addTo(settings);

        properties.put(FirstMethod.FIRST_METHOD, "lion");
        ip = new FirstMethod();
        ip.buildFrom(properties);
        ip.addTo(settings);

        assert !settings.methodNameFilter().accept("aardvark");
        assert settings.methodNameFilter().accept("zebra");

        return true;
    }

    public boolean addToTest() {
        init();
        Properties properties = new Properties();
        FirstMethod ip = new FirstMethod();
        ip.buildFrom(properties);
        ip.addTo(settings);
        properties.put(SinglePackage.SINGLE_PACKAGE, "org.animals");
        properties.put(SingleClass.SINGLE_CLASS, "Mammals");
        properties.put(FirstMethod.FIRST_METHOD, "");
        SinglePackage sp = new SinglePackage();
        sp.buildFrom(properties);
        sp.addTo(settings);
        SingleClass sc = new SingleClass();
        sc.buildFrom(properties);
        sc.addTo(settings);
        ip = new FirstMethod();
        ip.buildFrom(properties);
        ip.addTo(settings);

        assert settings.methodNameFilter().accept("aardvark");
        assert settings.methodNameFilter().accept("zebra");

        properties.put(FirstMethod.FIRST_METHOD, " ");
        ip = new FirstMethod();
        ip.buildFrom(properties);
        ip.addTo(settings);

        assert settings.methodNameFilter().accept("aardvark");
        assert settings.methodNameFilter().accept("zebra");

        return true;
    }

    public boolean valueInUserExplanationTest() {
        init();
        FirstMethod ip = new FirstMethod();
        Assert.aequals("", ip.valueInUserExplanation(settings));

        Properties properties = new Properties();
        properties.put(SinglePackage.SINGLE_PACKAGE, "org.animals");
        properties.put(SingleClass.SINGLE_CLASS, "Aardwolf");
        properties.put(FirstMethod.FIRST_METHOD, "grunt");
        SinglePackage sp = new SinglePackage();
        sp.buildFrom(properties);
        sp.addTo(settings);
        SingleClass sc = new SingleClass();
        sc.buildFrom(properties);
        sc.addTo(settings);
        ip = new FirstMethod();
        ip.buildFrom(properties);
        ip.addTo(settings);
        Assert.aequals("grunt", ip.valueInUserExplanation(settings));
        return true;
    }
}
