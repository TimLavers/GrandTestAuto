package org.grandtestauto.test.functiontest;

import org.grandtestauto.GrandTestAuto;

import java.security.Permission;

/** See the GrandTestAuto test specification. */
public abstract class MainTestBase extends FTBase {

    void runMainWithoutExit(String param) {
        forbidSystemExitCall();
        try {
            GrandTestAuto.main(new String[]{param});
            assert false : "Expected the system to exit.";
        } catch (ExitTrappedException e) {
            //
        } finally {
            enableSystemExitCall();
        }
    }

    private static class ExitTrappedException extends SecurityException {

    }

    // got from: www.java-forums.org/advanced-java/9344-system-exit-catch-block.html

    private static void forbidSystemExitCall() {
        final SecurityManager securityManager = new SecurityManager() {
            public void checkPermission(Permission permission) {
                if ("exitVM.0".equals(permission.getName())) {
                    throw new ExitTrappedException();
                }
            }
        };
        System.setSecurityManager(securityManager);
    }

    private static void enableSystemExitCall() {
        System.setSecurityManager(null);
    }
}
