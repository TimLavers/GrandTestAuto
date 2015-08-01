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

import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.Modifier;
import java.rmi.Remote;
import java.util.TreeMap;

/**
 * Finds the testable classes in a package.
 *
 * @author Tim Lavers
 */
public class PackageInfo extends ClassFinder {

    private TreeMap<String, Testability> classNameToTestability = new TreeMap<>();

    /**
     * Creates a <code>PackageInfo</code> that searches the
     * directory <code>classesDir</code> for testable classes.
     */
    public PackageInfo( String packageName, File classesDir ) {
        super( packageName, classesDir );
    }

    public void processClass( String relativeName ) {
        Class<?> klass = classFor( relativeName );
        //Weed out member classes.
        if (klass.isMemberClass()) return;

        //Weed out the classes annotated as not needing to be tested.
        Annotation annotation = klass.getAnnotation( DoesNotNeedTest.class );
        if (annotation != null) return;

        //Also weed out classes annotated with an annotation that indicates
        //that they are generated or for some other reason do not need tests.
        for (Annotation a : klass.getAnnotations()) {
            String nameLC = a.toString().toLowerCase();
            if (nameLC.contains("generated")) return;
            if (nameLC.contains("doesnotneedtest")) return;
        }

        //Weed out rmi-generated stub classes.
        if (Remote.class.isAssignableFrom( klass ) && klass.getName().endsWith( "_Stub" )) return;

        int m = klass.getModifiers();
        //Weed out the non-public ones.
        if (!Modifier.isPublic( m )) return;

        //Weed out those with no testable methods.
        if (new ClassAnalyser( klass ).testMethodNames().size() == 0) return;

        //Of those that remain, classify according to whether or not they are abstract
        if (Modifier.isAbstract( m )) {
            classNameToTestability.put( relativeName, Testability.CONTAINS_TESTABLE_METHODS );
        } else {
            classNameToTestability.put( relativeName, Testability.TEST_REQUIRED );
        }
    }

    public boolean foundSomeClasses() {
        return classNameToTestability().keySet().size() > 0;
    }

    public TreeMap<String, Testability> classNameToTestability() {
        return classNameToTestability;
    }
}