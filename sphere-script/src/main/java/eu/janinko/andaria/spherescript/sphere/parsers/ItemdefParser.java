package eu.janinko.andaria.spherescript.sphere.parsers;

import eu.janinko.andaria.spherescript.sphere.objects.Chardef;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import eu.janinko.andaria.spherescript.sphere.objects.Itemdef;
import eu.janinko.andaria.spherescript.sphere.objects.Resource;

/**
 *
 * @author Honza Brázdil <janinko.g@gmail.com>
 */
public class ItemdefParser extends ObjectdefParser<Itemdef> {

    private Set<Itemdef> itemdefs = new HashSet<>();

    public ItemdefParser() {
        init();
    }

    private final Matcher itemdefdefMatcher = Pattern.compile("\\[itemdef ([a-z0-9_]+)]").matcher("");

    void parse() {
        itemdefdefMatcher.reset(it.lowercase().trim());
        if (itemdefdefMatcher.matches()) {
            String defid = itemdefdefMatcher.group(1);
            //System.out.println("Parsing itemdef: " + defid);
            Itemdef itemdef = new Itemdef(defid);
            parseItemdef(itemdef);
            itemdefs.add(itemdef);
        } else {
            throw it.error("Failed to parse itemdef definition");
        }
    }

    private void init() {
        addStringParser("type", Itemdef::setType);
        addStringParser("layer", Itemdef::setLayer);
        addDotNumParser("weight", Itemdef::setWeight);
        addStringParser("tdata1", Itemdef::setTdata1);
        addStringParser("tdata2", Itemdef::setTdata2);
        addStringParser("tdata3", Itemdef::setTdata3);
        addStringParser("tdata4", Itemdef::setTdata4);
        addStringParser("dupeitem", Itemdef::setDupeitem);
        addStringParser("skill", Itemdef::setSkill);
        addNumberParser("value", Itemdef::setValue);
        addNumberParser("reqstr", Itemdef::setReqstr);
        addNumberParser("speed", Itemdef::setSpeed);
        addNumberParser("value", Itemdef::setValue);
        addNumberParser("armor", Itemdef::setArmor);
        addBooleanParser("repair", Itemdef::setRepair);
        addBooleanParser("pile", Itemdef::setPile);
        addBooleanParser("dye", Itemdef::setDye);
        addBooleanParser("flip", Itemdef::setFlip);
    }

    private void parseItemdef(Itemdef itemdef) {
        while (it.hasNext()) {
            it.next();
            if (parseObjectdef(itemdef)) {
                // noop
            } else if (it.lowercase().startsWith("skillmake=")) {
                itemdef.setSkillmake(parseSkillmake(it.substring(10)));
            } else if (it.lowercase().startsWith("twohands=")) {
                itemdef.setTwohands(parseBoolean2(it.substring(9)));
            } else if (it.lowercase().startsWith("dupelist=")) {
                itemdef.setDupelist(parseDupelist(it.substring(9)));
            } else if (it.lowercase().startsWith("range=")) {
                parseHigLow(it.substring(6), itemdef::setRange, itemdef::setRange);
            } else if (it.lowercase().startsWith("on=")) {
                parseTrigger(itemdef);
            } else if (it.line().startsWith("[")) {
                it.back();
                return;
            } else {
                it.unknown();
            }
        }
    }

    protected void parseOnCreate(Itemdef itemdef) {
        while (it.hasNext()) {
            it.next();
            if (it.lowercase().trim().startsWith("color=")) {
                itemdef.setColor(it.lowercase().trim().substring(6));
            } else if (it.normalise().startsWith("hitpoints=")) {
                parseRand(it.normalise().substring(10), itemdef::setHitpoints, itemdef::setHitpoints);
            } else if (it.normalise().startsWith("f_craft_hitpoints ")) {
                parseHigLow(it.normalise().substring(18), itemdef::setHitpoints, itemdef::setHitpoints);
            } else if (it.normalise().startsWith("on=")) {
                it.back();
                return;
            } else if (it.normalise().startsWith("[")) {
                it.back();
                return;
            } else {
                it.unknown();
            }
        }
    }

    private final Matcher defnameMatcher = Pattern.compile("[a-z]+").matcher("");

    private String parseSkillname(String defname) {
        defnameMatcher.reset(defname.trim());
        if (defnameMatcher.matches()) {
            return defnameMatcher.group();
        }
        throw it.error("Failed to parse skillname " + defname);
    }

    private final Matcher skillmakeMatcher = Pattern.compile("(?:([0-9]*\\.?[0-9]) )?([a-z]+)").matcher("");

    private List<Resource> parseSkillmake(String resources) {
        ArrayList<Resource> ret = new ArrayList<>();
        if (resources.trim().isEmpty()) {
            return ret;
        }
        for (String resource : resources.split(",")) {
            skillmakeMatcher.reset(resource.trim());
            if (skillmakeMatcher.matches()) {
                String count = skillmakeMatcher.group(1);
                String def = parseSkillname(skillmakeMatcher.group(2));
                Resource res;
                if (count == null) {
                    res = new Resource(1l, def);
                } else {
                    res = new Resource(parseDotNum(count), def);
                }
                ret.add(res);
            } else {
                ret.add(parseResource(resource));
            }
        }
        return ret;
    }

    Set<Itemdef> getItemdefs() {
        return itemdefs;
    }

    private List<String> parseDupelist(String substring) {
        return Arrays.asList(substring.split(","));
    }

}
