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

import java.util.Properties;

import net.sf.gsaapi.GSAQuery.GSAQueryTerm;
import net.sf.gsaapi.constants.Access;
import net.sf.gsaapi.constants.Filter;
import net.sf.gsaapi.constants.OutputFormat;
import net.sf.gsaapi.constants.SearchScope;
import net.sf.gsaapi.util.Util;

/**
 * simple tests to see that the underlying query is 
 * formed correctly
 * @author Amol S Deshmukh
 *
 */
public class TestGSAQuery extends GSATestCase {


    public void testAccess() {
        GSAQuery query = new GSAQuery();
        query.setAccess(Access.ALL);
        String value = query.getValue();
        assertTrue(value.indexOf("access=a") >= 0);
        assertTrue(value.indexOf("access=s") < 0);
        assertTrue(value.indexOf("access=p") < 0);

        query.setAccess(Access.PUBLIC);
        value = query.getValue();
        assertTrue(value.indexOf("access=a") < 0);
        assertTrue(value.indexOf("access=s") < 0);
        assertTrue(value.indexOf("access=p") >= 0);

        query.setAccess(Access.SECURE);
        value = query.getValue();
        assertTrue(value.indexOf("access=a") < 0);
        assertTrue(value.indexOf("access=s") >= 0);
        assertTrue(value.indexOf("access=p") < 0);

    }
    public void testSearchScope() {
        GSAQuery query = new GSAQuery();
        String value = null;
        query.setSearchScope(SearchScope.ENTIRE_PAGE);
        value = query.getValue();
        assertTrue(value.indexOf("as_occt=any") >= 0);
        assertTrue(value.indexOf("as_occt=title") < 0);
        assertTrue(value.indexOf("as_occt=url") < 0);

        query.setSearchScope(SearchScope.URL);
        value = query.getValue();
        assertTrue(value.indexOf("as_occt=any") < 0);
        assertTrue(value.indexOf("as_occt=title") < 0);
        assertTrue(value.indexOf("as_occt=url") >= 0);

        query.setSearchScope(SearchScope.TITLE);
        value = query.getValue();
        assertTrue(value.indexOf("as_occt=any") < 0);
        assertTrue(value.indexOf("as_occt=title") >= 0);
        assertTrue(value.indexOf("as_occt=url") < 0);

    }
    public void testFilter() {
        GSAQuery query = new GSAQuery();
        String value = null;
        query.setFilter(Filter.FULL_FILTER);
        value = query.getValue();
        assertTrue(value.indexOf("filter=1") >= 0);
        assertTrue(value.indexOf("filter=p") < 0);
        assertTrue(value.indexOf("filter=s") < 0);
        assertTrue(value.indexOf("filter=0") < 0);

        query.setFilter(Filter.DUPLICATE_DIRECTORY_FILTER);
        value = query.getValue();
        assertTrue(value.indexOf("filter=1") < 0);
        assertTrue(value.indexOf("filter=p") >= 0);
        assertTrue(value.indexOf("filter=s") < 0);
        assertTrue(value.indexOf("filter=0") < 0);

        query.setFilter(Filter.DUPLICATE_SNIPPET_FILTER);
        value = query.getValue();
        assertTrue(value.indexOf("filter=1") < 0);
        assertTrue(value.indexOf("filter=p") < 0);
        assertTrue(value.indexOf("filter=s") >= 0);
        assertTrue(value.indexOf("filter=0") < 0);

        query.setFilter(Filter.NO_FILTER);
        value = query.getValue();
        assertTrue(value.indexOf("filter=1") < 0);
        assertTrue(value.indexOf("filter=p") < 0);
        assertTrue(value.indexOf("filter=s") < 0);
        assertTrue(value.indexOf("filter=0") >= 0);

    }
    public void testOutputFormat() {
        GSAQuery query = new GSAQuery();
        String value = null;
        query.setOutputFormat(OutputFormat.XML_NO_DTD);
        value = query.getValue();
        assertTrue(value.indexOf("output=xml_no_dtd") >= 0);
        assertTrue(value.indexOf("output=xml&") < 0);
        assertTrue(value.indexOf("output=xml") != value.length()-10);

        query.setOutputFormat(OutputFormat.XML);
        value = query.getValue();
        assertTrue(value.indexOf("output=xml_no_dtd") < 0);
        assertTrue(value.indexOf("output=xml") >= 0);
    }
    public void testMaxResults() {
        GSAQuery query = new GSAQuery();
        String value = null;
        query.setMaxResults(100);
        value = query.getValue();
        assertTrue(value.indexOf("num=100") >= 0);
    }
    public void testFileTypes() {
        GSAQueryTerm term = new GSAQueryTerm();
        term.addFileType("dat", true);
        term.addFileType("doc", true);
        term.addFileType("txt", false);
        term.addFileType("pdf", false);
        String value = term.getValue();
        assertEquals("More than one OR found. ", 1, countDistinct(value, "OR"));
        assertEquals("More than one OR found. ", 1, countDistinct(term.getValue(), "OR"));
        assertEquals("Incorrect num of filetype: found.", 4, countDistinct(value, "filetype:"));
        assertEquals("Incorrect num of filetype: found.", 4, countDistinct(term.getValue(), "filetype:"));
        assertEquals("Incorrect num of -filetype: found.", 2, countDistinct(value, "-filetype:"));
        assertEquals("Incorrect num of -filetype: found.", 2, countDistinct(term.getValue(), "-filetype:"));
        assertTrue("filetype: not separated by OR.", term.getValue().indexOf("OR filetype:")>1);
    }
    public void testSite() {
        GSAQueryTerm term = new GSAQueryTerm();
        term.setSite("some1.url", true);
        term.setSite("some2.url", true);
        assertEquals("site not set correctly.", "site:some2.url ", term.getValue());
        term.setSite("some3.url", false);
        assertEquals("site not set correctly.", "-site:some3.url ", term.getValue());
        term.setSite(null, false);
        assertEquals("site not unset correctly.", "", term.getValue());
    }
    public void testRequiredFields() {
        GSAQuery query = new GSAQuery();
        Properties props = new Properties();
        props.put("nAme1", "vAlue1");
        props.put("name2", "value2");
        props.put("Name3", "VAlue3.value4");
        query.setRequiredMetaFields(props);
        
        // TODO: Use Util.extractQueryParams for the assertion
        assertEquals("requiredfields param from multiple key-value pairs failed",
                "access=p&output=xml&client=&requiredfields=Name3%3AVAlue3.value4%7CnAme1%3AvAlue1%7Cname2%3Avalue2",
                query.getValue());
        
        query = new GSAQuery();
        query.setRequiredMetaFields(props, true);
        assertEquals("requiredfields param from multiple key-value pairs failed",
                "access=p&output=xml&client=&requiredfields=Name3%3AVAlue3.value4%7CnAme1%3AvAlue1%7Cname2%3Avalue2",
                query.getValue());
        
        query = new GSAQuery();
        query.setRequiredMetaFields(props, false);
        assertEquals("requiredfields param from multiple key-value pairs failed",
                "access=p&output=xml&client=&requiredfields=Name3%3AVAlue3.value4.nAme1%3AvAlue1.name2%3Avalue2",
                query.getValue());
    }
    public void testPartialFields() {
        GSAQuery query = new GSAQuery();
        Properties props = new Properties();
        props.put("nAme1", "vAlue1");
        props.put("name2", "value2");
        props.put("Name3", "VAlue3.value4");
        query.setPartialMetaFields(props);
        assertEquals("partialfields param from multiple key-value pairs failed",
                "access=p&output=xml&client=&partialfields=Name3%3AVAlue3.value4%7CnAme1%3AvAlue1%7Cname2%3Avalue2",
                query.getValue());
        
        query = new GSAQuery();
        query.setPartialMetaFields(props, true);
        assertEquals("partialfields param from multiple key-value pairs failed",
                "access=p&output=xml&client=&partialfields=Name3%3AVAlue3.value4%7CnAme1%3AvAlue1%7Cname2%3Avalue2",
                query.getValue());

        query = new GSAQuery();
        query.setPartialMetaFields(props, false);
        assertEquals("partialfields param from multiple key-value pairs failed",
                "access=p&output=xml&client=&partialfields=Name3%3AVAlue3.value4.nAme1%3AvAlue1.name2%3Avalue2",
                query.getValue());
    }
    
