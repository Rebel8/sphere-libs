package eu.janinko.andaria.spherescript.sphere.objects;

import java.util.Map;
import java.util.TreeMap;

import lombok.Getter;
import lombok.ToString;

/**
 *
 * @author Honza Brázdil <janinko.g@gmail.com>
 */
@Getter
@ToString(callSuper = true)
public class Defname extends Def {
    
    public Defname() {
        super("defname");
    }

    private Map<String, String> defs = new TreeMap<>();

    public void addDef(String name, String value) {
        if(defs.containsKey(name)){
            System.err.println("Duplicate defname " + name);
        }else{
            defs.put(name, value);
        }
    }

    public String getDef(String name) {
        return defs.get(name);
    }
    
    
    
}
