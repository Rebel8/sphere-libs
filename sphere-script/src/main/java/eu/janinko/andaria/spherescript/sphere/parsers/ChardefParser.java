package eu.janinko.andaria.spherescript.sphere.parsers;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import eu.janinko.andaria.spherescript.sphere.objects.Chardef;
import eu.janinko.andaria.spherescript.sphere.objects.PossibleRange;

/**
 * @author Honza Brázdil <janinko.g@gmail.com>
 */
public class ChardefParser extends ObjectdefParser<Chardef> {
    private Set<Chardef> chardefs = new HashSet<>();


    private final Matcher chardefMatcher = Pattern.compile("\\[chardef ([a-z0-9_]+)]").matcher("");

    void parse() {
        chardefMatcher.reset(it.lowercase().trim());
        if (chardefMatcher.matches()) {
            String defid = chardefMatcher.group(1);
            Chardef chardef = new Chardef(defid);
            parseChardef(chardef);
            chardefs.add(chardef);
        } else {
            throw it.error("Failed to parse chardef definition");
        }
    }

    private void parseChardef(Chardef chardef) {
        while (it.hasNext()) {
            it.next();
            if (parseObjectdef(chardef)) {
                // noop
            } else if (it.lowercase().startsWith("armor=")) {
                chardef.setArmor(parseNum(it.substring(6)));
            } else if (it.lowercase().startsWith("moverate=")) {
                chardef.setMoverate(parseNum(it.substring(9)));
            } else if (it.lowercase().startsWith("reslevel=")) {
                chardef.setReslevel(parseNum(it.substring(9)));
            } else if (it.lowercase().startsWith("sound=")) {
                chardef.setSound(parseDefOrNum(it.substring(6)));
            } else if (it.lowercase().startsWith("icon=")) {
                chardef.setIcon(parseDefOrNum(it.substring(5)));
            } else if (it.lowercase().startsWith("id=")) {
                chardef.setId(parseDefOrNum(it.substring(3)));
            } else if (it.lowercase().startsWith("anim=")) {
                chardef.setAnim(parseNum(it.substring(5)));
            } else if (it.lowercase().startsWith("resources=")) {
                chardef.setResources(parseResources(it.substring(10)));
            } else if (it.lowercase().startsWith("foodtype=")) {
                chardef.setFoodtype(parseResources(it.substring(9)));
            } else if (it.lowercase().startsWith("desires=")) {
                chardef.setDesires(parseDeflist(it.substring(8)));
            } else if (it.lowercase().startsWith("aversions=")) {
                chardef.setAversions(parseDeflist(it.substring(10)));
            } else if (it.lowercase().startsWith("tspeech=")) {
                chardef.addSpeech(parseString(it.substring(8)));
            } else if (it.lowercase().startsWith("can=")) {
                // TODO skipping can
            } else if (it.lowercase().startsWith("on=")) {
                parseTrigger(chardef);
            } else if (it.line().startsWith("[")) {
                it.back();
                return;
            } else {
                it.unknown();
            }
        }
    }

    protected void parseTemplate(String line) {
        if (false) {
        } else {
            it.unknown();
        }
    }

    Set<Chardef> getChardefs() {
        return chardefs;
    }

    protected void parseOnCreate(Chardef chardef) {
        while (it.hasNext()) {
            it.next();
            if (it.normalise().startsWith("resfire=")) {
                chardef.getResistances().getResFire().setRange(parsePossibleNumRange(it.normalise().substring(8)));
            } else if (it.normalise().startsWith("respoison=")) {
                chardef.getResistances().getResPoison().setRange(parsePossibleNumRange(it.normalise().substring(10)));
            } else if (it.normalise().startsWith("resenergy=")) {
                chardef.getResistances().getResEnergy().setRange(parsePossibleNumRange(it.normalise().substring(10)));
            } else if (it.normalise().startsWith("rescold=")) {
                chardef.getResistances().getResCold().setRange(parsePossibleNumRange(it.normalise().substring(8)));
            } else if (it.normalise().startsWith("tag.resnegative=")) {
                chardef.getResistances().getResNegative().setRange(parsePossibleNumRange(it.normalise().substring(16)));
            } else if (it.normalise().startsWith("name=")) {
                chardef.setName(it.normalise().substring(5));
            } else if (it.normalise().startsWith("nameu=")) {
                chardef.setName(it.normalise().substring(6));
            } else if (it.normalise().startsWith("karma=")) {
                chardef.setKarma(new PossibleRange(parsePossibleNumRange(it.normalise().substring(6))));
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

    protected Long[] parsePossibleNumRange(String line) {
        if (line.startsWith("\"")) {
            line = line.substring(1, line.length() - 1);
        }
        if (line.startsWith("{")) {
            line = line.substring(1, line.length() - 1);
            String[] nums = line.split("\\s");
            if (nums.length == 2) {
                return new Long[]{parseNum(nums[0]), parseNum(nums[1])};
            } else {
                System.err.println(line + ": " + it.line());
            }
        } else {
            return new Long[]{parseNum(line)};
        }
        return null;
    }
}
