package eu.janinko.uo.tools.generator;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import eu.janinko.andaria.spherescript.sphere.objects.Itemdef;
import eu.janinko.andaria.spherescript.sphere.objects.Resource;
import eu.janinko.andaria.spherescript.sphere.parsers.Patterns;
import java.text.MessageFormat;
import java.text.Normalizer;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.stream.Collectors;
import org.apache.commons.io.IOUtils;
import org.apache.commons.text.StringSubstitutor;

/**
 *
 * @author Honza Brázdil <janinko.g@gmail.com>
 */
public class WeaponGenerator {

    private BufferedWriter out;

    public WeaponGenerator(Path path) throws IOException {
        this.out = Files.newBufferedWriter(path, Charset.forName("utf-8"));
    }

    public static void main(String[] args) throws IOException {
        
        WeaponGenerator wg = new WeaponGenerator(Paths.get("/tmp/melee_basic.scp"));
        List<Itemdef> itemy = wg.loadCSV(Paths.get("/tmp/zbrane.csv"));
        wg.write("// Generovany script");
        wg.write();
        for (Itemdef item : itemy) {
            wg.print(item);
        }
        wg.finish();

        generate(WeaponVariant.BLACK, "Zbraně z temného kovu");
        generate(WeaponVariant.COPPER, "Zbraně měděné");
        generate(WeaponVariant.SILVER, "Zbraně stříbrné");
        generate(WeaponVariant.GILDED, "Zbraně pozlacené");
        generate(WeaponVariant.GOLD, "Zbraně zlaté");
        generate(WeaponVariant.MITRIL, "Zbraně mitrilové");
        generate(WeaponVariant.SILVERED, "Zbraně postříbřené");
        generate(WeaponVariant.STEEL, "Zbraně ocelové");
    }

    private static void generate(WeaponVariant variant, String category) throws IOException {
        WeaponGenerator wg;
        List<Itemdef> itemy;
        wg = new WeaponGenerator(Paths.get("/tmp/melee_"+variant.name().toLowerCase()+".scp"));
        itemy = wg.loadCSV(Paths.get("/tmp/zbrane.csv"));
        wg.write("// Generovany script");
        wg.write();
        for (Itemdef item : itemy) {
            if(!item.getSkillmake().get(0).getDefname().equalsIgnoreCase("kovarstvi"))
                continue;
            wg.printColored(item, variant, category);
        }
        wg.finish();
    }

    public void finish() throws IOException {
        write("[EOF]");
        out.close();
    }

    public List<Itemdef> loadTable(Path path) throws IOException {
        List<Itemdef> itemy = new ArrayList<>();
        BufferedReader in = Files.newBufferedReader(path);
        for (String line = in.readLine(); line != null; line = in.readLine()) {
            Itemdef i = toItemdef(line);
            itemy.add(i);
        }
        Collections.sort(itemy, (a, b) -> a.getDefname().compareTo(b.getDefname()));
        return itemy;
    }


    public List<Itemdef> loadCSV(Path path) throws IOException {
        List<Itemdef> itemy = new ArrayList<>();
        CSVParser parser = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(Files.newBufferedReader(path));
        for (CSVRecord record : parser) {
            switch(record.get("type")){
                case "t_weapon_bow":
                case "t_weapon_xbow":
                    continue;
            }
            Itemdef i = toItemdef(record);
            itemy.add(i);
        }
        Collections.sort(itemy, (a, b) -> a.getDefname().compareTo(b.getDefname()));
        return itemy;
    }

