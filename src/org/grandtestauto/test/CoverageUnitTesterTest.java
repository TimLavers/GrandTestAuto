/**
 * *************************************************************************
 * <p/>
 * Name: CoverageUnitTester.java
 * <p/>
 * Synopsis: See javadoc class comments.
 * <p/>
 * Description: See javadoc class comments.
 * <p/>
 * Copyright 2002 Timothy Gordon Lavers (Australia)
 * <p/>
 * The Wide Open License (WOL)
 * <p/>
 * Permission to use, copy, modify, distribute and sell this software and its
 * documentation for any purpose is hereby granted without fee, provided that
 * the above copyright notice and this license appear in all source copies.
 * THIS SOFTWARE IS PROVIDED "AS IS" WITHOUT EXPRESS OR IMPLIED WARRANTY OF
 * ANY KIND. See http://www.dspguru.com/wol.htm for more information.
 * <p/>
 * ***************************************************************************
 */
package org.grandtestauto.test;

import org.grandtestauto.CoverageUnitTester;
import org.grandtestauto.GrandTestAuto;
import org.grandtestauto.Messages;
import org.grandtestauto.assertion.Assert;
import org.grandtestauto.test.dataconstants.org.grandtestauto.Grandtestauto;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * Unit test for <code>CoverageUnitTester</code>.
 *
 * @author Tim Lavers
 */
public class CoverageUnitTesterTest {

    private static List<String> methodsCalled = new LinkedList<String>();
    private static Set<String> testsCreated = new HashSet<String>();

    public static void ping(String methodCalled) {
        methodsCalled.add(methodCalled);
    }

    public static void recordTestCreated(Class testClass) {
        testsCreated.add(testClass.getName());
    }

//    public boolean coverageWithSplitClassHierarchyTest() throws Exception {
//        CoverageUnitTester cut = cutForConfiguredPackageWithSplitClasses(Grandtestauto.test30_zip, "a130.test");
//        assert cut.runTests();
//        return true;
//    }

    public boolean classAnnotatedWithHomeGrownDoesNotNeedTestDoesNotNeedTest() throws Exception {
        CoverageUnitTester cut = cutForConfiguredPackage(Grandtestauto.test122_zip, "a122.test", null, null, null, null, null);
        assert cut.runTests();
        return true;
    }

    public boolean classAnnotatedAsGeneratedDoesNotNeedTest() throws Exception {
        CoverageUnitTester cut = cutForConfiguredPackage(Grandtestauto.test117_zip, "a117.test", null, null, null, null, null);
        assert cut.runTests();
        return true;
    }

    public boolean repeatFlakyTestsThatThrowExceptionsTest() throws Exception {
        init();
        CoverageUnitTester cut = cutForConfiguredPackage(Grandtestauto.test115_zip, "a115.a.test", null, null, null, null, null);
        boolean result = cut.runTests();
        Assert.azzertFalse(result, "Tests should not have passed!");
        Assert.aequals(5, methodsCalled.size());
        for (int i = 0; i < 5; i++) {
            Assert.azzert(methodsCalled.contains("aTest: " + i));
        }
        //The failure is logged.
        Assert.azzert(Helpers.logFileContents().contains("aTest failed"));
        Assert.azzertFalse(Helpers.logFileContents().contains("aTest passed"));
        return true;
    }

    public boolean repeatFlakyTestsTest() throws Exception {
        init();
        CoverageUnitTester cut = cutForConfiguredPackage(Grandtestauto.test114_zip, "a114.a.test", null, null, null, null, null);
        boolean result = cut.runTests();
        Assert.azzertFalse(result, "Tests should not have passed!");
        Assert.aequals(5, methodsCalled.size());
        for (int i = 0; i < 5; i++) {
            Assert.azzert(methodsCalled.contains("aTest: " + i));
        }
        //The failure is logged.
        Assert.azzert(Helpers.logFileContents().contains("aTest failed"));
        Assert.azzertFalse(Helpers.logFileContents().contains("aTest passed"));
        return true;
    }

