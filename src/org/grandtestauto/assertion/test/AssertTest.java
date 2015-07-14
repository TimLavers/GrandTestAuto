package org.grandtestauto.assertion.test;

import org.grandtestauto.Cleanup;
import org.grandtestauto.assertion.Assert;
import org.grandtestauto.assertion.AssertException;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/** @author T.Lavers and F. Lopez */
public class AssertTest {
    private final String captureScreenshotOnFailure = System.getProperties().getProperty(Assert.CAPTURE_SCREENSHOT_ON_FAILURE);

    private final File screenshotsFolder = new File(System.getProperty("user.dir"), "screenshots");

    public boolean aequalsModuloWhitespaceTest() throws Exception {
        return true;
    }

    public boolean aequalsModuloLineTerminatorsTest() throws Exception {
        return true;
    }

    public boolean notNegative_long_Test() {
        Assert.notNegative(0l);
        try {
            Assert.notNegative(-1l);
            return false;
        } catch (Exception e) {
            return true;
        }
    }

    public boolean notNegative_int_Test() {
        Assert.notNegative(0);
        try {
            Assert.notNegative(-1);
            return false;
        } catch (Exception e) {
            return true;
        }
    }

    public boolean notNegative_long_String_Test() {
        Assert.notNegative(0l, "abc");
        try {
            Assert.notNegative(-1, "cde");
            return false;
        } catch (Exception e) {
            return true;
        }
    }

    public boolean aequalsOrNullTest() {
        return true;
    }

    public boolean equalz_SetState_SetState_Test() {
        return true;
    }

    public boolean equalz_Object_Object_Test() {
        return true;
    }

    public boolean equalz_int_int_Test() {
        return true;
    }

    public boolean equalz_long_long_Test() {
        return true;
    }

    public boolean equalz_double_double_Test() {
        return true;
    }

    public boolean equalz_float_float_Test() {
        return true;
    }

    public boolean aequals_Object_Object_Test() {
        return true;
    }

    public boolean aequals_SetState_SetState_Test() {
        return true;
    }

    public boolean aequals_SampleSequence_SampleSequence_Test() {
        return true;
    }

    public boolean aequals_int_int_Test() {
        return true;
    }

    public boolean aequals_double_double_Test() {
        return true;
    }

    public boolean aequals_long_long_Test() {
        return true;
    }

    public boolean aequals_float_float_Test() {
        return true;
    }

    public boolean aequals_boolean_boolean_Test() {
        return true;
    }

    public boolean aequals_intArray_intArray_Test() {
        return true;
    }

    public boolean aequals_byteArray_byteArray_Test() {
        return true;
    }

    public boolean aequals_ObjectArray_ObjectArray_Test() {
        return true;
    }

    public boolean aequals_String_String_Test() {
        return true;
    }

    public boolean aequals_String_String_String_Test() {
        return true;
    }

    public boolean aequals_StringArray_StringArray_Test() {
        return true;
    }

    public boolean aequals_StringArray2_StringArray2_Test() {
        return true;
    }

    public boolean aequalIteratorsTest() {
        return true;
    }

    public boolean componentIsWithinComponentTest() {
        return true;
    }

    public boolean betweenTest() {
        return true;
    }

    public boolean equalTest() {
        return true;
    }

    public boolean assertSameTest() {
        Assert.assertSame("Hello", "Hello");
        Object o1 = new Object();
        Assert.assertSame(o1, o1);
        try {
            Assert.assertSame("Hello", "World");
            return false;
        }
        catch (AssertException exception) {
            return true;
        }
    }

    public boolean notSameTest() {
        Assert.notSame("Hello", "World");
        Object o1 = new Object();
        Assert.assertSame(o1, o1);
        try {
            Assert.notSame("Hello", "Hello");
            return false;
        }
        catch (AssertException exception) {
            return true;
        }
    }

    public boolean equalIteratorsTest() {
        List<String> apples = Arrays.asList("Granny smith", "Golden", "Red Delicious", "Pink Lady");
        List<String> applesAgain = Arrays.asList("Granny smith", "Golden", "Red Delicious", "Pink Lady");
        Assert.equalIterators(apples.iterator(), applesAgain.iterator());

        try {
            List<String> applesWrongOrder = Arrays.asList("Pink Lady", "Granny smith", "Golden", "Red Delicious");
            Assert.equalIterators(apples.iterator(), applesWrongOrder.iterator());
            return false;
        }
        catch (AssertException exception) {
            Assert.aequals(Assert.DIFFERENT_ITERATORS_MESSAGE, exception.getLocalizedMessage());
            return true;
        }
    }

    public boolean equalIteratorsDifferentNumberTest() {
        List<String> shortList = Arrays.asList("Granny smith", "Golden");
        List<String> longList = Arrays.asList("Granny smith", "Golden", "Red Delicious", "Pink Lady");
        try {
            Assert.equalIterators(shortList.iterator(), longList.iterator());
            return false;
        }
        catch (AssertException exception) {
            return true;
        }
    }

    public boolean greaterThan_int_int_Test() {
        return true;
    }

    public boolean greaterThan_long_long_Test() {
        return true;
    }

    public boolean directoryExistsForFilenameTest() {
        return true;
    }

    public boolean isExistingDirectoryTest() {
        return true;
    }

    public boolean notNull_Object_Test() {
        return true;
    }

    public boolean notNull_Object_String_Test() {
        return true;
    }

