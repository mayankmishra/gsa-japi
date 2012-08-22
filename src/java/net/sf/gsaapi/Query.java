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

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import net.sf.gsaapi.util.Util;

/**
 * This is a POJO that maps its fields to the request 
 * parameters of the GSA's search query format. This 
 * class definition is subject to change if the GSA 
 * query parameter definitions are changed. 
 * <br/>
 * This class 
 * is for internal use only. GSA API users should use 
 * the GSAQuery class instead which is expected to be 
 * more stable.
 * @author Amol S Deshmukh adeshmuk .at inxight .dot com
 */
class Query {
    private char as_dt; // {i, e}:{include [as_sitesearch], exclude [as_sitesearch]}
    private String as_epq; // additional query terms
    private String[] as_eq; // exclude terms
    private String as_lq; // pages linking to this url
    private String as_occt; // {any, title, URL}:{search anywhere on page, search in title, search in url}
    private String[] as_oq; // any of these
    private String[] as_q;

    private String q; // *REQUIRED when sitesearch specified* Search query

    private String as_sitesearch; // *MAX 118 after URL encoding* set the value for this into "site"
    private String sitesearch; // needs q to be supplied
    private String[] sites; // *REQUIRED* (OR-ed) collection of "collections" ()

    private String client; // *REQUIRED* string specifying valid frontend      (default -needs to be speficied- is default_frontend)
    private String output; // *REQUIRED* format for search results      (default -needs to be specified- is xml_no_dtd)
    private String proxycustom; // {<HOME/>,<ADVANCED/>,<TEST/>}
    private boolean proxyreload; // {1} force reload of serverside stylesheet (else default reloaded after 15 mins)
    private String proxystylesheet; // if output==xml_no_dtd then {Omitted,}

    private char access; // {p, s, a}:{public, secure, all}
    private char filter; // 0,1,p,s  (default is 1)
    private String lr; // Language restrict

    private String ie; // Input encoding      (default is latin1)
    private String oe; // Output encoding     (default is UTF8)

    private long start; // {0..999} scroll into the search results (constraint: start+num <= 1000)
    private int num; // {1..100} max results per request     (default is 10)
    private byte numgm; // {0..5} max num of keymatches per result     (default is 3)

    private String[] getfields; // get meta tags
    private Map partialfields; // meta tag names and partial-values
    private boolean partialFieldsOr = true; // used in conjunction with partialFields

    private Map requiredfields; // meta tag names and complete-values
    private boolean requiredFieldsOr = true; // used in conjunction with requiredFields


    private String sort; // Only date is currently supported

    Query(String query, String output) {
        this.q = query;
        this.output = output == null ? _DEFAULT_OUTPUT : output;
        this.access = _DEFAULT_ACCESS;
        this.requiredfields = new HashMap();
        this.partialfields = new HashMap();
    }

    Query(String query) {
        this(query, _DEFAULT_OUTPUT);
    }

    Query() {
        this(null);
    }


    void setAccess(char access) {
        this.access = access;
    }

    void setAs_dt(char as_dt) {
        this.as_dt = as_dt;
    }

    void setAs_epq(String as_epq) {
        this.as_epq = as_epq;
    }

    void setAs_eq(String[] as_eq) {
        this.as_eq = as_eq;
    }

    void setAs_lq(String as_lq) {
        this.as_lq = as_lq;
    }

    void setAs_occt(String as_occt) {
        this.as_occt = as_occt;
    }

    void setAs_oq(String[] as_oq) {
        this.as_oq = as_oq;
    }

    void setAs_q(String[] as_q) {
        this.as_q = as_q;
    }

    void setAs_sitesearch(String as_sitesearch) {
        this.as_sitesearch = as_sitesearch;
    }

    void setClient(String client) {
        this.client = client;
    }

    void setFilter(char filter) {
        this.filter = filter;
    }

    void setGetfields(String[] getfields) {
        this.getfields = getfields;
    }

    void setPartialfields(Properties partialfields, boolean orIfTrueAndIfFalse) {
        this.partialfields.putAll(partialfields);
        this.partialFieldsOr = orIfTrueAndIfFalse;
    }

