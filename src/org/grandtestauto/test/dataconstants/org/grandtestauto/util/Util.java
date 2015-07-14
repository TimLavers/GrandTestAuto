package org.grandtestauto.test.dataconstants.org.grandtestauto.util;


import java.io.File;
public class Util {

public static String PATH = System.getProperty( "TestDataRoot" )  + "" + File.separator + "org" + File.separator + "grandtestauto" + File.separator + "util" + File.separator;

public static final String serr_txt = PATH + "serr.txt";
public static final String sout_txt = PATH + "sout.txt";

}