    private Itemdef toItemdef(CSVRecord r) {
        Itemdef i = new Itemdef(r.get("defname").toLowerCase());

        i.setId(r.get("id"));
        i.setName(r.get("name"));
        switch (r.get("rod")) {
            case "m":
                i.addTag("rod", "ý");
                break;
            case "z":
                i.addTag("rod", "á");
                break;
            case "s":
                i.addTag("rod", "é");
                break;
            case "p":
                i.addTag("rod", "é");
                break;
            default:
                throw new IllegalArgumentException("Unknow rod " + r.get("rod"));
        }
        i.setCategory(r.get("category"));
        i.setSubsection(r.get("subsection"));
        i.setDescription(r.get("description"));
        i.setType(r.get("type"));
        i.addTag("material", r.get("tag.material"));
        Boolean flip = parseBoolean(r.get("flip"));
        if (flip != null) i.setFlip(flip);
        i.setDamage(Long.parseLong(r.get("damMin")), Long.parseLong(r.get("damMax")));
        i.setSpeed(Long.parseLong(r.get("speed")));
        i.setHitpoints(Long.parseLong(r.get("hitLow")), Long.parseLong(r.get("hitMax")));
        i.setSkill(r.get("skill"));
        i.setReqstr(Long.parseLong(r.get("reqstr")));
        Boolean twohands = parseBoolean(r.get("twohands"));
        i.setTwohands(twohands == null ? false : twohands);
        i.setRange(Long.parseLong(r.get("range")));
        i.setResources(parseResources(r.get("resources")));
        i.setSkillmake(parseResources(r.get("skillmake")));
        i.setWeight(Long.parseLong(r.get("weight")));
        if (!r.get("dupelist").equals("null"))
            i.setDupelist(Arrays.asList(r.get("dupelist")));
        for (String t : r.get("tevents").split(",")) {
            if (!t.isEmpty())
                i.addTevent(t);
        }
        i.addTevent("t_contexts_weapons");
        i.addTevent("t_repairable");
        i.setTdata1(r.get("tdata1").equals("null") ? null : r.get("tdata1"));
        i.setTdata2(r.get("tdata2").equals("null") ? null : r.get("tdata2"));
        i.setTdata3(r.get("tdata3").equals("null") ? null : r.get("tdata3"));
        i.setTdata4(r.get("tdata4").equals("null") ? null : r.get("tdata4"));
        if (!r.get("tags").isEmpty()) {
            i.addTag(r.get("tags").split("=")[0], r.get("tags").split("=")[1]);
        }
        i.addTag("rqdex", r.get("reqdex"));

        i.setRepair(true);
        return i;
    }

    private Itemdef toItemdef(String line) {
        String[] s = line.split(";");
        Itemdef i = new Itemdef(s[0].toLowerCase());

        i.setId(s[1]);
        i.setName(s[2]);
        final String rod = s[3];
        switch (rod) {
            case "m":
                i.addTag("rod", "ý");
                break;
            case "z":
                i.addTag("rod", "á");
                break;
            case "s":
                i.addTag("rod", "é");
                break;
            case "p":
                i.addTag("rod", "é");
                break;
            default:
                throw new IllegalArgumentException("Unknow rod " + rod);
        }
        i.setCategory(s[4]);
        i.setSubsection(s[5]);
        i.setDescription(s[6]);
        i.setType(s[7]);
        i.addTag("material", s[8]);
        Boolean flip = parseBoolean(s[9]);
        if (flip != null) i.setFlip(flip);
        i.setDamage(Long.parseLong(s[10]), Long.parseLong(s[11]));
        i.setSpeed(Long.parseLong(s[12]));
        i.setHitpoints(Long.parseLong(s[14]), Long.parseLong(s[13]));
        i.setSkill(s[15]);
        i.setReqstr(Long.parseLong(s[16]));
        Boolean twohands = parseBoolean(s[17]);
        i.setTwohands(twohands == null ? false : twohands);
        i.setRange(Long.parseLong(s[18]));
        i.addTag("rrr", s[19]);
        i.addTag("sss", s[20]);
        i.setWeight(Long.parseLong(s[21]));
        if (!s[22].equals("null"))
            i.setDupelist(Arrays.asList(s[22]));
        for (String t : s[23].split(",")) {
            if (!t.isEmpty())
                i.addTevent(t);
        }
        i.addTevent("t_contexts_weapons");
        i.addTevent("t_repairable");
        i.setTdata1(s[24].equals("null") ? null : s[23]);
        i.setTdata2(s[25].equals("null") ? null : s[24]);
        i.setTdata3(s[26].equals("null") ? null : s[25]);
        i.setTdata4(s[27].equals("null") ? null : s[26]);
        if (!s[28].isEmpty()) {
            i.addTag(s[28].split("=")[0], s[28].split("=")[1]);
        }
        i.addTag("rqdex", s[29]);

        i.setRepair(true);
        return i;
    }
    
