/****************************************************************************
 *
 * Name: Settings.java
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
package org.grandtestauto.settings;

import org.grandtestauto.Messages;
import org.grandtestauto.settings.SettingsSpecification;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;

/**
 * The parameters for the invocation of <code>GrandTestAuto</code>
 * expressed in a single line.
 *
 * @author Tim Lavers
 */
class SingleLineSettings extends SettingsSpecification {

    public static final String INDICATOR = "-settings";
    public static final String DELIMITER = "|";

    private static enum PassType { UT, FT, LT}

    public SingleLineSettings(String str) {
        //-settings|classes_dir|ft|sp=a36.f
        //-classes_dir=d://dev//classes -ft -sp=a36.f -rf=ut.txt
        String[] split = str.split("\\|");
        assert split[0].equals("-settings");
        setClassesRoot(new File(split[1]));
        Set<PassType> passes = new HashSet<>();

        for (int i=2; i<split.length;i++) {
            PassType passType = PassType.valueOf(split[i].toUpperCase());
            if (passType != null) {
                passes.add(passType);
            }
        }

        //If no pass type info is specified, use the defaults. Otherwise, make use of the settings.
        if (!passes.isEmpty()) {
            runUnitTests = passes.contains(PassType.UT);
            runFunctionTests = passes.contains(PassType.FT);
            runLoadTests = passes.contains(PassType.LT);
        }

    }
}