    public boolean repeatFlakyTestUntilItPassesTest() throws Exception {
        init();
        CoverageUnitTester cut = cutForConfiguredPackage(Grandtestauto.test113_zip, "a113.a.test", null, null, null, null, null);
        boolean result = cut.runTests();
        Assert.azzert(result, "Tests should have passed!");
        Assert.aequals(3, methodsCalled.size());
        int i = 0;
        for (String methodCalled : methodsCalled) {
            Assert.aequals("aTest: " + i, methodCalled);
            i++;
        }
        //Only the successful run is logged.
        Assert.azzertFalse(Helpers.logFileContents().contains("aTest failed"));
        Assert.azzert(Helpers.logFileContents().contains("aTest passed"));
        return true;
    }

    public boolean repeatFlakyTestThatThrowsExceptionUntilItPassesTest() throws Exception {
        init();
        CoverageUnitTester cut = cutForConfiguredPackage(Grandtestauto.test121_zip, "a121.a.test", null, null, null, null, null);
        System.out.println(">>>>>> EXPECTED STACK TRACE START <<<<<<");
        boolean result = cut.runTests();
        System.out.println(">>>>>> EXPECTED STACK TRACE END <<<<<<");
        Assert.azzert(result, "Tests should have passed!");
        return true;
    }

    public boolean abstractSubclassOfAbstractClassTest() throws Exception {
        CoverageUnitTester cut = cutForConfiguredPackage(Grandtestauto.test109_zip, "a109.test", null, null, null, null, null);
        boolean result = cut.runTests();
        Assert.azzert(result, "Tests should have passed!");
        return true;
    }

    public boolean classNotFoundExceptionInNonMandatoryTestTest() throws Exception {
        CoverageUnitTester cut = cutForConfiguredPackage(Grandtestauto.test108_zip, "a108.test", null, null, null, null, null);
        System.out.println(">>>>>> EXPECTED STACK TRACE START <<<<<<");
        boolean result = cut.runTests();//This unit test does not actually pass, no matter.
        System.out.println(">>>>>> EXPECTED STACK TRACE END <<<<<<");
        Assert.azzertFalse(result, "Tests should have failed!");
        return true;
    }

    public boolean autoCleanupTest() throws Exception {
        init();
        CoverageUnitTester cut = cutForConfiguredPackage(Grandtestauto.test102_zip, "a102.test", null, null, null, null, null);
        System.out.println(">>>>>> EXPECTED STACK TRACE START <<<<<<");
        cut.runTests();//This unit test does not actually pass, no matter.
        System.out.println(">>>>>> EXPECTED STACK TRACE END <<<<<<");
        List<String> expected = new LinkedList<>();
        expected.add("a102.test.ATest.cleanup");
        Helpers.assertEqual(methodsCalled, expected);
        return true;
    }

    public boolean autoCleanupThrowsExceptionTest() throws Exception {
        init();
        CoverageUnitTester cut = cutForConfiguredPackage(Grandtestauto.test103_zip, "a103.test", null, null, null, null, null);
        System.out.println(">>>>>> EXPECTED STACK TRACE START <<<<<<");
        cut.runTests();//This unit test does not actually pass, no matter.
        List<String> expected = new LinkedList<>();
        expected.add("a103.test.ATest.cleanup");
        expected.add("a103.test.ATest.extraTest");//Should have happened despite the cleanup problems.
        System.out.println(">>>>>> EXPECTED STACK TRACE END <<<<<<");
        Helpers.assertEqual(methodsCalled, expected);
        //Check that the error was logged.
        String logFileContents = Helpers.logFileContents();
        String msgForError = Messages.message(Messages.OPK_CLEANUP_THREW_THROWABLE, "Throwable deliberately thrown in test.");
        assert logFileContents.contains(msgForError);
        return true;
    }

