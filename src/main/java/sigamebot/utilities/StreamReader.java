package sigamebot.utilities;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

public class StreamReader {
    public static String readFromInputStream(String path) throws IOException {
        return Files.readString(Path.of(path));
    }
}
