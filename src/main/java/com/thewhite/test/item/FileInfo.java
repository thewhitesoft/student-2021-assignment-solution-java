package com.thewhite.test.item;

import com.thewhite.test.util.Util;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;

import java.io.File;
import java.util.regex.Pattern;

import static com.thewhite.test.util.Util.compareContent;

@Getter
public final class FileInfo implements ItemInfo {

    private static final Pattern sizePattern = Pattern.compile("\\d+");

    private final File file;
    private final String name;
    private final int size;
    private final DirInfo dir;

    @Setter
    private String resultName;

    public FileInfo(DirInfo dir, File file) {
        this.dir = dir;
        this.file = file;
        this.name = Util.getNameWithoutUUID(file);
        this.size = getFileSize(file);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (FileInfo) obj;

        return this.name.equals(that.name) &&
               this.size == that.size &&
               compareContent(this.file, that.file);
    }

    public void delete() {
        dir.deleteFile(this);
    }

    @SneakyThrows
    public static int getFileSize(File file) {
//        Здесь можно поменять метод подсчета размера файла
//        String content;
//        content = Files.readString(file.toPath());
//        var sizeMatcher = sizePattern.matcher(content);
//        sizeMatcher.find();
//        return Integer.parseInt(sizeMatcher.group(0));

        return (int) file.length();
    }
}
