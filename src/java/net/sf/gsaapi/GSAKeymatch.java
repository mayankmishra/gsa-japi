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

/**
 * A class for encapsulating the key match results.
 * Contains a description and an URL
 * @author Shaun Farrugia   bugtank___ gmail
 */
public class GSAKeymatch {

    private String description;
    private String url;

    /**
     * returns the description of the keymatch.
     * @return description as a {@link String}
     */
    public String getDescription() {
        return description;
    }

    void setDescription(String description) {
        this.description = description;
    }

    /**
     * returns the url of the keymatch.
     * @return url as a {@link String}
     */
    public String getUrl() {
        return url;
    }

    void setUrl(String url) {
        this.url = url;
    }
}