    public boolean autoCleanupMethodNotPublicTest() throws Exception {
        init();
        CoverageUnitTester cut = cutForConfiguredPackage(Grandtestauto.test104_zip, "a104.test", null, null, null, null, null);
        System.out.println(">>>>>> EXPECTED STACK TRACE START <<<<<<");
        cut.runTests();//This unit test does not actually pass, no matter.
        System.out.println(">>>>>> EXPECTED STACK TRACE END <<<<<<");
        Helpers.assertEqual(methodsCalled.size(), 0);
        //THIS TEST WAS written before I realised that the getMethods() call
        //used to search for a cleanup method would only return public methods.
        //So the cleanup method is never found, never called, and the error
        //messages is not logged.
        String logFileContents = Helpers.logFileContents();
        String msgForError = Messages.message(Messages.SK_CLEANUP_NOT_PUBLIC);
        assert !logFileContents.contains(msgForError);
        return true;
    }

    public boolean autoCleanupMethodNotNoArgsTest() throws Exception {
        init();
        CoverageUnitTester cut = cutForConfiguredPackage(Grandtestauto.test105_zip, "a105.test", null, null, null, null, null);
        System.out.println(">>>>>> EXPECTED STACK TRACE START <<<<<<");
        cut.runTests();//This unit test does not actually pass, no matter.
        System.out.println(">>>>>> EXPECTED STACK TRACE END <<<<<<");
        Helpers.assertEqual(methodsCalled.size(), 0);//The cleanup method would ping were it called.
        String logFileContents = Helpers.logFileContents();
        String msgForError = Messages.message(Messages.SK_CLEANUP_NOT_NO_ARGS);
        assert logFileContents.contains(msgForError);
        return true;
    }

    public boolean unitTestIdentificationTest() throws Exception {
        init();
        CoverageUnitTester cut = cutForConfiguredPackage(Grandtestauto.test46_zip, "a46.test", null, null, null, null, null);
        cut.runTests();
        assert testsCreated.contains("a46.test.XTest");
        assert testsCreated.contains("a46.test.ATest");
        String logFileContents = Helpers.logFileContents();
        String msgForYTest = Messages.message(Messages.OPK_COULD_NOT_CREATE_TEST_CLASS, "a46.test.YTest");
        assert logFileContents.contains(msgForYTest);
        String msgForZTest = Messages.message(Messages.OPK_COULD_NOT_CREATE_TEST_CLASS, "a46.test.ZTest");
        assert logFileContents.contains(msgForZTest);

        return true;
    }

