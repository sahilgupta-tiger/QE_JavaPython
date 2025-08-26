package mobileutils;

import java.util.Map;

public class PageObjectBaseClassMobile {

    public static Map<String, String> datavalue;

    public static void setDatavalue(Map<String, String> data) {
        datavalue = data;
    }

    public Map<String, String> returnDataMap() {
        return datavalue;
    }

    public static String getDatavalue(String key) {
        return datavalue.get(key);
    }
}

