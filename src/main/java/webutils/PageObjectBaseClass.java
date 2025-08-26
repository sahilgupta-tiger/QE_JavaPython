package webutils;

import java.util.Map;

public class PageObjectBaseClass {

    public static Map<String, String> datavalue;

    public static void setDatavalue(Map<String, String> data) {
        datavalue = data;
    }

    public static String getDatavalue(String key) {
        return datavalue.get(key);
    }

}
