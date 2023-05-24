package org.example;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class MyScanner {
    public String read(Path path) throws IOException {
        Path fileName = Path.of(String.valueOf(path));
        String str = Files.readString(fileName);

        return str;
    }
}
