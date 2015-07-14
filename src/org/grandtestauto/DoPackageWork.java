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
import java.util.List;

/**
 * @author Tim Lavers
 */
public interface DoPackageWork {
    PackageResult doUnitTests( String packageName );
    PackageResult runAutoLoadTestPackage( boolean areFunctionTests, Collection<String> classesInPackage, String testPackageName );
    boolean verbose();
    List<String> preliminaryMessages();
    String messageForFinalResult(boolean passOrFail);
}
