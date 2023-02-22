package eu.janinko.andaria.spherescript.sphere.parsers;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import eu.janinko.andaria.spherescript.sphere.objects.Areadef;

/**
 *
 * @author Honza Brázdil <janinko.g@gmail.com>
 */
public class AreadefParser extends DefParser<Areadef> {

    private Set<Areadef> areadefs = new HashSet<>();

    public AreadefParser() {
        init();
    }

    private final Matcher areadefdefMatcher = Pattern.compile("\\[areadef ([a-z0-9_]+)]").matcher("");

    void parse() {
        areadefdefMatcher.reset(it.lowercase().trim());
        if (areadefdefMatcher.matches()) {
            String defid = areadefdefMatcher.group(1);
            //System.out.println("Parsing itemdef: " + defid);
            Areadef areadef = new Areadef(defid);
            parseAreadef(areadef);
            areadefs.add(areadef);
        } else {
            throw it.error("Failed to parse areadef definition");
        }
    }

    private void init() {
        addStringParser("name", Areadef::setName);
        addStringParser("group", Areadef::setGroup);
        addNumberParser("rainchance", Areadef::setRainchance);
        addNumberParser("coldchance", Areadef::setColdchance);
    }

    private void parseAreadef(Areadef areadef) {
        while (it.hasNext()) {
            it.next();
            if (runLineParsers(areadef)) {
                //noop
            } else if (it.lowercase().startsWith("p=")) {
                parsePosition(it.substring(2), areadef);
            } else if (it.lowercase().startsWith("rect=")) {
                areadef.addRect(parseRect(it.substring(5)));
            } else if (it.lowercase().startsWith("flags=")) {
                areadef.setFlags(parseFlags(it.substring(6)));
            } else if (it.lowercase().startsWith("events=")) {
                parseEvents(it.substring(7), areadef);
            } else if (it.lowercase().startsWith("tag.")) {
                parseTag(it.lowercase(), areadef);
            } else if (it.line().startsWith("[")) {
                it.back();
                return;
            } else {
                it.unknown();
            }
        }
    }

    Set<Areadef> getAreadefs() {
        return areadefs;
    }

    private void parsePosition(String line, Areadef areadef) {
        String[] split = line.split(",", 4);
        areadef.setX((int) parseNum(split[0]));
        if (split.length > 1) {
            areadef.setY((int) parseNum(split[1]));
        }
        if (split.length > 2) {
            areadef.setZ((int) parseNum(split[2]));
        }
        if (split.length > 3) {
            areadef.setM((int) parseNum(split[3]));
        }
    }

    private Areadef.Rect parseRect(String line) {
        String[] split = line.split(",");
        if (split.length < 4 || split.length > 5) {
            throw it.error("Failed to parse rect.");
        }
        int sx = (int) parseNum(split[0]);
        int sy = (int) parseNum(split[1]);
        int ex = (int) parseNum(split[2]);
        int ey = (int) parseNum(split[3]);

        if (sx > ex || sy > ey) {
            System.err.println("Wrong order! " + sx + "," + sy + "," + ex + "," + ey);
        }
        int m = 0;
        if (split.length == 5) {
            m = (int) parseNum(split[4]);
        }
        return new Areadef.Rect(sx, sy, ex, ey, m);
    }

    private void parseEvents(String line, Areadef areadef) {
        String[] events = line.split(",");
        for (String event : events) {
            areadef.addEvent(event.trim());
        }
    }

    private long parseFlags(String line) {
        String[] splits = line.split("\\|");
        long flags = 0;
        for (String split : splits) {
            if (isNum(split)) {
                long flag = parseNum(split);
                flags |= flag;
            } else {
                try {
                    Areadef.Flag flag = Areadef.Flag.valueOf(split.trim());
                    flags |= flag.getFlag();
                } catch (IllegalArgumentException ex) {
                    throw it.error("Failed to parse Areadef flag: unknown flag '" + split + "'");
                }
            }
        }
        return flags;
    }

}
