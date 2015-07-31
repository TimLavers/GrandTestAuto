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
package org.grandtestauto.test;

import java.io.File;

public class CFTestBase {
    protected File classesDir;

    protected void init( String archiveName, String packageName ) throws Exception {
        //Expand the zip archive into the temp directory.
        Helpers.cleanTempDirectory();
        File zip = new File( archiveName );
        Helpers.expandZipTo( zip, Helpers.classesDirClassic() );
        classesDir = new File( Helpers.classesDirClassic(), packageName.replace( '.', File.separatorChar ) );
    }
}
