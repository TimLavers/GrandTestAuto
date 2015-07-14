package org.grandtestauto.assertion;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;

/**
 * This class contains support for C-like assertions.
 * <p/>
 * Assertions are initially enabled. They can be enabled and disabled
 * by calling the <tt>enable</tt> and <tt>disable</tt> methods.
 * <p/>
 * To make an assertion, call the <tt>assert</tt> method with an
 * an expression that defines the assertion and a string that identifies
 * the assertion. If the expression evalutes to <tt>false</tt> and
 * assertions are enabled (only for <tt>assert</tt> method), then an
 * exception is thrown.
 * <p/>
 * Methods are also provided for simply checking conditions, see for example
 * {@link #notNull(Object) notNull( Object ) }.
 * These methods work whether assertions are enabled or dis-enabled.
 * <p/>
 * The following code snippet illustrates the use of this class:
 * <blockquote><pre>
 * Assert.azzert(s.length() >= 0, "s can't be empty");
 * Assert.isExistingDirectory(mightBeDir);
 * </pre></blockquote>
 * <p/>
 * Note: This class is used with permission of Pacific Knowledge Systems
 *
 * @author <a href="mailto:s.mudali@pks.com.au">Sugath Mudali</a>
 * @author Tim Lavers
 */
public final class Assert {

    public static final String DIFFERENT_ITERATORS_MESSAGE = "The iterators do not have the same number of elements in the same order";
    public static final String CAPTURE_SCREENSHOT_ON_FAILURE = "rdr.assert.screenshot";

    private static String NL = System.getProperty("line.separator");
    private static String LF = "\n";
    private static String CR = "" + (char) 13;

    /**
     * Default constructor. Declare it as private for this class not
     * to be instantiated from outside.
     */
    private Assert() {
    }

    public static void aequalsModuloWhitespace(String s1, String s2) {
        Assert.aequals(stripWhitespace(s1), stripWhitespace(s2));
    }

    public static void aequalsModuloLineTerminators(String s1, String s2) {
        Assert.aequals(stripLineTerminators(s1), stripLineTerminators(s2));
    }

    public static void azzert(boolean assertion) throws AssertException {
        if (!assertion) {
            doFail("");
        }
    }

    public static void azzert(boolean assertion, String message) throws AssertException {
        if (!assertion) {
            doFail(message);
        }
    }

    public static void azzertFalse(boolean assertion) throws AssertException {
        azzert(!assertion);
    }

    public static void azzertFalse(boolean assertion, String message) throws AssertException {
        azzert(!assertion, message);
    }

    public static void azzertNull(Object actual) throws AssertException {
        azzertNull(actual, "Expected a null object reference");
    }

    public static void azzertNull(Object actual, String message) throws AssertException {
        azzert(actual == null, message);
    }

    public static void azzertNotNull(Object actual) throws AssertException {
        azzertNotNull(actual, "Expected a non null object reference");
    }

    public static void azzertNotNull(Object actual, String message) throws AssertException {
        azzert(actual != null, message);
    }

    public static void equalz(Object o1, Object o2) throws AssertException {
        aequals(o1, o2, "Objects different as above");
    }

    public static void aequals(Object o1, Object o2, String message) throws AssertException {
        if (!o1.equals(o2)) {
            System.out.println("1st Object: '" + o1 + "'");
            System.out.println("2nd Object: '" + o2 + "'");
            doFail(message);
        }
    }

    public static void aequals(String o1, String o2, String message) throws AssertException {
        if (!o1.equals(o2)) {
            System.out.println("1st String: '" + o1 + "'");
            System.out.println("2nd String: '" + o2 + "'");
            doFail(message);
        }
    }

