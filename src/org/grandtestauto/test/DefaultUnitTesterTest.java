/****************************************************************************
 *
 * Name: CoverageUnitTester.java
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
package org.grandtestauto.test;

import org.grandtestauto.DefaultUnitTester;
import org.grandtestauto.GrandTestAuto;
import org.grandtestauto.assertion.Assert;
import org.grandtestauto.test.dataconstants.org.grandtestauto.Grandtestauto;

import java.io.File;

/**
 * @author Tim Lavers
 */
public class DefaultUnitTesterTest {
    public boolean coverageWithSplitClassHierarchyTest() throws Exception {
        init();
        GrandTestAuto gta = Helpers.setupForZipWithSeparateSourceAndTestClassRoots(new File(Grandtestauto.test130_zip));
        DefaultUnitTester dut = new DefaultUnitTester(gta, "a130.test");
        assert dut.runTests();
        return true;
    }

    public boolean constructorTest() throws Exception {
        init();
        GrandTestAuto gta = Helpers.setupForZip( Grandtestauto.test12_zip );
        DefaultUnitTester dut = new DefaultUnitTester(gta, "a12.test");
        boolean testResult = dut.runTests();
        Assert.azzert(testResult, "Tests should have passed.");
        return true;
    }

    private void init() {
        Helpers.cleanTempDirectory();
    }
}