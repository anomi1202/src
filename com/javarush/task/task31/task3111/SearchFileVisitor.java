package com.javarush.task.task31.task3111;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;

public class SearchFileVisitor extends SimpleFileVisitor<Path> {
    private String partOfName;
    private String partOfContent;
    private int minSize;
    private int maxSize;
    private List<Path> foundFiles = new ArrayList<>();

    public void setMaxSize(int maxSize) {
        this.maxSize = maxSize;
    }


    public void setMinSize(int minSize) {
        this.minSize = minSize;
    }


    public void setPartOfContent(String partOfContent) {
        this.partOfContent = partOfContent;
    }


    public void setPartOfName(String partOfName) {
        this.partOfName = partOfName;
    }

    public List<Path> getFoundFiles(){
        return foundFiles;
    }

    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {

        point:
        {
            if (partOfName != null) {
                if (!(file.getFileName().toString()).contains(partOfName))
                    break point;
            }
            if (partOfContent != null) {
                String content = new String(Files.readAllBytes(file));
                if (!content.contains(partOfContent))
                    break point;
            }

            long fileLenght = file.toFile().length();
            if (minSize != 0) {
                if (maxSize != 0) {
                    if (minSize < maxSize)
                        if ((int) fileLenght < minSize)
                            break point;
                } else if (fileLenght < (long) minSize)
                    break point;
            }
            if (maxSize > minSize) {
                if (fileLenght > (long) maxSize)
                    break point;
            }

            foundFiles.add(file);
        }

        return FileVisitResult.CONTINUE;

    }
}
