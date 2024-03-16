package eu.janinko.uo.util;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Iterator;

import java.io.InputStream;
import java.nio.file.Paths;

/**
 *
 * @author Honza Brázdil <janinko.g@gmail.com>
 */
public class ScriptReader implements Iterator<String> {

    //private final Iterator<String> it;
    private String previousLine;
    private String currentLine;
    private String nextLine;
    private String storedLine;
    private final BufferedReader f;
    private String lowercaseLine;
    private int errCount;
    private int errors = 400000;
    private int substring;
    private final Path file;
    private int lineNum;

    public ScriptReader(Path file) throws IOException {
        f = new BufferedReader(new InputStreamReader(new SphereInputStream(new BufferedInputStream(Files.newInputStream(file)))));
        this.file = file;
        nextLine = readLine();
    }

    public ScriptReader(InputStream input) throws IOException {
        f = new BufferedReader(new InputStreamReader(new SphereInputStream(new BufferedInputStream(input))));
        this.file = Paths.get("/input-stream");
        nextLine = readLine();
    }

    public ScriptReader(Path file, Charset cs) throws IOException {
        f = Files.newBufferedReader(file, cs);
        this.file = file;
        nextLine = readLine();
    }

    @Override
    public boolean hasNext() {
        return nextLine != null;
    }

    private String readLine() throws IOException {
        String line;
        do {
            line = f.readLine();
            lineNum++;
            line = line == null ? null : line.replaceFirst("//.*", "");
        } while (line != null && line.trim().isEmpty());
        return line;
    }

    @Override
    public String next() {
        try {
            if (storedLine == null) {
                set(currentLine, nextLine, readLine(), null);
            } else {
                set(currentLine, nextLine, storedLine, null);
                lineNum++;
            }
            return currentLine;
        } catch (IOException ex) {
            System.err.println("Exception at line " + lineNum);
            System.err.println("  :" + previousLine);
            System.err.println("  :" + currentLine);
            System.err.println("  :" + nextLine);
            throw new RuntimeException(ex);
        }
    }

    public String previous() {
        return previousLine;
    }

    public String line() {
        return currentLine;
    }

    public String lowercase() {
        return lowercaseLine;
    }

    public String normalise() {
        return lowercaseLine.trim();
    }

    public void unknown() {
        unknown("Unknown line");
    }

    public void unknown(String message) {
        System.err.println(message + ": " + line());
        if (++errCount > errors) {
            throw new RuntimeException("Too many unknown lines");
        }
    }

    public RuntimeException error(String message) {
        return new RuntimeException(message + ":\n" + file + ":" + lineNum + "\n" + currentLine + "\n" + new String(new char[substring]).replace('\0', ' ') + "^");
    }

    public String substring(int i) {
        substring = i;
        return lowercase().substring(i);
    }

    public void skipBlock() {
        while (hasNext() && !nextLine.startsWith("[")) {
            next();
        }
    }

    public void back() {
        if (previousLine == null) {
            throw error("Cannot back, previous line is null");
        }
        set(null, previousLine, currentLine, nextLine);
        lineNum--;
    }

    private void set(String previousLine, String currentLine, String nextLine, String storedLine) {
        this.previousLine = previousLine;
        this.currentLine = currentLine;
        this.nextLine = nextLine;
        this.storedLine = storedLine;
        this.lowercaseLine = currentLine.toLowerCase();
        this.substring = 0;
    }

    public void finish() {
        nextLine = null;
    }

}
