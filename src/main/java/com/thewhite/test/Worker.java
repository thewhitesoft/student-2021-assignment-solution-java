package com.thewhite.test;

import com.thewhite.test.item.DirInfo;
import com.thewhite.test.item.FileInfo;
import com.thewhite.test.item.ItemInfo;
import com.thewhite.test.util.ZipArchive;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class Worker {

    private final File src ;
    private final File destZip;

    private Map<String, List<DirInfo>> dirsByName = new HashMap<>();

    @SneakyThrows
    public void run() {
        init();
        deleteDuplicates();
        determineNames();
        zip();
    }

    private void init() {
        var dirInfos = new ArrayList<DirInfo>();
        for (File dir : src.listFiles(File::isDirectory)) {
            dirInfos.add(new DirInfo(dir));
        }
        dirInfos.add(new DirInfo(src, true));

        dirsByName = dirInfos.stream().collect(Collectors.groupingBy(DirInfo::getName));
    }

    private void deleteDuplicates() {
        //Удаляем дубликаты файлов
        dirsByName.forEach((s, files) -> deleteDuplicateInDir(files));

        //Удаляем пустые папки
        dirsByName.forEach((name, dirs) -> {
            dirs.removeIf(DirInfo::isEmpty);
        });
        dirsByName.entrySet().removeIf(entry -> entry.getValue().isEmpty());
    }

    private void deleteDuplicateInDir(List<DirInfo> dirs) {
        dirs.stream()
            .flatMap(dirInfo -> dirInfo.getFiles().stream())
            .collect(Collectors.groupingBy(FileInfo::getName))
            .forEach((s, files) -> {
                for (int i = 0; i < files.size(); i++) {
                    var fileInfo = files.get(i);
                    var duplicateFiles = files.stream()
                                              .filter(otherFileInfo -> otherFileInfo != fileInfo &&
                                                                       otherFileInfo.equals(fileInfo))
                                              .collect(Collectors.toList());

                    //Удаляем все одинаковые файлы, кроме оригинала
                    duplicateFiles.stream()
                                  .forEach(fileForDeleting -> {
                                      fileForDeleting.delete();
                                      files.remove(fileForDeleting);
                                  });
                    i -= duplicateFiles.size();
                }
            });
    }

    private void determineNames() {
        //Проставляем имена директориям
        dirsByName.values()
                  .forEach(dirs -> {
                      dirs.sort(Comparator.comparing(DirInfo::getSize).reversed());
                      determineItemNames(dirs);
                  });

        //Проставляем имена файлам
        dirsByName.values()
                  .stream()
                  .flatMap(Collection::stream)
                  .map(DirInfo::getFiles)
                  .forEach(files -> {
                      files.sort(Comparator.comparing(FileInfo::getSize).reversed());
                      files.stream().collect(Collectors.groupingBy(ItemInfo::getName))
                           .values()
                           .forEach(this::determineItemNames);
                  });
    }

    private void determineItemNames(List<? extends ItemInfo> items) {
        var fileName = items.get(0).getName();
        for (int j = 0; j < items.size(); j++) {
            items.get(j).setResultName(j == 0 ? fileName : fileName + " (%d)".formatted(j));
        }
    }

    private void zip() throws IOException {
        try (ZipArchive zip = new ZipArchive(destZip.getAbsolutePath())) {
            dirsByName.values()
                      .stream()
                      .flatMap(Collection::stream)
                      .forEach(zip::addDir);
        }
    }
}