    public static void aequals(String[][] arr1, String[][] arr2) {
        if (!(arr1.length == arr2.length)) {
            fail("Different array lengths: " + arr1.length + ", " + arr2.length);
        }
        for (int i = 0; i < arr1.length; i++) {
            String[] strings1 = arr1[i];
            String[] strings2 = arr2[i];
            if (!(strings1.length == strings2.length)) {
                System.out.println("\n1st:\t");
                for (String s : strings1) {
                    System.out.print(s);
                    System.out.print('\t');
                }
                System.out.println("");
                System.out.println("2nd:\t");
                for (String s : strings2) {
                    System.out.print(s);
                    System.out.print('\t');
                }
                System.out.println("");
                fail("different sub-array lengths for row: " + i + ". 1st: " + strings1.length + ". 2nd: " + strings2.length);
            }
            for (int j = 0; j < strings1.length; j++) {
                String message = "Values different for row: " + i + ", col: " + j + ". Values: '" + strings1[j] + "', '" + strings2[j] + "'";
                aequals(strings1[j], strings2[j], message);
            }
        }

    }

    public static void equalz(int i1, int i2) throws AssertException {
        if (i1 != i2) {
            System.out.println("1st int: " + i1);
            System.out.println("2nd int: " + i2);
            doFail("Ints different as above");
        }
    }

    public static void equalz(long i1, long i2) throws AssertException {
        if (i1 != i2) {
            System.out.println("1st long: " + i1);
            System.out.println("2nd long: " + i2);
            doFail("Longs different as above");
        }
    }

    public static void equalz(float i1, float i2) throws AssertException {
        if (i1 != i2) {
            System.out.println("1st float: " + i1);
            System.out.println("2nd float: " + i2);
            doFail("Floats different as above");
        }
    }

    public static void equalz(double i1, double i2) throws AssertException {
        if (i1 != i2) {
            System.out.println("1st double: " + i1);
            System.out.println("2nd double: " + i2);
            doFail("Doubles different as above");
        }
    }

    public static void aequals(Object o1, Object o2) throws AssertException {
        equalz(o1, o2);
    }

    public static void aequalsOrNull(Object o1, Object o2) throws AssertException {
        if (o1 == null) {
            azzert(o2 == null, "o1 null but not o2");
        } else {
            equalz(o1, o2);
        }
    }

    public static void aequals(String o1, String o2) throws AssertException {
        equalz(o1, o2);
    }

    public static void aequals(boolean o1, boolean o2) throws AssertException {
        azzert(o1 == o2, Boolean.toString(o1) + " " + Boolean.toString(o2));
    }

    public static void aequals(int[] i1, int[] i2) throws AssertException {
        int l = i1.length;
        int m = i2.length;
        azzert(l == m, "Different sized arrays: " + l + ", " + m);
        for (int i = 0; i < l; i++) {
            azzert(i1[i] == i2[i], "Different element at position " + i + ": " + i1[i] + ", " + i2[i]);
        }
    }

    public static void aequals(byte[] i1, byte[] i2) throws AssertException {
        int l = i1.length;
        int m = i2.length;
        azzert(l == m, "Byte array lengths are different. 1st: " + l + " 2nd: " + m);
        for (int i = 0; i < l; i++) {
            azzert(i1[i] == i2[i], "mismatch at index " + i + ". 1st: '" + i1[i] + "' 2nd: '" + i2[i] + "'");
        }
    }

    public static void aequals(Object[] i1, Object[] i2) throws AssertException {
        Assert.azzert(Arrays.equals(i1, i2), "Arrays are not equal");
    }

    public static void assertSame(Object o1, Object o2) throws AssertException {
        //noinspection ObjectEquality
        Assert.azzert(o1 == o2, "The object references are not the same.");

    }

    public static void notSame(Object o1, Object o2) throws AssertException {
        //noinspection ObjectEquality
        Assert.azzert(o1 != o2, "The object references are the same.");
    }

