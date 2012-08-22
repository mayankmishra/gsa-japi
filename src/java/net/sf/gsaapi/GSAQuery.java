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
import java.util.Properties;

import net.sf.gsaapi.constants.Access;
import net.sf.gsaapi.constants.Filter;
import net.sf.gsaapi.constants.OutputFormat;
import net.sf.gsaapi.constants.SearchScope;
import net.sf.gsaapi.util.Util;

/**
 * Encapsulates the query parameters to be sent to the GSA. A typical use of
 * this class would be:
 * 
 * <pre>
 *   ...
 *   GSAQuery query = new GSAQuery();
 *   GSAQueryTerm term = new GSAQuery.GSAQueryTerm(&quot;instrumental music&quot;);
 *   term.setInTitle(new String[]{&quot;music&quot;}); // search for music in page title
 *   term.setIncludeFileType(new String[]{&quot;pdf&quot;,&quot;html&quot;}); // restrict to pdf and html file types
 *   query.setQueryTerm(term);
 *   query.setCollections(sites); // set the collection ofadmin-configured sites to restrict search to
 *   ...
 * </pre>
 * 
 * @see GSAClient
 * @author Amol S Deshmukh adeshmuk .at inxight .dot com
 */
public class GSAQuery {

    /*
     * Dev Note: This class actually wraps the class Query (which is package
     * accessible). Query maps directly to the GSA Request Protocol
     * specification. However since Query can allow different search requests
     * that are semantically-identical, this class attempts to provide just a
     * single way of achieving these semantically-identical but
     * literally-different approaches.
     */

    /**
     * the absolute maximum number of results that a query can generate. Thus,
     * setStart+setMaxResults can't exceed this value.
     */
    public static final int MAX_RESULTS = 1000;

    /**
     * The maximum number of results to be fetched in one invokation of a query.
     * This is the upperbound for the call to {@link GSAQuery#setMaxResults}.
     */
    public static final int MAX_RESULTS_PER_QUERY = 100;

    private Query query;

    private GSAQueryTerm queryTerm;

    /**
     * create an instance of GSAQuery.
     */
    public GSAQuery() {
        query = new Query();
        query.setOutput(OutputFormat.XML.getValue());
    }

    /**
     * Restrict search to exclude documents from specified "siteCollections". A
     * siteCollection is a set of domains that is configured by the GSA admin.
     * <br/> For exampl, a particular search site may have a collection named
     * "support" for all domains served by the Support department and another
     * named "engineering" for all domains served by the Engineering department.
     * <br/>
     * <em>Note: <code>siteCollection</code> is not the same as an internet domain name -
     * generally a <code>siteCollection</code> is mapped to a set of domain names.
     * </em>
     * 
     * @param siteCollections
     */
    public void setSiteCollections(String[] siteCollections) {
        query.setSites(siteCollections);
    }

    /**
     * A string representing any valid frontend.
     * 
     * @param frontend
     */
    public void setFrontend(String frontend) {
        query.setClient(frontend);
    }

    /**
     * @see net.sf.gsaapi.constants.OutputFormat
     * @param of
     *            The OutputFormat enum value
     */
    public void setOutputFormat(OutputFormat of) {
        query.setOutput(of.getValue());
    }

    /**
     * Restrict search results to these many at most.
     * 
     * @param maxResults
     *            maximum results desired. Large values will be limited to
     *            {@link GSAQuery#MAX_RESULTS}
     */
    public void setMaxResults(int maxResults) {
        query.setNum(Math.min(maxResults, MAX_RESULTS));
    }

    /**
     * Number of keymatches in a document (max value accepted by GSA is 5).
     * 
     * @param keyMatches
     *            number of keyMatches desired
     */
    public void setNumKeyMatches(byte keyMatches) {
        query.setNumgm(keyMatches);
    }

    /**
     * Indicate part of page to which the search is to be restricted.
     * 
     * @param searchScope
     *            A {@link SearchScope} enum value.
     */
    public void setSearchScope(SearchScope searchScope) {
        query.setAs_occt(searchScope.getValue());
    }

    /**
     * Set the filter char code that indicates type of filtering to be
     * performed.
     * 
     * @param filter
     *            A {@link Filter} enum value
     */
    public void setFilter(Filter filter) {
        query.setFilter(filter.getValue());
    }

