package com.thewhite.test;

import lombok.SneakyThrows;

import java.io.File;
import java.nio.file.Files;
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

        if (!destFile.exists()) Files.createDirectories(destFile.toPath());
        File destZip = new File(destFile.getAbsolutePath() + "/" + zipName);

        new Worker(srcFile, destZip).run();
    }
}
