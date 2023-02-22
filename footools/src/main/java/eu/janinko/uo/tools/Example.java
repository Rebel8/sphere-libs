/*
 * Copyright (C) 2023 janinko
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
package eu.janinko.uo.tools;

import eu.janinko.andaria.spherescript.sphere.objects.Chardef;
import eu.janinko.andaria.spherescript.sphere.parsers.SphereParser;
import eu.janinko.uo.saves.SaveFile;
import eu.janinko.uo.saves.SaveParser;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;

/**
 *
 * @author janinko
 */
public class Example {

    public static void main(String[] args) throws IOException {
        readScriptFile(Paths.get("/home/janinko/Ultima/andaria/sphere/npcs/n_farmy.scp"), Paths.get("/home/janinko/Ultima/andaria/sphere/npcs/n_fleder.scp"));
        readSaveFile(Paths.get("/path/to/save"));

    }

    private static void readScriptFile(Path scriptFile1, Path scriptFile2) throws IOException {
        SphereParser parser = new SphereParser();
        parser.parse(scriptFile1);
        parser.parse(scriptFile2);
        final Set<Chardef> chardefs = parser.getChardefs();

        for (Chardef chardef : chardefs) {
            System.out.println(chardef.getName());
        }
    }

    private static void readSaveFile(Path saveFile) throws IOException {
        SaveParser parser = new SaveParser();
        SaveFile save = parser.parse(saveFile);

        for (eu.janinko.uo.saves.Character character : save.getCharacters()) {
            System.out.println(character.getName());
        }
    }

}