    /**
     * @param queryTerm
     *            The {@link GSAQueryTerm} instance encapsulating the query string.
     */
    public void setQueryTerm(GSAQueryTerm queryTerm) {
        this.queryTerm = queryTerm;
        query.setQ(queryTerm.getValue());
    }

    /**
     * search documents containing any or all of specified terms.
     * 
     * @param orTerms
     *            Terms that will be used to match the results documents.
     *            Documents matching any of the terms in the String array will
     *            be returned. Note that the terms will be further tokenized on
     *            spaces. <br/>
     * 
     * <pre>
     * String[] str1 = new String[] { &quot;this&quot;, &quot;that&quot; };
     * 
     * String[] str2 = new String[] { &quot;this that&quot; };
     * </pre>
     * 
     * Using either of the above String arrays as an argument will yield same
     * results.
     */
    public void setOrQueryTerms(String[] orTerms) {
        query.setAs_oq(orTerms);
    }

    /**
     * restrict search to documents containing all of the specified terms.
     * 
     * @param andTerms
     *            Terms that will be used to match the results documents. Only
     *            documents matching all of the terms in the String array will
     *            be returned. Note that the terms will be further tokenized on
     *            spaces. <br/>
     * 
     * <pre>
     * String[] str1 = new String[] { &quot;this&quot;, &quot;that&quot; };
     * 
     * String[] str2 = new String[] { &quot;this that&quot; };
     * </pre>
     * 
     * Using either of the above String arrays as an argument will yield same
     * results.
     */
    public void setAndQueryTerms(String[] andTerms) {
        query.setAs_q(andTerms);
    }
    
    /**
     * Restrict search to documents that <em>do not</em> include
     * <em>any</em> of the specified terms.
     * @param notTerms
     *            Terms that will be used to match the results documents that 
     *            do not contain any of them. Note that the terms will be further
     *            tokenized on spaces. <br/>
     * 
     * <pre>
     * String[] str1 = new String[] { &quot;this&quot;, &quot;that&quot; };
     * 
     * String[] str2 = new String[] { &quot;this that&quot; };
     * </pre>
     * 
     * Using either of the above String arrays as an argument will yield same
     * results.
     */
    public void setNotQueryTerms(String[] notTerms) {
    	query.setAs_eq(notTerms);
    }

    /**
     * search for the documents containing the exact specified phrase.
     * 
     * @param phrase
     *            The exact phrase to search for.
     */
    public void setExactPhraseQueryTerm(String phrase) {
        query.setAs_epq(phrase);
    }

    /**
     * Set input encoding (advanced use only)
     * 
     * @param inputEncoding
     *            The input encoding.
     */
    public void setInputEncoding(String inputEncoding) {
        query.setIe(inputEncoding);
    }

    /**
     * Set output encoding (advanced use only)
     * 
     * @param outputEncoding
     *            The output encoding.
     */
    public void setOutputEncoding(String outputEncoding) {
        query.setOe(outputEncoding);
    }

    /**
     * Restrict search to pages in specified languages only. 
     * For example, "lang_en" for
     * english. For details refer to the 
     * <a href="http://code.google.com/gsa_apis/xml_reference.html#request_subcollections">
     * XML protocol reference</a>.
     * 
     * @param language
     *            The language.
     */
    public void setLanguage(String language) {
        query.setLr(language);
    }

    /**
     * Fetch specified META tags if they exist in the document. (mainly useful
     * for html documents).
     * 
     * @param fields
     *            The meta fields associated with a result to fetch.
     */
    public void setFetchMetaFields(String[] fields) {
        query.setGetfields(fields);
    }

    /**
     * Calls {@link GSAQuery#setRequiredMetaFields(Properties, boolean)} with the
     * boolean argument set to <code>true</code>.
     * 
     * @param requiredFields The {@link Properties} object encapsulating
     * the required field names and values.
     */
    public void setRequiredMetaFields(Properties requiredFields) {
        query.setRequiredfields(requiredFields, true);
    }

