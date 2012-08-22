package net.sf.gsaapi;

/**
 * User: mayank
 * Date: 22/08/12
 * Time: 2:08 PM
 */

public class GSADynamicNavigationAttributeResult {
    private String value;
    private String lowerRage;
    private String higherRange;
    private Long count;

    public Long getCount() {
        return count;

    }

    public void setCount(Long count) {
        this.count = count;
    }

    public String getLowerRage() {
        return lowerRage;
    }

    public void setLowerRage(String lowerRage) {
        this.lowerRage = lowerRage;
    }

    public String getHigherRange() {
        return higherRange;
    }

    public void setHigherRange(String higherRange) {
        this.higherRange = higherRange;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
