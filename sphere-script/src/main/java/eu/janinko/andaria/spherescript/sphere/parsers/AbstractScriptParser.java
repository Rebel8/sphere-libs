package eu.janinko.andaria.spherescript.sphere.parsers;

import eu.janinko.uo.util.AbstractParser;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import eu.janinko.andaria.spherescript.sphere.objects.Resource;
import eu.janinko.andaria.spherescript.sphere.objects.Tagable;

/**
 *
 * @author Honza Brázdil <janinko.g@gmail.com>
 */
public abstract class AbstractScriptParser extends AbstractParser {


    Matcher numPatternMatcher = Pattern.compile("[0-9a-f]+").matcher("");
    protected boolean isNum(String line){
        return numPatternMatcher.reset(line.trim()).matches();
    }


    private final Matcher dotNumMatcher = Pattern.compile("[0-9]*\\.?[0-9]").matcher("");
    protected long parseDotNum(String oline) {
        String line = oline.trim();
        dotNumMatcher.reset(line);
        if(!dotNumMatcher.matches()){
            throw it.error("Failed to parse dot number");
        }
        line = line.replace(".", "");
        try {
            if(line.startsWith("0") && !oline.contains(".")){
                return Long.parseLong(line,16);
            }else{
                return Long.parseLong(line);
            }
        } catch (NumberFormatException ex) {
            throw it.error("Failed to parse number");
        }
    }



    private final Matcher defnameMatcher = Pattern.compile("(([cifetr]|snd|spk)_|job)[a-z0-9_]+").matcher("");
    protected String parseDefname(String defname) {
        defnameMatcher.reset(defname.trim());
        if(defnameMatcher.matches())
            return defnameMatcher.group();
        throw it.error("Failed to parse defname " + defname);
    }

    protected String parseDefOrNum(String defname) {
        defname = defname.trim();
        try {
            if(defname.startsWith("0")){
                return Long.valueOf(defname,16).toString();
            }else{
                return Long.valueOf(defname).toString();
            }
        } catch (NumberFormatException ex) {
            defnameMatcher.reset(defname);
            if(defnameMatcher.matches())
                return defnameMatcher.group();
        }

        throw it.error("Failed to parse defname or number " + defname);
    }

    protected String parseString(String string) {
        string = string.trim();
        if(string.startsWith("\"")){
            if(string.endsWith("\"")){
                string = string.substring(1, string.length()-1);
            }else{
                throw it.error("String starts with quotes but does not end");
            }
        }
        return string;
    }


    private final Matcher resourceMatcher = Patterns.RESOURCE_PATTERN.matcher("");
    protected Resource parseResource(String resource) {
        resourceMatcher.reset(resource.trim());
        if (resourceMatcher.matches()) {
            String count = resourceMatcher.group(1);
            String def = parseDefname(resourceMatcher.group(2));
            Resource res;
            if (count == null) {
                res = new Resource(1l, def);
            } else {
                res = new Resource(parseNum(count), def);
            }
            return res;
        } else {
            throw it.error("Failed to match resource " + resource);
        }
    }

    private final Matcher tagMatcher = Pattern.compile("tag\\.([a-z._0-9]+) ?= ?(.*)").matcher("");
    protected void parseTag(String line, Tagable objectdef) {
        tagMatcher.reset(line);
        if(tagMatcher.matches()){
            String name = tagMatcher.group(1);
            String value = tagMatcher.group(2);
            objectdef.addTag(name, value);
        }else{
            it.unknown("Failed while parsing tag");
        }
    }

    protected boolean parseBoolean(String line) {
        switch(line.trim()){
            case "0": return false;
            case "1": return true;
            default: throw it.error("Failed to parse boolean");
        }
    }

    protected boolean parseBoolean2(String line) {
        switch(line.trim()){
            case "y": return true;
            case "1": return true;
            case "Y": return true;
            default: return false;
        }
    }

    protected List<Resource> parseResources(String resources) {
        ArrayList<Resource> ret = new ArrayList<>();
        if(resources.trim().isEmpty())
            return ret;
        for(String resource : resources.split(",")){
            ret.add(parseResource(resource));
        }
        return ret;
    }

    protected List<String> parseDeflist(String defs) {
        ArrayList<String> ret = new ArrayList<>();
        if(defs.trim().isEmpty())
            return ret;
        for(String def : defs.split(",")){
            ret.add(parseDefname(def));
        }
        return ret;
    }

}
