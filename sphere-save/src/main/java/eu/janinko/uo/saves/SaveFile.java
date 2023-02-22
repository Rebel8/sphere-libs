package eu.janinko.uo.saves;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import lombok.Data;

/**
 *
 * @author Honza Brázdil <janinko.g@gmail.com>
 */
@Data
public class SaveFile {

    private String title;
    private String version;
    private long time;
    private int savecount;

    private Set<Sector> sectors = new HashSet<>();
    private Set<Item> items = new HashSet<>();
    private Set<Character> characters = new HashSet<>();
    private Map<String, String> globals = new HashMap<>();

    public void put(Item i) {
        items.add(i);
    }

    public void put(Character i) {
        characters.add(i);
    }

    public void put(Sector i) {
        sectors.add(i);
    }
}
