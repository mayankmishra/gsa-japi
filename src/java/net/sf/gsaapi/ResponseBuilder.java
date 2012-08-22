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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * This is the default response builder that the GSAClient will 
 * use in binding the XML response to Java objects.
 * @author Amol S Deshmukh adeshmuk .at inxight .dot com
 */
public class ResponseBuilder extends DefaultHandler {

    private static final SAXParserFactory saxfactory = SAXParserFactory.newInstance();
    static {
        saxfactory.setValidating(false);
    }

    private GSASpelling spelling;
    private GSASuggestion currSuggestion;
    private GSAResponse response;
    private GSAResult currResult;
    private GSAOneBoxResponse currOneBoxResponse;
    private GSAOneBoxResult currOneBoxResult;
    private String currFieldName;
    private StringBuffer contentBuff;
    private GSAKeymatch currKeymatch;
    private List resultsList;

    private boolean inSpelling = false;
    private boolean inSynonyms = false;
    private boolean inResult = false;
    private boolean inResponse = false;
    private boolean inOneBoxResult = false;
    private boolean inOneBoxResponse = false;
    private boolean inKeymatchResults = false;

    private GSADynamicNavigationAttribute currNavigationAttribute;
    private GSADynamicNavigationResponse navigationResponse;
    private List navgationResultList;

    private boolean inNavigationResponse = false;
    private boolean inNavigationResult  = false;

    private ResponseBuilder() {}

