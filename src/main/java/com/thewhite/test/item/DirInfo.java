package com.thewhite.test.item;

import com.thewhite.test.util.Util;
import lombok.Getter;
import lombok.Setter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Getter
public final class DirInfo implements ItemInfo {

    private final File file;
    private final String name;
    private final List<FileInfo> files = new ArrayList<>();
    private final int size;
    private final boolean root;

    @Setter
    private String resultName;

    public DirInfo(File file) {
        this(file, false);
    }

    public DirInfo(File dir, boolean root) {
        this.name = root ? "" : Util.getNameWithoutUUID(dir);
        this.file = dir;
        this.root = root;

        int size = 0;
        for (File file : dir.listFiles(File::isFile)) {
            var fileInfo = new FileInfo(this, file);
            files.add(fileInfo);
            size += fileInfo.getSize();
        }

        this.size = size;
    }

    public void deleteFile(FileInfo fileInfo) {
        files.remove(fileInfo);
    }

    public boolean isEmpty() {
        return files.isEmpty();
    }
}
