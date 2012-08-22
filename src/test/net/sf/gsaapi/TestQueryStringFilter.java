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

import net.sf.gsaapi.util.QueryStringFilter;

public class TestQueryStringFilter extends GSATestCase {

    public void testSimple() {
        String unfiltered = "param1=value1&param2=value2&param3=value3&param4=value4";
        String[] retainThese = new String[]{"param1", "param3"};
        QueryStringFilter qsf = new QueryStringFilter(retainThese);
        assertEquals(qsf.filter(unfiltered), "param1=value1&param3=value3");
    }

    public void testMultiValued() {
        String unfiltered = "param1=value1&param1=value11&param2=value2&param3=value3&param1=value12&param4=value4";
        String[] retainThese = new String[]{"param1", "param3"};
        QueryStringFilter qsf = new QueryStringFilter(retainThese);
        assertEquals(qsf.filter(unfiltered), "param1=value1&param1=value11&param3=value3&param1=value12");
    }
}
