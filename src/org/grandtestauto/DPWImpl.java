package org.grandtestauto;

import java.lang.reflect.*;

/**
 * @author Tim Lavers
 */
abstract class DPWImpl implements DoPackageWork {
    protected GrandTestAuto gta;

    public PackageResult doUnitTests( String packageName ) {
        String utClassName = packageName + ".test.UnitTester";
        PackageResultImpl result = new PackageResultImpl();
        //Try to create and invoke the UnitTester.
        try {
            Class<?> utClass = Class.forName( utClassName );
            int mods = utClass.getModifiers();
            if (!Modifier.isPublic( mods )) {
                result.setErrorMessage( Messages.message( Messages.OPK_UNIT_TESTER_NOT_PUBLIC, packageName ) );
                gta.testingError( Messages.message( Messages.OPK_UNIT_TESTER_NOT_PUBLIC, packageName ), null );
                return result;
            }
            if (Modifier.isAbstract( mods )) {
                result.setErrorMessage( Messages.message( Messages.OPK_UNIT_TESTER_ABSTRACT, packageName ) );
                gta.testingError( Messages.message( Messages.OPK_UNIT_TESTER_ABSTRACT, packageName ), null );
                return result;
            }
            UnitTesterIF ut;
            if (UnitTesterIF.class.isAssignableFrom( utClass )) {
                Constructor<?> ct;
                try {
                    ct = utClass.getConstructor( gta.getClass() );
                    ut = (UnitTesterIF) ct.newInstance( gta );
                } catch (NoSuchMethodException e) {
                    //Don't worry, try a no-args constructor.
                    try {
                        ct = utClass.getConstructor();
                        ut = (UnitTesterIF) ct.newInstance();
                    } catch (NoSuchMethodException nsme) {
                        //The UnitTester could not be created.
                        result.setErrorMessage( Messages.message( Messages.OPK_UNIT_TESTER_DOES_NOT_HAVE_REQURIED_CONSTRUCTOR, packageName ) );
                        gta.testingError( Messages.message( Messages.OPK_UNIT_TESTER_DOES_NOT_HAVE_REQURIED_CONSTRUCTOR, packageName ), null );
                        return result;
                    }
                }
                result.setResult( invokeRun( packageName, ut ) );
            } else {
                //The UnitTester does not implement UnitTesterIF.
                result.setErrorMessage( Messages.message( Messages.OPK_UNIT_TESTER_NOT_UNITTESTERIF, packageName ) );
                gta.testingError( Messages.message( Messages.OPK_UNIT_TESTER_DOES_NOT_HAVE_REQURIED_CONSTRUCTOR, packageName ), null );
                return result;
            }
        } catch (ClassNotFoundException cnfe) {
            result.setResult(runForNotUnitTester(packageName));
            //The package has no unit tester.
            String errorMessage = Messages.message(Messages.OPK_COULD_NOT_FIND_UNIT_TESTER, packageName + ".test");
//            result.setErrorMessage(errorMessage);
//            gta.testingError( errorMessage, null );
//            gta.nonUnitTestedPackageNames().add( packageName );
        } catch (InstantiationException ie) {
            //The UnitTester could not be created.
            result.setErrorMessage( Messages.message( Messages.OPK_COULD_NOT_CREATE_UNIT_TESTER, packageName ) );
            gta.testingError( Messages.message( Messages.OPK_COULD_NOT_CREATE_UNIT_TESTER, packageName ), null );
        } catch (IllegalAccessException iae) {
            //The UnitTester could not be created.
            result.setErrorMessage( Messages.message( Messages.OPK_COULD_NOT_CREATE_UNIT_TESTER, packageName ) );
            gta.testingError( Messages.message( Messages.OPK_COULD_NOT_CREATE_UNIT_TESTER, packageName ), null );
        } catch (ClassCastException cce) {
            //The UnitTester is not of the correct type.
            result.setErrorMessage( Messages.message( Messages.OPK_UNIT_TESTER_NOT_UNITTESTERIF, packageName ) );
            gta.testingError( Messages.message( Messages.OPK_UNIT_TESTER_NOT_UNITTESTERIF, packageName ), null );
        } catch (InvocationTargetException ite) {
            //This means that the test failed.
            result.setErrorMessage( Messages.message( Messages.SK_TEST_FAILED_DUE_TO_EXCEPTION ) );
            gta.resultsLogger().log( Messages.message( Messages.SK_TEST_FAILED_DUE_TO_EXCEPTION ), ite );
        }

        if (verbose()) {
            gta.reportUTResult( packageName, result );
        }
        return result;
    }

    public boolean verbose() {
        return true;
    }

    abstract Boolean invokeRun( String packageName, UnitTesterIF ut );

    void setGTA( GrandTestAuto gta ) {
        this.gta = gta;
    }

    private Boolean runForNotUnitTester(String packageName) {
        DefaultUnitTester tester = new DefaultUnitTester(gta, packageName + ".test");
        return invokeRun(packageName, tester);
    }
}
