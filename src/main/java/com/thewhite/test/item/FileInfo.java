package com.thewhite.test.item;

import com.thewhite.test.util.Util;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;

import java.io.File;
import java.util.regex.Pattern;

import static com.thewhite.test.util.Util.compareContent;

@Getter
public final class FileInfo implements ItemInfo, Comparable<FileInfo> {

    private static final Pattern sizePattern = Pattern.compile("\\d+");

    private final File file;
    private final String name;
    private final int size;
    private final DirNameInfo dirNameInfo;

    @Setter
    private String resultName;

    public FileInfo(File file, boolean rootDir) {
        this.file = file;
        this.name = Util.getNameWithoutUUID(file.getName());
        this.size = getFileSize(file);

        var dirName = file.getParentFile().getName();
        this.dirNameInfo = rootDir ?
                           new DirNameInfo("", "") :
                           new DirNameInfo(dirName, Util.getNameWithoutUUID(dirName));
    }

    public String getDirInitialName() {
        return dirNameInfo.initialName;
    }

    @SneakyThrows
    public int getFileSize(File file) {
//        Здесь можно поменять метод подсчета размера файла
//        String content;
//        content = Files.readString(file.toPath());
//        var sizeMatcher = sizePattern.matcher(content);
//        sizeMatcher.find();
//        return Integer.parseInt(sizeMatcher.group(0));

        return (int) file.length();
    }

    @Override
    public int compareTo(FileInfo that) {
        if (that.equals(this)) return 0;

        return (this.size > that.size) ? -1 : 1;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (FileInfo) obj;

        return this.dirNameInfo.name.equals(that.dirNameInfo.name) &&
               this.name.equals(that.name) &&
               this.size == that.size &&
               compareContent(this.file, that.file);
    }

    @Override
    public int hashCode() {
        int result = getName().hashCode();
        result = 31 * result + getSize();
        result = 31 * result + dirNameInfo.name.hashCode();
        return result;
    }

    private static record DirNameInfo(String initialName, String name) {}
}
