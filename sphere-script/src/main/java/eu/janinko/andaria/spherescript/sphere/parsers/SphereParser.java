package eu.janinko.andaria.spherescript.sphere.parsers;

import eu.janinko.uo.util.ScriptReader;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.util.Map;
import java.util.Set;

import eu.janinko.andaria.spherescript.sphere.objects.Areadef;
import eu.janinko.andaria.spherescript.sphere.objects.Chardef;
import eu.janinko.andaria.spherescript.sphere.objects.Itemdef;

/**
 *
 * @author Honza Brázdil <janinko.g@gmail.com>
 */
public class SphereParser {
    private static ScriptReader it;
    private final ChardefParser chardef = new ChardefParser();
    private final ItemdefParser itemdef = new ItemdefParser();
    private final AreadefParser areadef = new AreadefParser();
    private final DefnameParser defname = new DefnameParser();

    public Set<Chardef> getChardefs() {
        return chardef.getChardefs();
    }

    public Set<Itemdef> getItemdefs() {
        return itemdef.getItemdefs();
    }

    public Set<Areadef> getAreadefs() {
        return areadef.getAreadefs();
    }

    public Map<String, String> getDefnames() {
        return defname.getDefnames().getDefs();
    }


    public void parse(Path scriptFile) throws IOException {
        System.out.println("parsing " + scriptFile);
        it = new ScriptReader(scriptFile);
        chardef.reset(it);
        itemdef.reset(it);
        areadef.reset(it);
        defname.reset(it);
        parseFile();
    }

    public void parse(Path scriptFile, Charset cs) throws IOException {
        it = new ScriptReader(scriptFile, cs);
        chardef.reset(it);
        itemdef.reset(it);
        areadef.reset(it);
        defname.reset(it);
        parseFile();
    }

    private void parseFile() {
        while(it.hasNext()){
            parseLine(it.next());
        }
    }

    private void parseLine(String line) {
        if(line.startsWith("[")){
            parseNewBlock();
        }else{
            it.unknown();
        }
    }

    private void parseNewBlock() {
        if(it.lowercase().startsWith("[chardef ")){
            chardef.parse();
        }else if(it.lowercase().startsWith("[itemdef ")){
            itemdef.parse();
        }else if(it.lowercase().startsWith("[areadef ")){
            areadef.parse();
        }else if(it.lowercase().startsWith("[defname")){
            defname.parse();
        }else if(it.lowercase().startsWith("[eof]")){
            it.finish();
        }else{
            it.unknown("Unknown block");
            it.skipBlock();
        }
    }
}
