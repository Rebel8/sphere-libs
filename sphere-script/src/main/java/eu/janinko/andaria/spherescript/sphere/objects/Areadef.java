package eu.janinko.andaria.spherescript.sphere.objects;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 *
 * @author Honza Brázdil <janinko.g@gmail.com>
 */
@Getter
@ToString(callSuper = true)
public class Areadef extends Def implements Tagable {

    public static enum Flag {

        region_antimagic_all(0x1), // Magie je zakazana.
        region_antimagic_recall_in(0x2), // Nelze se sem privolat
        region_antimagic_recall_out(0x4), // Nelze se odtud privolat
        region_antimagic_gate(0x8), // Nele odtud (sem?) vytvorit pruchod
        region_antimagic_teleport(0x10), // Nemoznost teleportace.
        region_antimagic_damage(0x20), // Zakaz zle magie

        region_flag_ship(0x40), // Region lodicky
        region_flag_nobuilding(0x80), // Zadne staveni
        region_flag_announce(0x200), // Oznameni pri prichodu - Neni treba
        region_flag_insta_logout(0x400), // Okamzity LogOut
        region_flag_underground(0x800), // Podzemi - neprsi, nesnezi ...
        region_flag_nodecay(0x1000), // Nic na zemi NIKDY nezmizi

        region_flag_safe(0x2000), // Zadne zraneni tady
        region_flag_guarded(0x4000), // Strazeno - Nedoporucuji
        region_flag_no_pvp(0x8000), // Hraci se nemohou zranovat
        region_flag_arena(0x10000), // Killy neupravuji karmu, nebo snad se nezvysuje pocet killu?

        region_flag_no_fire_spells(0x20000), // nový flag - v regionu nepùjdou sesílat ohnivá kouzla
        region_flag_no_cold_spells(0x40000), // nový flag - v regionu nepùjdou sesílat ledová kouzla
        region_flag_no_acid_spells(0x80000), // nový flag - v regionu nepùjdou sesílat kyselinová kouzla
        region_flag_no_energy_spells(0x100000), // nový flag - v regionu nepùjdou sesílat energetická kouzla
        region_antimagic_mark(0x200000); // Nelze tu oznaèit runa

        private final long flag;

        private Flag(long flag) {
            this.flag = flag;
        }

        public boolean isIn(long flags) {
            return (flags & flag) != 0;
        }

        public long getFlag() {
            return flag;
        }

        public static EnumSet<Flag> flags(long f) {
            EnumSet<Flag> flags = EnumSet.noneOf(Flag.class);
            for (Flag flag : Flag.values()) {
                if (flag.isIn(f)) {
                    flags.add(flag);
                }
            }
            return flags;
        }
    }

    public Areadef(String defname) {
        super(defname);
    }

    @Setter
    private String name;
    @Setter
    private int x, y, z, m;
    @Setter
    private String group;
    @Setter
    private Long rainchance;
    @Setter
    private Long coldchance;
    private List<Rect> rects = new ArrayList<>();
    private List<String> events = new ArrayList<>();
    @Setter
    private EnumSet<Flag> flags = EnumSet.noneOf(Flag.class);
    private Map<String, String> tags = new TreeMap<>();

    public void addRect(Rect rect) {
        rects.add(rect);
    }

    public void addEvent(String event) {
        this.events.add(event);
    }

    @Override
    public void addTag(String name, String value) {
        tags.put(name, value);
    }

    @Override
    public String getTag(String name) {
        return tags.get(name);
    }

    public void setFlags(long flags) {
        this.flags = Flag.flags(flags);
    }

    @Data
    public static class Rect {

        private final int sx, sy;
        private final int ex, ey;
        private final int m;

        @Override
        public String toString() {
            return sx + "," + sy + "," + ex + "," + ey + (m == 0 ? "" : "," + m);
        }
    }

}
