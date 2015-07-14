package org.grandtestauto.loganalysis.test;

import org.grandtestauto.*;
import org.grandtestauto.assertion.Assert;
import org.grandtestauto.loganalysis.LogDirectoryAnalyser;
import org.grandtestauto.test.dataconstants.org.grandtestauto.loganalysis.dir0.Dir0;
import org.grandtestauto.test.dataconstants.org.grandtestauto.loganalysis.dir1.Dir1;
import org.grandtestauto.test.dataconstants.org.grandtestauto.loganalysis.dir2.Dir2;
import org.grandtestauto.test.dataconstants.org.grandtestauto.loganalysis.dir3.Dir3;
import org.grandtestauto.test.dataconstants.org.grandtestauto.loganalysis.dir4.Dir4;
import org.grandtestauto.test.dataconstants.org.grandtestauto.loganalysis.dir5.Dir5;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.*;

/**
 * @author Tim Lavers
 */
public class LogDirectoryAnalyserTest {
    private LogDirectoryAnalyser lda;
    private List<String> loggedMessages;
    private List<Throwable> loggedExceptions;
    private L log;
    
    public boolean preliminaryMessagesTest() throws Exception {
        init(Dir3.PATH);
        List<String> expected = new LinkedList<String>();
        expected.add(Messages.message(Messages.OPK_LOG_FILE_DIRECTORY, new File(Dir3.PATH).getAbsolutePath()));
        expected.add(Messages.message(Messages.SK_LOG_FILES));
        expected.add("Log0.txt");
        expected.add("Log1.txt");
        Assert.aequals(expected, lda.preliminaryMessages());
        return true;
    }

    public boolean messageForFinalResultTest() throws Exception {
        init(Dir3.PATH);
        Assert.aequals(Messages.message(Messages.SK_ALL_OK), lda.messageForFinalResult(true));
        Assert.aequals(Messages.message(Messages.SK_TESTING_PROBLEMS), lda.messageForFinalResult(false));
        return true;
    }

    public boolean doUnitTestsTest() throws Exception {
        init(Dir2.PATH);
        PackageResult packageResult = lda.doUnitTests("fruit.apple.seed");
        Assert.azzert( packageResult.passed());
        Assert.aequals(0l, packageResult.timeTakenInMillis());
        Assert.azzertNull(packageResult.errorMessage());
        return true;
    }

    public boolean doUnitTestsFailedTest() throws Exception {
        init(Dir3.PATH);
        String packageName = "fruit.apple";
        PackageResult packageResult = lda.doUnitTests(packageName);
        Assert.azzertFalse(packageResult.passed());
        Assert.aequals(0l, packageResult.timeTakenInMillis());
        String expectedMessage = Messages.message(Messages.OPK_UNIT_TESTS_FAILED_FOR, packageName );
        Assert.aequals(expectedMessage, packageResult.errorMessage());
        Assert.azzert(loggedMessages.indexOf(expectedMessage) >= 0);
        return true;
    }

    public boolean doUnitTestsResultMissingTest() throws Exception {
        init(Dir2.PATH);
        String packageName = "fruit.apple.pinklady";
        PackageResult packageResult = lda.doUnitTests(packageName);
        Assert.azzertFalse(packageResult.passed());
        String expectedMessage = Messages.message(Messages.OPK_NO_RESULTS_FOUND_FOR, packageName );
        Assert.aequals(expectedMessage, packageResult.errorMessage());
        Assert.azzert(loggedMessages.indexOf(expectedMessage) >= 0);
        return true;
    }

    public boolean verboseTest() throws Exception {
        init(Dir0.PATH);
        Assert.azzertFalse(lda.verbose());
        return true;
    }