    /**
     * Set the meta fields based filtering criteria. The argument is a
     * Properties object containing key-value pairs of the form
     * &lt;meta-field-name&gt;, &lt;meta-field-value&gt; The specified value is
     * used to perform an exact match on the documents meta field.
     * 
     * <pre>
     *   Note: The name and value strings must be url encoded.
     * </pre>
     * 
     * <br/> For further details refer to: <a
     * href="http://code.google.com/gsa_apis/xml_reference.html#request_meta_filter">
     * http://code.google.com/gsa_apis/xml_reference.html#request_meta_filter</a>
     * 
     * @param requiredFields
     *            The Properties instance specifying the required fields filter.
     *            All keys and values in the properties instance must be url
     *            encoded.
     * @param orIfTrueAndIfFalse
     *            If true, the required field constraints are ORed -- ie.
     *            results containing any of the required fields are retrieved.
     *            If false, the required field contraints are ANDed ie. only
     *            results containing all the results are returned.
     */
    public void setRequiredMetaFields(Properties requiredFields, boolean orIfTrueAndIfFalse) {
        query.setRequiredfields(requiredFields, orIfTrueAndIfFalse);
    }

    /**
     * Calls {@link GSAQuery#setPartialMetaFields(Properties, boolean)} with the
     * boolean argument set to <code>true</code>.
     * 
     * @param partialFields
     */
    public void setPartialMetaFields(Properties partialFields) {
        query.setPartialfields(partialFields, true);
    }

    /**
     * Set the meta fields based filtering criteria. The argument is a
     * Properties object containing key-value pairs of the form
     * &lt;meta-field-name&gt;, &lt;meta-field-value&gt; <br/> The specified
     * value is used to perform a <em>subphrase</em> match on the documents
     * meta field. For further details refer to: <a
     * href="http://code.google.com/gsa_apis/xml_reference.html#request_meta_filter">
     * http://code.google.com/gsa_apis/xml_reference.html#request_meta_filter</a>
     * 
     * @param partialFields
     *            The Properties instance specifying the partial fields filter.
     * @param orIfTrueAndIfFalse
     *            If true, the required field constraints are ORed -- ie.
     *            results containing any of the required fields are retrieved.
     *            If false, the required field contraints are ANDed ie. only
     *            results containing all the results are returned.
     */
    public void setPartialMetaFields(Properties partialFields, boolean orIfTrueAndIfFalse) {
        query.setPartialfields(partialFields, orIfTrueAndIfFalse);
    }

    /**
     * sort by date (default sorted by "relevance") and in the specified
     * sort-direction and mode If this method is invoked, the returned results
     * also contain a custom field named "date" with the date value specified in
     * YYYY-MM-DD formatted string. This value can be retrieved from the
     * GSAResult object using the
     * 
     * @param asc
     *            true if desired sort-direction is ascending. false otherwise.
     * @param mode
     *            mode can be one of 'S' 'R' 'L' to mean "Sort relevant
     *            results", "Sort all results", "Dont sort, fetch date for each
     *            result " respectively.
     */
    public void setSortByDate(boolean asc, char mode) {
        query.setSort("date:" + (asc
                ? "A:"
                : "D:") + mode + ":d1");
    }

    /**
     * This method removes any sort-value set by previous calls to
     * setSortByDate.
     */
    public void unsetSortByDate() {
        query.setSort(null);
    }

    /**
     * scroll ahead to display (n+1)th result as the first result. Useful for
     * paginating through the results.
     * 
     * @param n
     *            the starting index (0-based). ie. The (n+1)th result will be
     *            the first result to be displayed.
     */
    public void setScrollAhead(int n) {
        query.setStart(n);
    }

    /**
     * restrict results to public or secure (or do not restrict) based on the
     * value of the Access parameter.
     * 
     * @see net.sf.gsaapi.constants.Access
     * @param access
     *            The Access enum value.
     */
    public void setAccess(Access access) {
        query.setAccess(access.getValue());
    }

    /**
     * sets the proxycustom url param.
     * 
     * @param proxycustom
     *            proxycustom string value.
     */
    public void setProxycustom(String proxycustom) {
        query.setProxycustom(proxycustom);
    }

    /**
     * set the value for proxystylesheet. NOTE: It is not advisable to set this
     * param if you intend to use the XML parsing capabilities of this API.
     * 
     * @param proxystylesheet
     *            proxystylesheet String value.
     */
    public void setProxystylesheet(String proxystylesheet) {
        query.setProxystylesheet(proxystylesheet);
    }

