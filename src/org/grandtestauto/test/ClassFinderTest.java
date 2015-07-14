package org.grandtestauto.test;

import org.grandtestauto.*;
import org.grandtestauto.test.dataconstants.org.grandtestauto.*;

import java.io.*;
import java.util.*;

/**
 * Unit test for <code>ClassFinder</code>.
 *
 * @author Tim Lavers
 */
public class ClassFinderTest extends CFTestBase {

    private ClassFinderExt cfe;

    private class ClassFinderExt extends ClassFinder {
        SortedSet<String> classesVisited = new TreeSet<String>( );
        public ClassFinderExt( String packageName, File classesDir ) {
            super( packageName, classesDir );
        }

        public boolean foundSomeClasses() {
            return classesVisited.size() > 0;
        }

        public void processClass( String relativeName ) {
            classesVisited.add( relativeName );
        }
    }

    public boolean seekTest() throws Exception {
        init( Grandtestauto.test1_zip, "a1" );
        assert cfe.classesVisited.size() == 0;
        cfe.seek();
        assert cfe.classesVisited.size() == 3;
        Iterator<String> itor = cfe.classesVisited.iterator();
        assert itor.next().equals( "X" );
        assert itor.next().equals( "Y" );
        assert itor.next().equals( "Z" );
        return true;
    }

    protected void init( String archiveName, String packageName ) throws Exception {
        super.init( archiveName, packageName );
        cfe = new ClassFinderTest.ClassFinderExt( packageName, classesDir );
    }
}
