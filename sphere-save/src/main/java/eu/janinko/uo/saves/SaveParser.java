package eu.janinko.uo.saves;

import eu.janinko.uo.saves.parsers.CharParser;
import eu.janinko.uo.saves.parsers.ItemParser;
import java.io.IOException;
import java.nio.file.Path;
import java.util.function.BiConsumer;

import eu.janinko.uo.util.ScriptReader;
import java.io.InputStream;

/**
 *
 * @author Honza Brázdil <janinko.g@gmail.com>
 */
public class SaveParser {

    private ScriptReader it;
    private ItemParser items = new ItemParser();
    private CharParser chars = new CharParser();
    private transient SaveFile currentSave;

    public SaveFile parse(InputStream input) throws IOException {
        it = new ScriptReader(input);
        reset();
        doParse();
        return currentSave;
    }

    public SaveFile parse(Path file) throws IOException {
        it = new ScriptReader(file);
        reset();
        doParse();
        return currentSave;
    }

    private void reset() {
        items.reset(it);
        chars.reset(it);

    }

    private void doParse() {
        currentSave = parseHeader();
        parseFile();
        currentSave.setItems(items.get());
        currentSave.setCharacters(chars.get());
    }

    private void parseFile() {
        while (it.hasNext()) {
            parseLine(it.next());
        }
    }

    private void parseLine(String line) {
        if (line.startsWith("[")) {
            parseNewBlock();
        } else {
            it.unknown();
        }
    }

    private SaveFile parseHeader() {
        SaveFile save = new SaveFile();
        while (it.hasNext()) {
            String line = it.next();
            if (line.startsWith("TITLE")) {
                save.setTitle(line.split("=")[1]);
            } else if (line.startsWith("VERSION")) {
                save.setVersion(line.split("=")[1]);
            } else if (line.startsWith("TIME")) {
                save.setTime(Long.parseLong(line.split("=")[1]));
            } else if (line.startsWith("SAVECOUNT")) {
                save.setSavecount(Integer.parseInt(line.split("=")[1]));
            } else if (line.startsWith("[")) {
                it.back();
                return save;
            }
        }
        return save;
    }

    private void parseNewBlock() {
        if (it.lowercase().startsWith("[sector ")) {
            currentSave.put(parseSector());
        } else if (it.lowercase().startsWith("[wi ")) {
            items.parse();
        } else if (it.lowercase().startsWith("[wc ")) {
            chars.parse();
        } else if ("[TIMERF]".equals(it.line())) {
            System.err.println("Skipping timerf parsing");
            skipSection();
        } else if ("[GLOBALS]".equals(it.line())) {
            parseSection(currentSave.getGlobals()::put);
        } else if (it.line().startsWith("[LIST")) {
            System.err.println("Skipping list parsing");
            skipSection();
        } else if (it.line().startsWith("[WS")) {
            System.err.println("Skipping WS parsing");
            skipSection();
        } else if (it.line().startsWith("[GMPAGE")) {
            System.err.println("Skipping GM page parsing");
            skipSection();
        } else if (it.lowercase().startsWith("[eof]")) {
            it.finish();
        } else {
            it.unknown("Unknown block");
            it.skipBlock();
        }
    }

    private void parseSection(BiConsumer<String, String> lineParser) {
        try {
            String line;
            while (it.hasNext()) {
                line = it.next();
                if (line.startsWith("[")) {
                    it.back();
                    return;
                }
                String[] split = line.split("=", 2);
                if (split.length == 1) {
                    lineParser.accept(split[0], null);
                } else {
                    lineParser.accept(split[0], split[1]);
                }
            }
        } catch (RuntimeException ex) {
            throw it.error(ex.getMessage());
        }
    }

    @Deprecated
    private void skipSection() {
        while (it.hasNext()) {
            String line = it.next();
            if (line.startsWith("[")) {
                it.back();
                return;
            }
        }
    }

    private Sector parseSector() {
        Sector s = new Sector(it.line());
        parseEntry(s);
        return s;
    }

    private Character parseCharacter() {
        Character s = new Character(it.line());
        parseEntry(s);
        return s;
    }

    private void parseEntry(Entry s) {
        try {
            String line;
            while (it.hasNext()) {
                line = it.next();
                if (line.startsWith("[")) {
                    it.back();
                    return;
                }
                String[] split = line.split("=", 2);
                if (split.length == 1) {
                    s.put(split[0], "");
                } else {
                    if (split[0].startsWith("TAG.")) {
                        s.addTag(split[0].substring(4), split[1]);
                    } else {
                        s.put(split[0], split[1]);
                    }
                }
            }
        } catch (RuntimeException ex) {
            throw it.error(ex.getMessage());
        }
    }

}