    /**
     * Setting this to true forces reloading of the proxystylesheet by the GSA
     * server. By default, GSA refreshes the stylesheet every 15 mins or so.
     * (refer to documentation of your target GSA to determine the default
     * proxystylsheet reload time).
     * 
     * @param force
     *            true forces reloading the proxystylesheet for the results of
     *            this search.
     */
    public void setProxyReload(boolean force) {
        query.setProxyreload(force);
    }

    /**
     * return the HTTP GET query string.
     * 
     * @return returns the complete query string generated as a result of all
     *         current options set in this instance of GSAQuery class.
     */
    public String getValue() {
        return query.getValue();
    }

    /**
     * This method is mainly to facilicate automated testing. Should not be
     * required except to test the query string built internally.
     * 
     * @return returns the query string generated as a result of all current
     *         options set in the GSAQueryTerm instance associated with this
     *         GSAQuery instance.
     */
    public String getQueryString() {
        return queryTerm.getValue();
    }

    /**
     * This class is required because the GSA query term supports a "special
     * term" syntax. For example, when interacting with a GSA using a browser, 
     * the user can specify: <code>
     * instrumental music intitle:edu filetype:pdf OR filetype:html
     * </code>
     * Which means that the user wants to search for PDF or HTML documents having
     * "edu" in their title and containing both "instrumental" and "music" in
     * the document contents. This class models these special query term
     * semantics by providing an attribute for each type of special term.
     */
    public static class GSAQueryTerm {
        private List inTitleTerms;

        private List notInTitleTerms;

        private List inUrlTerms;

        private List notInUrlTerms;

        private List includeFiletype;

        private List excludeFiletype;

        private List allInTitleTerms;

        private List allInUrlTerms;

        private String site;

        private boolean includeSite;

        private String dateRange;

        private String webDocLocation;

        private String cacheDocLocation;

        private String link;

        private String queryString;

        /**
         * Search for all the words specified in the query string (words are
         * whitespace delimited in the string)
         * 
         * @param queryString
         */
        public GSAQueryTerm(String queryString) {
            this.queryString = queryString;
        }

        public GSAQueryTerm() {
            this(null);
        }

        /**
         * set the queryString. Calling the no-arg constructor followed by call
         * to this method is equivalent to calling the one-arg constructor with
         * this argument. This method is useful if you want to reuse the same
         * GSAQueryTerm object for making multiple queries.
         * 
         * @param queryString
         */
        public void setQueryString(String queryString) {
            this.queryString = queryString;
        }

        /**
         * search for the specified strings in title.
         * 
         * @param inTitleTerms
         */
        public void setInTitle(List inTitleTerms) {
            this.inTitleTerms = inTitleTerms;
        }

        /**
         * remove results that contain specified terms in title
         * 
         * @param notInTitleTerms
         */
        public void setNotInTitle(List notInTitleTerms) {
            this.notInTitleTerms = notInTitleTerms;
        }

        /**
         * add a term to titles inclusion/exclusion list. If the boolean param
         * is true, term is added to inclusion list, else added to exclusion
         * list.
         * 
         * @param term
         *            String to include/exclude
         * @param include
         *            <code>true</code> means include, <code>false</code>
         *            means exclude
         * @return The current GSAQueryTerm instance to allow chaining of
         *         multiple calls as so: gsaQueryTerm.addFileType("pdf",
         *         true).addFileType("html", true);
         */
        public GSAQueryTerm addInTitle(String term, boolean include) {
            if (include) inTitleTerms.add(term);
            else notInTitleTerms.add(term);
            return this;
        }

        /**
         * all the terms specified must be in the page title. No other
         * properties of this instance may be set if this property is set.
         * 
         * @param allInTitleTerms
         */
        public void setAllInTitle(List allInTitleTerms) {
            this.allInTitleTerms = allInTitleTerms;
        }

        /**
         * search for the specified strings in url.
         * 
         * @param inUrlTerms
         */
        public void setInUrl(List inUrlTerms) {
            this.inUrlTerms = inUrlTerms;
        }

        public void setNotInUrl(List notInUrlTerms) {
            this.notInUrlTerms = notInUrlTerms;
        }