    public boolean aequals_Object_Object_String_Test() {
        String message = "This is the message.";
        String messageReceived = null;
        try {
            Assert.aequals(new Object(), new Object(), message);
        } catch (AssertException e) {
            messageReceived = e.getMessage();
        }
        return message.equals(messageReceived);
    }

    public boolean azzert_boolean_Test() {
        // No exceptions if the condition evaluates to true.
        Assert.azzert(true);
        //noinspection EmptyCatchBlock
        try {
            Assert.azzert(false);
            return false;
        } catch (AssertException ex) {

        }
        // Another condition.
        int i = 10;
        // Condition is true.
        Assert.azzert((i > 0) && (i == 10) && (i < 50));
        try {
            Assert.azzert(i == 9, "i not within a valid range");
            return false;
        } catch (AssertException ex) {
            return true;
        }
    }

    public boolean azzertFalse_boolean_Test() {
        // No exceptions if the condition evaluates to false.
        Assert.azzertFalse(false);
        try {
            Assert.azzertFalse(true);
            return false;
        } catch (AssertException ex) {
            return true;
        }
    }

    public boolean azzertFalseWithComplexConditionTest() {
        int i = Integer.valueOf("-10");
        // Condition is false.
        Assert.azzertFalse((Integer.valueOf("-50") > 0) && (i == 10) && (i < -11));
        return true;
    }

    public boolean azzert_boolean_String_Test() {
        return true;
    }

    public boolean azzertFalse_boolean_String_Test() {
        int i = -10;
        try {
            Assert.azzertFalse(i == -10, "i expected to have other value than 10");
            return false;
        } catch (AssertException ex) {
            return true;
        }
    }

    public boolean azzertNull_Object_Test() {
        Assert.azzertNull(null);
        try {
            Assert.azzertNull("Not null");
            return false;
        } catch (AssertException ex) {
            return true;
        }
    }

    public boolean azzertNull_Object_String_Test() {
        Assert.azzertNull(null, "this will pass");
        try {
            Assert.azzertNull("Not null", "this will not pass");
            return false;
        } catch (AssertException exception) {
            return true;
        }
    }

    public boolean azzertNotNull_Object_Test() {
        Assert.azzertNotNull("Not null string");
        try {
            Assert.azzertNotNull(null);
            return false;
        } catch (AssertException exception) {
            return true;
        }
    }

    public boolean azzertNotNull_Object_String_Test() {
        Assert.azzertNotNull("Not null string");
        try {
            Assert.azzertNotNull(null, "this will not pass");
            return false;
        } catch (AssertException exception) {
            return true;
        }
    }

    public boolean fail_String_Test() {
        try {
            Assert.fail("fail");
            return false;
        } catch (AssertException ex) {
            return true;
        }
    }

    public boolean fail_Throwable_String_Test() {
        String cause = "Unique cause string";
        //noinspection ThrowableInstanceNeverThrown
        Exception exception = new Exception(cause);
        try {
            Assert.fail(exception, "fail");
            return false;
        } catch (AssertException ex) {
            Assert.aequals(ex.getCause().getMessage(), cause);
            StringWriter sw = new StringWriter();
            ex.printStackTrace(new PrintWriter(sw));
            Assert.azzert(sw.getBuffer().toString().contains(cause));
        }
        return true;
    }

    public boolean datesAreIncreasingTest() {
        Date date1 = new Date();
        Date date2 = new Date();
        Date date3 = new Date();
        Assert.datesAreIncreasing(date1, date2, date3);
        try {
            Assert.datesAreIncreasing(date3, date2, date1);
            Assert.fail("dates were not increasing");
            return false;
        } catch (Exception e) {
            return true;
        }
    }

    public boolean doNotCaptureScreenshotOnFailTest() throws Exception {
        initScreenshotFolder();

        int previousLenght = screenshotsFolder.listFiles().length;
        System.getProperties().put(Assert.CAPTURE_SCREENSHOT_ON_FAILURE, "false");

        try {
            Assert.fail("ooops!");
        } catch (AssertException exception) {
            Assert.aequals(previousLenght, screenshotsFolder.listFiles().length);
        }
        cleanup();
        return true;
    }

    public boolean doCaptureScreenshotOnFailTest() throws Exception {
        initScreenshotFolder();

        System.getProperties().put(Assert.CAPTURE_SCREENSHOT_ON_FAILURE, "true");
        int previousLenght = screenshotsFolder.listFiles().length;

        try {
            Assert.fail("ooops!");
        } catch (AssertException exception) {
            if (previousLenght + 1 != screenshotsFolder.listFiles().length) {
                return false;
            }
        }
        try {
            Assert.fail("ooops I did it again!");
        } catch (AssertException exception) {
            if (previousLenght + 2 != screenshotsFolder.listFiles().length) {
                return false;
            }
        }
        cleanup();
        return true;
    }

    private void initScreenshotFolder() {
        if (!screenshotsFolder.exists()) {
            screenshotsFolder.mkdirs();
        }
    }

    @Cleanup
    public void cleanup() {
        if (captureScreenshotOnFailure == null) {
            System.getProperties().put(Assert.CAPTURE_SCREENSHOT_ON_FAILURE, "false");
        } else {
            System.getProperties().put(Assert.CAPTURE_SCREENSHOT_ON_FAILURE, captureScreenshotOnFailure);
        }
    }

}
