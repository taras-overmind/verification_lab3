package org.example;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

public class Main {

    public static void main(String[] args) throws IOException {
        if (args.length < 1) {
            System.out.println("Filename not specified.");
            return;
        }

        try {
//            printResult(processFile(Paths.get(args[0])));
            printResult(processFile(Paths.get(args[0])));
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
        }
    }

    public static String processFile(Path path) throws IOException {
        MyParser parser = new MyParser();
        return parser.process(path);
    }

    private static void printResult(String max) {
        System.out.println(max);
    }
}