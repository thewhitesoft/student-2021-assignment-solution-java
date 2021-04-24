package com.thewhite.test.util;

import com.thewhite.test.item.DirInfo;
import com.thewhite.test.item.FileInfo;
import lombok.SneakyThrows;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ZipArchive implements AutoCloseable {
    private final FileOutputStream fos;
    private final ZipOutputStream zipOut;

    @SneakyThrows
    public ZipArchive(String path) {
        this.fos = new FileOutputStream(path);
        this.zipOut = new ZipOutputStream(fos);
    }

    @SneakyThrows
    public void addDir(DirInfo dir) {
        if (!dir.getResultName().equals("")) {
            zipOut.putNextEntry(new ZipEntry(dir.getResultName() +
                                             FileSystems.getDefault().getSeparator()));
            zipOut.closeEntry();
        }

        for (FileInfo fileInfo : dir.getFiles()) {
            addFile(dir.getResultName(), fileInfo);
        }
    }

    @SneakyThrows
    public void addFile(String dirName, FileInfo file) {
        var name = dirName.equals("") ?
                   file.getResultName() :
                   dirName + FileSystems.getDefault().getSeparator() + file.getResultName();

        zipOut.putNextEntry(new ZipEntry(name));
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
