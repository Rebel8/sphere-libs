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
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import lombok.Value;

/**
 *
 * @author janinko
 */
public class ItemsTools {

    public static final Path SAVES_FOLDER = Paths.get("/home/janinko/Ultima/savy");

    public static void main(String[] args) throws IOException {
        //printDifferentceOfItemCount(SAVES_FOLDER.resolve("191223"), SAVES_FOLDER.resolve("201223"));
        printPositionsWithInstancesCount(SAVES_FOLDER.resolve("201223"));
    }

    private static void printDifferentceOfItemCount(Path oldSaveDir, Path newSaveDir) throws IOException {
        Map<String, Integer> count20 = countItemsNotOnGMIsland(newSaveDir);
        Map<String, Integer> count19 = countItemsNotOnGMIsland(oldSaveDir);

        count19.forEach((k, v) -> count20.putIfAbsent(k, 0));
        for (Map.Entry<String, Integer> e : count20.entrySet()) {
            String key = e.getKey();
            int val20 = e.getValue();
            int val19 = count19.getOrDefault(key, 0);
            int diff = val20 - val19;
            System.out.println(key + ":\t" + val19 + "\t=>\t" + val20 + "\td: " + diff);
        }
    }

    private static Map<String, Integer> countItems(Path directory) throws IOException {
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
                .flatMap(s -> s.getItems().stream())
                .collect(Collectors.toMap(Item::getDef, Item::getAmount, Integer::sum));
    }
    
    private static Predicate<Item> notOnGMIsland(Map<Long, Item> items, Map<Long, Character> chars){
        return (i) -> {
            Position pos = getTopPosition(items, chars, i);
            return pos.getX() > 700 || pos.getY() < 3500;
        };
    }
    
    private static Map<String, Integer> countItemsNotOnGMIsland(Path saveDir) throws IOException {
        List<SaveFile> saves = parseDirectory(saveDir);
        Map<Long, Item> items = getItems(saves).stream().collect(Collectors.toMap(Item::getSerial, Function.identity()));
        Map<Long, Character> chars = getChars(saves).stream().collect(Collectors.toMap(Character::getSerial, Function.identity()));
        
        
        return items.values().stream()
                .filter(notOnGMIsland(items,chars))
                .collect(Collectors.toMap(Item::getDef, Item::getAmount, Integer::sum));
    }

    private static void printItemPositions(Path saveDir, String itemdef) throws IOException {
        List<SaveFile> saves = parseDirectory(saveDir);
        Map<Long, Item> items = getItems(saves).stream().collect(Collectors.toMap(Item::getSerial, Function.identity()));
        Map<Long, Character> chars = getChars(saves).stream().collect(Collectors.toMap(Character::getSerial, Function.identity()));
        /*
        items.values().stream().filter(f -> itemdef.equals(f.getDef())).map(ItemsTools::getTopPosition).forEach(p -> {
            System.out.println(p.getX() + "," + p.getY());
        });*/
    }
    
    private static void printPositionsWithInstancesCount(Path saveDir) throws IOException {
        List<SaveFile> saves = parseDirectory(saveDir);
        Map<Long, Item> items = getItems(saves).stream().collect(Collectors.toMap(Item::getSerial, Function.identity()));
        Map<Long, Character> chars = getChars(saves).stream().collect(Collectors.toMap(Character::getSerial, Function.identity()));
                
        Map<Position, Long> collect = items.values().stream().collect(Collectors.groupingBy(i -> getTopPosition(items, chars, i), Collectors.counting()));
        collect.entrySet().stream()
                .sorted(Comparator.comparing(Map.Entry<Position, Long>::getValue).reversed())
                .forEach(e -> System.out.println(e.getKey() + ": " + e.getValue()));
    }
    
    private static Position getTopPosition(Map<Long, Item> items, Map<Long, Character> chars, Item item) {
        while (item.getCont() != null) {
            Item cont = items.get(item.getCont());
            if (cont == null) {
                Character chr = chars.get(item.getCont());
                return new Position(chr.getPx(), chr.getPy());
            }
            item = cont;
        }
        return new Position(item.getPx(), item.getPy());
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
    
    @Value
    private static class Position{
        int x,y;
    }
}
