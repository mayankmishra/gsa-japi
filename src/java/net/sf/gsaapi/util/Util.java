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

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Utilities class. Mostly intended for internal use.
 * @author Amol S Deshmukh adeshmuk .at inxight .dot com
 */
public class Util {
    
    /**
     * calls the overloaded public method: 
     * getString(Object, String, boolean)
     * with the value true for the boolean parameter
     */
    public static String getString(Object obj, String defolt) {
        return Util.getString(obj, defolt, true);
    }
    
    
    /**
     * Returns <code>obj.toString()</code> if obj is not null 
     * and if the expression 
     * <code>!nullstrict || !"".equals(obj.toString())</code>
     * evaluates to true.
     * Otherwise returns the value specified by the 
     * <code>defolt</code> parameter.
     * @param obj The object to be converted to String.
     * @param defolt The String value to be returned if conversion fails.
     * @param nullstrict Whether the result of obj#toString should be
     * returned (rather than defolt) even if it is an empty string.
     * @return Returns the result of toString invokation
     * on the object if the object is non null and toString
     * representation is not null or an empty string.
     * Else returns the <code>defolt</code> value.
     */
    public static String getString(Object obj, String defolt, boolean nullstrict) {
        String retval = defolt;
        if (null != obj 
                && null != obj.toString() 
                && (!nullstrict || !"".equals(obj.toString()))) {
            retval = obj.toString();
        }
        return retval;
    }

    /**
     * Given a list of string tokens, returns the string
     * <em>&lt;prefix&gt;</em>&lt;token1&gt;<em>&lt;delim&gt;</em><em>&lt;prefix&gt;</em>&lt;token2&gt;<em>&lt;delim&gt;</em><em>&lt;prefix&gt;</em>&lt;tokenN&gt;
     * .
     * @param tokens The list of String tokens.
     * @param prefix The prefix to be used for each token.
     * @param delim The delimiter that separates each token.
     * @return returns a single string starting with the prefix and 
     * followed by tokens separated with the delim
     */
    public static final String stringSeparated(List tokens, String prefix, String delim) {
        StringBuffer sbuf = new StringBuffer();

        if (tokens != null) {
            for (int i = 0, iSize = tokens.size(); i < iSize; i++) {
                sbuf.append(i <= 0 || i >= iSize ? "" : delim);
                sbuf.append(prefix == null ? "" : prefix).append(tokens.get(i));
            }
        }
        return sbuf.toString();
    }

    /**
     * overloaded form of stringSeparated(List, String, String) that takes
     * String[] instead of List
     * @param tokens
     * @param prefix
     * @param delim
     * @return returns a single string starting with the prefix and 
     * followed by tokens separated with the delim 
     */
    public static final String stringSeparated(String[] tokens, String prefix, String delim) {
        StringBuffer sbuf = new StringBuffer();

        if (tokens != null) {
            for (int i = 0, iSize = tokens.length; i < iSize; i++) {
                sbuf.append(i <= 0 || i >= iSize ? "" : delim);
                sbuf.append(prefix == null ? "" : prefix).append(tokens[i]);
            }
        }
        return sbuf.toString();
    }

    /**
     * For example, appends "&param=value" to StringBuffer. <code>value</code> is URLEncoded before
     * appending.
     * @param sbuf 
     * @param param
     * @param value
     */
    public static final void appendQueryParam(StringBuffer sbuf, String param, String value) {
        if (sbuf.length() > 0 && '&' != sbuf.charAt(sbuf.length()-1)) {
            sbuf.append('&');
        }
        sbuf.append(param).append('=').append(encode(value));
    }

    /**
     * invokes appendQueryParam(StringBuffer, String, String) on each of the values.
     * @param sbuf
     * @param param
     * @param values
     */
    public static final void appendQueryParam(StringBuffer sbuf, String param, String[] values) {
        if (param != null && values != null && values.length > 0) {
            for (int i = 0, iSize = values.length; i < iSize; i++) {
                appendQueryParam(sbuf, param, values[i]);
            }
        }
    }


    /**
     * For example, appends "&param=key1:value1|key2:value2|key3:value3"
     * to the specified StringBuffer.
     * @param sbuf StringBuffer to which the query param string is appended.
     * @param param name of the query parameter.
     * @param fieldsMap the map of field names and values which together
     * form the "value" of the query param.
     */
    public static final void appendMappedQueryParams(
            StringBuffer sbuf, String param, Map fieldsMap, String delimiter) {
        if (sbuf.length() > 0 && '&' != sbuf.charAt(sbuf.length()-1)) {
        	sbuf.append("&");
        }
        sbuf.append(param).append("=");
        Set entries = fieldsMap.entrySet();
        boolean firstTime = true;
        for (Iterator i = entries.iterator(); i.hasNext();) {
            Map.Entry entry = (Map.Entry) i.next(); // entry: {key, value}}
            String key = (String) entry.getKey();
            String value = (String) entry.getValue();
            if (!firstTime) {
                sbuf.append(encode(delimiter));
            }
            
            /* key and value are expected to be URL escaped.
             * Here we perform the "double" url escaping as
             * required by GSA.
             */
            String encvalue = encode(key + (value == null ? "" : ":" + value));
            sbuf.append(encvalue);
            firstTime = false;
        }
    }


