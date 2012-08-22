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

import java.io.FileInputStream;
import java.util.List;
import java.util.Map;

import net.sf.gsaapi.constants.Access;
import net.sf.gsaapi.constants.Filter;
import net.sf.gsaapi.constants.OutputFormat;
import net.sf.gsaapi.util.GSACacheQueryUtil;

public class TestResponseBuilder extends GSATestCase {

    /*
     * To enable these tests, you will have to ensure that 
     * GSAClient.DEFAULT_XML_SYSTEM_ID is set to an appropriate
     * value. (eg. http://gsa.host.url/)
     * This can be done by setting the appropriate property
     * in file src/resources/META-INF/gsaclient.properties
     */
 
    public void testSimple10() throws Exception {
        GSAResponse response = ResponseBuilder.buildResponse(
                new FileInputStream("src/test/data/Simple10.xml"),
                GSAClient.DEFAULT_XML_SYSTEM_ID);
        assertEquals(10, response.getResults().size());
        assertEquals("/search?q=machine+amazement&num=100&hl=en&lr=&ie=UTF-8&output=xml&access=p&start=10&sa=N", response.getNextResponseUrl());
        assertEquals("/search?q=machine+amazement&num=100&hl=en&lr=&ie=UTF-8&output=xml&access=p&start=0&sa=N", response.getPreviousResponseUrl());
        assertEquals(0, response.getOneBoxResponses().size());
    }

    public void testSimple100() throws Exception {
        GSAResponse response = ResponseBuilder.buildResponse(
                new FileInputStream("src/test/data/Simple100.xml"),
                GSAClient.DEFAULT_XML_SYSTEM_ID);
        assertEquals(100, response.getResults().size());
        
        assertEquals(0, response.getOneBoxResponses().size());
    }

    public void testKeywords() throws Exception {
        GSAResponse response = ResponseBuilder.buildResponse(
                new FileInputStream("src/test/data/Simple100.xml"),
                GSAClient.DEFAULT_XML_SYSTEM_ID);
        assertEquals("http://www.google.com/", ((GSAKeymatch)response.getKeymatchResults().get(0)).getUrl());
        assertEquals("Description 1", ((GSAKeymatch)response.getKeymatchResults().get(0)).getDescription());
    }
    
    public void testSimple10Cache() throws Exception {
        GSAResponse response = ResponseBuilder.buildResponse(
                new FileInputStream("src/test/data/Simple10.xml"),
                GSAClient.DEFAULT_XML_SYSTEM_ID);
        List results = response.getResults();
        GSAResult result = (GSAResult) results.get(0);
        assertEquals(result.getCacheDocEncoding(), "ISO-8859-1");
        assertEquals(result.getCacheDocId(), "OjydvtGXC1sJ");
        assertEquals(result.getCacheDocSize(), "23k");
        
        result = (GSAResult) results.get(7);
        assertEquals(result.getCacheDocEncoding(), null);
        assertEquals(result.getCacheDocId(), null);
        assertEquals(result.getCacheDocSize(), null);
        
        result = (GSAResult) results.get(9);
        assertEquals(result.getCacheDocEncoding(), "UTF-8");
        assertEquals(result.getCacheDocId(), "DOIf-6LmKBwJ");
        assertEquals(result.getCacheDocSize(), "");
        
    }

    public void testCacheQuery() throws Exception {
        GSAResponse response = ResponseBuilder.buildResponse(
                new FileInputStream("src/test/data/Simple10.xml"),
                GSAClient.DEFAULT_XML_SYSTEM_ID);
        List results = response.getResults();
        GSAClient client = new GSAClient("host.name", 80, "/search");
        GSAResult result = (GSAResult) results.get(0);
        GSAQuery query = new GSAQuery();
        query.setOutputFormat(OutputFormat.XML_NO_DTD);
        query.setQueryTerm(new GSAQuery.GSAQueryTerm("java sdk"));
        query.setAccess(Access.SECURE);
        query.setFilter(Filter.DUPLICATE_SNIPPET_FILTER);
        query.setFrontend("default_frontend");
        query.setMaxResults(100);
        query.setNumKeyMatches((byte) 3);
        query.setScrollAhead(10);
        GSACacheQueryUtil cqu = new GSACacheQueryUtil(
                client, query);
        cqu.setProxystylesheet("default_frontend");
        String cacheDocUrl = cqu.getCacheDocUrl(result, false);
        assertEquals("http://host.name:80/search?q=cache:OjydvtGXC1sJ:http://blue.none.url/posts/comp.htm&proxystylesheet=default_frontend&oe=ISO-8859-1&access=s&output=xml_no_dtd&client=default_frontend&num=100"
                , cacheDocUrl);
        cacheDocUrl = cqu.getCacheDocUrl(result, true);
        assertEquals("http://host.name:80/search?q=cache:OjydvtGXC1sJ:http://blue.none.url/posts/comp.htm+java+sdk&proxystylesheet=default_frontend&oe=ISO-8859-1&access=s&output=xml_no_dtd&client=default_frontend&num=100"
                , cacheDocUrl);
    }

