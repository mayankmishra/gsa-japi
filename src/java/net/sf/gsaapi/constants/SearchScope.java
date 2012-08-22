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
package net.sf.gsaapi.constants;

/**
 * This class is a Java2 style enumeration implementation.
 * @author Amol S Deshmukh adeshmuk .at inxight .dot com
 */
public class SearchScope {

    /**
     * Search for the query terms anywhere in the page.
     */
    public static final SearchScope ENTIRE_PAGE = new SearchScope("any");

    /**
     * Search for the query terms in document title only.
     */
    public static final SearchScope TITLE = new SearchScope("title");

    /**
     * Search for the query terms in document url only.
     */
    public static final SearchScope URL = new SearchScope("url");

    private String value;

    private SearchScope(String value) {
        this.value = (value == null) ? "" : value;
    }

    public int hashCode() {
        return value == null ? 0 : value.hashCode();
    }

    public boolean equals(Object o) {
        boolean retval = false;
        if (o != null && o instanceof SearchScope) {
            SearchScope other = (SearchScope) o;
            retval = other.value.equals(this.value);
        }
        return retval;
    }

    public String getValue() {
        return value;
    }
}
