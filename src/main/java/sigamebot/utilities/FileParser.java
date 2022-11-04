package sigamebot.utilities;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class FileParser {
    public static List<File> getAllFilesFromDir(String dirPath) {
        var dir = new File(dirPath);
        var files = dir.listFiles();
        return Arrays.asList(files != null ? files : new File[0]);
    }

    public static void renameFile(String pathToFile, String newName) throws IOException {
        Path source = Paths.get(pathToFile);
        Files.move(source, source.resolveSibling(newName));
    }
}
