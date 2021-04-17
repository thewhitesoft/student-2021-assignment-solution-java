package com.thewhite.test;

import lombok.SneakyThrows;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

public class Main {

    private static final String zipName = "Utrom's secrets.zip";

    @SneakyThrows
    public static void main(String[] args) {
        var list = Arrays.asList(args);
        var srcPath = args[list.indexOf("--src") + 1];
        var destPath = args[list.indexOf("--dest") + 1];

        var srcFile = new File(srcPath);
        var destFile = new File(destPath);

        if (!srcFile.exists()) {
            System.err.println("Wrong src: %s".formatted(srcFile.getAbsolutePath()));
            System.exit(0);
        }

        if (!destFile.exists()) Files.createDirectories(destFile.toPath());
        File destZip = Paths.get(destFile.getAbsolutePath(), zipName).toFile();

        new Worker(srcFile, destZip).run();
        System.out.println("Path to Utrom's secrets.zip: %s".formatted(destFile.getAbsolutePath()));
    }
}
