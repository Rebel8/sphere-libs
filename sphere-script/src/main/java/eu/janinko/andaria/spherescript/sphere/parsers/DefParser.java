package eu.janinko.andaria.spherescript.sphere.parsers;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

import eu.janinko.andaria.spherescript.sphere.objects.Def;

/**
 *
 * @author Honza Brázdil <janinko.g@gmail.com>
 */
public abstract class DefParser<T extends Def> extends AbstractScriptParser{
    private final Map<String, BiConsumer<T, String>> lineParsers = new HashMap<>();

    protected void addStringParser(String key, BiConsumer<T, String> setter) {
        lineParsers.put(key, (i, s) -> setter.accept(i, parseString(s)));
    }

    protected void addNumberParser(String key, BiConsumer<T, Long> setter) {
        lineParsers.put(key, (i, s) -> setter.accept(i, parseNum(s)));
    }

    protected void addDotNumParser(String key, BiConsumer<T, Long> setter) {
        lineParsers.put(key, (i, s) -> setter.accept(i, parseDotNum(s)));
    }

    protected void addBooleanParser(String key, BiConsumer<T, Boolean> setter) {
        lineParsers.put(key, (i, s) -> setter.accept(i, parseBoolean(s)));
    }
    
    protected boolean runLineParsers(T def) {
        if (it.line().contains("=")) {
            String[] split = it.line().split("=", 2);
            BiConsumer<T, String> lineParser = lineParsers.get(split[0].toLowerCase().trim());
            if (lineParser != null) {
                lineParser.accept(def, split[1]);
                return true;
            }
        }
        return false;
    }
   
    
}
