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
 * A class for encapsulating a suggestion for a query term
 * as returned in the GSA response. A suggestion consists of 
 * 2 parts: plain text and text with markup.
 * @author Amol S Deshmukh adeshmuk .at inxight .dot com
 */
public class GSASuggestion {

    private String text;
    private String textWithMarkup;

    void setText(String text) {
        this.text = text;
    }
    
    /**
     * Get the text of the suggestion.
     * @see #getTextWithMarkup()
     * @return text of the suggestion.
     */
    public String getText() {
        return text;
    }

    void setTextWithMarkup(String textWithMarkup) {
        this.textWithMarkup = textWithMarkup;
    }
    
    /**
     * Get the suggestion with markup. In case of multi-term queries,
     * if only some of the terms needed suggestions, only these are
     * highlighted using markup. Thus e.g., for a query:
     * <code>instrumental musicc</code> where the term "musicc" is misspelt,
     * the textWithMarkup would be of the form:
     * <code>instrumental &lt;b&gt;&lt;i&gt;musicc&lt;/i&gt;&lt;/b&gt;</code>
     * @return The text with embedded HTML markup.
     */
    public String getTextWithMarkup() {
        return textWithMarkup;
    }
}
