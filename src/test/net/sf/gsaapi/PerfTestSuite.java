package net.sf.gsaapi;

import junit.framework.Test;
import junit.framework.TestSuite;

public class PerfTestSuite {

    public static Test suite() {
        TestSuite suite= new TestSuite();
        suite.addTestSuite(PerfTestXmlParsing.class);
        return suite;
    }
}