    public boolean runTestsTest() throws Exception {
        init();

        /* Unit Test Identification */
        CoverageUnitTester cut = cutForConfiguredPackage(Grandtestauto.test46_zip, "a46.test", null, null, null, null, null);
        cut.runTests();
        assert testsCreated.contains("a46.test.XTest");
        assert testsCreated.contains("a46.test.ATest");
        String logFileContents = Helpers.logFileContents();
        String msgForYTest = Messages.message(Messages.OPK_COULD_NOT_CREATE_TEST_CLASS, "a46.test.YTest");
        assert logFileContents.contains(msgForYTest);
        String msgForZTest = Messages.message(Messages.OPK_COULD_NOT_CREATE_TEST_CLASS, "a46.test.ZTest");
        assert logFileContents.contains(msgForZTest);

        /* Test Method Identification */
        init();
        cut = cutForConfiguredPackage(Grandtestauto.test17_zip, "a17.test", null, null, null, null, null);
        cut.runTests();//This unit test does not actually pass, no matter.
        List<String> expected = new LinkedList<>();
        expected.add("mTest");
        expected.add("nTest");
        Helpers.assertEqual(methodsCalled, expected);

        /* Test Returns False */
        cut = cutForConfiguredPackage(Grandtestauto.test14_zip, "a14.test", null, null, null, null, null);
        assert !cut.runTests();

        /* Exception Thrown by Test */
        System.out.println(">>>>>> EXPECTED STACK TRACE START <<<<<<");
        cut = cutForConfiguredPackage(Grandtestauto.test23_zip, "a23.test", null, null, null, null, null);
        assert !cut.runTests();
        System.out.println(">>>>>> EXPECTED STACK TRACE END <<<<<<");

        /* Tests for Methods */
        //Public method not tested.
        cut = cutForConfiguredPackage(Grandtestauto.test5_zip, "a5.test", null, null, null, null, null);
        assert !cut.runTests();
        logFileContents = Helpers.logFileContents();
        //This tests the locale specific form of the error message. Will only work in _en locales.
        String msgForMissingTest = "In a5 the following method is not unit-tested:" + Helpers.NL + "public void a5.X.m()";
        assert logFileContents.contains(msgForMissingTest) : logFileContents;

        //Test for overridden method missing.
        cut = cutForConfiguredPackage(Grandtestauto.test26_zip, "a26.test", null, null, null, null, null);
        assert !cut.runTests();
        logFileContents = Helpers.logFileContents();
        msgForMissingTest = "In a26 the following method is not unit-tested:" + Helpers.NL + "protected void a26.A.a()";
        assert logFileContents.contains(msgForMissingTest) : logFileContents;

        //Overridden method from abstract class missing test
        cut = cutForConfiguredPackage(Grandtestauto.test27_zip, "a27.test", null, null, null, null, null);
        assert !cut.runTests();
        logFileContents = Helpers.logFileContents();
        msgForMissingTest = "In a27 the following method is not unit-tested:" + Helpers.NL + "protected void a27.A.a()";
        assert logFileContents.contains(msgForMissingTest) : logFileContents;

        //Another example of overridden method not being tested.
        cut = cutForConfiguredPackage(Grandtestauto.test28_zip, "a28.test", null, null, null, null, null);
        assert !cut.runTests();
        logFileContents = Helpers.logFileContents();
        msgForMissingTest = "In a28 the following method is not unit-tested:" + Helpers.NL + "protected void a28.A.a()";
        assert logFileContents.contains(msgForMissingTest) : logFileContents;

        //Yet another example of overridden method not being tested.
        cut = cutForConfiguredPackage(Grandtestauto.test32_zip, "a32.test", null, null, null, null, null);
        assert !cut.runTests();
        logFileContents = Helpers.logFileContents();
        msgForMissingTest = "In a32 the following method is not unit-tested:" + Helpers.NL + "protected void a32.A.a()";
        assert logFileContents.contains(msgForMissingTest) : logFileContents;

        //Overridden method is tested in test for abstract class.
        cut = cutForConfiguredPackage(Grandtestauto.test29_zip, "a29.test", null, null, null, null, null);
        assert cut.runTests();

        //Overridden method is tested in test for concrete class.
        cut = cutForConfiguredPackage(Grandtestauto.test30_zip, "a30.test", null, null, null, null, null);
        assert cut.runTests();

        //Overridden method is tested in test for concrete class, no test for the abstract class.
        cut = cutForConfiguredPackage(Grandtestauto.test31_zip, "a31.test", null, null, null, null, null);
        assert cut.runTests();

        /* Synthetic Methods do not Need Testing */
        cut = cutForConfiguredPackage(Grandtestauto.test18_zip, "a18.test", null, null, null, null, null);
        assert cut.runTests();

        /* Some Enum Methods do not Need Testing */
        cut = cutForConfiguredPackage(Grandtestauto.test19_zip, "a19.test", null, null, null, null, null);
        assert cut.runTests();

        /* Stop at First Failure Within Unit Test */
        cut = cutForConfiguredPackage(Grandtestauto.test19_zip, "a19.test", null, null, null, null, null);
        assert cut.runTests();

        /* Name Mangling for Method Tests*/
        cut = cutForConfiguredPackage(Grandtestauto.test47_zip, "a47.test", null, null, null, null, null);
        cut.runTests();
        logFileContents = Helpers.logFileContents();
        String listOfUntestedMethods = "In a47 the following methods are not unit-tested:" + Helpers.NL +
                "public void a47.W.a(java.lang.String)" + Helpers.NL +
                "public void a47.X.b(int,int)" + Helpers.NL +
                "public void a47.Y.c(java.lang.String[],int,int[]).";
        assert logFileContents.contains(listOfUntestedMethods) : "Got: '" + logFileContents + "'";

        /* Name Mangling for VarArgs Methods */
        cut = cutForConfiguredPackage(Grandtestauto.test16_zip, "a16.test", null, null, null, null, null);
        init();
        assert cut.runTests();
        Helpers.assertEqual(methodsCalled.size(), 4);
        List<String> expectedMethodsCalled = new LinkedList<>();
        expectedMethodsCalled.add("a_StringArray_StringArray_Test");
        expectedMethodsCalled.add("a_StringArray_Test");
        expectedMethodsCalled.add("a_String_StringArray_Test");
        expectedMethodsCalled.add("a_String_Test");
        Helpers.assertEqual(methodsCalled, expectedMethodsCalled);

        /* Name Mangling not Needed on Account of Hidden Methods */
        cut = cutForConfiguredPackage(Grandtestauto.test48_zip, "a48.test", null, null, null, null, null);
        assert cut.runTests();

        /* Testable Classes */
        cut = cutForConfiguredPackage(Grandtestauto.test4_zip, "a4.test", null, null, null, null, null);
        assert !cut.runTests();
        logFileContents = Helpers.logFileContents();
        msgForMissingTest = "In a4 the following class is not unit-tested:" + Helpers.NL + "X";
        assert logFileContents.contains(msgForMissingTest) : logFileContents;

        /* Inner Classes not Tested */
        cut = cutForConfiguredPackage(Grandtestauto.test24_zip, "a24.test", null, null, null, null, null);
        assert cut.runTests();

        /* RMI Stub Classes not Tested */
        cut = cutForConfiguredPackage(Grandtestauto.test22_zip, "a22.test", null, null, null, null, null);
        assert cut.runTests();

        /* Classes annotated with DoesNotNeedTest do not need test */
        cut = cutForConfiguredPackage(Grandtestauto.test86_zip, "a86.test", null, null, null, null, null);
        assert cut.runTests();

        /* Bug whereby a package with no testable classes caused a NullPointerException. */
        cut = cutForConfiguredPackage(Grandtestauto.test87_zip, "a87.test", null, null, null, null, null);
        assert cut.runTests();

        /* Constructors for Testable Classes Need Testing */
        //protected constructor untested - We now allow this if there is only one constructor
        cut = cutForConfiguredPackage(Grandtestauto.test12_zip, "a12.test", null, null, null, null, null);
        assert cut.runTests();
        logFileContents = Helpers.logFileContents();

        //name mangling
        cut = cutForConfiguredPackage(Grandtestauto.test49_zip, "a49.test", null, null, null, null, null);
        assert !cut.runTests();
        logFileContents = Helpers.logFileContents();
        String listOfUntestedConstructors = "In a49 the following constructors were not tested:" + Helpers.NL +
                "public a49.W(java.lang.String)" + Helpers.NL +
                "public a49.Z(java.lang.String)"  + Helpers.NL +
                "public a49.Y(java.lang.String[],int,int[])"  + Helpers.NL +
                "public a49.X(int,int).";
        assert logFileContents.contains(listOfUntestedConstructors) : "Got: '" + logFileContents + "'.";

        //Success.
        cut = cutForConfiguredPackage(Grandtestauto.test50_zip, "a50.test", null, null, null, null, null);
        assert !cut.runTests();

        /* Other tests. */
        //Protected method untested.
        cut = cutForConfiguredPackage(Grandtestauto.test11_zip, "a11.test", null, null, null, null, null);
        assert !cut.runTests();

        //Re-definition of method not tested.
        cut = cutForConfiguredPackage(Grandtestauto.test13_zip, "a13.test", null, null, null, null, null);
        assert !cut.runTests();

        //Complex hierarchy based on a real example.
        cut = cutForConfiguredPackage(Grandtestauto.test33_zip, "a33.test", null, null, null, null, null);
        assert cut.runTests();

        //Extra test method (i.e. not corresponding to a method or constructor) returns false.
        cut = cutForConfiguredPackage(Grandtestauto.test95_zip, "a95.test", null, null, null, null, null);
        assert !cut.runTests();

        //Extra test method (i.e. not corresponding to a method or constructor) throws an exception.
        System.out.println(">>>>>> EXPECTED STACK TRACE START <<<<<<");
        cut = cutForConfiguredPackage(Grandtestauto.test96_zip, "a96.test", null, null, null, null, null);
        boolean runResult = cut.runTests();
        System.out.println(">>>>>> EXPECTED STACK END START <<<<<<");
        assert !runResult;//If this fails, we don't want it mixed up with the expected stack trace.

        //Extra test class has a test method that returns false.
        cut = cutForConfiguredPackage(Grandtestauto.test98_zip, "a98.test", null, null, null, null, null);
        assert !cut.runTests();

        //Extra test class has a test method that throws an exception.
        System.out.println(">>>>>> EXPECTED STACK TRACE START <<<<<<");
        cut = cutForConfiguredPackage(Grandtestauto.test97_zip, "a97.test", null, null, null, null, null);
        runResult = cut.runTests();
        System.out.println(">>>>>> EXPECTED STACK END START <<<<<<");
        assert !runResult;//If this fails, we don't want it mixed up with the expected stack trace.

        //A bug. There was a failure in the mandatory tests, but no problems with
        //the extra tests. The overall result was 'true'.
        cut = cutForConfiguredPackage(Grandtestauto.test101_zip, "a101.test", null, null, null, null, null);
        assert !cut.runTests();

        return true;
    }

