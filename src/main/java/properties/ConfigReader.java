package properties;

import org.apache.log4j.Logger;
import org.testng.Assert;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.Set;

/**
 * To read the properties from the properties files say executionconfig.properties
 *
 * @author gayathri
 */
public class ConfigReader {
    static Logger log;
    static String propFilePath;
    static Properties prop;
    static InputStream fis;

    static {
        ConfigReader.log = Logger.getLogger(ConfigReader.class);
        ConfigReader.propFilePath = "src/test/resources/Executionconfig.properties";
        try {
            ConfigReader.fis = ConfigReader.class.getClassLoader().getResourceAsStream(ConfigReader.propFilePath);
            /** ConfigReader.fis = new FileInputStream(ConfigReader.propFilePath); **/
            (ConfigReader.prop = new Properties()).load(ConfigReader.fis);
            ConfigReader.log.info((Object) ("Loaded file: " + ConfigReader.propFilePath + " successfully."));
        } catch (FileNotFoundException e) {
            ConfigReader.log
                    .error((Object) (ConfigReader.propFilePath + " file Not Found, check the fileName or path" + e));
            System.exit(0);
        } catch (IOException e2) {
            ConfigReader.log.error((Object) ("Error reading file " + ConfigReader.propFilePath + e2));
            System.exit(0);
        }
    }

    private ConfigReader() {
    }

    private static String getFilename() {
        final String[] tmpArray = ConfigReader.propFilePath.split("/");
        return tmpArray[tmpArray.length - 1];
    }

    /**
     * To get the value from the required key from properties file
     *
     * @param key the key for which the value should return
     * @return returns the value of the required key from properties file
     */
    public static String getProperty(final String key) {
        final String value = ConfigReader.prop.getProperty(key);
        if (value != null) {
            ConfigReader.log.info((Object) ("Property value for key: " + key + " is " + value));
        } else {
            Assert.assertTrue(false, "The key " + key + " is not found in " + getFilename());
        }
        return value;
    }

    public static boolean containsKey(final String value) {
        return ConfigReader.prop.containsKey(value);
    }

    public static void setProperty(final String key, final String value) {
        ConfigReader.prop.setProperty(key, value);
    }

    public static Set<Object> getKeySet() {
        return ConfigReader.prop.keySet();
    }
}
