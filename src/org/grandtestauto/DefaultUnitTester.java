/****************************************************************************
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

import org.jetbrains.annotations.NotNull;

/**
 * A <code>CoverageUnitTester</code> created on the fly to cover testing for
 * a package that does not have a defined <code>UnitTester</code>.
 *
 * @author Tim Lavers
 */
public class DefaultUnitTester extends CoverageUnitTester implements UnitTesterIF {

    private String packageName;

    public DefaultUnitTester(@NotNull GrandTestAuto gta, String packageName) {
        super(gta);
        this.packageName = packageName;
    }

    @Override
    String testPackageName() {
        return packageName;
    }
}