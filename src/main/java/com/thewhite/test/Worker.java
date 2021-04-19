package com.thewhite.test;

import com.thewhite.test.item.DirInfo;
import com.thewhite.test.item.FileInfo;
import com.thewhite.test.item.ItemInfo;
import com.thewhite.test.util.ZipArchive;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class Worker {

    private final File src;
    private final File destZip;
    private final Set<FileInfo> fileInfos = new TreeSet<>();
    private Map<String, List<DirInfo>> dirsByName;

    @SneakyThrows
    public void run() {
        init();
        determineNames();
        zip();
    }

    private void init() {
        processDir(src, true);
        for (File dir : src.listFiles(File::isDirectory)) {
            processDir(dir, false);
        }

        dirsByName = fileInfos.stream()
                              .collect(Collectors.groupingBy(FileInfo::getDirInitialName))
                              .entrySet()
                              .stream()
                              .map(entry -> new DirInfo(entry.getKey(), entry.getValue()))
                              .sorted(Comparator.comparing(DirInfo::getSize).reversed())
                              .collect(Collectors.groupingBy(DirInfo::getName));
    }

    private void processDir(File dir, boolean root) {
        for (File file : dir.listFiles(File::isFile)) {
            fileInfos.add(new FileInfo(file, root));
        }
    }

    private void determineNames() {
        //Проставляем имена директориям
        dirsByName.values()
                  .forEach(this::determineItemNames);

        //Проставляем имена файлам
        dirsByName.values()
                  .stream()
                  .flatMap(Collection::stream)
                  .map(DirInfo::getFiles)
                  .forEach(files -> {
                      files.stream()
                           .collect(Collectors.groupingBy(ItemInfo::getName))
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

    private void zip() {
        try (ZipArchive zip = new ZipArchive(destZip.getAbsolutePath())) {
            dirsByName.values()
                      .stream()
                      .flatMap(Collection::stream)
                      .forEach(zip::addDir);
        }
    }
}
