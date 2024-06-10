/*
package com.ta.api.framework.listeners;


import org.testng.TestNG;
import org.testng.collections.Lists;
import org.testng.xml.XmlPackage;
import org.testng.xml.XmlSuite;
import org.testng.xml.XmlTest;

import java.util.List;

public class TestRunner {

    public static void main(String[] args) {
        XmlSuite suite = new XmlSuite();
        suite.setName("API Automation Framework");

        XmlTest test = new XmlTest(suite);
        test.setName("WebServices Testing");

        // Get the Excel data path and sheet name from the testng.xml file
        String excelWorkBook = test.getParameter("excelWorkBook");
        String excelSheetName = test.getParameter("excelSheetName");

        // Pass the Excel data path and sheet name as parameters to the test class
        XmlPackage xmlPackage = new XmlPackage("com.ta.api.framework");

        XmlParameters parameters = new XmlParameters();
        parameters.setParameter("excelWorkBook", excelWorkBook);
        parameters.setParameter("excelSheetName", excelSheetName);
        xmlPackage.setParameters(parameters);

        test.setPackages(Lists.newArrayList(xmlPackage));

        List<XmlSuite> suites = Lists.newArrayList();
        suites.add(suite);

        TestNG tng = new TestNG();
        tng.setXmlSuites(suites);
        tng.run();
    }
}
*/
