/*
 * Copyright (C) 2020 Honza Brázdil &lt;jbrazdil@redhat.com&gt;
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package eu.janinko.uo.tools.save;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

import eu.janinko.uo.saves.Item;
import eu.janinko.uo.saves.SaveParser;

/**
 *
 * @author Honza Brázdil &lt;jbrazdil@redhat.com&gt;
 */
public class GoldCounter {

    public static final Path saveFolder = Paths.get("/mnt/data/Ultima/savy/201223");
    private static long gold;
    private static long silver;
    private static long copper;
    private static long ucet;
    private static long cechy;
    private static long max;
    private static BufferedWriter out;

    public static void main(String[] args) throws IOException {
        out = Files.newBufferedWriter(Paths.get("/tmp/zlato"));
/*



55
        */

        String part = "55";

        //out.append(part);
        out.newLine();
        SaveParser parser = new SaveParser();
        Files.walk(saveFolder)
                .filter(Files::isRegularFile)
                //.filter(p -> p.toString().contains(part))
                .filter(p -> p.toString().endsWith("scp"))
                .map(f -> {
                    try {
                        return parser.parse(f);
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                })
                .peek(s -> processVars(s.getGlobals()))
                .flatMap(s -> s.getItems().stream())
                .forEach(GoldCounter::processItem);

        out.append("mince: " + (copper + silver * 10 + gold * 100));
        out.newLine();
        out.append("ucty: " + ucet);
        out.newLine();
        out.append("cechy: " + cechy);
        out.newLine();
        out.close();
    }

    private static void processVars(Map<String, String> vars) {
        processMesto(vars, "andor");
        processMesto(vars, "thyris");
        processMesto(vars, "lewan");
        processMesto(vars, "imer");
        processMesto(vars, "margaard");
    }

    private static void processItem(Item item) {
        switch (item.getDef()) {
            case "i_gold":
                gold += item.getAmount();
                break;
            case "i_silver":
                silver += item.getAmount();
                break;
            case "i_copper":
                copper += item.getAmount();
                break;
            case "i_script_ucet":
                ucet += Integer.parseInt(item.get("MORE1", "1"), 16);
                break;
            case "i_guildstone":
                cechy += Integer.parseInt(item.getTag("guildcash"), 16);
                break;

        }
    }

    private static void processMesto(Map<String, String> vars, String mesto) {
        String penize = vars.get(mesto);
        if (penize != null) {
            try {
                out.append(mesto + ": " + Integer.parseInt(penize, 16));
                out.newLine();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

}
