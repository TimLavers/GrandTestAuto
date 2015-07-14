package org.grandtestauto.assertion.test;

import org.grandtestauto.assertion.AssertException;

/** @author Tim Lavers and Fede Lopez */
public class AssertExceptionTest {

    public boolean constructorTest() {
        AssertException ae = new AssertException("AAABBBCCC");
        return ae.getMessage().equals("AAABBBCCC");
    }
}
