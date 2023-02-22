/*
 * Copyright (C) 2021 janinko
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

import eu.janinko.uo.saves.Character;
import eu.janinko.uo.saves.Item;
import eu.janinko.uo.saves.SaveParser;
import eu.janinko.uo.saves.SaveFile;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 *
 * @author janinko
 */
public class CharacterTools {

    public static final Path SAVES_FOLDER = Paths.get("/home/janinko/Ultima/savy");

    public static void main(String[] args) throws IOException {
        printDifferentceOfDeaths(SAVES_FOLDER.resolve("191223"), SAVES_FOLDER.resolve("201223"));
        //printItemPositions(SAVES_FOLDER.resolve("201223"), "i_corpse");
    }

    private static void printDifferentceOfDeaths(Path oldSaveDir, Path newSaveDir) throws IOException {
        Map<String, Long> count20 = countDeaths(newSaveDir);
        Map<String, Long> count19 = countDeaths(oldSaveDir);

        count19.forEach((k, v) -> count20.putIfAbsent(k, 0l));
        for (Map.Entry<String, Long> e : count20.entrySet()) {
            String key = e.getKey();
            long val20 = e.getValue();
            long val19 = count19.getOrDefault(key, 0l);
            long diff = val20 - val19;
            System.out.println(key + ":\t" + val19 + "\t=>\t" + val20 + "\td: " + diff);
        }
    }

    private static Map<String, Long> countDeaths(Path saveDir) throws IOException {
        List<SaveFile> saves = parseDirectory(saveDir);
        return getChars(saves).stream()
                .filter(c -> c.getAccount() != null)
                .collect(Collectors.toMap(c -> c.getAccount() + " - " + c.getName() + "(" + c.getSkillclass() +")", Character::getDeaths));
        
    }
        
    private static List<SaveFile> parseDirectory(Path directory) throws IOException {
        SaveParser parser = new SaveParser();
        return Files.walk(directory)
                .filter(Files::isRegularFile)
                .filter(p -> p.toString().endsWith("scp"))
                .map(f -> {
                    try {
                        return parser.parse(f);
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                })
                .collect(Collectors.toList());
    }
    
    private static Set<Item> getItems(List<SaveFile> saves) throws IOException {
        return saves.stream()
                .flatMap(s -> s.getItems().stream())
                .collect(Collectors.toSet());
    }
    
    private static Set<Character> getChars(List<SaveFile> saves) throws IOException {
        return saves.stream()
                .flatMap(s -> s.getCharacters().stream())
                .collect(Collectors.toSet());
    }
    
}
