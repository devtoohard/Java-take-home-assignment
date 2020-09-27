package org.assignment.Utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class TestUtils {

    public static List<File> fetchAllFiles(String source) {
        List<File> files = new ArrayList<>();
        try {
            files = Files.walk(Paths.get(source)).filter(Files::isRegularFile).map(Path::toFile)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            System.out.println("Could not fetch the files" + e.getMessage());
        }
        return files;
    }

}
