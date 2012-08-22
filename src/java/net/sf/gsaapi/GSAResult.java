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

import net.sf.gsaapi.util.Util;

/**
 * The Java binding to the result element of the XML response
 * received from querying the GSA.
 * @author Amol S Deshmukh adeshmuk .at inxight .dot com
 */
public class GSAResult {

    private String mimeType;
    private int indentation;
    private String url;
    private String escapedUrl;
    private String title;
    private int rating;
    private Map metas;
    private Map fields;
    private String summary;
    private String language;
    
    private String cacheDocId;
    private String cacheDocEncoding;
    private String cacheDocSize;

    /**
     * constructor is intended for internal use only.
     */
    public GSAResult() {
        metas = new HashMap();
        fields = new HashMap();
    }

    /**
     * set the indentation for this result item (suggestion).
     * This happens typically when this the second consecutive
     * result from the same website (domain).
     * @param indentation
     */
    public void setIndentation(int indentation) {
        this.indentation = indentation;
    }

    /**
     * set the language that the result is supposed to be of.
     * @param language
     */
    public void setLanguage(String language) {
        this.language = language;
    }

    /**
     * set the meta field-values associated with this result if any.
     * @param metas
     */
    public void setMetas(Map metas) {
        this.metas.putAll(metas);
    }

    /**
     * Add a meta field value pair.
     * @param key meta field key.
     * @param value meta field value.
     */
    public void addMeta(String key, String value) {
        this.metas.put(key, value);
    }

    /**
     * Set the "additional" field-values associated with this result if any.
     * <em>Note: Fields are not the same as meta-fields.</em>
     * @param fields Map of fields and their values.
     */
    public void setFields(Map fields) {
        this.fields.putAll(fields);
    }

    /**
     * Add an "additional" field-value pair.
     * @param key field key.
     * @param value field value.
     */
    public void addField(String key, String value) {
        this.fields.put(key, value);
    }

    /**
     * Mime type of the result.
     * @param mimeType The mime type of the result.
     */
    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    /**
     * Relevance rating as returned. The value should
     * be between 0 and 10 inclusive.
     * @param rating The rating (a value between 0 and 10 inclusive).
     */
    public void setRating(int rating) {
        this.rating = rating;
    }

    /**
     * Set the result summary.
     * @param summary The result summary.
     */
    public void setSummary(String summary) {
        this.summary = summary;
    }

    /**
     * Set the result title.
     * @param title The result title.
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Set the content url for the result.
     * @param url the content url for the result.
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * Get the suggested indentation level.
     * @return indentation level.
     */
    public int getIndentation() {
        return indentation;
    }

    /**
     * get the language associated with this result.
     * @return language string.
     */
    public String getLanguage() {
        return language;
    }

    /**
     * get the map of meta information associated with
     * this result (if any).
     * @return Map of meta information associated with this result.
     * This is returned only if the query specifically indicated
     * that the meta information should be returned and if the
     * result actually had any meta information associated with it.
     */
    public Map getMetas() {
        Map retval = new HashMap(metas);
        return retval;
    }
    
    /**
     * get the value for the meta (custom) field.
     * @param name The name of the meta field.
     * @return returns the value of the meta field specified by the name.
     */
    public String getMeta(String name) {
        return Util.getString(metas.get(name), null, false);
    }

    /**
     * get the map of "additional" fields associated with
     * this result (if any).
     * @return Map of "additional" fields associated with this result.
     * This is returned only if the
     * result actually had any additional fields
     * associated with it.
     */
    public Map getFields() {
        Map retval = new HashMap(fields);
        return retval;
    }
    
    /**
     * Get the value for the "additional" field.
     * @param name The name of the additional field.
     * @return returns the value of the "additional" field specified by the name
     */
    public String getField(String name) {
        return Util.getString(fields.get(name), null, false);
    }

    /**
     * Get the result mime type.
     * @return mime type.
     */
    public String getMimeType() {
        return mimeType;
    }

    /**
     * 0-10 relevance rating for this result.
     * @return rating as an integer.
     */
    public int getRating() {
        return rating;
    }

    /**
     * get result summary.
     * @return summary string.
     */
    public String getSummary() {
        return summary;
    }

    /**
     * get result title.
     * @return title string.
     */
    public String getTitle() {
        return title;
    }

    /**
     * get url for the result.
     * @return url as a string.
     */
    public String getUrl() {
        return url;
    }

    /**
     * If a cached version is available, the 
     * encoding of the cached document.
     * @return String representing encoding of cached doc.
     */
    public String getCacheDocEncoding() {
        return cacheDocEncoding;
    }

    /**
     * set the value for encoding of the cached document.
     * @param cacheDocEncoding
     */
    public void setCacheDocEncoding(String cacheDocEncoding) {
        this.cacheDocEncoding = cacheDocEncoding;
    }

    /**
     * returns the cache identifier for the document.
     * @return identifier for the cached doc.
     */
    public String getCacheDocId() {
        return cacheDocId;
    }

    /**
     * set value for the cache identifier for the document.
     * @param cacheDocId
     */
    public void setCacheDocId(String cacheDocId) {
        this.cacheDocId = cacheDocId;
    }

    /**
     * String representing the value of the cached
     * documents approximate size.
     * @return size of the cached doc.
     */
    public String getCacheDocSize() {
        return cacheDocSize;
    }

    /**
     * set the value for the cached documents approximate size.
     * @param cacheDocSize
     */
    public void setCacheDocSize(String cacheDocSize) {
        this.cacheDocSize = cacheDocSize;
    }

    /**
     * return the escaped url as returned by the GSA.
     * @return escaped url as returned by the GSA.
     */
    public String getEscapedUrl() {
        return escapedUrl;
    }

    /**
     * set the value for the escaped url String.
     * @param escapedUrl
     */
    public void setEscapedUrl(String escapedUrl) {
        this.escapedUrl = escapedUrl;
    }

    /**
     * toString method: creates a String representation of the object
     * @return the String representation
     */
    public String toString() {
        /*
         * Auto generated by the info.vancauwenberge.tostring eclipse plugin
         */
        StringBuffer buffer = new StringBuffer();
        buffer.append("GSAResult[");
        buffer.append("mimeType = ").append(mimeType);
        buffer.append(", indentation = ").append(indentation);
        buffer.append(", url = ").append(url);
        buffer.append(", escapedUrl = ").append(escapedUrl);
        buffer.append(", title = ").append(title);
        buffer.append(", rating = ").append(rating);
        buffer.append(", metas = ").append(metas);
        buffer.append(", fields = ").append(fields);
        buffer.append(", summary = ").append(summary);
        buffer.append(", language = ").append(language);
        buffer.append(", cacheDocId = ").append(cacheDocId);
        buffer.append(", cacheDocEncoding = ").append(cacheDocEncoding);
        buffer.append(", cacheDocSize = ").append(cacheDocSize);
        buffer.append("]");
        return buffer.toString();
    }
}
