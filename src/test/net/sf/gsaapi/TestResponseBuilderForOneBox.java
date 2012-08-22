/********************************************************************************
 *  
 *  Product: GSA-JAPI
 *  Description: A Java API for programmatically accessing the Google Search
 *               Appliance.
 *
 *  (c) Copyright 2006 Inxight Software, Inc.
 *  
 *  Licensed under the Inxight Software, Inc., GSA-JAPI License (the "License").
 *  You may not use this file except in compliance with the License. You should
 *  have received a copy of the License with this distribution. If not, you may
 *  obtain a copy by contacting:
 *
 *      Inxight Software, Inc.
 *      500 Macara Ave.
 *      Sunnyvale, CA 94085
 *
 *  Unless required by applicable law or agreed to in writing, software distributed
 *  under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR
 *  CONDITIONS OF ANY KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations under the License.
 ********************************************************************************/
package net.sf.gsaapi;

import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class TestResponseBuilderForOneBox extends GSATestCase {

    public void testOneBox10() throws Exception {
        GSAResponse response = ResponseBuilder.buildResponse(
                new FileInputStream("src/test/data/OneBox10.xml"),
                GSAClient.DEFAULT_XML_SYSTEM_ID);
        assertEquals(10, response.getResults().size());
        assertEquals(1, response.getOneBoxResponses().size());
        
        GSAOneBoxResponse onebox1 = (GSAOneBoxResponse) response.getOneBoxResponses().get(0);
        assertEquals("http://directory.corp.acme.com/images/directory.jpg", onebox1.getImageSource().trim());
        assertEquals("ACME Employee Directory", onebox1.getProviderName().trim());
        assertEquals("http://directory.corp.acme.com/cgi-bin/search?bill%20smith", onebox1.getTitleLink().trim());
        assertEquals("13 results in The ACME directory", onebox1.getTitleText().trim());
    }

    public void testOneBox100() throws Exception {
        GSAResponse response = ResponseBuilder.buildResponse(
                new FileInputStream("src/test/data/OneBox100.xml"),
                GSAClient.DEFAULT_XML_SYSTEM_ID);
        assertEquals(100, response.getResults().size());
        assertEquals(2, response.getOneBoxResponses().size());
        
        GSAOneBoxResponse onebox1 = (GSAOneBoxResponse) response.getOneBoxResponses().get(0);
        assertEquals("http://directory.corp.acme.com/images/directory.jpg", onebox1.getImageSource().trim());
        assertEquals("ACME Employee Directory", onebox1.getProviderName().trim());
        assertEquals("http://directory.corp.acme.com/cgi-bin/search?machine%20amazement", onebox1.getTitleLink().trim());
        assertEquals("13 results in The ACME directory", onebox1.getTitleText().trim());

        onebox1 = (GSAOneBoxResponse) response.getOneBoxResponses().get(1);
        assertEquals("http://telephone.corp.acme.com/images/telephone.jpg", onebox1.getImageSource().trim());
        assertEquals("ACME Telephone Directory", onebox1.getProviderName().trim());
        assertEquals("http://telephone.corp.acme.com/cgi-bin/search?machine%20amazement", onebox1.getTitleLink().trim());
        assertEquals("2 results in The ACME Telephone directory", onebox1.getTitleText().trim());
        Map fieldEntriesMap = new HashMap();
        Entry[] entries = ((GSAOneBoxResult) onebox1.getModuleResults().get(0)).getFieldEntries();
        for  (int i=0; i<entries.length; i++) {
            fieldEntriesMap.put(entries[i].getKey(), entries[i].getValue());
        }
        
        assertEquals("Turner, John", fieldEntriesMap.get("display"));
        assertEquals("John", fieldEntriesMap.get("firstname"));
        assertEquals("http://telephone.corp.acme.com/cqi-bin/lookup?photo=448478", fieldEntriesMap.get("picture"));
    }
        

}
