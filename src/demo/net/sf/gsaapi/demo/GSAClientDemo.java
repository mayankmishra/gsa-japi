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
package net.sf.gsaapi.demo;

import java.util.List;

import net.sf.gsaapi.*;
import net.sf.gsaapi.GSAQuery.GSAQueryTerm;

/**
 * A Java application demonstrating the use of the GSA-JAPI.
 * It might help to get this example running for your target
 * GSA before you attempt to build an application that uses
 * this API. The String constants in this file may need to be
 * changed for your target GSA to make this example work.
 *  
 * @author Amol S Deshmukh adeshmuk .at inxight .dot com
 */
public class GSAClientDemo {
    
    // target GSA's hostname
    private static final String HOSTNAME = "my.gsa.host";
    
    // query string to search for
    private static final String QUERY_STRING = "java";
    
    // The value for the frontend configured for the GSA 
    // (If you dont know this, ask GSA admin for correct value for your target GSA.)
    private static final String SETTING_FRONTEND = "default_frontend";

    /**
     * no command line args expected. To change the settings,
     * change the values of the String constants defined in the
     * source java file.
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        GSAClient client = new GSAClient(HOSTNAME);  
        GSAQuery query = new GSAQuery();
        
        // typical way to generate query term.
        GSAQueryTerm term = new GSAQueryTerm(QUERY_STRING);
        query.setQueryTerm(term);
        System.out.println("Searching for: "+query.getQueryString());
        
        // above 2 lines may be equivalently replaced by: 
        // query.setAndQueryTerms(new String[]{QUERY_STRING}); 
        
        /*
         * You may need to see the GSA xml reference to understand
         * why there are multiple ways of doing the same query,
         * and which is the better approach for your particular scenario. 
         */
        
        query.setFrontend(SETTING_FRONTEND); // required!
        
        GSAResponse response = client.getGSAResponse(query);

        System.out.println("Found " + response.getNumResults() + " results");
        List results = response.getResults();
        System.out.println("Showing top " + results.size() + " results");
        System.out.println("[To get more top N results, use query.setMaxResults(int)]");
        for (int i=0, iSize=results.size(); i<iSize; i++) {
            GSAResult result = (GSAResult) results.get(i);
            System.out.println("--------------------------------------------------");
            System.out.println(result.getRating() +"\t" + result.getTitle());
            System.out.println(result.getSummary());
            System.out.println(result.getUrl());
        }


        List navigationResults = response.getNavigationResponse().getResults();
        for(int i=0, iSize = navigationResults.size(); i<iSize;i++){
            System.out.println("-----------------");
            GSADynamicNavigationAttribute navigationAttribute = (GSADynamicNavigationAttribute) navigationResults.get(i);
            System.out.println("Attribute Name  : " + navigationAttribute.getName());
            System.out.println("Attribute Label : " + navigationAttribute.getLabel());
            System.out.println("Attribute isRange : " + navigationAttribute.isRange());
            System.out.println("Attribute Type : " + navigationAttribute.getType());

            List attributeResultList = navigationAttribute.getResultList();
            for(int j=0, jSize = attributeResultList.size(); j<jSize; j++){
                GSADynamicNavigationAttributeResult attributeResult = (GSADynamicNavigationAttributeResult) attributeResultList.get(j);
                System.out.println("Value: " + attributeResult.getValue() + " (" +attributeResult.getCount() + ")");
                System.out.println("Range: " + attributeResult.getLowerRage() + " - " + attributeResult.getHigherRange());
            }
        }
    }
}
