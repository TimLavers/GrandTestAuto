/****************************************************************************
 * Copyright 2012 Timothy Gordon Lavers (Australia)
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
package org.grandtestauto;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Tim Lavers
 */
class RunsTestsInPackages extends DPWImpl {

    Boolean invokeRun( String packageName, UnitTesterIF ut ) {
        Boolean runResult;
        try {
            runResult = ut.runTests();
        } catch (Throwable e) {
            e.printStackTrace();
            String msg = Messages.message( Messages.OPK_ERROR_RUNNING_UNIT_TESTER_TEST, packageName );
            gta.resultsLogger().log( msg, null );
            runResult = false;
        }
        return runResult;
    }

    public PackageResult runAutoLoadTestPackage( boolean areFunctionTests, Collection<String> classesInPackage, String testPackageName ) {
        String key;
        key = areFunctionTests ? Messages.OPK_RUNNING_FUNCTION_TEST_PACKAGE : Messages.OPK_RUNNING_LOAD_TEST_PACKAGE;
        gta.resultsLogger().log( Messages.message( key, testPackageName ), null );
        boolean packageResult = true;
        PackageResultImpl result = new PackageResultImpl();
        //Log that the tests are to be run.
        for (String testName : classesInPackage) {
            //Shortcut the tests for this package if a test has failed.
            if (!gta.continueWithTests( packageResult )) break;

            //Ignore any tests that are filtered out by package name.
            if (gta.classIsToBeSkippedBecauseOfSettings( testName )) continue;
            //Get the class. It is a AutoLoadTest.
            String fullClassName = testPackageName + "." + testName;
            try {
                //Run the test and add the result to the overall package result.
                AutoLoadTestRun altr = new AutoLoadTestRun(fullClassName, testName, gta.resultsLogger());
                packageResult &= altr.runAutoLoadTest();
            } catch (InstantiationException e) {
                String msg = Messages.message( Messages.OPK_AUTO_LOAD_TEST_DOES_NOT_HAVE_REQURIED_CONSTRUCTOR, testName );
                gta.resultsLogger().log( msg, null );
                packageResult = false;
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                //
                System.out.println( "XXXXX unexpected error, could not find: " + fullClassName );
            }
        }
        result.setResult( packageResult );
        return result;
    }

    @Override
    public List<String> preliminaryMessages() {
        List<String> result = new LinkedList<>();
        if (!gta.settings().lessVerboseLogging()) {
            result.add(Messages.message(Messages.SK_ABOUT_TO_RUN_TESTS));
            result.add(gta.settings().summary());
        }
        return result;
    }

    @Override
    public String messageForFinalResult(boolean passOrFail) {
        String pf = Messages.passOrFail(passOrFail);
        return Messages.message(Messages.OPK_OVERALL_GTA_RESULT, pf);
    }
}
