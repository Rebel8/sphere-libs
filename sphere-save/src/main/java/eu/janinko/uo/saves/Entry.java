package eu.janinko.uo.saves;

import java.util.HashMap;
import java.util.Map;
import lombok.Data;

/**
 *
 * @author Honza Brázdil <janinko.g@gmail.com>
 */
@Data
public class Entry {

    private final String def;
    private final Map<String, String> fields = new HashMap<>();
    private final Map<String, String> tags = new HashMap<>();
    private String name;
    private Long serial;
    private Integer px, py, pz, pm;

    public Entry(String def) {
        this.def = def;
    }

    public void put(String key, String value) {
        fields.put(key, value);
    }

    public void addTag(String key, String value) {
        tags.put(key, value);
    }

    public String getTag(String key) {
        return tags.get(key);
    }

    public String get(String key) {
        return fields.get(key);
    }

    public String get(String key, String def) {
        return fields.getOrDefault(key, def);
    }

    public Map<String, String> getFields() {
        return fields;
    }

}
