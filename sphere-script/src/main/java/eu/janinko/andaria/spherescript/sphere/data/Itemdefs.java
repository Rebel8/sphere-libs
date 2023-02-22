package eu.janinko.andaria.spherescript.sphere.data;

import eu.janinko.andaria.spherescript.sphere.objects.Itemdef;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 *
 * @author janinko
 */
public class Itemdefs {

    private final Map<String, Itemdef> byDefname = new HashMap<>();
    private final Map<String, String> idToDefname = new HashMap<>();

    public void addAll(Collection<Itemdef> items) {
        items.forEach(this::add);
    }

    public void add(Itemdef item) {
        String defname = getDefname(item);
        defname = defname.toLowerCase();
        if (byDefname.containsKey(defname)) {
            Itemdef conflicting = byDefname.get(defname);
            System.err.println("Duplicate: " + conflicting.getDefname() + " (" + conflicting.getDefid() + ") "
                    + item.getDefname() + " (" + item.getDefid() + ")");
            return;
        }
        byDefname.put(defname, item);
        if (item.getDefname() != null && !item.getDefid().equals(item.getDefname())) {
            idToDefname.put(item.getDefid().toLowerCase(), item.getDefname().toLowerCase());
        }
    }

    public static String getDefname(Itemdef item) {
        String defname = item.getDefname();
        if (defname == null) {
            defname = item.getDefid();
        }
        return defname;
    }

    public Itemdef get(String identifier) {
        Itemdef item = byDefname.get(identifier.toLowerCase());
        if (item == null) {
            String defname = idToDefname.get(identifier.toLowerCase());
            item = byDefname.get(defname);
        }
        return item;
    }

    public Collection<Itemdef> values() {
        return byDefname.values();
    }

    public Long getId(Itemdef item) {
        String id;
        if (!item.getDefid().equals(item.getDefname())) {
            id = item.getDefid();
        } else {
            id = item.getId();
        }
        if (id == null) {
            System.err.println("Item nema id: " + item);
            return null;
        }
        try {
            if (id.startsWith("0")) {
                return Long.parseLong(id, 16);
            }
            return Long.parseLong(id, 10);
        } catch (NumberFormatException ex) {
            Itemdef i2 = get(id);
            return i2 == null ? null : getId(i2);
        }
    }

    public void resolveInheritance() {
        for (Itemdef item : byDefname.values()) {
            String parentId = item.getId();
            if (parentId != null) {
                Itemdef parent = get(parentId);
                if (parent == null) {
                    if (!parentId.startsWith("0")) {
                        System.out.println("Item " + item.getDefname() + "(" + item.getDefid() + ") has unknow parent " + parentId);
                    }
                } else {
                    item.inherit(parent);
                }
            }
        }
    }

}
