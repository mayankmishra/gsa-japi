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

import java.util.Map;
import java.util.List;
import java.util.HashMap;
import java.util.ArrayList;

/**
 * The Java binding to the XML response received from 
 * querying the GSA.
 * @author Amol S Deshmukh adeshmuk .at inxight .dot com
 */
public class GSAResponse {

    private double searchTime;
    private String query;
    private Map params = new HashMap();
    private long startIndex;
    private long endIndex;
    private long numResults;
    private boolean isFiltered;
    private String previousResponseUrl;
    private String nextResponseUrl;
    private GSASpelling spelling;
    private List synonyms = new ArrayList();
    private List results = new ArrayList();
    private List oneboxResponses = new ArrayList();
    private List keymatchResults = new ArrayList();
    private GSADynamicNavigationResponse navigationResponse = new GSADynamicNavigationResponse();

    /**
     * constructor is intended for internal use only.
     */
    public GSAResponse() {
    }
    
    /**
     * index of the last available result
     * @return the index as a long
     */
    public long getEndIndex() {
        return endIndex;
    }

    void setEndIndex(long endIndex) {
        this.endIndex = endIndex;
    }

    /**
     * true if result is automatically filtered
     * @return boolean indicating whether or not result was filtered.
     */
    public boolean isFiltered() {
        return isFiltered;
    }

    void setFiltered(boolean filtered) {
        isFiltered = filtered;
    }

    void setResults(List results) {
        this.results = results;
    }

    /**
     * return the url to the previous batch of responses
     * @return the urlstring pointing to the next set of responses 
     * (to request the previous "page" of results).
     */
    public String getPreviousResponseUrl() {
        return previousResponseUrl;
    }

    void setPreviousResponseUrl(String previousResponseUrl) {
        this.previousResponseUrl = previousResponseUrl;
    }

    /**
     * return the url to the next batch of responses
     * @return the urlstring pointing to the next set of responses
     * (ie. to request the next "page" of results) 
     */
    public String getNextResponseUrl() {
        return nextResponseUrl;
    }

    void setNextResponseUrl(String nextResponseUrl) {
        this.nextResponseUrl = nextResponseUrl;
    }

    /**
     * return a List of Keymatch Objects.
     * @return List containing {@link GSAKeymatch} instances
     */
    public List getKeymatchResults() {
        return keymatchResults;
    }

    void setKeymatchResults(List keymatchResults) {
        this.keymatchResults = keymatchResults;
    }

    void addKeymatchResult(GSAKeymatch gsaKeymatch) {
        this.keymatchResults.add(gsaKeymatch);
    }

    /**
     * return a List of objects that wraps the OneBox response.
     * @return List containing {@link GSAOneBoxResponse} instances
     */
    public List getOneBoxResponses() {
        return oneboxResponses;
    }
    
    void setOneBoxResponses(List oneboxResponses) {
        this.oneboxResponses = oneboxResponses;
    }
    
    void addOneBoxResponse(GSAOneBoxResponse oneboxResponse) {
        this.oneboxResponses.add(oneboxResponse);
    }

    /**
     * returns the List of GSASuggestion objects.
     * @return List containing (@link GSASuggestion} instances.
     */
    public GSASpelling getSpelling() {
        return spelling;
    }
    
    void setSpelling(GSASpelling spelling) {
        this.spelling = spelling;
    }
    
    /**
     * <p><b>
     *   NOTE: Experimental support for synonyms. Not well tested.
     * </b></p>
     * Returns the list of synonym Strings.
     * @return a list of strings that point to synonyms of the query term.
     * 
     */
    public List getSynonymsWithMarkup() {
        return synonyms;
    }
    
    void addSynonymWithMarkup(String synonym) {
        synonyms.add(synonym);
    }

    /**
     * (canonical form of) the query that was made
     * in order to get this response
     * @return String representing the query
     */
    public String getQuery() {
        return query;
    }

    void setQuery(String query) {
        this.query = query;
    }

    /**
     * number of results
     * @return total number of results as a long
     */
    public long getNumResults() {
        return numResults;
    }

    void setNumResults(long numResults) {
        this.numResults = numResults;
    }

    /**
     * meta field-values associated with this
     * query-response
     * @return Map of metafield names and values.
     */
    public Map getParams() {
        return params;
    }

    void setParams(Map params) {
        this.params = params;
    }

    void putParam(String name, String value) {
        params.put(name, value);
    }

    /**
     * how long did this search take (in seconds)
     * @return search time in seconds
     */
    public double getSearchTime() {
        return searchTime;
    }

    void setSearchTime(double searchTime) {
        this.searchTime = searchTime;
    }

    /**
     * start index of the results with respect to first 
     * result for the query
     * @return index (1-based) as a long value.
     */
    public long getStartIndex() {
        return startIndex;
    }

    void setStartIndex(long startIndex) {
        this.startIndex = startIndex;
    }

    /**
     * get list of results in this batch
     * @return List of GSAResult instances.
     */
    public List getResults() {
        return results;
    }

    public GSADynamicNavigationResponse getNavigationResponse() {
        return navigationResponse;
    }

    public void setNavigationResponse(GSADynamicNavigationResponse navigationResponse) {
        this.navigationResponse = navigationResponse;
    }

    /**
     * to aid in debugging
     * @return String indicating the value of internal fields.
     */
    public String toString() {
        StringBuffer buf = new StringBuffer();
        String indent = "";
        buf.append(indent).append("searchTime=").append(searchTime).append("\n");
        buf.append(indent).append("query=").append(query).append("\n");
        buf.append(indent).append("startIndex=").append(startIndex).append("\n");
        buf.append(indent).append("endIndex=").append(endIndex).append("\n");
        buf.append(indent).append("numResults=").append(numResults).append("\n");
        buf.append(indent).append("isFiltered=").append(isFiltered).append("\n");
        buf.append(indent).append("nextResponseUrl=").append(nextResponseUrl).append("\n");
        buf.append(indent).append("results=").append(results).append("\n");
        buf.append(indent).append("params=").append(params).append("\n");
        buf.append(indent).append("keymatches=").append(keymatchResults).append("\n");
        
        return buf.toString();
    }
}
