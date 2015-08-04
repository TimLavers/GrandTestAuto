/****************************************************************************
 * The Wide Open License (WOL)
 * <p>
 * Permission to use, copy, modify, distribute and sell this software and its
 * documentation for any purpose is hereby granted without fee, provided that
 * the above copyright notice and this license appear in all source copies.
 * THIS SOFTWARE IS PROVIDED "AS IS" WITHOUT EXPRESS OR IMPLIED WARRANTY OF
 * ANY KIND. See http://www.dspguru.com/wol.htm for more information.
 *****************************************************************************/
package org.grandtestauto.settings;

import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

/**
 * @author Tim Lavers
 */
public class SettingsSpecificationFromCommandLine extends SettingsSpecification {

    public SettingsSpecificationFromCommandLine(String[] commandLine) {
        Properties properties = new Properties();
        //Add the system properties.
        properties.putAll(System.getProperties());
        //Add the properties obtained from the command line,
        //overriding the system properties where there are duplicates.
        for (String pair : commandLine) {
            String[] keyValue = pair.split("=");
            if (keyValue.length == 2) {
                properties.setProperty(keyValue[0], keyValue[1]);
            }
        }
        loadFromProperties(properties);
    }

    /**
     * Always return an empty set, as unknown keys are expected.
     */
    public Set<String> unknownKeys() {
        return new HashSet<>();
    }
}