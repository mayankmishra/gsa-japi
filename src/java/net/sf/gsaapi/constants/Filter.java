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
public final class Filter {

    /**
     * specifies that no filtering should be performed
     */
    public static final Filter NO_FILTER = new Filter('0');

    /**
     * specifies that both "Duplicate directory" and "Duplicate snippet"
     * filtering should be performed
     */
    public static final Filter FULL_FILTER = new Filter('1');
    
    /**
     * Only "Duplicate snippet" filtering should be performed
     */
    public static final Filter DUPLICATE_SNIPPET_FILTER = new Filter('s');
    
    /**
     * Only "Duplicate directory" filtering should be performed
     */
    public static final Filter DUPLICATE_DIRECTORY_FILTER = new Filter('p');

    private char value;

    private Filter(char value) {
        this.value = value;
    }

    public int hashCode() {
        return value;
    }

    public boolean equals(Object o) {
        boolean retval = false;
        if (o != null && o instanceof Filter) {
            Filter other = (Filter) o;
            retval = other.value == this.value;
        }
        return retval;
    }

    public char getValue() {
        return value;
    }
}
