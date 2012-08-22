package net.sf.gsaapi;

import java.util.ArrayList;
import java.util.List;

/**
 * User: mayank
 * Date: 22/08/12
 * Time: 2:11 PM
 */
public class GSADynamicNavigationResponse {

    private List results = new ArrayList();

    public List getResults() {
        return results;
    }

    public void setResults(List results) {
        this.results = results;
    }
}
