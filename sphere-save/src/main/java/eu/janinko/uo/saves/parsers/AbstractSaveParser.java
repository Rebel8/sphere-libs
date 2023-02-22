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

import eu.janinko.uo.saves.Entry;
import eu.janinko.uo.util.AbstractParser;

/**
 *
 * @author janinko
 */
public class AbstractSaveParser extends AbstractParser {

    protected void parsePosition(Entry entry, String substring) {
        String[] split = substring.split(",");
        if (split.length < 2) {
            throw it.error("Position must have at least 2 coordinates");
        }
        entry.setPx((int) parseNum(split[0]));
        entry.setPy((int) parseNum(split[1]));
        if (split.length > 2) {
            entry.setPz((int) parseNum(split[2]));
        }
        if (split.length > 3) {
            entry.setPm((int) parseNum(split[3]));
        }
    }

}
