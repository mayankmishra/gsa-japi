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

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * <p/>
 * Use a GSAClient instance to fire queries to a specific GSA site.
 * Typical usage:
 * <pre>
 * GSAClient client = new GSAClient("http", "www.mygsa.net", 7777, "/search");
 * GSAQuery query = new GSAQuery();
 * // insert calls to (see GSAQuery): query.setXyz(xyzValue);
 * InputStream istream = client.search(query);
 * // 'istream' is an InputStream to the gsa xml results.
 * // parse data from 'istream' using desired XML parser api.
 * </pre>
 * A single GSAClient instance may be shared by multiple threads accessing
 * the same target search appliance. Note however that the same GSAClientDelegate
 * instance (if used) will be used for all the threads. Thus you
 * must appropriately design the GSAClientDelegate for multi-threaded
 * access.
 * @see GSAQuery
 * @author Amol S Deshmukh adeshmuk .at inxight .dot com
 */
public class GSAClient {
    static String DEFAULT_XML_SYSTEM_ID;
    static {
        try {
            ResourceBundle defaultProps = ResourceBundle.getBundle("META-INF/gsaclient");
            String s = defaultProps.getString("xmlsystemid");
            if (s != null && !"".equals(s.trim()))
                DEFAULT_XML_SYSTEM_ID = s;
        }
        catch (Exception e) {e.printStackTrace();}
    }
    
    private String protocol;
    private String host;
    private String path;
    private int port;
    private String xmlSystemId = DEFAULT_XML_SYSTEM_ID;
    private GSAClientDelegate delegate;

    /**
     * @param protocol protocol to use to connect to GSA eg. "http"
     * @param host     hostname for the GSA eg. "www.mysite.net"
     * @param port     port at which GSA is serving eg. 3300
     * @param path     path for the search program on GSA eg. "/search"
     */
    public GSAClient(String protocol, String host, int port, String path) {
        this.protocol = protocol;
        this.host = host;
        this.port = port;
        this.path = path;
        if (null == xmlSystemId) { 
            this.xmlSystemId = protocol + "://" + host + ":" + port + "/";
        } // else there was an overriding value in META-INF/gsaclient
    }

    /**
     * overloaded constructor that sets the defaults:
     * DEFAULT_PROTOCOL
     *
     * @param host
     * @param port
     * @param path
     */
    public GSAClient(String host, int port, String path) {
        this(DEFAULT_PROTOCOL, host, port, path);
    }

    /**
     * overloaded constructor that sets the defaults:
     * DEFAULT_PROTOCOL, DEFAULT_PORT
     *
     * @param host
     * @param path
     */
    public GSAClient(String host, String path) {
        this(host, 80, path);
    }

    /**
     * overloaded constructor that sets the defaults:
     * DEFAULT_PROTOCOL, DEFAULT_PORT, DEFAULT_PATH
     *
     * @param host
     */
    public GSAClient(String host) {
        this(host, DEFAULT_PATH);
    }
    
    /**
     * specify the GSAClientDelegate instance that will
     * actually query the url and return an input stream
     * to the response. If the GSAClientDelegate is not
     * specified, the GSAClient will itself connect to
     * the request url and attempt to fetch the
     * results.
     * @param delegate
     */
    public void setClientDelegate(GSAClientDelegate delegate) {
        this.delegate = delegate;
    }


    /**
     * If a valid query does not result in any results, an
     * empty List will be returned.
     *
     * @return List of GSAResponse objects
     */
    public InputStream search(GSAQuery query) throws IOException {
        return search(query.getValue());
    }

    /**
     * Normally, you should be using one of the
     * overloaded "search(GSAQuery)" methods which internally
     * call this method.
     * <br/>
     * The rawQuery parameter to this function should be of
     * the general form: "key1=value1&key2=value2&key3=value3"
     * In this case, hostname, port and path should not be specified.
     * The leading '?' character may or may not be present.
     * <br/>
     * For further details, refer to the GSA protocol reference
     * page at http://code.google.com/gsa_apis/xml_reference.html
     *
     * @param rawQuery rawQuery string
     * @return InputStream to the search results page
     */
    public InputStream search(String rawQuery) throws IOException {
        InputStream retval = null;

        if (rawQuery != null) {
            String fullUrl = rawQuery;
            if (rawQuery.indexOf("://") < 0) {
                // build full url
                fullUrl = protocol + "://"
                        + host + ':' + port
                        + (path.startsWith("/") ? path : ("/" + path))
                        + (rawQuery.startsWith("?") ? rawQuery : ("?" + rawQuery));
            }

            if (delegate != null) { // use the delegate to fetch the response
                retval = delegate.getResponseStream(fullUrl);
            }
            else { // connect to url & fetch response
                retval = new URL(fullUrl).openStream();
            }
        }
        return retval;
    }

    /**
     * Returns the search results parsed and wrapped by GSAReponse
     * instance.
     *
     * @param query The properly configured GSAQuery instance
     * @return GSAResponse instance wrapping the search results
     * @throws IOException
     */
    public GSAResponse getGSAResponse(GSAQuery query) throws IOException {
        return getGSAResponse(query.getValue());
    }

    /**
     * Normally you should be using one of the overloaded
     * "search(GSAQuery)" functions.
     * Returns the search results parsed and wrapped by GSAReponse
     * instance.
     * <br/>
     * The rawQuery parameter to this function should be of
     * the general form: "key1=value1&key2=value2&key3=value3"
     * ie. hostname, port and path should not be specified.
     * The leading '?' character may or may not be present.
     * <br/>
     * For further details, refer to the GSA protocol reference
     * page at http://code.google.com/gsa_apis/xml_reference.html
     *
     * @param rawQuery raw query string
     * @return GSAResponse instance wrapping the search results
     * @throws IOException
     */
    public GSAResponse getGSAResponse(String rawQuery) throws IOException {
        return ResponseBuilder.buildResponse(search(rawQuery), xmlSystemId);
    }


    /**
     * default port number used by constructor that allows you to skip specifying the port
     */
    public static final int DEFAULT_PORT = 80;

    /**
     * default path used by contructor that allows you to skip speciyfing the path
     */
    public static final String DEFAULT_PATH = "/search";

    /**
     * default protocol used by constructor that allows you to skip specifying the protocol
     */
    public static final String DEFAULT_PROTOCOL = "http";

    /**
     * returns the value for the host parameter passed to the
     * constructor 
     * @return returns the hostname as a String
     */
    public String getHost() {
        return host;
    }

    /**
     * returns the value for the path parameter passed to the
     * constructor 
     * @return returns the path to the search appliance 
     * following the host port specification.
     */
    public String getPath() {
        return path;
    }

    /**
     * returns the value for the port parameter passed to the
     * constructor 
     * @return returns the port number at which the search appliance
     * serves search results.
     */
    public int getPort() {
        return port;
    }

    /**
     * returns the value for the protocol parameter passed to the
     * constructor 
     * @return returns the protocol used by the search appliance
     */
    public String getProtocol() {
        return protocol;
    }
}
