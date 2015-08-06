/****************************************************************************
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

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * If an AutoLoadTest class or unit test method that is marked with the annotation throws an exception,
 * then the execution will pause for the specified number of seconds.
 *
 * @author Tim Lavers
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface PauseOnException {
    int seconds();
}
