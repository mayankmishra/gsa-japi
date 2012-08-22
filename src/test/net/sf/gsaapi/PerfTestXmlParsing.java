package net.sf.gsaapi;

import java.io.FileInputStream;

import junit.framework.TestCase;

public class PerfTestXmlParsing extends TestCase {

    public void testOne() throws Exception {
        
        long start = System.currentTimeMillis();
        for (int i = 0; i < 1000; i++) {
            FileInputStream fis = new FileInputStream("src/test/data/Simple10.xml");
            ResponseBuilder.buildResponse(fis, GSAClient.DEFAULT_XML_SYSTEM_ID);
            fis.close();
        }
        System.out.println("Total time: "+(System.currentTimeMillis()-start) + "ms");
    }
}
