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

import java.util.List;

import net.sf.gsaapi.constants.Filter;

/**
 * Functional automated tests.
 * @author Amol S Deshmukh
 *
 */
public class TestFunctional extends GSATestCase {
    
	private static String QUERY_TERM = "the";
    private static String GSA_URL = "gsa.url";
    private static String DEFAULT_FRONTEND = "default_frontend";
    private static String GOOGLE_MINI_URL = "gmini.url";
    
    /*
     * add a setUp method to set appropriate values for the above declared
     * variables before running the unit tests. Or create host entries
     * on the test machine that point gsa.url to the target GSA
     * and gmini.url to the target google mini.
     */

    public void testBasic_Mini() throws Exception {
		GSAClient client = new GSAClient(GOOGLE_MINI_URL);
		GSAQuery query = new GSAQuery();
		query.setFrontend(DEFAULT_FRONTEND);
		query.setExactPhraseQueryTerm(QUERY_TERM);
        query.setFilter(Filter.NO_FILTER);
		GSAResponse response = client.getGSAResponse(query);
		//PrettyOutput.response(response);
		int size = response.getResults().size();
		assertEquals("", 10, size);
	}
	public void testBasic_GSA() throws Exception {
		GSAClient client = new GSAClient(GSA_URL);
		GSAQuery query = new GSAQuery();
		query.setFrontend(DEFAULT_FRONTEND);
		query.setExactPhraseQueryTerm(QUERY_TERM);
        query.setFilter(Filter.NO_FILTER);
		GSAResponse response = client.getGSAResponse(query);
		//PrettyOutput.response(response);
		int size = response.getResults().size();
		assertEquals("", 10, size);
	}
    
    public void testDateRetrieval() throws Exception {
        GSAClient client = new GSAClient(GSA_URL);
        GSAQuery query = new GSAQuery();
        query.setFrontend(DEFAULT_FRONTEND);
        query.setExactPhraseQueryTerm(QUERY_TERM);
        query.setFilter(Filter.NO_FILTER);
        query.unsetSortByDate();
        GSAResponse response = client.getGSAResponse(query);
        List results = response.getResults();
        for (int i=0, iSize=results.size(); i<iSize; i++) {
            GSAResult result = (GSAResult) results.get(i);
            String date = result.getField("date");
            if (null != date && !"".equals(date))
                assertTrue(i+"th String does not match date format:"+date, date.matches("[0-9]{4}-[0-9]{2}-[0-9]{2}"));
        }
    }
    
    public void testSortByDate() throws Exception {
        GSAClient client = new GSAClient(GSA_URL);
        GSAQuery query = new GSAQuery();
        query.setFrontend(DEFAULT_FRONTEND);
        query.setExactPhraseQueryTerm(QUERY_TERM);
        query.setFilter(Filter.NO_FILTER);
        query.setSortByDate(true, 'R');
        GSAResponse response = client.getGSAResponse(query);
        PrettyOutput.response(response);
    }
}