    public void testSortByDate() {
        GSAQuery query = new GSAQuery();
        query.setSortByDate(false, 'S');
        assertEquals("sortbydate (false, S) failed",
                "access=p&output=xml&sort=date%3AD%3AS%3Ad1&client=",
                query.getValue());
        query.setSortByDate(true, 'R');
        assertEquals("sortbydate (true, R) failed",
                "access=p&output=xml&sort=date%3AA%3AR%3Ad1&client=",
                query.getValue());
        query.setSortByDate(false, 'L');
        assertEquals("sortbydate (false, L) failed",
                "access=p&output=xml&sort=date%3AD%3AL%3Ad1&client=",
                query.getValue());
    }
    
    public void testOrQueryTerms() {
        GSAQuery query = new GSAQuery();
        query.setOrQueryTerms(new String[]{"this that", "here", "there"});
        assertTrue(query.getValue().indexOf("as_oq=this+that+here+there")>0);
    }
    
    public void testAndQueryTerms() {
        GSAQuery query = new GSAQuery();
        query.setAndQueryTerms(new String[]{"this", "that", "here", "there"});
        assertTrue(query.getValue().indexOf("as_q=this+that+here+there")>0);
    }
    
    public void testNotQueryTerms() {
    	GSAQuery query = new GSAQuery();
    	query.setNotQueryTerms(new String[]{"this", "that"});
    	String paramValue = Util.extractQueryParamValue(query.getValue(), "as_eq");
    	assertEquals("this+that", paramValue);
    }
}
