package net.sf.gsaapi;

public abstract class GSATestCase extends junit.framework.TestCase {

    
    public static int countDistinct(String str, String sub) {
        int retval = 0;
        for (int i = str.indexOf(sub, 0), strlen=str.length()
               ; i >= 0 && i < strlen
               ; i = str.indexOf(sub, i)) {
            retval++;
            i += (i < 0) ? 0 : sub.length();
        }
        return retval;
    }

}
