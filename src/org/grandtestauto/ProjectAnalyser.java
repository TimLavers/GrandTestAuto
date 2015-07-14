package org.grandtestauto;

import org.grandtestauto.settings.SettingsSpecification;

import java.io.*;
import java.util.*;

/**
 * @author Tim Lavers
 */
public class ProjectAnalyser {
    private List<String> unitTestPackages = new LinkedList<String>();
    private List<String> functionTestPackages = new LinkedList<String>();
    private List<String> loadTestPackages = new LinkedList<String>();

    public ProjectAnalyser( SettingsSpecification settings ) throws IOException {
        PackageLister packageLister = new PackageLister();
        GrandTestAuto gta = new GrandTestAuto( settings, packageLister );
        gta.resultsLogger().log( "Analysing classes...", null );
        packageLister.setGTA( gta );
        gta.runAllTests();
        gta.resultsLogger().log( "Analysis complete...starting distributor..", null );
    }

    public Collection<String> unitTestPackages() {
        return unitTestPackages;
    }

    public Collection<String> functionTestPackages() {
        return functionTestPackages;
    }

    public List<String> loadTestPackages() {
        return loadTestPackages;
    }

    private class PackageLister extends DPWImpl {
        Boolean invokeRun( String packageName, UnitTesterIF ut ) {
            if (ut != null) {
                unitTestPackages.add( packageName );
            }
            return true;
        }

        public PackageResult runAutoLoadTestPackage( boolean areFunctionTests, Collection<String> classesInPackage, String testPackageName ) {
            if (areFunctionTests) {
                functionTestPackages.add( testPackageName );
            } else {
                loadTestPackages.add( testPackageName );
            }
            return new PackageResult() {
                public boolean passed() {
                    return true;
                }

                public long timeTakenInMillis() {
                    return 0;
                }

                public String errorMessage() {
                    return null;
                }
            };
        }

        public String messageForFinalResult(boolean passOrFail) {
            return "";
        }

        @Override
        public List<String> preliminaryMessages() {
            return Collections.emptyList();
        }

        @Override
        public boolean verbose() {
            return false;
        }
    }
}