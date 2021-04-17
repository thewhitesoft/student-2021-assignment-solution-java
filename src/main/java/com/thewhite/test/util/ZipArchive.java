package com.thewhite.test.util;

import com.thewhite.test.item.DirInfo;
import com.thewhite.test.item.FileInfo;
import lombok.SneakyThrows;

import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ZipArchive implements AutoCloseable{
    private final FileOutputStream fos;
    private final ZipOutputStream zipOut;

    @SneakyThrows
    public ZipArchive(String path) {
        this.fos = new FileOutputStream(path);
        this.zipOut = new ZipOutputStream(fos);
    }

    @SneakyThrows
    public void addDir(DirInfo dir) {
        zipOut.putNextEntry(new ZipEntry(dir.getResultName() + "/"));
        zipOut.closeEntry();

        for (FileInfo fileInfo : dir.getFiles()) {
            addFile(fileInfo);
        }
    }

    @SneakyThrows
    public void addFile(FileInfo file) {
        zipOut.putNextEntry(new ZipEntry(file.getDir().getResultName() + "/" + file.getResultName()));
        copy(file.getFile());
        zipOut.closeEntry();
    }

    private void copy(File file) throws IOException {
        try (FileInputStream fis = new FileInputStream(file)) {
            final byte[] buffer = new byte[8192];
            int n;
            while (-1 != (n = fis.read(buffer))) {
                zipOut.write(buffer, 0, n);
            }
        }
    }

    @Override
    @SneakyThrows
    public void close() {
        zipOut.close();
        fos.close();
    }
}
