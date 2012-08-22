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

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import net.sf.gsaapi.util.Util;

public class TestUtil extends GSATestCase {
	
	public void testAppendMappedQueryParams() {
		Map map = new TreeMap();
		map.put("a", "abc");
		map.put("b", "bcd");
		map.put("c", "cde");
		map.put("d", "def");
		
		StringBuffer sbuf = new StringBuffer();
		Util.appendMappedQueryParams(sbuf, "param", map, "|");
		Util.appendMappedQueryParams(sbuf, "param", map, "|");
		assertEquals("", "param=a%3Aabc%7Cb%3Abcd%7Cc%3Acde%7Cd%3Adef&param=a%3Aabc%7Cb%3Abcd%7Cc%3Acde%7Cd%3Adef", sbuf.toString());
	}
	
	public void testAppendQueryParam1() {
		StringBuffer sbuf = new StringBuffer();
		Util.appendQueryParam(sbuf, "param1", "value1");
		assertEquals("", "param1=value1", sbuf.toString());
		Util.appendQueryParam(sbuf, "param2", "val&ue2");
		assertEquals("", "param1=value1&param2=val%26ue2", sbuf.toString());
	}

	public void testAppendQueryParam2() {
		StringBuffer sbuf = new StringBuffer();
		Util.appendQueryParam(sbuf, "param1", new String[]{"value1", "value2"});
		assertEquals("", "param1=value1&param1=value2", sbuf.toString());
		Util.appendQueryParam(sbuf, "param2", "val&ue2");
		assertEquals("", "param1=value1&param1=value2&param2=val%26ue2", sbuf.toString());
		Util.appendQueryParam(sbuf, "param3", new String[]{"", "value3"});
		assertEquals("", "param1=value1&param1=value2&param2=val%26ue2&param3=&param3=value3", sbuf.toString());
	}

	public void testEncode() {
		String s = Util.encode("abc&\"-~!@#$%^&*()-_+=def");
		assertEquals("", "abc%26%22-%7E%21%40%23%24%25%5E%26*%28%29-_%2B%3Ddef", s);
	}

	public void testStringSeparated1() {
		List tokens = Arrays.asList(new String[]{"tok1", "tok2", "tok3"});
		String prefix = "pre";
		String separator = "SEP";
		String s = Util.stringSeparated(tokens, prefix, separator);
		assertEquals("", "pretok1SEPpretok2SEPpretok3", s);
	}

	public void testStringSeparated2() {
		String[] tokens = new String[]{"tok1", "tok2", "tok3"};
		String prefix = "pre";
		String separator = "SEP";
		String s = Util.stringSeparated(tokens, prefix, separator);
		assertEquals("", "pretok1SEPpretok2SEPpretok3", s);
	}

	public void testToHtmlCode() {
		String code = Util.toHtmlCode('&');
		assertEquals("", "&#38;", code);
		code = Util.toHtmlCode('!');
		assertEquals("", "&#33;", code);
		code = Util.toHtmlCode('@');
		assertEquals("", "&#64;", code);
		code = Util.toHtmlCode('#');
		assertEquals("", "&#35;", code);
		code = Util.toHtmlCode('$');
		assertEquals("", "&#36;", code);
		code = Util.toHtmlCode('%');
		assertEquals("", "&#37;", code);
		code = Util.toHtmlCode('^');
		assertEquals("", "&#94;", code);
		code = Util.toHtmlCode('*');
		assertEquals("", "&#42;", code);
	}
	
    public void testToJulian() {
        Calendar c = new GregorianCalendar();
        c.clear();
        Date date = null;
        
        // 1 Jan 2
        c.set(Calendar.YEAR, 2); c.set(Calendar.MONTH, Calendar.JANUARY); c.set(Calendar.DAY_OF_MONTH, 1);
        date = c.getTime(); 
        assertEquals("Gregorian date:" + date, 1721789, Util.toJulian(date));
        
        // 31 Dec 1
        c.set(Calendar.YEAR, 1); c.set(Calendar.MONTH, Calendar.DECEMBER); c.set(Calendar.DAY_OF_MONTH, 31);
        date = c.getTime(); 
        assertEquals("Gregorian date:" + date, 1721788, Util.toJulian(date));
        
        // 1 Jan 1582
        c.set(Calendar.YEAR, 1582); c.set(Calendar.MONTH, Calendar.JANUARY); c.set(Calendar.DAY_OF_MONTH, 1);
        date = c.getTime(); 
        assertEquals("Gregorian date:" + date, 2298884, Util.toJulian(date));
        
        // 31 Dec 1582
        c.set(Calendar.YEAR, 1582); c.set(Calendar.MONTH, Calendar.DECEMBER); c.set(Calendar.DAY_OF_MONTH, 31);
        date = c.getTime(); 
        assertEquals("Gregorian date:" + date, 2299238, Util.toJulian(date));

        // 13 May 1870
        c.set(Calendar.YEAR, 1870); c.set(Calendar.MONTH, Calendar.MAY); c.set(Calendar.DAY_OF_MONTH, 13);
        date = c.getTime(); 
        assertEquals("Gregorian date:" + date, 2404196, Util.toJulian(date));
        
        // 28 Sep 2006
        c.set(Calendar.YEAR, 2006); c.set(Calendar.MONTH, Calendar.SEPTEMBER); c.set(Calendar.DAY_OF_MONTH, 28);
        date = c.getTime(); 
        assertEquals("Gregorian date:" + date, 2454007, Util.toJulian(date));
        
        // 1 Jan 2010
        c.set(Calendar.YEAR, 2010); c.set(Calendar.MONTH, Calendar.JANUARY); c.set(Calendar.DAY_OF_MONTH, 1);
        date = c.getTime(); 
        assertEquals("Gregorian date:" + date, 2455198, Util.toJulian(date));
        
        // 31 Dec 2010
        c.set(Calendar.YEAR, 2010); c.set(Calendar.MONTH, Calendar.DECEMBER); c.set(Calendar.DAY_OF_MONTH, 31);
        date = c.getTime(); 
        assertEquals("Gregorian date:" + date, 2455562, Util.toJulian(date));
        
        // 1 Jan 3333
        c.set(Calendar.YEAR, 3333); c.set(Calendar.MONTH, Calendar.JANUARY); c.set(Calendar.DAY_OF_MONTH, 1);
        date = c.getTime(); 
        assertEquals("Gregorian date:" + date, 2938414, Util.toJulian(date));
        
        // 31 Dec 3333
        c.set(Calendar.YEAR, 3333); c.set(Calendar.MONTH, Calendar.DECEMBER); c.set(Calendar.DAY_OF_MONTH, 31);
        date = c.getTime(); 
        assertEquals("Gregorian date:" + date, 2938778, Util.toJulian(date));
    }
    
    public void testExtractQueryParam() {
    	String query = "http://somehost.com/somepath/q?abcd=1234&efg=&hi=89";
    	
    	String paramName = "abcd";
    	String value = Util.extractQueryParamValue(query, paramName);
    	assertEquals("1234", value);
    	
    	paramName = "efg";
    	value = Util.extractQueryParamValue(query, paramName);
    	assertEquals("", value);
    	
    	paramName = "hi";
    	value = Util.extractQueryParamValue(query, paramName);
    	assertEquals("89", value);

    	paramName = "abc";
    	value = Util.extractQueryParamValue(query, paramName);
    	assertTrue(value==null);
    }
}
