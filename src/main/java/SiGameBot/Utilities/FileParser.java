package SiGameBot.Utilities;

import java.io.File;
import java.util.Arrays;
import java.util.List;

public class FileParser {

    public static List<File> getAllFilesFromDir(String dirPath) {
        var dir = new File(dirPath);
        var files = dir.listFiles();
        return Arrays.asList(files != null ? files : new File[0]);
    }
}
