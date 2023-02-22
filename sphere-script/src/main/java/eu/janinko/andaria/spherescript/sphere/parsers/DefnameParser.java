package eu.janinko.andaria.spherescript.sphere.parsers;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import eu.janinko.andaria.spherescript.sphere.objects.Areadef;
import eu.janinko.andaria.spherescript.sphere.objects.Defname;

/**
 *
 * @author Honza Brázdil <janinko.g@gmail.com>
 */
public class DefnameParser extends DefParser<Areadef> {

    private Defname defnames = new Defname();

    public DefnameParser() {
        init();
    }

    private final Matcher defnamedefMatcher = Pattern.compile("\\[defname( [a-z0-9_]+)?]").matcher("");

    void parse() {
        defnamedefMatcher.reset(it.lowercase().trim());
        if (defnamedefMatcher.matches()) {
            parseDefname();
        } else {
            throw it.error("Failed to parse defname definition");
        }
    }

    private void init() {
        addStringParser("name", Areadef::setName);
        addStringParser("group", Areadef::setGroup);
        addNumberParser("rainchance", Areadef::setRainchance);
        addNumberParser("coldchance", Areadef::setColdchance);
    }

    private void parseDefname() {
        while (it.hasNext()) {
            it.next();
            String[] parts = it.line().split("\\s+", 2);
            if (it.line().startsWith("[")) {
                it.back();
                return;
            }
            if (parts.length == 2) {
                defnames.addDef(parts[0].toLowerCase(), parseString(parts[1]));
            } else {
                it.unknown();
            }

        }
    }

    public Defname getDefnames() {
        return defnames;
    }

}