    public boolean unitTestPackageResultsTest() throws Exception {
        init(Dir0.PATH);
        Assert.azzert(lda.unitTestPackageResults().isEmpty());

        init(Dir1.PATH);
        Assert.aequals(5, lda.unitTestPackageResults().size());
        Assert.azzert(lda.unitTestPackageResults().get("fruit.apple"));
        Assert.azzert(lda.unitTestPackageResults().get("fruit.apple.core"));
        Assert.azzert(lda.unitTestPackageResults().get("fruit.apple.seed"));
        Assert.azzert(lda.unitTestPackageResults().get("fruit.apple.skin"));
        Assert.azzert(lda.unitTestPackageResults().get("fruit.apple.validate"));

        init(Dir2.PATH);
        Assert.aequals(25, lda.unitTestPackageResults().size());
        Assert.azzert(lda.unitTestPackageResults().get("fruit.apple"));
        Assert.azzert(lda.unitTestPackageResults().get("fruit.apple.core"));
        Assert.azzert(lda.unitTestPackageResults().get("fruit.apple.seed"));
        Assert.azzert(lda.unitTestPackageResults().get("fruit.apple.skin"));
        Assert.azzert(lda.unitTestPackageResults().get("fruit.apple.validate"));

        Assert.azzert(lda.unitTestPackageResults().get("fruit.apricot"));
        Assert.azzert(lda.unitTestPackageResults().get("fruit.apricot.core"));
        Assert.azzert(lda.unitTestPackageResults().get("fruit.apricot.seed"));
        Assert.azzert(lda.unitTestPackageResults().get("fruit.apricot.skin"));
        Assert.azzert(lda.unitTestPackageResults().get("fruit.apricot.validate"));

        Assert.azzert(lda.unitTestPackageResults().get("fruit.banana"));
        Assert.azzert(lda.unitTestPackageResults().get("fruit.banana.core"));
        Assert.azzert(lda.unitTestPackageResults().get("fruit.banana.seed"));
        Assert.azzert(lda.unitTestPackageResults().get("fruit.banana.skin"));
        Assert.azzert(lda.unitTestPackageResults().get("fruit.banana.validate"));

        Assert.azzert(lda.unitTestPackageResults().get("fruit.pear"));
        Assert.azzert(lda.unitTestPackageResults().get("fruit.pear.core"));
        Assert.azzert(lda.unitTestPackageResults().get("fruit.pear.seed"));
        Assert.azzert(lda.unitTestPackageResults().get("fruit.pear.skin"));
        Assert.azzert(lda.unitTestPackageResults().get("fruit.pear.validate"));

        Assert.azzert(lda.unitTestPackageResults().get("fruit.pomegranate"));
        Assert.azzert(lda.unitTestPackageResults().get("fruit.pomegranate.core"));
        Assert.azzert(lda.unitTestPackageResults().get("fruit.pomegranate.seed"));
        Assert.azzert(lda.unitTestPackageResults().get("fruit.pomegranate.skin"));
        Assert.azzert(lda.unitTestPackageResults().get("fruit.pomegranate.validate"));

        init(Dir3.PATH);
        Assert.azzertFalse(lda.unitTestPackageResults().get("fruit.apple")) ;
        return true;
    }

    public boolean runAutoLoadTestPackageTest() throws Exception {
        init(Dir4.PATH);
        Collection<String> classNames = Arrays.asList("AddOnlineFormsProject", "DefaultUsersFunctionTest", "ExportOnlineProject");
        Assert.azzert(lda.runAutoLoadTestPackage(true, classNames, "rippledown.admin.functiontest").passed());
        Assert.aequals(0l, lda.runAutoLoadTestPackage(true, classNames, "rippledown.admin.functiontest").timeTakenInMillis());
        Assert.azzertNull(lda.runAutoLoadTestPackage(true, classNames, "rippledown.admin.functiontest").errorMessage());
        checkNoExceptions();
        return true;
    }

    public boolean runAutoLoadTestPackageClassMissingTest() throws Exception {
        init(Dir4.PATH);
        Collection<String> classNames = Arrays.asList("AcmeInterface", "DefaultUsersFunctionTest", "ExportOnlineProject");
        PackageResult packageResult = lda.runAutoLoadTestPackage(true, classNames, "rippledown.admin.functiontest");
        Assert.azzertFalse(packageResult.passed());
        Assert.aequals(0l, packageResult.timeTakenInMillis());
        String expectedMessage = "No results found for rippledown.admin.functiontest.AcmeInterface.";
        Assert.aequals(expectedMessage, packageResult.errorMessage());
        Assert.aequals(expectedMessage, packageResult.errorMessage());
        Assert.azzert(loggedMessages.indexOf(expectedMessage) >= 0);
        checkNoExceptions();
        return true;
    }

    public boolean runAutoLoadTestPackageTestFailedTest() throws Exception {
        init(Dir5.PATH);
        Collection<String> classNames = Arrays.asList("AddOnlineFormsProject", "DefaultUsersFunctionTest", "ExportOnlineProject");
        PackageResult packageResult = lda.runAutoLoadTestPackage(true, classNames, "rippledown.admin.functiontest");
        Assert.azzertFalse(packageResult.passed());
        Assert.aequals(0l, packageResult.timeTakenInMillis());
        String expectedMessage = "One or more tests failed in rippledown.admin.functiontest.";
        Assert.aequals(expectedMessage, packageResult.errorMessage());
        Assert.azzert(loggedMessages.indexOf(expectedMessage) >= 0);
        checkNoExceptions();
        return true;
    }
    
    private void checkNoExceptions() {
        for (Throwable t : loggedExceptions) {
            Assert.azzertNull(t);
        }
    }

    private void  init(String path) throws Exception {
        loggedMessages = new ArrayList<String>();
        loggedExceptions = new ArrayList<Throwable>();
        log = new L();
        lda = new LogDirectoryAnalyser(new File(path), log);
    }
    
    private class L implements GTALogger {
        @Override
        public void log(String message, @Nullable Throwable t) {
            loggedMessages.add(message);
            loggedExceptions.add(t);
        }
    }
}
