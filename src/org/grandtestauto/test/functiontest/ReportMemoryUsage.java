package org.grandtestauto.test.functiontest;

import org.grandtestauto.GrandTestAuto;
import org.grandtestauto.test.Helpers;
import org.grandtestauto.test.dataconstants.org.grandtestauto.Grandtestauto;

import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/** See the GrandTestAuto test specification. */
public class ReportMemoryUsage extends FTBase {
    public static List<String> stringList = new LinkedList<String>();

    public boolean runTest() {
        GrandTestAuto gta = Helpers.setupForZip(new File(Grandtestauto.test107_zip), true, true, true);
        gta.runAllTests();
        List<String> packageResultLines = Helpers.unitTestResultLogLines();
        assert packageResultLines.size() == 3 : "Got: " + packageResultLines;
        int memoryReported0 = getReportedMemoryUsage(packageResultLines.get(0));
        assert memoryReported0 > 8 : "Reported memory usage less than expected, got: " + memoryReported0;

        int memoryReported1 = getReportedMemoryUsage(packageResultLines.get(1));
        assert memoryReported1 >= memoryReported0 : "Reported memory usage less than expected, got: " + memoryReported0;

        int memoryReported2 = getReportedMemoryUsage(packageResultLines.get(2));
        assert memoryReported2 >= memoryReported1 : "Reported memory usage less than expected, got: " + memoryReported0;

        //For the function and load tests, the memory usage change is much smaller and will not necessarily affect the reported values.
        packageResultLines = Helpers.functionTestResultLogLines();
        memoryReported0 = getReportedMemoryUsage(packageResultLines.get(0));
        assert memoryReported0 >= memoryReported2 : "Reported memory usage less than expected, got: " + memoryReported0;

        memoryReported1 = getReportedMemoryUsage(packageResultLines.get(1));
        assert memoryReported1 >= memoryReported0 : "Reported memory usage less than expected, got: " + memoryReported0;

        memoryReported2 = getReportedMemoryUsage(packageResultLines.get(2));
        assert memoryReported2 >= memoryReported1 : "Reported memory usage less than expected, got: " + memoryReported0;

        packageResultLines = Helpers.loadTestResultLogLines();
        assert packageResultLines.size() == 3 : "Got: " + packageResultLines;
        memoryReported0 = getReportedMemoryUsage(packageResultLines.get(0));
        assert memoryReported0 >= memoryReported2 : "Reported memory usage less than expected, got: " + memoryReported0;

        memoryReported1 = getReportedMemoryUsage(packageResultLines.get(1));
        assert memoryReported1 >= memoryReported0 : "Reported memory usage less than expected, got: " + memoryReported0;

        memoryReported2 = getReportedMemoryUsage(packageResultLines.get(2));
        assert memoryReported2 >= memoryReported1 : "Reported memory usage less than expected, got: " + memoryReported0;
        return true;
    }

    private int getReportedMemoryUsage(String s) {
        Pattern memoryUsagePattern = Pattern.compile(".* ([0-9]{1,3})M\\.*");
        Matcher m = memoryUsagePattern.matcher(s);
        m.find();
        return Integer.parseInt(m.group(1));
    }
}