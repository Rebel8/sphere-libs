package eu.janinko.andaria.spherescript.sphere.parsers;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import eu.janinko.andaria.spherescript.sphere.objects.Chardef;

/**
 *
 * @author Honza Brázdil <janinko.g@gmail.com>
 */
public class ChardefParser extends ObjectdefParser<Chardef> {
    private Set<Chardef> chardefs = new HashSet<>();


    private final Matcher chardefMatcher = Pattern.compile("\\[chardef ([a-z0-9_]+)]").matcher("");
    void parse() {
        chardefMatcher.reset(it.lowercase().trim());
        if(chardefMatcher.matches()){
            String defid = chardefMatcher.group(1);
            //System.out.println("Parsing chardef: " + defid);
            Chardef chardef = new Chardef(defid);
            parseChardef(chardef);
            chardefs.add(chardef);
        }else{
            throw it.error("Failed to parse chardef definition");
        }
    }

    private void parseChardef(Chardef chardef) {
        while(it.hasNext()){
            it.next();
            if(parseObjectdef(chardef)){
                // noop
            }else if(it.lowercase().startsWith("armor=")){
                chardef.setArmor(parseNum(it.substring(6)));
            }else if(it.lowercase().startsWith("moverate=")){
                chardef.setMoverate(parseNum(it.substring(9)));
            }else if(it.lowercase().startsWith("reslevel=")){
                chardef.setReslevel(parseNum(it.substring(9)));
            }else if(it.lowercase().startsWith("sound=")){
                chardef.setSound(parseDefOrNum(it.substring(6)));
            }else if(it.lowercase().startsWith("icon=")){
                chardef.setIcon(parseDefOrNum(it.substring(5)));
            }else if(it.lowercase().startsWith("id=")){
                chardef.setId(parseDefOrNum(it.substring(3)));
            }else if(it.lowercase().startsWith("anim=")){
                chardef.setAnim(parseNum(it.substring(5)));
            }else if(it.lowercase().startsWith("resources=")){
                chardef.setResources(parseResources(it.substring(10)));
            }else if(it.lowercase().startsWith("foodtype=")){
                chardef.setFoodtype(parseResources(it.substring(9)));
            }else if(it.lowercase().startsWith("desires=")){
                chardef.setDesires(parseDeflist(it.substring(8)));
            }else if(it.lowercase().startsWith("aversions=")){
                chardef.setAversions(parseDeflist(it.substring(10)));
            }else if(it.lowercase().startsWith("tspeech=")){
                chardef.addSpeech(parseString(it.substring(8)));
            }else if(it.lowercase().startsWith("can=")){
                // TODO skipping can
            }else if(it.lowercase().startsWith("on=")){
                parseTrigger(chardef);
            }else if(it.line().startsWith("[")){
                it.back();
                return;
            }else{
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
                System.err.println("RESFIRE");
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

}
