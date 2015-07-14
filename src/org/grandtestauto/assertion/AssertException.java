package org.grandtestauto.assertion;

/**
 * This class throws an unchecked exception (runtime exception)
 * when an assert fails.
 * <dl>
 * <dt><b>Usage:</b>
 * <dd><code>
 * try {
 * Assert.azzert(s.length() >= 0, "s can't be empty");<br>
 * catch(AssertException ex) {<br>
 * System.err.println(ex.getMessage());
 * </code>
 * </dl>
 * <strong>Note: </strong> this is <code>Serializable</code> so that <code>Result</code>
 * objects, which often make use of <code>AssertException</code>s, can be used in remtote calls.
 * This is really an issue with the <code>Result</code> class.
 *
 * @author <a href="mailto:s.mudali@pks.com.au">Sugath Mudali</a>
 * @see <a href="doc-files/AssertException.java">AssertException.java</a>
 * @see Assert
 */
public final class AssertException extends RuntimeException implements java.io.Serializable {

    /**
     * This constructor makes an assertion exception with a message.
     *
     * @param message the assertion message.
     */
    public AssertException(String message) {
        super(message);
    }
}
