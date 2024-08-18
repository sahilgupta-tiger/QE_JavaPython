package webutils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Properties;

public class LoadProperties {
    public static final String WEB_PROPERTY_PATH = "src/test/resources/webparams.properties";
    public static Properties prop=new Properties();

    static {
        try {
            prop.load(new FileInputStream(WEB_PROPERTY_PATH));
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