    /**
     * url encodes but catches UnsupportedEncodingException
     * (since we know that will never be thrown).
     * @param value String to be url encoded.
     * @return returns the UTF-8 url-encoded string or empty string
     * if "UTF-8" encoding is not supported on the current platform.
     */
    public static final String encode(String value) {
        String retval = "";
        try {
            if (value != null) {
                retval = URLEncoder.encode(value, "UTF-8");
            }
        } catch (UnsupportedEncodingException uee) {
            //uee.printStackTrace(); // TODO: use logging??
        }
        return retval;
    }

    /**
     * converts the character to HTML entity &#c;
     * where c is the integer value of the character.
     * @param c the character to be encoded as HTML entity.
     * @return returns the html entity reference string
     * for the specified character.
     */
    public static String toHtmlCode(char c) {
        return "&#" + ((int) c) + ";";
    }
    
    /**
     * returns <code>defolt</code> if the URLEncoder.encode(str, "UTF-8")
     * call fails. Else returns URLEncoder.encode(str, "UTF-8").
     * @param str String to be url encoded.
     * @param defolt Return value expected if conversion fails.
     * @return the UTF-8 urlencoded string if encoding was successful
     * else returns the value specified by the <code>defolt</code>
     * parameter.
     */
    public static String escape(String str, String defolt) {
        String retval = defolt;
        try {
            retval = URLEncoder.encode(str, "UTF-8");
        } catch (Exception e) {}
        return retval;
    }
    
    /**
     * Converts a java.util.Date instance to a long value 
     * representing the Julian Day Number. The java.util.Date 
     * should represent a date later than "1 January 100 AD".
     * <br/>
     * <em>Note: The calculation done in this method is very 
     * simplistic and you should probably do some tests to 
     * verify that this works for your case. You may want to 
     * have a look at the unit tests for this method to
     * see the range of dates for which this method has 
     * been tested.</em>  
     * <br/>
     * 
     * @param date The java.util.Date instance to be converted
     * to its corresponding Julian Day Number representation.
     * @return a long value representing the Julian Day Number.
     */
    public static long toJulian(Date date) {
        Calendar cal = new GregorianCalendar();
        cal.setTime(date);
        cal.set(Calendar.HOUR, 13);
        Date copy = cal.getTime();
        long millis = copy.getTime() - EPOCH.getTime();
        long days = JULIAN_DAYS_EPOCH_OFFSET + (millis /MILLIS_IN_DAY);
        return days;
    }
    
    /**
     * Given an HTML-like query string and a paramName, this method 
     * returns the value of the parameter. If the parameter does
     * not exist in the query string, null is returned. If the parameter
     * exists but has not value, this method will return an empty String. 
     * @param query The query String
     * @param paramName The name of the parameter whose value is to be
     * extracted. 
     * @return returns the String representing the value of the query
     * parameter or null if the parameter does not exist in the query.
     */
    public static String extractQueryParamValue(String query, String paramName) {
    	String retval = null;
    	int beginIndex = query.indexOf(paramName); // paramName=Value
    	if (beginIndex >= 0) {
    		beginIndex += paramName.length();
    		if (query.charAt(beginIndex) == '=') {
    			beginIndex++;
    	    	int endIndex = query.indexOf("&", beginIndex);
    	    	if (endIndex < 0) endIndex = query.length(); // last param in query
    	    	retval = query.substring(beginIndex, endIndex);
    		}
    	}
    	return retval;
    }
    
    private static final long MILLIS_IN_DAY = 24 * 60 * 60 * 1000;
    private static final long JULIAN_DAYS_EPOCH_OFFSET = 1721425L;
    private static final Date EPOCH = new Date();
    static {
        Calendar cal = new GregorianCalendar();
        cal.set(Calendar.YEAR, 1);
        cal.set(Calendar.MONTH, 0);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.set(Calendar.HOUR, 13);
        cal.set(Calendar.MINUTE, 1);
        cal.set(Calendar.SECOND, 1);
        EPOCH.setTime(cal.getTimeInMillis());
    }
}