    public static void equalIterators(Iterator i1, Iterator i2) throws AssertException {
        boolean b = true;

        // if both of the iterations are empty, means that they are identical
        if (!i1.hasNext()) {
            b = !i2.hasNext();
        } else {
            Object next1;
            Object next2;
            // Begin searching all of the iteration's elements
            while (i1.hasNext() && i2.hasNext() && b) {
                next1 = i1.next();
                next2 = i2.next();
                if (b != next1.equals(next2)) {
                    b = false;
                }
            }
        }
        b &= !i1.hasNext() && !i2.hasNext();
        Assert.azzert(b, DIFFERENT_ITERATORS_MESSAGE);
    }

    public static void datesAreIncreasing(Date before, Date middle, Date after) {
        Assert.azzert(!before.after(middle));
        Assert.azzert(!middle.after(after));
    }

    public static void componentIsWithinComponent(Component enclosing, Component enclosed) {
        if (!enclosing.getBounds().contains(enclosed.getBounds())) {
            Assert.fail("Enclosed component: " + enclosed.getBounds() + " is not within enclosing component: " + enclosing.getBounds());
        }
    }

    /**
     * Makes an assertion failure.
     *
     * @param message a string to identify the assertion.
     * @throws AssertException thrown when assertions are enabled.
     */
    public static void fail(String message) {
        doFail(message);
    }

    public static void fail(Throwable e, String message) {
        AssertException ae = new AssertException(message);
        ae.initCause(e);
        throw ae;
    }

    public static void between(int p, int x, int y) {
        if (p < x) {
            doFail("p < x");
        }
        if (p > y) {
            doFail("p > y");
        }
    }

    public static void equal(Object a, Object b) {
        if (!a.equals(b)) {
            doFail("unequal values: " + a + ", " + b);
        }
    }

    public static void isExistingDirectory(File d) {
        if (!d.exists()) {
            doFail("non-existent directory: " + d);
        }
        if (!d.isDirectory()) {
            doFail("not a directory: " + d);
        }
    }

    public static void notNull(Object o) {
        if (o == null) {
            doFail("Null");
        }
    }

    public static void notNull(Object o, String comment) {
        if (o == null) {
            doFail(comment);
        }
    }

    public static void notNegative(long l, String s) {
        if (l < 0) {
            doFail(s);
        }
    }

    public static void notNegative(long l) {
        if (l < 0) {
            doFail("Value less than 0: " + l);
        }
    }

    public static void notNegative(int n) {
        if (n < 0) {
            doFail("value less than 0: " + n);
        }
    }

    private static void doFail(String str) {
        if (Boolean.parseBoolean(System.getProperty(CAPTURE_SCREENSHOT_ON_FAILURE))) {
            captureScreenshot();
        }
        if (Boolean.parseBoolean(System.getProperty("rdr.assert.pause"))) {
            Thread.dumpStack();
            System.out.println("ASSERTION FAILURE. WILL PAUSE FOR 10 MINUTES THEN THROW EXCEPTION. SEE STACK ABOVE.");
            System.out.println("Failure cause is: '" + str + "'");
            try {
                Thread.sleep(600000l);
            } catch (InterruptedException ie) {
                ie.printStackTrace();
            }
        }
        throw new AssertException(str);
    }

    private static void captureScreenshot() {
        try {
            Robot robot = new Robot();
            Rectangle screenSize = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
            BufferedImage capture = robot.createScreenCapture(screenSize);

            String fileName = String.valueOf(System.nanoTime()) + ".png";
            File screenshot = new File(System.getProperty("user.dir") + File.separator + "screenshots", fileName);
            ImageIO.write(capture, "png", screenshot);
            System.out.println("Screenshot image captured: " + fileName);
        } catch (Exception exception) {
            System.out.println("Could not create screenshot image: " + exception);
        }
    }

    private static String stripLineTerminators(String str) {
        str = str.replaceAll(NL, "");
        str = str.replaceAll(CR, "");
        return str.replaceAll(LF, "");
    }

    private static String stripWhitespace(String str) {
        StringBuffer buf = new StringBuffer();
        char[] chars = str.toCharArray();
        for (char aChar : chars) {
            if (!Character.isWhitespace(aChar)) {
                buf.append(aChar);
            }
        }
        return buf.toString();
    }

}
