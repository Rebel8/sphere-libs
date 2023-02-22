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

import eu.janinko.uo.saves.Item;
import eu.janinko.uo.util.ScriptReader;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author janinko
 */
public class ItemParser extends AbstractSaveParser {

    private Set<Item> items;

    private final Matcher wiMatcher = Pattern.compile("\\[WI ([a-z0-9_]+)]").matcher("");

    @Override
    public void reset(ScriptReader it) {
        items = new HashSet<>();
        super.reset(it);
    }

    public Set<Item> get() {
        return items;
    }

    public void parse() {
        wiMatcher.reset(it.line().trim());
        if (wiMatcher.matches()) {
            String defname = wiMatcher.group(1);
            Item item = new Item(defname);
            parseItem(item);
            items.add(item);
        } else {
            throw it.error("Failed to parse itemdef definition");
        }
    }

    private void parseItem(Item item) {
        while (it.hasNext()) {
            it.next();
            if (it.lowercase().startsWith("serial=")) {
                item.setSerial(parseNum(it.substring(7)));
            } else if (it.lowercase().startsWith("cont=")) {
                item.setCont(parseNum(it.substring(5)));
            } else if (it.lowercase().startsWith("amount=")) {
                item.setAmount(parseNum(it.substring(7)));
            } else if (it.lowercase().startsWith("name=")) {
                item.setName(it.substring(5));
            } else if (it.lowercase().startsWith("p=")) {
                parsePosition(item, it.substring(2));
            } else if (it.line().startsWith("[")) {
                it.back();
                return;
            } else {
                String[] split = it.line().split("=");
                if (split.length == 1) {
                    item.put(split[0], "");
                } else {
                    if (split[0].startsWith("TAG.")) {
                        item.addTag(split[0].substring(4), split[1]);
                    } else {
                        item.put(split[0], split[1]);
                    }
                }
                //it.unknown();
            }
        }
    }

}
