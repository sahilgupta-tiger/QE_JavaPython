package webutils;

import java.util.HashMap;
import java.util.Map;

public class ScenarioContext {
    private Map<String, Object> scenarioData;

    public ScenarioContext() {
        scenarioData = new HashMap<>();
    }

    public void put(String key, Object value) {
        scenarioData.put(key, value);
    }

    public Object get(String key) {
        return scenarioData.get(key);
    }

    public boolean containsKey(String key) {
        return scenarioData.containsKey(key);
    }

    public void clear() {
        scenarioData.clear();
    }
}