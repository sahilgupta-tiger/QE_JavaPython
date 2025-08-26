package helperfunctions;

import org.yaml.snakeyaml.Yaml;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Map;

public class ConfigLoader {

    public static Map<String, Object> loadYamlFileUnderResource(String filePath) {
        Yaml yaml = new Yaml();
        try (InputStream in = ConfigLoader.class.getClassLoader().getResourceAsStream(filePath)) {
            if (in == null) {
                throw new RuntimeException("Unable to find YAML file: " + filePath);
            }
            return yaml.load(in);
        } catch (Exception e) {
            throw new RuntimeException("Failed to load YAML file: " + filePath, e);
        }
    }

    public static Map<String, Object> loadYamlFile(String filePath) {
        Yaml yaml = new Yaml();
        try (InputStream in = new FileInputStream(filePath)) { // Use FileInputStream for absolute paths
            // Load the YAML file into a Map
            Map<String, Object> data = yaml.load(in);
            System.out.println("Successfully loaded YAML data: " + data); // Debug print
            return data;
        } catch (Exception e) {
            throw new RuntimeException("Failed to load YAML file: " + filePath, e);
        }
    }
}