        /**
         * add a term to url inclusion/exclusion list. If the boolean param is
         * true, term is added to inclusion list, else added to exclusion list.
         * 
         * @param term
         *            String to include/exclude
         * @param include
         *            <code>true</code> means include, <code>false</code>
         *            means exclude
         * @return The current GSAQueryTerm instance to allow chaining of
         *         multiple calls as so: gsaQueryTerm.addFileType("pdf",
         *         true).addFileType("html", true);
         */
        public GSAQueryTerm addInUrl(String term, boolean include) {
            if (include) inUrlTerms.add(term);
            else notInUrlTerms.add(term);
            return this;
        }

        /**
         * all the terms specified must be in the page url. No other properties
         * of this instance may be set if this property is set.
         * 
         * @param allInUrlTerms
         */
        public void setAllInUrl(List allInUrlTerms) {
            this.allInUrlTerms = allInUrlTerms;
        }

        /**
         * Restrict search to specified file extensions. Commonly used
         * values are: "pdf", "html", "doc".
         * 
         * @param filetype The filetypes to restrict the search to.
         */
        public void setIncludeFileType(List filetype) {
            this.includeFiletype = filetype;
        }

        /**
         * Exclude documents with specified file extensions.
         * 
         * @param filetype The filetypes to exclude from the search.
         */
        public void setExcludeFileType(List filetype) {
            this.excludeFiletype = filetype;
        }

        /**
         * Add a term to filetypes inclusion/exclusion list. If the boolean
         * param is true, term is added to inclusion list, else added to
         * exclusion list.
         * 
         * @param term
         *            String to include/exclude.
         * @param include
         *            <code>true</code> means include, <code>false</code>
         *            means exclude.
         * @return The current GSAQueryTerm instance to allow chaining of
         *         multiple calls as so: <code>gsaQueryTerm.addFileType("pdf",
         *         true).addFileType("html", true);</code>
         */
        public GSAQueryTerm addFileType(String term, boolean include) {
            if (include) {
                if (null == includeFiletype) includeFiletype = new ArrayList();
                includeFiletype.add(term);
            }
            else {
                if (null == excludeFiletype) excludeFiletype = new ArrayList();
                excludeFiletype.add(term);
            }
            return this;
        }

        /**
         * Set the site (domain) for inclusion or exclusion. As per the GSA
         * documentation, only one site (domain) can be included or excluded. To
         * remove a previously set site, simply call:
         * <code>setSite(null, false)</code> OR
         * <code>setSite(null, true)</code> (The boolean parameter is ignored
         * if the String parameter is null) <br/> <em>
         * NOTE: This has been modified since v1.4 of the GSA-JAPI.
         * Previous versions of the API allowed you to set
         * multiple sites for inclusion and/or exclusion.
         * However, as per GSA documentation, only one site
         * should be specified for inclusion or exclusion.
         * </em>
         * 
         * @param site
         *            The site/domain name as a string
         * @param include
         *            Whether to use the site as an inclusion filter (if true)
         *            or exclusion filter (if false)
         */
        public void setSite(String site, boolean include) {
            this.includeSite = include;
            this.site = site;
        }

        /**
         * search for document at specified location. No other properties of
         * this instance may be set if this property is set.
         * 
         * @param docLocation
         */
        public void setWebDocument(String docLocation) {
            this.webDocLocation = docLocation;
        }

        /**
         * search for cached document at specified location. No other properties
         * of this instance may be set if this property is set.
         * 
         * @param docLocation
         */
        public void setCachedDocument(String docLocation) {
            this.cacheDocLocation = docLocation;
        }

        /**
         * search for documents linking to specified document (url string). The
         * specified document must be accessible to the GSA. eg. usage:
         * link:www.mysite.net/path/somedoc.html
         * 
         * @param link
         */
        public void setWithLinksTo(String link) {
            this.link = link;
        }

