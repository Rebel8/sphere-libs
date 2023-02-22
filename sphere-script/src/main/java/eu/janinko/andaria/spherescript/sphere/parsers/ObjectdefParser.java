package eu.janinko.andaria.spherescript.sphere.parsers;

import java.util.function.BiConsumer;
import java.util.function.LongConsumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import eu.janinko.andaria.spherescript.sphere.objects.Objectdef;

/**
 *
 * @author Honza Brázdil <janinko.g@gmail.com>
 * @param <T> Parsed type
 */
public abstract class ObjectdefParser<T extends Objectdef> extends DefParser<T> {

    public ObjectdefParser() {
        init();
    }
    
    private void init(){
        addStringParser("defname", Objectdef::setDefname);
        addStringParser("id", Objectdef::setId);
        addStringParser("name", Objectdef::setName);
        addStringParser("category", Objectdef::setCategory);
        addStringParser("subsection", Objectdef::setSubsection);
        addStringParser("description", Objectdef::setDescription);
        addStringParser("name", Objectdef::setName);
    }
    
    protected boolean parseObjectdef(T objectdef) {
        if(runLineParsers(objectdef)){
            //noop
        } else if (it.lowercase().startsWith("resources=")) {
            objectdef.setResources(parseResources(it.substring(10)));
        } else if (it.lowercase().startsWith("tevents=")) {
            objectdef.addTevent(parseString(it.substring(8)));
        } else if(it.lowercase().startsWith("dam=")){
            parseHigLow(it.substring(4), objectdef::setDamage, objectdef::setDamage);
        } else if (it.lowercase().startsWith("tag.")) {
            parseTag(it.lowercase(), objectdef);
        } else {
            return false;
        }
        return true;
    }

    protected void parseHigLow(String substring, BiConsumer<Long, Long> hilow, LongConsumer single) {
        if (substring.contains(",")) {
            String[] hilo = substring.split(",", 2);
            hilow.accept(parseNum(hilo[0]), parseNum(hilo[1]));
        } else {
            single.accept(parseNum(substring));
        }
    }
    
    private final Matcher randMatcher = Pattern.compile("\\{([0-9]+) ([0-9]+)\\}").matcher("");
    protected void parseRand(String substring, BiConsumer<Long, Long> hilow, LongConsumer single) {
        randMatcher.reset(substring);
        if(randMatcher.matches()){
            String low = randMatcher.group(1);
            String high = randMatcher.group(2);
            hilow.accept(parseNum(low), parseNum(high));
        }else{
            single.accept(parseNum(substring));
        }
    }
    
    protected void parseTrigger(T objectdef){
        if (it.lowercase().trim().equals("on=@create")){
            parseOnCreate(objectdef);
            return;
        } else {
            System.err.println("Skipping trigger " + it.line());
            while (it.hasNext()) {
                it.next();
                if (it.normalise().startsWith("on=")) {
                    it.back();
                    return;
                } else if (it.normalise().startsWith("[")) {
                    it.back();
                    return;
                }
            }
        }
    }

    protected abstract void parseOnCreate(T objectdef);

}