    /**
     * intended for internal use by GSAClient.
     * @param istream
     * @param xmlSystemId
     * @return  the GSAResponse instance
     */
    static GSAResponse buildResponse(InputStream istream, String xmlSystemId) {
        GSAResponse response = null;
        ResponseBuilder handler = new ResponseBuilder();

        try {
            SAXParser parser = null;
            synchronized (saxfactory) {
                parser = saxfactory.newSAXParser();
            }
            parser.parse(istream, handler, xmlSystemId);
            response = handler.getGSAResponse();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (istream != null) istream.close();
            } catch (IOException ioe) {
            }
        }
        return response;
    }

    /**
     * returns the last built response. This is simply a convenience
     * method to avoid having to build the same response over and over.
     * Ideally, client should cache the result of buildResponse
     * externally instead of using this method.
     * @return the GSAResponse instance
     */
    GSAResponse getGSAResponse() {
        return response;
    }

    /**
     * overridden startDocument from DefaultHandler
     */
    public void startDocument() throws SAXException {
        super.startDocument();
        response = new GSAResponse();
        contentBuff = new StringBuffer();
        resultsList = new ArrayList();
        currResult = new GSAResult();
        currOneBoxResponse = new GSAOneBoxResponse();
        currOneBoxResult = new GSAOneBoxResult();
        currKeymatch = new GSAKeymatch();

        currNavigationAttribute = new GSADynamicNavigationAttribute();
        navigationResponse = new GSADynamicNavigationResponse();
        navgationResultList = new ArrayList();
    }

    /**
     * overridden startElement from DefaultHandler
     */
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        int tag = getTagIndex(qName);
        inResponse = inResponse || (tag == RES); // inside response element
        inResult = inResponse && (inResult || (tag == R)); // inside results element
        inNavigationResponse = inNavigationResponse || (tag == PARM); // inside PARM element i.e Navigation Response
        inNavigationResult = inNavigationResult || (tag == PMT); // inside PMT element i.e Navigation Result
        inOneBoxResponse = inOneBoxResponse || (tag == OBRES);
        inKeymatchResults = inKeymatchResults || (tag == GM) ;
        inOneBoxResult = inOneBoxResponse && (inOneBoxResult || (tag == MODULE_RESULT)); // inside oneBox results element
        inSpelling = inSpelling || (tag == SPELLING);
        switch (tag) {
        case RES:
            String startIndex = attributes.getValue("SN");
            String endIndex = attributes.getValue("EN");
            response.setStartIndex(startIndex != null ? Long.parseLong(startIndex) : 1);
            response.setEndIndex(endIndex != null ? Long.parseLong(endIndex) : 1);
            break;
        case R:
            String mimeType = attributes.getValue("MIME");
            String indentation = attributes.getValue("L");
            currResult.setIndentation(indentation == null ? 1 : Integer.parseInt(indentation));
            currResult.setMimeType(mimeType);
            break;
        case PARAM:
            String name = attributes.getValue("name");
            String value = attributes.getValue("value");
            response.putParam(name, value);
            break;
        case FIELD:
            currFieldName = attributes.getValue("name");
            break;
        case C:
            if (inResult) {
                String cid = attributes.getValue("CID");
                String size = attributes.getValue("SZ");
                String encoding = attributes.getValue("ENC");
                if (null == encoding || "".equals(encoding.trim())) {
                    encoding = "UTF-8";
                }
                currResult.setCacheDocEncoding(encoding);
                currResult.setCacheDocId(cid);
                currResult.setCacheDocSize(size);
            }
            break;
        case FS: 
            String fieldName = attributes.getValue("NAME");
            String fieldValue = attributes.getValue("VALUE");
            currResult.addField(fieldName, fieldValue);
            break;
        case MT:
            String metaName = attributes.getValue("N");
            String metaValue = attributes.getValue("V");
            currResult.addMeta(metaName, metaValue);
            break;
        case GM:
        case GL:
        case GD:                
            inKeymatchResults = true;
            break;
        
        case SPELLING:
            inSpelling = true;
            spelling = new GSASpelling();
            break;
        case SUGGESTION:
            if (inSpelling) {
                currSuggestion = new GSASuggestion();
                currSuggestion.setText(attributes.getValue("q"));
            }
            break;
        case SYNONYMS:
            inSynonyms = true;
            break;
        case ONE_SYNONYM:
            if (inSynonyms) {
                response.addSynonymWithMarkup(attributes.getValue("q"));
            }
            break;

        case PARM:
            break;

        case PMT:
            String attrName = attributes.getValue("NM");
            String attrLabel = attributes.getValue("DN");
            String attrType = attributes.getValue("T");
            currNavigationAttribute.setName(attrName);
            currNavigationAttribute.setLabel(attrLabel);
            currNavigationAttribute.setType(Integer.parseInt(attributes.getValue("T")));
            currNavigationAttribute.setRange(attributes.getValue("IR").equals("1"));
            break;

        case PV:
            if(inNavigationResult){
                GSADynamicNavigationAttributeResult currGSADynamicNavigationAttributeResult = new GSADynamicNavigationAttributeResult();
                String attrValue = attributes.getValue("V");
                String count = attributes.getValue("C");
                String lowRange = attributes.getValue("L");
                String highRange = attributes.getValue("H");
                currGSADynamicNavigationAttributeResult.setValue(attrValue);
                currGSADynamicNavigationAttributeResult.setCount(count != "" ? Long.parseLong(count) : 0);
                currGSADynamicNavigationAttributeResult.setLowerRage(lowRange);
                currGSADynamicNavigationAttributeResult.setHigherRange(highRange);
                currNavigationAttribute.addAttributeResult(currGSADynamicNavigationAttributeResult);
            }
            break;
        }
        clearContent();
    }

    /**
     * overridden characters from DefaultHandler
     */
    public void characters(char[] ch, int start, int length)
            throws SAXException {
        contentBuff.append(ch, start, length);
    }

    /**
     * overridden endElement from DefaultHandler
     */
    public void endElement(String uri, String localName, String qName) throws SAXException {
        int tag = getTagIndex(qName);
        if (inOneBoxResult) {
            doOneBoxResult(tag);
        } else if (inOneBoxResponse) {
            doOneBoxResponse(tag);
        } else if (inResult) {
            doResult(tag);
        } else if(inNavigationResult){
            doNavigationResult(tag);
        } else if(inNavigationResponse){
            doNavigationResponse(tag);
        } else if (inResponse) {
            doResponse(tag);
        } else if (inSpelling) {
            doSpelling(tag);
        } else if (inSynonyms) {
            doSynonym(tag);
        } else if (inKeymatchResults) {
            doKeymatch(tag);
        } else { // in <GSP>
            doTopLevel(tag);
        }
    }
    
    private void clearContent() {
        if (contentBuff.length() > 0) contentBuff.delete(0, contentBuff.length());
    }

    private void doKeymatch(int tag) {
        switch (tag) {
        case GM:
            response.addKeymatchResult(currKeymatch);
            currKeymatch = new GSAKeymatch();
            inKeymatchResults = false;
            break;
        case GD:
            currKeymatch.setDescription(contentBuff.toString());
            break;
        case GL:
            currKeymatch.setUrl(contentBuff.toString());
            break;
        }
    }

    private void doNavigationResult(int tag){
        switch (tag){
            case PMT:
                navgationResultList.add(currNavigationAttribute);
                currNavigationAttribute = new GSADynamicNavigationAttribute();
                inNavigationResult = false;
                break;
        }
    }

    private void doNavigationResponse(int tag){
        switch (tag){
            case PARM:
                navigationResponse.setResults(navgationResultList);
                inNavigationResponse = false;
                break;
        }
    }

    private void doResult(int tag) {
        switch (tag) {
        case R:
            resultsList.add(currResult);
            currResult = new GSAResult();
            inResult = false;
            break;
        case U:
            currResult.setUrl(contentBuff.toString());
            break;
        case UE:
            currResult.setEscapedUrl(contentBuff.toString());
            break;
        case T:
            currResult.setTitle(contentBuff.toString());
            break;
        case RK:
            currResult.setRating(Integer.parseInt(contentBuff.toString()));
            break;
        case S:
            currResult.setSummary(contentBuff.toString());
            break;
        case LANG:
            currResult.setLanguage(contentBuff.toString());
            break;
            //case ??: indentation
            //case ??: mime
        }
    }
    
    private void doSynonym(int tag) {
        inSynonyms = (tag != SYNONYMS);
    }
    
    private void doSpelling(int tag) {
        switch (tag) {
        case SPELLING:
            response.setSpelling(spelling);
            inSpelling = false;
            break;
        case SUGGESTION:
            currSuggestion.setTextWithMarkup(contentBuff.toString());
            spelling.addSuggestion(currSuggestion);
            break;
        }
    }
    
    private void doResponse(int tag) {
        switch (tag) {
        case RES:
            response.setResults(resultsList);
            response.setNavigationResponse(navigationResponse);
            inResponse = false;
            break;
        case M:
            response.setNumResults(Long.parseLong(contentBuff.toString()));
            break;
        case FI:
            response.setFiltered(true);
            break;
        case PU:
            response.setPreviousResponseUrl(contentBuff.toString());
            break;
        case NU:
            response.setNextResponseUrl(contentBuff.toString());
            break;
        }
    }
    
    private void doTopLevel(int tag) {
        switch (tag) {
        case TM:
            response.setSearchTime(Double.parseDouble(contentBuff.toString()));
            break;
        case Q:
            response.setQuery(contentBuff.toString());
            break;
        }
    }
    
    private void doOneBoxResult(int tag) {
        switch (tag) {
        case MODULE_RESULT:
            currOneBoxResponse.addResult(currOneBoxResult);
            currOneBoxResult = new GSAOneBoxResult();
            inOneBoxResult = false;
            break;
        case U:
            currOneBoxResult.setUrl(contentBuff.toString());
            break;
        case FIELD:
            currOneBoxResult.addFieldEntry(currFieldName, contentBuff.toString());
            break;
        }
    }
    
    private void doOneBoxResponse(int tag) {
        switch (tag) {
        case OBRES:
            response.addOneBoxResponse(currOneBoxResponse);
            currOneBoxResponse = new GSAOneBoxResponse();
            inOneBoxResponse = false;
            break;
        case PROVIDER:
            currOneBoxResponse.setProviderName(contentBuff.toString());
            break;
        case URLTEXT:
            currOneBoxResponse.setTitleText(contentBuff.toString());
            break;
        case URLLINK:
            currOneBoxResponse.setTitleLink(contentBuff.toString());
            break;
        case IMAGE_SOURCE:
            currOneBoxResponse.setImageSource(contentBuff.toString());
            break;
        }
    }


    private static int getTagIndex(String tagName) {
        int retval = -1;
        Integer index = (Integer) INDEX_MAP.get(tagName);
        retval = index != null ? index.intValue() : -1;
        return retval;
    }


    private static final int GSP = 1;
    private static final int TM = 2;
    private static final int Q = 3;
    private static final int PARAM = 4;
    private static final int RES = 5;
    private static final int M = 6;
    private static final int FI = 7;
    private static final int NB = 8;
    private static final int NU = 9;
    private static final int R = 10;
    private static final int U = 11;
    private static final int UE = 12;
    private static final int T = 13;
    private static final int RK = 14;
    private static final int FS = 15;
    private static final int S = 16;
    private static final int LANG = 17;
    private static final int OBRES = 19;
    private static final int PROVIDER = 20;
    private static final int URLTEXT = 22;
    private static final int URLLINK = 23;
    private static final int IMAGE_SOURCE = 24;
    private static final int MODULE_RESULT = 25;
    private static final int FIELD = 27;
    private static final int C = 28;
    private static final int MT = 29;
    private static final int PU = 30;
    private static final int SPELLING = 31;
    private static final int SUGGESTION = 32;
    private static final int SYNONYMS = 33;
    private static final int ONE_SYNONYM = 34;
    private static final int GM = 35;
    private static final int GL = 36;
    private static final int GD = 37;

    private static final int PARM = 38;
    private static final int PC = 39;
    private static final int PMT = 40;
    private static final int PV = 41;

    private static Map INDEX_MAP = new HashMap();

    static {
        INDEX_MAP.put("GSP", new Integer(GSP));
        INDEX_MAP.put("TM", new Integer(TM));
        INDEX_MAP.put("Q", new Integer(Q));
        INDEX_MAP.put("PARAM", new Integer(PARAM));
        INDEX_MAP.put("RES", new Integer(RES));
        INDEX_MAP.put("M", new Integer(M));
        INDEX_MAP.put("FI", new Integer(FI));
        INDEX_MAP.put("NB", new Integer(NB));
        INDEX_MAP.put("PU", new Integer(PU));
        INDEX_MAP.put("NU", new Integer(NU));
        INDEX_MAP.put("R", new Integer(R));
        INDEX_MAP.put("U", new Integer(U));
        INDEX_MAP.put("UE", new Integer(UE));
        INDEX_MAP.put("T", new Integer(T));
        INDEX_MAP.put("RK", new Integer(RK));
        INDEX_MAP.put("FS", new Integer(FS));
        INDEX_MAP.put("MT", new Integer(MT));
        INDEX_MAP.put("S", new Integer(S));
        INDEX_MAP.put("LANG", new Integer(LANG));
        INDEX_MAP.put("OBRES", new Integer(OBRES));
        INDEX_MAP.put("provider", new Integer(PROVIDER));
        INDEX_MAP.put("urlText", new Integer(URLTEXT));
        INDEX_MAP.put("urlLink", new Integer(URLLINK));
        INDEX_MAP.put("Field", new Integer(FIELD));
        INDEX_MAP.put("IMAGE_SOURCE", new Integer(IMAGE_SOURCE));
        INDEX_MAP.put("MODULE_RESULT", new Integer(MODULE_RESULT));
        INDEX_MAP.put("C", new Integer(C));
        INDEX_MAP.put("Suggestion", new Integer(SUGGESTION));
        INDEX_MAP.put("Spelling", new Integer(SPELLING));
        INDEX_MAP.put("Synonyms", new Integer(SYNONYMS));
        INDEX_MAP.put("OneSynonym", new Integer(ONE_SYNONYM));
        INDEX_MAP.put("GM", new Integer(GM));
        INDEX_MAP.put("GL", new Integer(GL));
        INDEX_MAP.put("GD", new Integer(GD));

        INDEX_MAP.put("PARM", new Integer(PARM));
        INDEX_MAP.put("PC", new Integer(PC));
        INDEX_MAP.put("PMT", new Integer(PMT));
        INDEX_MAP.put("PV", new Integer(PV));
    }

}