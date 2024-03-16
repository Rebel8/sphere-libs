package eu.janinko.uo.tools.characters;

import eu.janinko.uo.saves.Character;
import eu.janinko.uo.saves.Item;
import eu.janinko.uo.saves.SaveParser;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;

public class GetTagChars {

    public static final Path SAVE_FILE = Paths.get("data/sphereworld.scp");

    public static void main(String[] args) throws IOException {
        //Load Scriptu
        SaveParser saveParser = new SaveParser();
        saveParser.parse(SAVE_FILE);
        Set<Character> chars = saveParser.getChars().get();

        OutputStream csvSpawnersOutputFile = Files.newOutputStream(Paths.get("output.txt"));
        try (PrintWriter pw = new PrintWriter(new OutputStreamWriter(csvSpawnersOutputFile, StandardCharsets.UTF_8))) {
            for (Character ch : chars) {
                if (!ch.getTags().isEmpty() && ch.getTag("textdialogu") != null && !ch.getTag("textdialogu").isEmpty()){
                    pw.println("UID: " + ch.getSerial());
                    pw.println("Tag.textdialogu= " + ch.getTag("textdialogu"));
                }
            }
        }
    }
}
