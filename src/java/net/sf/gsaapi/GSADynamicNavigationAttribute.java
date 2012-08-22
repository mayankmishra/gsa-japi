package net.sf.gsaapi;

import java.util.ArrayList;

/**
 * User: mayank
 * Date: 22/08/12
 * Time: 2:10 PM
 */

public class GSADynamicNavigationAttribute {
    private String name;
    private String label;
    private boolean isRange = false;
    private ArrayList resultList;
    private int type;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public ArrayList getResultList() {
        return resultList;
    }

    public GSADynamicNavigationAttribute() {
        resultList = new ArrayList();
    }

    public void addAttributeResult(GSADynamicNavigationAttributeResult GSADynamicNavigationAttributeResult){
        resultList.add(GSADynamicNavigationAttributeResult);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public boolean isRange() {
        return isRange;
    }

    public void setRange(boolean range) {
        isRange = range;
    }
}