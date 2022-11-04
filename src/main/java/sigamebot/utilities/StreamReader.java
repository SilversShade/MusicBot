package sigamebot.utilities;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

public class StreamReader {
    public static String readFromInputStream(String path) throws IOException {
        StringBuilder resultStringBuilder = new StringBuilder();
        var inputStream = new FileInputStream(path);
        try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = br.readLine()) != null) {
                resultStringBuilder.append(line).append("\n");
            }
        }
        inputStream.close();
        return resultStringBuilder.toString();
        //return Files.readString(Path.of(path));
    }
}
