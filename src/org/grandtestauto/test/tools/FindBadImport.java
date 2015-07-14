package org.grandtestauto.test.tools;

import org.apache.commons.io.*;
import org.apache.commons.io.filefilter.*;

import java.io.*;
import java.util.*;

/**
 * @author Tim Lavers
 */
public class FindBadImport extends DirectoryWalker {

    public static void main( String[] args ) throws IOException {
        File src = new File( args[0] );
        FindBadImport fbi = new FindBadImport( src );
        fbi.doIt();
    }

    private List<File> javaFiles = new LinkedList<File>();
    private File srcRoot;

    public FindBadImport( File srcRoot ) {
        super( new NotFileFilter( new RegexFileFilter( ".*test" ) ), new SuffixFileFilter( ".java" ), 1000 );
        this.srcRoot = srcRoot;
    }

    public void doIt() throws IOException {
        walk( srcRoot, javaFiles );
        for (File f : javaFiles) {
            System.out.println( f.getPath() );
        }
    }

    protected void handleFile( File file, int depth, Collection results ) {
        System.out.println("file = " + file);
        LineIterator it;
        try {
            it = FileUtils.lineIterator( file, "UTF-8" );
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        try {
            while (it.hasNext()) {
                String line = it.nextLine();
//                if (line.startsWith( "import" )) {
                    if (line.contains( ".functiontest" ) || line.contains( ".loadtest" ) ||line.contains( ".test" )) {
                        System.out.println("line = " + line);
                        results.add( file );
                    }
//                }
            }
        } finally {
            LineIterator.closeQuietly( it );
        }
    }
}
class SourceFileFilter implements java.io.FilenameFilter {
    public boolean accept(File dir, String name) {
        return !dir.getName().endsWith("test"); 
    }
}