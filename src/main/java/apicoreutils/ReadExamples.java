package apicoreutils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ReadExamples {

    private static final String FEATURE_FILE = Paths.get(System.getProperty("user.dir"), "features", "bddtestapi.feature").toString();

    public static Map<String, String> extractTestDataValues(String featureFile) {
        Map<String, String> dataMap = new HashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader(featureFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.contains("#testdata")) {
                    Pattern pattern = Pattern.compile("'([^']*)'\\s*=\\s*'([^']*)'");
                    Matcher matcher = pattern.matcher(line);
                    while (matcher.find()) {
                        dataMap.put(matcher.group(1), matcher.group(2));
                    }
                    break; // Assuming only one line with #testdata
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return dataMap;
    }


}