    void setRequiredfields(Properties requiredfields, boolean orIfTrueAndIfFalse) {
        this.requiredfields.putAll(requiredfields);
        this.requiredFieldsOr = orIfTrueAndIfFalse;
    }

    void setIe(String ie) {
        this.ie = ie;
    }

    void setLr(String lr) {
        this.lr = lr;
    }

    void setNum(int num) {
        this.num = num;
    }

    void setNumgm(byte numgm) {
        this.numgm = numgm;
    }

    void setOe(String oe) {
        this.oe = oe;
    }

    void setOutput(String output) {
        this.output = output;
    }

    void setProxycustom(String proxycustom) {
        this.proxycustom = proxycustom;
    }

    void setProxyreload(boolean proxyreload) {
        this.proxyreload = proxyreload;
    }

    void setProxystylesheet(String proxystylesheet) {
        this.proxystylesheet = proxystylesheet;
    }

    void setQ(String q) {
        this.q = q;
    }

    /**
     * Set one or more site collections. Site collections are
     * preconfigured by a GSA admin - usually as a group of related
     * websites.
     * "site" collections are different from
     * <ul>
     * <li>The "sitesearch" or "as_sitesearch" parameters
     * which specifies the domain to include/exclude in the search</li>
     * <li>The "site" special query term (which is specified as a
     * part of the query string as 'site:some.domain.name')</li>
     * </ul>
     */
    void setSites(String[] sites) {
        this.sites = sites;
    }

    /**
     * Specifies the domain name to restrict the search to.
     *
     * @param sitesearch
     */
    void setSitesearch(String sitesearch) {
        this.sitesearch = sitesearch;
    }

    /**
     * Specifies the value for the sort parameter.
     * See http://code.google.com/gsa_apis/xml_reference.html#request_sort
     * for more information.
     *
     * @param sort
     */
    void setSort(String sort) {
        this.sort = sort;
    }

    /**
     * Specifies the index (0-based) at which to scroll into the
     * search results. Thus a value of 10 would scroll into
     * the results to begin retrieving at the 11th result.
     * The default value is 0. This parameters in conjunction
     * with <code>num</code> is useful in paginating thru
     * the results.
     *
     * @param start
     */
    void setStart(long start) {
        this.start = start;
    }

