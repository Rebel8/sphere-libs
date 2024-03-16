package eu.janinko.uo.tools.characters;

import eu.janinko.andaria.spherescript.sphere.objects.Chardef;
import eu.janinko.andaria.spherescript.sphere.parsers.SphereParser;
import eu.janinko.uo.saves.Item;
import eu.janinko.uo.saves.SaveParser;
import lombok.Getter;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;

public class Resistance {

    public static final Path SCRIPTS_FOLDER = Paths.get("data/ShereSkriptUTF");
    public static final Path SAVE_FILE = Paths.get("data/sphereworld.scp");

    private static Set<ChardefSpawn> chardefSpawns = new HashSet<>();

    public static void main(String[] args) throws IOException {
        //Load Scriptu
        SphereParser parser = new SphereParser();
        Files.walk(SCRIPTS_FOLDER).filter(f -> f.toString().endsWith(".scp")).forEach(f -> {
            try {
                System.out.println("Parsing: " + f.getFileName());
                parser.parse(f, StandardCharsets.UTF_8);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
        for (Chardef chardef : parser.getChardefs()) {
            if (chardef.getKarma() != null) {
                if (chardef.getKarma().getValue() <= 1000) {
                    chardefSpawns.add(new ChardefSpawn(chardef));
                }
            }
        }


        //Load Savu a ulozeni do struc
        SaveParser saveParser = new SaveParser();
        saveParser.parse(SAVE_FILE);
        Set<Item> items = saveParser.getItems().get();
        for (Item item : items) {
            addItemToChardefSpawn(item);
        }

        OutputStream csvOutputFile = Files.newOutputStream(Paths.get("Resistance-output.csv"));
        try (PrintWriter pw = new PrintWriter(new OutputStreamWriter(csvOutputFile, StandardCharsets.UTF_8))) {
            pw.println(String.join(";", "SPAWNERS-count", "TOTAL-NPCs-FROM_SPAWNERS", "CHARDEF", "DEFNAME", "NAME", "resFIRE-value", "resFIRE-lowRange", "resFIRE-highRange", "resFIRE-avgRange", "resCold-value", "resCold-lowRange", "resCold-highRange", "resCold-avgRange", "resEnergy-value", "resEnergy-lowRange", "resEnergy-highRange", "resEnergy-avgRange", "resPoison-value", "resPoison-lowRange", "resPoison-highRange", "resPoison-avgRange", "resNegative-value", "resNegative-lowRange", "resNegative-highRange", "resNegative-avgRange"));
            chardefSpawns.forEach(struc -> pw.println(String.join(";",
                    String.valueOf(struc.getSpawnItems().size()),
                    String.valueOf(struc.getSpawnTotalCount()),
                    struc.getChardef().getDefid(),
                    struc.getChardef().getDefname(),
                    struc.getChardef().getName(),
                    struc.getChardef().getResistances().getResFire().toCSV(),
                    struc.getChardef().getResistances().getResCold().toCSV(),
                    struc.getChardef().getResistances().getResEnergy().toCSV(),
                    struc.getChardef().getResistances().getResPoison().toCSV(),
                    struc.getChardef().getResistances().getResNegative().toCSV())
            ));
        }
        OutputStream csvSpawnersOutputFile = Files.newOutputStream(Paths.get("Resistance-spawners-output.csv"));
        try (PrintWriter pw = new PrintWriter(new OutputStreamWriter(csvSpawnersOutputFile, StandardCharsets.UTF_8))) {
            pw.println(String.join(";", "DEFNAME", "SPAWN-AMOUNT", "POS.X", "POS.Y", "POS.Z"));
            for (ChardefSpawn chardefSpawn : chardefSpawns) {
                String defname = chardefSpawn.getChardef().getDefname();
                for (Item spawnItem : chardefSpawn.getSpawnItems()) {
                    pw.println(String.join(";", defname, String.valueOf(spawnItem.getAmount()), String.valueOf(spawnItem.getPx()), String.valueOf(spawnItem.getPy()), String.valueOf(spawnItem.getPz())));
                }
            }
        }
    }

    public static void addItemToChardefSpawn(Item item) {
        for (ChardefSpawn chardefSpawn : chardefSpawns) {
            if (item.getDef().equalsIgnoreCase("i_worldgem_bit")) {
                if (item.getMore1() != null && item.getMore1().equalsIgnoreCase(String.valueOf(chardefSpawn.getChardef().getDefname()))) {
                    chardefSpawn.spawnItems.add(item);
                    return;
                }
            }
            if (item.getDef().equalsIgnoreCase("i_worldgem_bit_advance")) {
                if (item.getTag("spawn") != null && item.getTag("spawn").equalsIgnoreCase(String.valueOf(chardefSpawn.getChardef().getDefname()))) {
                    chardefSpawn.spawnItems.add(item);
                    return;
                }
            }
        }
    }
}

class ChardefSpawn {

    @Getter
    public Chardef chardef;

    @Getter
    public Set<Item> spawnItems = new HashSet<>(); //only i_worldgem_bit,i_worldgem_bit_advance

    public ChardefSpawn(Chardef chardef) {
        this.chardef = chardef;
    }

    public int getSpawnTotalCount() {
        int totalCount = 0;
        for (Item item : spawnItems) {
            totalCount += item.getAmount();
        }
        return totalCount;
    }
}