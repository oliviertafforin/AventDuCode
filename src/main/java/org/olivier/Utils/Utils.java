package org.olivier.Utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Utils {


    public static String getFileContent(String nom) throws IOException {
        Path path = Paths.get("src/main/java/org/olivier/resources/"+nom);
        return Files.readString(path);
    }
}