    public void testMeta() throws Exception {
        GSAResponse response = ResponseBuilder.buildResponse(
                new FileInputStream("src/test/data/Meta.xml"),
                GSAClient.DEFAULT_XML_SYSTEM_ID);
        List results = response.getResults();
        GSAResult result = (GSAResult) results.get(0);
        Map metas = result.getMetas();
        assertNotNull(metas);
        assertEquals(2, metas.size());
        assertEquals(metas.get("COMP_NAME"), "blurb");
        assertEquals(metas.get("HQ"), "Somewhere - Around");
        
        assertEquals(result.getMeta("COMP_NAME"), "blurb");
        assertEquals(result.getMeta("HQ"), "Somewhere - Around");
    }

    public void testField() throws Exception {
        GSAResponse response = ResponseBuilder.buildResponse(
                new FileInputStream("src/test/data/Meta.xml"),
                GSAClient.DEFAULT_XML_SYSTEM_ID);
        List results = response.getResults();
        GSAResult result = (GSAResult) results.get(0);
        Map fields = result.getFields();
        assertNotNull(fields);
        assertEquals(1, fields.size());
        assertEquals("", fields.get("date"));
        
        assertEquals("", result.getField("date"));
    }
    
    public void testSuggestionsAndSynonyms() throws Exception {
        GSAResponse response = ResponseBuilder.buildResponse(
                new FileInputStream("src/test/data/SuggestionsAndSynonyms.xml"),
                GSAClient.DEFAULT_XML_SYSTEM_ID);
        
        // check that GSASuggestion was parsed successfully
        GSASuggestion suggestion = (GSASuggestion) response.getSpelling().getSuggestions().get(0);
        assertEquals(suggestion.getText(), "music art instrumental");
        assertEquals(suggestion.getTextWithMarkup(), "<b><i>music</i></b> art <b><i>instrumental</i></b>");
        
        // check that synonyms were parsed successfully
        List synonyms = response.getSynonymsWithMarkup();
        assertEquals(2, synonyms.size());
        assertEquals("car", synonyms.get(0));
        assertEquals("truck", synonyms.get(1));
        
        // ensure that main response is also parsed, that suggestions do not cause a sideeffect.
        List results = response.getResults();
        GSAClient client = new GSAClient("host.name", 80, "/search");
        GSAResult result = (GSAResult) results.get(0);
        GSAQuery query = new GSAQuery();
        query.setOutputFormat(OutputFormat.XML_NO_DTD);
        query.setQueryTerm(new GSAQuery.GSAQueryTerm("java sdk"));
        query.setAccess(Access.SECURE);
        query.setFilter(Filter.DUPLICATE_SNIPPET_FILTER);
        query.setFrontend("default_frontend");
        query.setMaxResults(100);
        query.setNumKeyMatches((byte) 3);
        query.setScrollAhead(10);
        GSACacheQueryUtil cqu = new GSACacheQueryUtil(
                client, query);
        cqu.setProxystylesheet("default_frontend");
        String cacheDocUrl = cqu.getCacheDocUrl(result, false);
        assertEquals("http://host.name:80/search?q=cache:OjydvtGXC1sJ:http://blue.none.url/posts/comp.htm&proxystylesheet=default_frontend&oe=ISO-8859-1&access=s&output=xml_no_dtd&client=default_frontend&num=100"
                , cacheDocUrl);
        cacheDocUrl = cqu.getCacheDocUrl(result, true);
        assertEquals("http://host.name:80/search?q=cache:OjydvtGXC1sJ:http://blue.none.url/posts/comp.htm+java+sdk&proxystylesheet=default_frontend&oe=ISO-8859-1&access=s&output=xml_no_dtd&client=default_frontend&num=100"
                , cacheDocUrl);    
        
    }

    public void testDynamicNavigation() throws Exception {
        GSAResponse response = ResponseBuilder.buildResponse(
                new FileInputStream("src/test/data/DynamicNavigation.xml"),
                GSAClient.DEFAULT_XML_SYSTEM_ID);
        List result = response.getNavigationResponse().getResults();

        GSADynamicNavigationAttribute departmentAttribute = (GSADynamicNavigationAttribute) result.get(0);
        assertEquals(departmentAttribute.getName(), "dept");
        assertEquals(departmentAttribute.getLabel(), "Department");
        assertEquals(departmentAttribute.getType(), 0);
        assertEquals(departmentAttribute.isRange(), false);

        List departmentAttributeResults = departmentAttribute.getResultList();
        GSADynamicNavigationAttributeResult departmentAttributeResult = (GSADynamicNavigationAttributeResult) departmentAttributeResults.get(0);
        assertEquals(departmentAttributeResult.getValue(), "Sales");
        assertEquals(new Long(8), departmentAttributeResult.getCount());
        assertEquals(departmentAttributeResult.getLowerRage(), "");
        assertEquals(departmentAttributeResult.getHigherRange(), "");


        GSADynamicNavigationAttribute dateAttribute = (GSADynamicNavigationAttribute) result.get(1);
        assertEquals(dateAttribute.getName(), "join-date");
        assertEquals(dateAttribute.getLabel(), "Join Date");
        assertEquals(dateAttribute.getType(), 4);
        assertEquals(dateAttribute.isRange(), true);

        List dateAttributeResults = dateAttribute.getResultList();
        GSADynamicNavigationAttributeResult dateAttributeResult = (GSADynamicNavigationAttributeResult) dateAttributeResults.get(1);

        assertEquals(dateAttributeResult.getValue(), "");
        assertEquals(new Long(790), dateAttributeResult.getCount());
        assertEquals(dateAttributeResult.getLowerRage(), "2011-01-01");
        assertEquals(dateAttributeResult.getHigherRange(), "2012-01-01");



    }

}