    private void fillFlipAndDupelist(Map<String, Object> variables, Itemdef i) {
        if (i.getFlip() != null || i.getDupelist() != null) {
            String flipAndDupelist = "\n";
            if (i.getFlip() != null) {
                flipAndDupelist += "flip=";
                flipAndDupelist += i.getFlip() ? "1" : "0";
                flipAndDupelist += "\n";
            }
            if (i.getDupelist() != null) {
                flipAndDupelist += "dupelist=";
                flipAndDupelist += String.join(",", i.getDupelist());
                flipAndDupelist += "\n";
            }
            variables.put("flipAndDupelist", flipAndDupelist);
        }
    }

    private void fillTagsAndTevetns(Map<String, Object> variables, Map<String, String> tags, List<String> tevents) {
        String teventsString = tevents.stream().map(t -> "tevents=" + t).collect(Collectors.joining("\n"));
        variables.put("tevents", teventsString);
        String tagsString = tags.entrySet().stream()
                .map(e -> "tag." + e.getKey() + "=" + e.getValue())
                .collect(Collectors.joining("\n"));
        variables.put("tags", tagsString);
    }
    
    private void fillName(Map<String, Object> variables, String name) {
        final String accentless = stripAccents(name);

        variables.put("nameAccentless", accentless);
        if (!name.equals(accentless)) {
            String nameu = "nameU=" + name;
            variables.put("nameu", nameu);
        }
    }
    
    private void fillResourcesAndSkillmake(Map<String, Object> variables, List<Resource> resources, List<Resource> skillmake){
        variables.put("resources", resources.stream()
                .map(r -> r.getCount() + " " + r.getDefname())
                .collect(Collectors.joining(", ")));
        variables.put("skillmake", skillmake.stream()
                .map(r -> r.getCount() + " " + r.getDefname())
                .collect(Collectors.joining(", ")));
    }

    private void fillBasicInfo(Map<String, Object> variables, Itemdef i){
        variables.put("id", i.getId());
        variables.put("defname", i.getDefname());
        variables.put("type", i.getType());
        variables.put("damMin", i.getDamageMin());
        variables.put("damMax", i.getDamageMax());
        variables.put("speed", i.getSpeed());
        variables.put("range", i.getRangeMax());
        variables.put("skill", i.getSkill());
        variables.put("reqstr", i.getReqstr());
        variables.put("twohands", i.getTwohands() ? "1" : "0");
        variables.put("weight", (i.getWeight() / 10) + "." + (i.getWeight() % 10));
        variables.put("repair", i.getRepair() ? "1" : "0");
        variables.put("category", i.getCategory());
        variables.put("subsection", i.getSubsection());
        variables.put("description", i.getDescription());
        variables.put("hitsMin", i.getHitMin());
        variables.put("hitsMax", i.getHitMax());
    }

    private Map<String, Object> getVariables(Itemdef i) {
        Map<String, Object> variables = new HashMap<>();
        fillBasicInfo(variables, i);
        fillFlipAndDupelist(variables, i);
        fillName(variables, i.getName());
        fillTagsAndTevetns(variables, i.getTags(), i.getTevents());
        fillResourcesAndSkillmake(variables, i.getResources(), i.getSkillmake());
        return variables;
    }

