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
package org.grandtestauto;

import java.lang.annotation.*;

/**
 * This annotation is for marking a method in a test class that is
 * to be called by the framework if a test method has thrown an exception.
 *
 * @author Tim Lavers
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Cleanup {
}