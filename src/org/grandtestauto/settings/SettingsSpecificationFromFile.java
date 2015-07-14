/****************************************************************************
 *
 * Synopsis: See javadoc class comments.
 *
 * Description: See javadoc class comments.
 *
 * Copyright 2015 Timothy Gordon Lavers (Australia)
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
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;

/**
 * The parameters for the invocation of <code>GrandTestAuto</code>.
 * These may set in a properties file, with default values used for
 * missing properties, or set at construction time.
 * The properties and their defaults are:
 * <p/>
 * <table border="2" cellpadding="2" width="80%">
 * <tr>
 * <th align="left">Property</th>
 * <th align="left">Purpose</th>
 * <th align="left">Default value</th>
 * </tr>
 * <tr>
 * <td>{@link #CLASSES_ROOT} </td>
 * <td>The name of the root of the classes directory
 * containing the classes to be tested. </td>
 * <td><code>System.getProperty( &quot;user.dir&quot; )</code>
 * </td>
 * </tr>
 * <tr>
 * <td>{@link #LOG_TO_FILE} </td>
 * <td>Whether the results should be logged to a file (as
 * well as to the console) </td>
 * <td>true </td>
 * </tr>
 * <tr>
 * <td>{@link #LOG_FILE_NAME} </td>
 * <td>The name of the file to which results are logged. </td>
 * <td>{@link #DEFAULT_LOG_FILE_NAME} </td>
 * </tr>
 * </table>
 * <br>
 *
 * @author Tim Lavers
 */
public class SettingsSpecificationFromFile extends SettingsSpecification {

    /**
     * Key in the settings file for the root of the classes.
     */
    public static final String CLASSES_ROOT = "CLASSES_ROOT";

    /**
     * Key in the settings file for whether to log to file.
     */
    public static final String LOG_TO_FILE = "LOG_TO_FILE";

    /**
     * Key in the settings file for whether to log to the console.
     */
    public static final String LOG_TO_CONSOLE = "LOG_TO_CONSOLE";

    /**
     * Key in the settings file for the name of the log file.
     */
    public static final String LOG_FILE_NAME = "LOG_FILE_NAME";

    /**
     * Key in the settings file for the name of the package from which to run the tests.
     */
    public static final String INITIAL_PACKAGE = "FIRST_PACKAGE";

    /**
     * Key in the settings file for the name of the package to  which to run the tests.
     */
    public static final String FINAL_PACKAGE = "LAST_PACKAGE";

    /**
     * Key in the settings file for the name of the single test package to run.
     */
    public static final String SINGLE_PACKAGE = "SINGLE_PACKAGE";

    /**
     * Key in the settings file for whether or not to run unit tests.
     */
    public static final String RUN_UNIT_TESTS = "RUN_UNIT_TESTS";

    /**
     * Key in the settings file for whether or not to run load tests.
     */
    public static final String RUN_LOAD_TESTS = "RUN_LOAD_TESTS";

    /**
     * Key in the settings file for whether to stop testing when a UnitTester or AutoLoadTest fails..
     */
    public static final String RUN_FUNCTION_TESTS = "RUN_FUNCTION_TESTS";

    /**
     * Key in the settings file for whether to stop testing after the first UnitTester or AutoLoadTest to return false or throw an exception..
     */
    public static final String STOP_AT_FIRST_FAILURE = "FAIL_FAST";

    /**
     * Key in the settings file for the name of the first test to be run within a single package of tests to be run.
     */
    public static final String INITIAL_CLASS_WITHIN_SINGLE_PACKAGE = "FIRST_CLASS";

    /**
     * Key in the settings file for the name of the last test to be run within a single package of tests to be run.
     */
    public static final String FINAL_CLASS_WITHIN_SINGLE_PACKAGE = "LAST_CLASS";

    /**
     * Key in the settings file for the name of the single test to be run within a single package of tests to be run.
     */
    public static final String SINGLE_CLASS_WITHIN_SINGLE_PACKAGE = "SINGLE_CLASS";

    /**
     * Key in the settings file for the name of the first test method to be run within a single unit to be run.
     */
    public static final String INITIAL_METHOD_WITHIN_SINGLE_UNIT_TEST_CLASS = "FIRST_METHOD";

    /**
     * Key in the settings file for the name of the last test method to be run within a single unit to be run.
     */
    public static final String FINAL_METHOD_WITHIN_SINGLE_UNIT_TEST_CLASS = "LAST_METHOD";

    /**
     * Key in the settings file for the name of the single test method to be run withing a single unit test to be run.
     */
    public static final String SINGLE_METHOD_WITHIN_SINGLE_UNIT_TEST_CLASS = "SINGLE_METHOD";

    /**
     * Key to select a less verbose form of logging used by the distributed GTA system.
     */
    public static final String LESS_VERBOSE = "LESS_VERBOSE";

    /**
     * The name for the results file, if not set explicitly.
     */
    public static final String DEFAULT_LOG_FILE_NAME = "GTAResults.txt";

    private static Set<String> LEGITIMATE_KEYS;

    static {
        LEGITIMATE_KEYS = new HashSet<>();
        Field[] fields = SettingsSpecificationFromFile.class.getDeclaredFields();
        for (Field field : fields) {
            if (field.getName().equals( "DEFAULT_LOG_FILE_NAME" )) continue;
            int m = field.getModifiers();
            if (Modifier.isFinal( m ) && Modifier.isPublic( m ) && Modifier.isStatic( m )) {
                try {
                    LEGITIMATE_KEYS.add( field.get( null ).toString() );
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                    //These are all public. This will not happen.
                }
            }
        }
    }

    private Set<String> unknownKeys = new HashSet<>();

    /**
     * A <code>Settings</code> that can be used to produce model settings
     * using {@link  #commentedPropertiesWithTheseValues()},  for showing to the user.
     */
    public SettingsSpecificationFromFile() {
        this(new File( System.getProperty( "user.dir" ) ));
    }

    /**
     * A <code>Settings</code> that can be used to produce model settings
     * using {@link  #commentedPropertiesWithTheseValues()},  for showing to the user.
     */
    public SettingsSpecificationFromFile(File classesRoot) {
        setClassesRoot(classesRoot);
        logToConsole = true;
    }

    /**
     * Creates a <code>Settings</code> that gets its parameters
     * from the named file. Defaults are used for missing properties.
     *
     * @throws IOException if the settings file can't be read or if the log file is needed but cannot be opened.
     */
    public SettingsSpecificationFromFile(String settingsFileName) throws IOException {
        Properties props = new Properties();
        InputStream is = new BufferedInputStream( new FileInputStream(new File( settingsFileName )) );
        props.load(is);

        settingsStream().forEach(s -> s.buildFrom(props));
        settingsStream().forEach(s -> s.addTo(this));

        //Make sure that there are no unknown keys in the properties file.
        //A mis-spelt key can waste a lot of time.
        for (Object key : props.keySet()) {
            if (!LEGITIMATE_KEYS.contains( key.toString() )) {
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