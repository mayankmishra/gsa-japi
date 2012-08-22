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

/**
 * Wrapper class for holding spelling suggestion objects of type {@link GSASuggestion}.
 * @author Amol S Deshmukh adeshmuk .at inxight .dot com
 */
public class GSASpelling {

    List suggestions = new ArrayList();
    
    void addSuggestion(GSASuggestion suggestion) {
        suggestions.add(suggestion);
    }
    
    /**
     * returns the List of {@link GSASuggestion} objects.
     * @return a List of {@link GSASuggestion} objects.
     */
    public List getSuggestions() {
        return suggestions;
    }
}
