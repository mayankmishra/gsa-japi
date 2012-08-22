package net.sf.gsaapi;

import java.util.ArrayList;
import java.util.List;

/**
 * <em>
 * NOTE: OneBox support in this API is experimental only. 
 * Please test thoroughly for your search appliance and 
 * one box source installation that this works.
 * </em>
 * <br/>
 * @author Amol S Deshmukh adeshmuk .at inxight .dot com
 *
 */
public class GSAOneBoxResponse {

    private String titleText;
    private String titleLink;
    private String imageSource;
    private String providerName;
    private List moduleResults;
    
    /**
     * create the GSAOneBoxResponse instance.
     * Intended for internal use only.
     */
    public GSAOneBoxResponse() {
        this.moduleResults = new ArrayList();
    }

    /**
     * @return Returns the imageSource.
     */
    public String getImageSource() {
        return imageSource;
    }

    /**
     * @return Returns the moduleResults.
     */
    public List getModuleResults() {
        return moduleResults;
    }

    /**
     * @return Returns the providerName.
     */
    public String getProviderName() {
        return providerName;
    }

    /**
     * @return Returns the titleLink.
     */
    public String getTitleLink() {
        return titleLink;
    }

    /**
     * @return Returns the titleText.
     */
    public String getTitleText() {
        return titleText;
    }

    void setImageSource(String imageSource) {
        this.imageSource = imageSource;
    }

    void setModuleResults(List moduleResults) {
        this.moduleResults = moduleResults;
    }
    
    void addResult(GSAOneBoxResult oneboxResult) {
        this.moduleResults.add(oneboxResult);
    }

    void setProviderName(String providerName) {
        this.providerName = providerName;
    }

    void setTitleLink(String titleLink) {
        this.titleLink = titleLink;
    }

    void setTitleText(String titleText) {
        this.titleText = titleText;
    }
    
    
}