        /**
         * set a daterange filter. Only documents that have the associated date
         * within the specified range will be retrieved. <br/> NOTE: Date range
         * filter is not a well-documented feature and a particular installation
         * of GSA may or may not support filtering by date range. Please ensure
         * that the target GSA supports the "daterange:<julianDayNumber1>-<julianDayNumber2>"
         * query filter before using this method. (where <julianDayNumber1> and
         * <julianDayNumber2> are julian dates. See also:
         * {@link net.sf.gsaapi.util.Util#toJulian}
         * 
         * @param fromDateInJulianDays
         *            The begin date (as a Julian Day Number) for the date
         *            range.
         * @param toDateInJulianDays
         *            The end date (as a Julian Day Number) for the date range.
         */
        public void setDateRange(long fromDateInJulianDays, long toDateInJulianDays) {
            StringBuffer dateRange = new StringBuffer(String.valueOf(fromDateInJulianDays));
            dateRange.append('-');
            dateRange.append(toDateInJulianDays);
            this.dateRange = dateRange.toString();
        }

        /**
         * builds and returns the string representation of this object.
         * 
         * @return
         */
        String getValue() {
            String retval = null;
            StringBuffer qbuf = new StringBuffer();
            if (allInTitleTerms != null && allInTitleTerms.size() > 0) {
                qbuf.append(_ALL_IN_TITLE).append(Util.stringSeparated(allInTitleTerms, null, _SP)).append(' ');
            }
            if (allInUrlTerms != null && allInUrlTerms.size() > 0) {
                qbuf.append(_ALL_IN_URL).append(Util.stringSeparated(allInUrlTerms, null, _SP)).append(' ');
            }

            if (webDocLocation != null && webDocLocation.length() > 0) {
                qbuf.append(_INFO).append(webDocLocation).append(' ');
            }

            if (cacheDocLocation != null && cacheDocLocation.length() > 0) {
                qbuf.append(_CACHE).append(cacheDocLocation).append(' ');
            }
            if (link != null && link.length() > 0) {
                qbuf.append(_LINK).append(link);
            }

            if (inTitleTerms != null && inTitleTerms.size() > 0) {
                qbuf.append(Util.stringSeparated(inTitleTerms, _IN_TITLE, _SP)).append(' ');
            }
            if (notInTitleTerms != null && notInTitleTerms.size() > 0) {
                qbuf.append(Util.stringSeparated(notInTitleTerms, _NOT_IN_TITLE, _SP)).append(' ');
            }

            if (inUrlTerms != null && inUrlTerms.size() > 0) {
                qbuf.append(Util.stringSeparated(inUrlTerms, _IN_URL, _SP)).append(' ');
            }
            if (notInUrlTerms != null && notInUrlTerms.size() > 0) {
                qbuf.append(Util.stringSeparated(notInUrlTerms, _NOT_IN_URL, _SP)).append(' ');
            }

            if (site != null && site.length() > 0) {
                qbuf.append(includeSite
                        ? _INCLUDE_SITE
                        : _EXCLUDE_SITE).append(site).append(' ');
            }

            if (includeFiletype != null && includeFiletype.size() > 0) {
                qbuf.append(Util.stringSeparated(includeFiletype, _INCLUDE_FILETYPE, _OR)).append(' ');
            }
            if (excludeFiletype != null && excludeFiletype.size() > 0) {
                qbuf.append(Util.stringSeparated(excludeFiletype, _EXCLUDE_FILETYPE, _SP)).append(' ');
            }
            if (dateRange != null) {
                qbuf.append(_DATERANGE).append(dateRange).append(' ');
            }

            if (queryString != null) qbuf.append(queryString);

            retval = qbuf.toString();
            return retval;
        }

        private static final String _IN_TITLE = "intitle:";

        private static final String _NOT_IN_TITLE = "-intitle:";

        private static final String _IN_URL = "inurl:";

        private static final String _NOT_IN_URL = "-inurl:";

        private static final String _INCLUDE_FILETYPE = "filetype:";

        private static final String _EXCLUDE_FILETYPE = "-filetype:";

        private static final String _INCLUDE_SITE = "site:";

        private static final String _EXCLUDE_SITE = "-site:";

        private static final String _DATERANGE = "daterange:";

        private static final String _ALL_IN_TITLE = "allintitle:";

        private static final String _ALL_IN_URL = "allinurl:";

        private static final String _INFO = "info:";

        private static final String _CACHE = "cache:";

        private static final String _LINK = "link:";

        private static final String _OR = " OR ";

        private static final String _SP = " ";
    }
}
