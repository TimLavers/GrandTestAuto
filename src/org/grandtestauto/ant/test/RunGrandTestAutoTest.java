/****************************************************************************
 *
 * Name: RunGrandTestAutoTest.java
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
package org.grandtestauto.ant.test;

import org.apache.tools.ant.BuildException;
import org.grandtestauto.Messages;
import org.grandtestauto.ant.RunGrandTestAuto;
import org.grandtestauto.test.Helpers;
import org.grandtestauto.test.dataconstants.org.grandtestauto.Grandtestauto;

import java.io.File;

/**
 * Unit test for <code>RunGrandTestAuto</code>.
 *
 * @author Tim Lavers
 */
public class RunGrandTestAutoTest {

    private static String pingString;

    public static void ping(Class c) {
        pingString = c.getName();
    }

    public boolean constructorTest() {
        //The single constructor is tested quite
        //effectively in executeTest().
        return true;
    }

    public boolean executeTest() throws Exception {
        //This test works by expanding a configured class hierarchy
        //into the testing temp dir. A RunGrandTestAuto is created
        //and set to run GTA on this dir. The test class is defined to
        //call ping(). The test will pass if ping() has been seen
        //to be called.
        Helpers.setupForZip(Grandtestauto.test9_zip);
        RunGrandTestAuto rgta = new RunGrandTestAuto();
        File settingsFile = new File(Helpers.tempDirectory(), "TestSettings.txt");
        rgta.setSettingsFileName(settingsFile.getAbsolutePath());
        rgta.execute();
        return pingString.equals("a9.test.XTest");
    }

    public boolean executeFailedTest() throws Exception {
        boolean result;
        Helpers.setupForZip(Grandtestauto.test10_zip);
        File settingsFile = new File(Helpers.tempDirectory(), "TestSettings.txt");
        RunGrandTestAuto task = new RunGrandTestAuto();
        task.setSettingsFileName(settingsFile.getAbsolutePath());
        try {
            task.execute();
            //If we got here, no exception was thrown.
            result = false;
            assert false : "RunGrandTestAuto should have thrown an exception";
        } catch (BuildException exception) {
            String failed = Messages.message(Messages.SK_FAILED);
            result = exception.getMessage().contains(failed);
        }
        return result;
    }

    public boolean setSettingsFileNameTest() {
        //This is tested quite effectively in executeTest().
        return true;
    }
}