    /**
     * This test is to help find a problem with one of the function tests.
     */
    public boolean a1Test() {
        CoverageUnitTester cut = cutForConfiguredPackage(Grandtestauto.test1_zip, "a1.test", null, null, null, null, null);
        assert !cut.runTests();
        String log = Helpers.logFileContents();
        String errMsg = "In a1 the following classes are not unit-tested:" + Helpers.NL +
                "X" + Helpers.NL +
                "Y.";
        assert log.contains(errMsg) : "Got: '" + log + "'";
        return true;
    }

    public boolean overriddentParametrisedMethodTest() {
        CoverageUnitTester cut = cutForConfiguredPackage(Grandtestauto.test84_zip, "a84.test", null, null, null, null, null);
        assert cut.runTests() : "log was: " + Helpers.logFileContents();
        return true;
    }

    public boolean restrictionOfMethodsRunTest() {
        //First a sanity check: with no restrictions, all tests run.
        CoverageUnitTester cut = cutForConfiguredPackage(Grandtestauto.test83_zip, "a83.test", null, null, null, null, null);
        methodsCalled.clear();
        cut.runTests();
        List<String> expected = new LinkedList<>();
        expected.add("constructor_A_Test");
        expected.add("aTest");
        expected.add("bTest");
        expected.add("cTest");
        expected.add("dTest");
        expected.add("eTest");
        expected.add("constructor_B_Test");
        expected.add("xTest");
        expected.add("x_int_Test");
        expected.add("x_String_Test");
        expected.add("yTest");
        expected.add("y_intArray_Test");
        Helpers.assertSameContents(methodsCalled, expected);

        //Run just ATest, and only methods b to dTest.
        cut = cutForConfiguredPackage(Grandtestauto.test83_zip, "a83.test", "a83", "A", "b", "dTest", null);
        methodsCalled.clear();
        cut.runTests();
        expected = new LinkedList<>();
        expected.add("bTest");
        expected.add("cTest");
        expected.add("constructor_A_Test");
        expected.add("dTest");
        Helpers.assertSameContents(methodsCalled, expected);

        //Run just ATest, and only methods to d.
        cut = cutForConfiguredPackage(Grandtestauto.test83_zip, "a83.test", "a83", "A", null, "d", null);
        methodsCalled.clear();
        cut.runTests();
        expected = new LinkedList<>();
        expected.add("aTest");
        expected.add("bTest");
        expected.add("cTest");
        expected.add("constructor_A_Test");
        Helpers.assertSameContents(methodsCalled, expected);

        //Run just ATest, and only methods from c.
        cut = cutForConfiguredPackage(Grandtestauto.test83_zip, "a83.test", "a83", "A", "c", null, null);
        methodsCalled.clear();
        cut.runTests();
        expected = new LinkedList<>();
        expected.add("cTest");
        expected.add("constructor_A_Test");
        expected.add("dTest");
        expected.add("eTest");
        Helpers.assertSameContents(methodsCalled, expected);

        //Run just ATest, and only cTest..
        cut = cutForConfiguredPackage(Grandtestauto.test83_zip, "a83.test", "a83", "A", null, null, "cTest");
        methodsCalled.clear();
        cut.runTests();
        expected = new LinkedList<>();
        expected.add("cTest");
        Helpers.assertSameContents(methodsCalled, expected);

        //Single method overrides other settings
        cut = cutForConfiguredPackage(Grandtestauto.test83_zip, "a83.test", "a83", "A", "a", "d", "cTest");
        methodsCalled.clear();
        cut.runTests();
        expected = new LinkedList<>();
        expected.add("cTest");
        Helpers.assertSameContents(methodsCalled, expected);

        return true;
    }

