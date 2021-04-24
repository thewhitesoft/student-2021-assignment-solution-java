package com.thewhite.test.item;

import lombok.Getter;

import java.util.List;

@Getter
public final class DirInfo extends ItemInfo {

    private final List<FileInfo> files;
    private final int size;

    public DirInfo(String name, List<FileInfo> files) {
        super(name);
        this.files = files;

        int size = 0;
        for (FileInfo file : files) {
            size += file.getSize();
        }
        this.size = size;
    }
}