    /**
     * Generates the URL string for getting results from the
     * GSA using current state of this object.
     *
     * @return
     */
    String getValue() {
        StringBuffer sbuf = new StringBuffer();
        Util.appendQueryParam(sbuf, "access", String.valueOf(access));
        if (output != null) Util.appendQueryParam(sbuf, "output", output);
        if (sort != null) Util.appendQueryParam(sbuf, "sort", sort);
        if (ie != null) Util.appendQueryParam(sbuf, "ie", ie);
        if (oe != null) Util.appendQueryParam(sbuf, "oe", oe);
        Util.appendQueryParam(sbuf, "client", client);
        if (start > 0) Util.appendQueryParam(sbuf, "start", String.valueOf(start));
        if (q != null) Util.appendQueryParam(sbuf, "q", q);
        if (as_dt == AS_DT_INCLUDE || as_dt == AS_DT_EXCLUDE)
            Util.appendQueryParam(sbuf, "as_dt", String.valueOf(as_dt));
        if (as_epq != null) Util.appendQueryParam(sbuf, "as_epq", as_epq);
        if (as_eq != null) {
            String temp = Util.stringSeparated(as_eq, null, " ");
        	Util.appendQueryParam(sbuf, "as_eq", temp);
        }
        if (as_lq != null) Util.appendQueryParam(sbuf, "as_lq", as_lq);
        if (as_occt != null) Util.appendQueryParam(sbuf, "as_occt", as_occt);
        if (as_oq != null) {
            String temp = Util.stringSeparated(as_oq, null, " ");
            Util.appendQueryParam(sbuf, "as_oq", temp);
        }
        if (as_q != null) {
            String temp = Util.stringSeparated(as_q, null, " ");
            Util.appendQueryParam(sbuf, "as_q", temp);
        }

        if (as_sitesearch != null) Util.appendQueryParam(sbuf, "as_sitesearch", as_sitesearch);
        if (filter == FILTER_DUP_DIRECTORY || filter == FILTER_DUP_SNIPPET
                || filter == FILTER_DUP_SNIPPET_AND_DIRECTORY || filter == FILTER_OFF)
            Util.appendQueryParam(sbuf, "filter", String.valueOf(filter));
        if (lr != null) Util.appendQueryParam(sbuf, "lr", lr);
        if (num > 0) Util.appendQueryParam(sbuf, "num", String.valueOf(num));
        if (numgm > 0) Util.appendQueryParam(sbuf, "numgm", String.valueOf(numgm));
        if (proxycustom != null) Util.appendQueryParam(sbuf, "proxycustom", proxycustom);
        if (proxyreload) Util.appendQueryParam(sbuf, "proxyreload", "1");
        if (proxystylesheet != null) Util.appendQueryParam(sbuf, "proxystylesheet", proxystylesheet);
        if (sitesearch != null) Util.appendQueryParam(sbuf, "sitesearch", sitesearch);
        if (requiredfields != null && requiredfields.size() > 0) {
            Util.appendMappedQueryParams(
                    sbuf, 
                    "requiredfields", 
                    requiredfields, 
                    requiredFieldsOr ? "|" : ".");
        }
        if (partialfields != null && partialfields.size() > 0) {
            Util.appendMappedQueryParams(
                    sbuf, 
                    "partialfields", 
                    partialfields, 
                    partialFieldsOr ? "|" : ".");
        }
        if (getfields != null && getfields.length > 0) {
            String allFields = Util.stringSeparated(getfields, "", ".");
            Util.appendQueryParam(sbuf, "getfields", allFields);
        }
        if (sites != null && sites.length > 0) {
            String allSites = Util.stringSeparated(sites, "", "|");
            Util.appendQueryParam(sbuf, "site", allSites);
        }
        return sbuf.toString();
    }

    static final char ACCESS_PUBLIC = 'p';
    static final char ACCESS_SECURE = 's';
    static final char ACCESS_ALL = 'a';

    static final char AS_DT_INCLUDE = 'i';
    static final char AS_DT_EXCLUDE = 'e';

    static final String SEARCH_IN_PAGE = "any";
    static final String SEARCH_IN_TITLE = "title";
    static final String SEARCH_IN_URL = "URL";

    static final char FILTER_DUP_SNIPPET_AND_DIRECTORY = '1';
    static final char FILTER_OFF = '0';
    static final char FILTER_DUP_SNIPPET = 'p';
    static final char FILTER_DUP_DIRECTORY = 's';

    static final String PROXY_CUSTOM_HOME = "<HOME/>";
    static final String PROXY_CUSTOM_ADVANCED = "<ADVANCED/>";
    static final String PROXY_CUSTOM_TEST = "<TEST/>";

    static final char SORT_DIRECTION_ASC = 'A';
    static final char SORT_DIRECTION_DESC = 'D';
    static final char SORT_MODE_RELEVANT_RESULTS = 'S';
    static final char SORT_MODE_ALL_RESULTS = 'R';
    static final char SORT_MODE_NO_SORT_DATE_LOOKUP = 'L';

    static final short DEFAULT_NUM_RESULTS = 10;
    static final short DEFAULT_NUM_KEYMATCHES = 3;
    static final String DEFAULT_INPUT_ENCODING = "latin1";
    static final String DEFAULT_OUTPUT_ENCODING = "UTF8";

    static final short MIN_NUM_RESULTS = 1;
    static final short MAX_NUM_RESULTS = 100;
    static final short MIN_NUM_KEYMATCHES = 0;
    static final short MAX_NUM_KEYMATCHES = 5;

    private static final char _DEFAULT_ACCESS = 'p';
    private static final String _DEFAULT_OUTPUT = "xml_no_dtd";
}
