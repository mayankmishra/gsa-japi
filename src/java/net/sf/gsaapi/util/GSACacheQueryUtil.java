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

import net.sf.gsaapi.GSAClient;
import net.sf.gsaapi.GSAQuery;
import net.sf.gsaapi.GSAResult;

/**
 * This is a utility class for constructing the urls
 * for retrieving the "cached" version of the document
 * crawled by the GSA.
 * <br/>
 * Typical usage:<br/>
 * <code>
 * GSAClient client = new GSAClient("http", "www.mygsa.net", 7777, "/search");
 * GSAQuery query = new GSAQuery();
 * query.setQueryTerm(...);
 * // call other setters on the query
 * 
 * GSACacheQueryUtil gcu = new GSACacheQueryUtil(client, query);
 * gcu.setProxystylesheet("default_frontend");
 * 
 * GSAResponse response = client.getGSAResponse(query);
 * 
 * 
 * </code>
 * @author Amol S Deshmukh adeshmuk .at inxight .dot com
 *
 */
public class GSACacheQueryUtil {
    private String protocol;
    private String host;
    private int port;
    private String path;
    private String baseQueryString;
    private String queryTerm;
    private String proxystylesheet;

    /**
     * Construct a GSACacheQueryUtil instance from the
     * GSAClient and GSAQuery instances
     * @param client
     * @param query
     */
    public GSACacheQueryUtil(GSAClient client, GSAQuery query) {
        this.protocol = client.getProtocol();
        this.host = client.getHost();
        this.port = client.getPort();
        this.path = client.getPath();
        this.baseQueryString = QSF.filter(query.getValue());
        this.queryTerm = query.getQueryString();
    }
    
    /**
     * If set, the returned url link contains the following
     * substring: "proxystylesheet=&lt;value&gt;" where
     * &lt;value&gt; is the value passed to this method. 
     * @param name
     */
    public void setProxystylesheet(String name) {
        this.proxystylesheet = name;
    }
    
    /**
     * returns a url pointing to the cached document if
     * the cached document exists. Otherwise returns null
     * @param result 
     * @param hilited Whether to include the "query term" as a part
     * of the cache url. Setting this to true will return a link
     * to the cached document in which the query terms will be highlighted.
     * @return returns the Url of the cached doc as a string.
     */
    public String getCacheDocUrl(GSAResult result, boolean hilited) {
        String retval = null;
        String cacheId = result.getCacheDocId();
        String encoding = result.getCacheDocEncoding();
        String escUrl = result.getEscapedUrl();

        if (null != cacheId && !"".equals(cacheId)) {
            StringBuffer sbuf = new StringBuffer();
            sbuf.append(protocol).append("://").append(host)
                .append(':').append(port).append(path);

            sbuf.append("?q=").append("cache:").append(cacheId).append(':').append(escUrl);
            if (hilited) {
                sbuf.append("+").append(Util.escape(queryTerm, ""));
            }
            if (null != this.proxystylesheet) {
                sbuf.append('&').append("proxystylesheet=").append(proxystylesheet);
            }
            sbuf.append("&oe=").append(Util.getString(encoding, "UTF-8"));
            sbuf.append('&');
            sbuf.append(baseQueryString);
            retval = sbuf.toString();
        }
        
        return retval;
    }
    
    
    // ----- private -----
    
    /* This list is based on the default XSL that is used by
     * a GSAv3.6 to generate the "Cached" Link  
     */
    private static final String[] RETAINED_PARAMS = new String[] {
          "client"
        , "size"
        , "num"
        , "output"
        , "proxystylesheet"
        , "access"
        , "restrict"
        , "lr"
        , "ie"};
    private static final QueryStringFilter QSF = new QueryStringFilter(RETAINED_PARAMS);
}
