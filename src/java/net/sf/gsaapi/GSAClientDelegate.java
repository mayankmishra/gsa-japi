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

import java.io.InputStream;

/**
 * GSAClientDelegate allows specifying an implementation
 * class that will handle the requests to a url and
 * return the response. This is required if there is
 * a need for handling challenge-response authentication.
 *
 * @author Amol S Deshmukh adeshmuk .at inxight .dot com
 */
public interface GSAClientDelegate {

    /**
     * Returns the InputStream pointing to the url specified.
     * @param requestUrl
     * @return returns the InputStream to the XML results corresponding
     * to the request URL. 
     */
    public InputStream getResponseStream(String requestUrl);
}
