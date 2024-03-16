/*
 * Copyright (C) 2020 janinko
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
package eu.janinko.uo.saves.parsers;

import eu.janinko.uo.util.ScriptReader;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import eu.janinko.uo.saves.Character;

/**
 *
 * @author janinko
 */
public class CharParser extends AbstractSaveParser {

    private Set<Character> chars;

    private final Matcher wiMatcher = Pattern.compile("\\[WC ([a-z0-9_]+)]").matcher("");

    @Override
    public void reset(ScriptReader it) {
        chars = new HashSet<>();
        super.reset(it);
    }

    public Set<Character> get() {
        return chars;
    }

    public void parse() {
        wiMatcher.reset(it.line().trim());
        if (wiMatcher.matches()) {
            String defname = wiMatcher.group(1);
            Character character = new Character(defname);
            parseItem(character);
            chars.add(character);
        } else {
            throw it.error("Failed to parse chardef definition");
        }
    }

    private void parseItem(Character character) {
        while (it.hasNext()) {
            it.next();
            if (it.lowercase().startsWith("serial=")) {
                character.setSerial(parseNum(it.substring(7)));
            } else if (it.lowercase().startsWith("p=")) {
                parsePosition(character, it.substring(2));
            } else if (it.lowercase().startsWith("deaths=")) {
                character.setDeaths(parseNum(it.substring(7)));
            } else if (it.lowercase().startsWith("name=")) {
                character.setName(it.substring(5));
            } else if (it.lowercase().startsWith("account=")) {
                character.setAccount(it.substring(8));
            } else if (it.lowercase().startsWith("skillclass=")) {
                character.setSkillclass(it.substring(11));
            } else if (it.line().startsWith("[")) {
                it.back();
                return;
            } else {
                String[] split = it.line().split("=");
                if (split.length != 1) {
                    if (split[0].startsWith("TAG.")) {
                        character.addTag(split[0].substring(4), split[1]);
                    }
                }
            }
        }
    }
}
