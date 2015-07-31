package org.grandtestauto.test;

import org.grandtestauto.PackageInfo;
import org.grandtestauto.PackagesInfo;
import org.grandtestauto.Testability;
import org.grandtestauto.test.dataconstants.org.grandtestauto.Grandtestauto;

import java.io.File;
import java.util.Iterator;
import java.util.List;

/**
 * Unit test for <code>PackagesInfo</code>.
 *
 * @author Tim Lavers
 */
public class PackagesInfoTest {

    //One to test.
    private PackagesInfo<PackageInfo> pi;

    private void init() throws Exception {
        //Expand the zip archive into the temp directory.
        Helpers.cleanTempDirectory();
        File zip = new File(Grandtestauto.test1_zip);
        Helpers.expandZipTo(zip, Helpers.classesDirClassic());
        pi = new PackagesInfo<PackageInfo>(new PackagesInfo.Filter() {
            public boolean accept(String packageName) {
                return PackagesInfo.namesPackageThatMightNeedUnitTests(packageName);
            }
        }, Helpers.classesDirClassic()) {
            public PackageInfo createClassFinder(String packageName, File baseDir) {
                return new PackageInfo(packageName, baseDir);
            }
        };
    }

    public boolean classesRootTest() throws Exception {
        init();
        assert pi.classesRoot().equals(Helpers.classesDirClassic());
        return true;
    }

    public boolean namesPackageThatMightNeedUnitTestsTest() {
        assert PackagesInfo.namesPackageThatMightNeedUnitTests("a1");
        assert !PackagesInfo.namesPackageThatMightNeedUnitTests("a1.test");
        assert !PackagesInfo.namesPackageThatMightNeedUnitTests("a1.test.extra");
        assert !PackagesInfo.namesPackageThatMightNeedUnitTests("a1.functiontest");
        assert !PackagesInfo.namesPackageThatMightNeedUnitTests("a1.functiontest.junk");
        assert !PackagesInfo.namesPackageThatMightNeedUnitTests("a1.loadtest");
        assert !PackagesInfo.namesPackageThatMightNeedUnitTests("a1.loadtest.blah");
        return true;
    }

    public boolean namesFunctionTestPackageTest() {
        assert !PackagesInfo.namesFunctionTestPackage("a1");
        assert !PackagesInfo.namesFunctionTestPackage("a1.test");
        assert PackagesInfo.namesFunctionTestPackage("a1.functiontest");
        assert !PackagesInfo.namesFunctionTestPackage("a1.loadtest");
        return true;
    }

    public boolean namesLoadTestPackageTest() {
        assert !PackagesInfo.namesLoadTestPackage("a1");
        assert !PackagesInfo.namesLoadTestPackage("a1.test");
        assert !PackagesInfo.namesLoadTestPackage("a1.functiontest");
        assert PackagesInfo.namesLoadTestPackage("a1.loadtest");
        return true;
    }

    public boolean testablePackageNamesTest() throws Exception {
        init();
        List<String> s = pi.testablePackageNames();
        assert s.size() == 6;
        assert s.get(0).equals("a1");
        assert s.get(1).equals("a1.b");
        assert s.get(2).equals("a1.b.e");
        assert s.get(3).equals("a1.b.e.g");
        assert s.get(4).equals("a1.c.h");
        assert s.get(5).equals("a1.d");
        return true;
    }

    public boolean packageInfoTest() throws Exception {
        init();
        PackageInfo info = pi.packageInfo("a1");
        Iterator<String> itor = info.classNameToTestability().keySet().iterator();
        assert itor.next().equals("X");
        assert info.classNameToTestability().get("X").equals(Testability.TEST_REQUIRED);
        assert itor.next().equals("Y");
        assert info.classNameToTestability().get("Y").equals(Testability.TEST_REQUIRED);
        assert !itor.hasNext();

        info = pi.packageInfo("a1.b.e.g");
        itor = info.classNameToTestability().keySet().iterator();
        assert itor.next().equals("UnitTester");
        assert info.classNameToTestability().get("UnitTester").equals(Testability.TEST_REQUIRED);
        assert itor.next().equals("X");
        assert info.classNameToTestability().get("X").equals(Testability.TEST_REQUIRED);
        assert !itor.hasNext();
        return true;
    }
}