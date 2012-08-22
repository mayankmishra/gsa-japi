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
package net.sf.gsaapi.util;

import java.util.HashSet;
import java.util.Set;

/**
 * This is a simple utility to manipulate url query strings.
 * By specifying an array of url parameter names, this
 * class allows you to use the filter method to strip out
 * any other parameter names/values from an arbitrary 
 * query string.
 * <br/>
 * <code>
 * String[] retainThese = new String[]{"param1", "param3"};
 * 
 * QueryStringFilter qsf = new QueryStringFilter(retainThese);
 * 
 * System.out.println(qsf.filter("param1=value1&param2=value2&param3=value3&param4=value4"));
 * </code>
 * <br/>
 * The above code will print out: "param1=value1&param3=value3"
 * <br/>
 * In general, the order of the parameters is not guaranteed.
 * @author Amol S Deshmukh adeshmuk .at inxight .dot com
 *
 */
public class QueryStringFilter {
    private Set paramSet = new HashSet();

    /**
     * Constructor specifying the array of Strings 
     * representing the params that should be 
     * retained when filtering.
     * @param retainedParams
     */
    public QueryStringFilter(String[] retainedParams) {
        for (int i=0, iSize=retainedParams.length; i<iSize; i++) {
            paramSet.add(retainedParams[i]);
        }
    }
    
    /**
     * Returns the filtered queryString (Thus, only the params specified
     * in the constructor are retained and other params are stripped
     * out of the query string)
     * @param queryString
     * @return the filtered HTTP queryString
     */
    public String filter(String queryString) {
        StringBuffer sbuf = new StringBuffer();
        String[] params = queryString.split("&");
        boolean firstTime = true;
        for (int i=0, iSize=params.length; i<iSize; i++, firstTime = false) {
            if (null != params[i]) {
                String[] keyValue = params[i].split("=");
                if (paramSet.contains(keyValue[0])) {
                    if (!firstTime) {
                        sbuf.append('&');
                    }
                    sbuf.append(keyValue[0]).append('=').append(keyValue[1]);
                }
            }
        }
        
        return sbuf.toString();
    }
}
