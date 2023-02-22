/*
 * Copyright (C) 2021 janinko
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package eu.janinko.uo.tools.generator;

import eu.janinko.andaria.spherescript.sphere.objects.Itemdef;
import eu.janinko.andaria.spherescript.sphere.objects.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Getter;

/**
 *
 * @author janinko
 */
public enum WeaponVariant {
    COPPER("copper", 50, -5, 1.2, 0.8, "měděn", "med", "color_a_copper", "i_ingot_copper"),
    SILVERED("silver", 50, 0, 1.05, 0.95, "postříbřen", "postribrene", "color_a_silver", "i_ingot_silver") {
        @Override
        public List<Resource> resources(Itemdef i) {
            return gild(i, super.resources(i));
        }
    },
    SILVER("full_silver", 100, 5, 1.16, 0.9, "stříbrn", "stribro", "color_a_silver", "i_ingot_silver"),
    GILDED("gold", 100, 3, 1.2, 0.85, "pozlacen", "pozlacene", "color_a_gold", "i_ingot_gold") {
        @Override
        public List<Resource> resources(Itemdef i) {
            return gild(i, super.resources(i));
        }
    },
    GOLD("full_gold", 150, 10, 1.6, 0.8, "zlat", "zlato", "color_a_gold", "i_ingot_gold"),
    STEEL("steel", 200, 5, 1.2, 0, "ocelov", null, "color_o_steel", "i_ingot_steel") {
        @Override
        public long hitMin(Itemdef i) {
            return 131;
        }

        @Override
        public long hitMax(Itemdef i) {
            return 170;
        }

        @Override
        public List<String> tevents(Itemdef i) {
            return i.getTevents();
        }

    },
    MITRIL("mytheril", 300, -10, 0.8, 1.3, "mitrilov", "mitril", "color_a_mytheril", "i_ingot_mytheril"),
    BLACK("blackrock", 350, 1, 0.8, 1.1, null, "temnykov", "color_a_blackrock", "i_ingot_blackrock") {
        @Override
        public String name(Itemdef i, String suffix) {
            return i.getName() + " z temného kovu";
        }

    };

    @Getter
    private final String suffix;
    private final int skillDiff;
    private final int strDiff;
    private final double weightMod;
    private final double durMod;
    private final String namePrefix;
    private final String navodSuffix;
    @Getter
    private final String color;
    private final String material;

    private WeaponVariant(String suffix, int skillDiff, int strDiff, double weightMod, double durMod, String namePrefix, String navodSuffix, String color, String material) {
        this.suffix = suffix;
        this.skillDiff = skillDiff;
        this.strDiff = strDiff;
        this.weightMod = weightMod;
        this.durMod = durMod;
        this.namePrefix = namePrefix;
        this.navodSuffix = navodSuffix;
        this.color = color;
        this.material = material;
    }


    public String name(Itemdef i, String suffix) {
        return namePrefix + suffix + " " + i.getName();
    }

    public List<Resource> resources(Itemdef i) {
        List<Resource> resources = new ArrayList<>(i.getResources());
        Resource ingot = resources.get(0);
        if (!ingot.getDefname().equalsIgnoreCase("i_ingot_iron")) {
            throw new IllegalArgumentException("Wrong resources for " + i);
        }
        ingot = new Resource(ingot.getCount(), material);
        resources.set(0, ingot);
        if (navodSuffix != null) {
            resources.add(new Resource(1, "i_navod_zbran_" + navodSuffix));
        }
        return resources;
    }

    public List<Resource> skillmake(Itemdef i) {
        List<Resource> skillmake = new ArrayList<>(i.getSkillmake());
        Resource kovarstvi = skillmake.get(0);
        if (!kovarstvi.getDefname().equalsIgnoreCase("kovarstvi")) {
            throw new IllegalArgumentException("Wrong skillmake for " + i);
        }
        kovarstvi = new Resource(kovarstvi.getCount() + skillDiff, "kovarstvi");
        skillmake.set(0, kovarstvi);
        return skillmake;
    }

    public Map<String, String> tags(Itemdef i) {
        Map<String, String> tags = new HashMap<>(i.getTags());
        tags.put("material", "material_" + suffix);
        return tags;
    }

    public List<String> tevents(Itemdef i) {
        List<String> tevents = new ArrayList<>(i.getTevents());
        tevents.add("t_weapon_" + suffix);
        return tevents;
    }

    public long hitMax(Itemdef i) {
        return Math.round(i.getHitMax() * durMod);
    }

    public long hitMin(Itemdef i) {
        return Math.round(i.getHitMin() * durMod);
    }

    public long reqstr(Itemdef i) {
        long reqstr = i.getReqstr() + strDiff;
        if (reqstr < 1) {
            reqstr = 1;
        } else if (reqstr > 100) {
            reqstr = 100;
        }
        return reqstr;
    }

    public long weight(Itemdef i) {
        return Math.round(i.getWeight() * weightMod);
    }

    private static List<Resource> gild(Itemdef i, List<Resource> resources) {
        Resource ingot = resources.get(0);
        long pocet = (ingot.getCount() + 1) / 2;
        if (pocet > 4) {
            pocet = 4;
        }
        resources.set(0, new Resource(pocet, ingot.getDefname()));
        resources.add(0, new Resource(1, i.getDefname()));
        return resources;
    }

}
