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
package org.grandtestauto.settings;

import java.io.*;
import java.util.*;

/**
 * @author Tim Lavers
 */
public class SettingsSpecificationFromFile extends SettingsSpecification {
    private Set<String> unknownKeys = new HashSet<>();

    public SettingsSpecificationFromFile() {
        this(new File( System.getProperty( "user.dir" ) ));
    }

    public SettingsSpecificationFromFile(File classesRoot) {
        setClassesRoot(classesRoot);
        logToConsole = true;
    }

    public SettingsSpecificationFromFile(String settingsFileName) throws IOException {
        Properties properties = new Properties();
        InputStream is = new BufferedInputStream( new FileInputStream(new File( settingsFileName )) );
        properties.load(is);

        loadFromProperties(properties);
        Set<String> legitimateKeys = new HashSet<>();
        settingsStream().forEach(s -> legitimateKeys.add(s.key()));

        //Make sure that there are no unknown keys in the properties file.
        //A mis-spelt key can waste a lot of time.
        for (Object key : properties.keySet()) {
            if (!legitimateKeys.contains( key.toString() )) {
                unknownKeys.add( key.toString() );
            }
        }
    }

    /**
     * Any keys in the properties file from which this was created, that are not
     * known keys, are stored here, so that they can be shown to the user.
     */
    public Set<String> unknownKeys() {
        return unknownKeys;
    }

    public String commentedPropertiesWithTheseValues() {
        ResourceBundle commentsRB = PropertyResourceBundle.getBundle( getClass().getName() );
        StringBuilder sb = new StringBuilder();
        sb.append( commentsRB.getString( "INTRO1" ) );
        sb.append( NL );
        sb.append( commentsRB.getString( "INTRO2" ) );

        settingsStream().forEach(s -> {
            String key = s.key();
            if (key != null) {
                addCommentAndKey(sb, commentsRB, key);
                sb.append(s.valueInUserExplanation(this));
            }
        });
        return sb.toString();
    }

    private void addCommentAndKey( StringBuilder sb, ResourceBundle commentsRB, String key ) {
        sb.append( NL );
        sb.append( NL );
        sb.append( '#' );
        sb.append( commentsRB.getString( key ) );
        sb.append( NL );
        sb.append( key );
        sb.append( '=' );
    }
}