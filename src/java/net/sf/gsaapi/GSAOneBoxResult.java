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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * <em>
 * NOTE: OneBox support in this API is experimental only. 
 * Please test thoroughly for your search appliance and 
 * one box source installation that this works.
 * </em>
 * <br/>
 * The Java binding to the result element of the XML response
 * received from querying the GSA.
 * @author Amol S Deshmukh adeshmuk .at inxight .dot com
 */
public class GSAOneBoxResult {


    private String url;
    private List fieldEntries;
    
    /**
     * constructor is intended for internal use only.
     */
    public GSAOneBoxResult() {
        this.fieldEntries = new ArrayList();
    }
    
    /**
     * returns the url associated with this GSAOneBoxResult
     * instance
     * @return returns the url as a String
     */
    public String getUrl() {
        return url;
    }
    void setUrl(String url) {
        this.url = url;
    }

    /**
     * returns the array of Entry objects correponding to the
     * "field" entries in this GSAOneBoxResult instance.
     * @return returns the field entries as an array of Map.Entry objects
     */
    public Entry[] getFieldEntries() {
        return (Entry[]) fieldEntries.toArray(ZERO_LENGTH_ENTRY_ARRAY);
    }
    void addFieldEntry(String key, String value) {
        this.fieldEntries.add(new EntryImpl(key, value));
    }
    
    private static final Entry[] ZERO_LENGTH_ENTRY_ARRAY = new Entry[0];
}

/**
 * Basic impl of Map.Entry for use as a key-value pair
 * @author Amol S Deshmukh
 */
class EntryImpl implements Map.Entry {
    private Object key;
    private Object value;

    public EntryImpl(Object key, Object value) {
        this.key = key;
        this.value = value;
    }
    
    public Object getKey() {
        return key;
    }

    public Object getValue() {
        return value;
    }

    public Object setValue(Object value) {
        Object retval = value;
        this.value = value;
        return retval;
    }
    
}
