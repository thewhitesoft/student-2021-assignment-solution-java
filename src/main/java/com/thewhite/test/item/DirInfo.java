package com.thewhite.test.item;

import com.thewhite.test.util.Util;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
public final class DirInfo implements ItemInfo {

    private final String initialName;
    private final String name;
    private final List<FileInfo> files;
    private final int size;

    @Setter
    private String resultName;

    public DirInfo(String name, List<FileInfo> files) {
        this.initialName = name;
        this.name = Util.getNameWithoutUUID(name);
        this.files = files;

        int size = 0;
        for (FileInfo file : files) {
            size += file.getSize();
        }
        this.size = size;
    }
}
