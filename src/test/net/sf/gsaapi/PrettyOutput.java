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

public final class PrettyOutput {
	
	private PrettyOutput(){}
	
	public static void response(GSAResponse response) {
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
	}

}