    public boolean constructorTest() throws Exception {
        GrandTestAuto gta = Helpers.setupForZip(Grandtestauto.test4_zip);
        Class<?> ut = Class.forName("a4.test.UnitTester");
        //Check that the flag is initially false.
        Field flag = ut.getDeclaredField("flag");
        assert !flag.getBoolean(null) : "Flag not initially false";
        Constructor constructor = ut.getConstructor(GrandTestAuto.class);
        CoverageUnitTester cut = (CoverageUnitTester) constructor.newInstance(gta);
        assert !cut.runTests();
        return true;
    }

    public boolean noPrintoutOfMissingTestsWhenTestsAreRestrictedTest() {
        CoverageUnitTester cut = cutForConfiguredPackage(Grandtestauto.test83_zip, "a83.test", "a83", "A", "b", "dTest", null);
        cut.runTests();
        String log = Helpers.logFileContents();
        System.out.println("log = " + log);
        return true;
    }

    private CoverageUnitTester cutForConfiguredPackageWithSplitClasses(String zipName, String packageName) {
        try {
            GrandTestAuto gta = Helpers.setupForZipWithSeparateSourceAndTestClassRoots(new File(zipName));
            Class<?> ut = Class.forName(packageName + ".UnitTester");
            Constructor ctr = ut.getConstructor(GrandTestAuto.class);
            return (CoverageUnitTester) ctr.newInstance(gta);
        } catch (Exception e) {
            e.printStackTrace();
            throw new AssertionError("See above stack trace.");
        }
    }

    private CoverageUnitTester cutForConfiguredPackage(String zipName, String packageName, String singlePackage, String singleTest, String initialTestMethod, String finalTestMethod, String singleTestMethod) {
        GrandTestAuto gta = Helpers.setupForZip(new File(zipName), true, true, true, null, null, singlePackage, false, true, Helpers.defaultLogFile().getPath(), null, null, null, singleTest, initialTestMethod, finalTestMethod, singleTestMethod);
        try {
            Class<?> ut = Class.forName(packageName + ".UnitTester");
            Constructor ctr = ut.getConstructor(GrandTestAuto.class);
            return (CoverageUnitTester) ctr.newInstance(gta);
        } catch (Exception e) {
            e.printStackTrace();
            throw new AssertionError("See above stack trace.");
        }
    }

    private void init() {
        methodsCalled = new LinkedList<String>();
        testsCreated = new HashSet<String>();
    }
}