    public void print(Itemdef i) throws IOException {
        long rqdex = Long.parseLong(i.getTag("rqdex"));
        i.getTags().remove("rqdex");
        i.getTags().remove("rod");
        
        Map<String, Object> variables = getVariables(i);
        
        write(replaceTemplate("/generator/weapon.scp", variables));
        if (rqdex > 0) {
            write();
            write("on=@EquipTest");
            write("    return <CheckReqDex ", rqdex, ">");
        }
        if (i.getDupelist() != null) {
            for (String did : i.getDupelist()) {
                write();
                write("[itemdef ", did, "]");
                write("dupeitem=", i.getId());
            }
        }
        write();
    }

    private String replaceTemplate(final String templateName, Map<String, Object> variables) throws IOException {
        String template = IOUtils.toString(WeaponGenerator.class.getResourceAsStream(templateName));
        StringSubstitutor sas = new StringSubstitutor(variables);
        return sas.replace(template);
    }

    private final Matcher resourceMatcher = Patterns.RESOURCE_PATTERN.matcher("");
    private Resource parseResource(String resource) {
        resourceMatcher.reset(resource.trim());
        if (resourceMatcher.matches()) {
            String count = resourceMatcher.group(1);
            String def = resourceMatcher.group(2);
            if (count == null) {
                return new Resource(1l, def);
            } else {
                return new Resource(parseNum(count), def);
            }
        } else {
            throw new RuntimeException("Failed to match resource " + resource);
        }
    }
    
    private long parseNum(String line) {
        line = line.trim();
        try {
            if (line.startsWith("0")) {
                return Long.parseLong(line, 16);
            } else {
                return Long.parseLong(line);
            }
        } catch (NumberFormatException ex) {
            throw new RuntimeException("Failed to parse number '" + line + "'");
        }
    }
    
    private List<Resource> parseResources(String resourcesString) {
        return Arrays.stream(resourcesString.split(",")).map(this::parseResource).collect(Collectors.toList());
    }

    public void printColored(Itemdef i, WeaponVariant variant, String category) throws IOException {
        long rqdex = Long.parseLong(i.getTag("rqdex"));
        i.getTags().remove("rqdex");
        String nameSuffix = i.getTag("rod");
        i.getTags().remove("rod");

        Map<String, Object> variables = getVariables(i);
        fillResourcesAndSkillmake(variables, variant.resources(i), variant.skillmake(i));
        fillTagsAndTevetns(variables, variant.tags(i), variant.tevents(i));
        fillName(variables, variant.name(i, nameSuffix));
        variables.put("category", category);
        variables.put("hitsMin", variant.hitMin(i));
        variables.put("hitsMax", variant.hitMax(i));
        final long weight = variant.weight(i);
        variables.put("weight", (weight / 10) + "." + (weight % 10));
        variables.put("reqstr", variant.reqstr(i));
        variables.put("suffix", variant.getSuffix());
        variables.put("color", variant.getColor());
        
        write(replaceTemplate("/generator/weapon_color.scp", variables));
        if (rqdex > 0) {
            write("on=@EquipTest");
            write("\treturn <CheckReqDex ", rqdex, ">");
            write();
        }
    }

    private void writef(String format, Object... ss) throws IOException {
        
        MessageFormat mf = new MessageFormat(format);
        out.append(MessageFormat.format(format, ss));
        out.newLine();
    }
    
    private void write(Object... ss) throws IOException {
        for (Object s : ss) {
            out.append(s.toString());
        }
        out.newLine();
    }

    private Boolean parseBoolean(String s) {
        switch (s) {
            case "1":
            case "true": return true;
            case "0":
            case "false": return false;
            case "null": return null;
            default: throw new IllegalArgumentException(s);
        }
    }

    public static String stripAccents(String s) {
        s = Normalizer.normalize(s, Normalizer.Form.NFD);
        s = s.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");
        return s;
    }
}
