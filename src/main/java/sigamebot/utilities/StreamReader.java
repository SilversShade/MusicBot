package sigamebot.utilities;

import java.io.*;

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
    }
}
