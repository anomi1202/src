package com.javarush.task.task31.task3101;

import java.io.*;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;

/*
Проход по дереву файлов
*/
public class Solution {
    public static void main(String[] args) throws IOException {
        Path path = Paths.get(args[0]);
        File resultFileAbsolute = new File(args[1]);

        //обрабатываем директорию согласно условию
        ArrayList<File> fileList = getFileList(path, resultFileAbsolute.toPath());

        //переименовываем входной файл в файл-рузультат
        File allFilesContent = new File(resultFileAbsolute.getParent() + "\\" + "allFilesContent.txt");
        FileUtils.renameFile(resultFileAbsolute, allFilesContent);

        //записываем данные из "правильных" файлов в файл-результат
        FileOutputStream wr = new FileOutputStream(allFilesContent);
        for (File file : fileList){
            BufferedReader rd = new BufferedReader(new FileReader(file));
            while(rd.ready()){
                wr.write(rd.readLine().getBytes());
            }
            rd.close();
            wr.write(System.lineSeparator().getBytes());
        }
        wr.close();
    }

    //обрабатываем директорию, согласно условию (все файлы > 50 байт удаляем)
    private static ArrayList<File> getFileList(Path filePath, final Path resultFileAbsolutePath) throws IOException {
        final TreeMap<String, File> map = new TreeMap<>(); //IDEA ругалась, если не final
        //"гуляем" по директории, полученной в первом агрументе метода main
        Files.walkFileTree(filePath,
                new SimpleFileVisitor<Path>() {
                    //обрабатываем файлы в дирректории, в которую зашли, согласно условию
                    @Override
                    public FileVisitResult visitFile(Path filePath, BasicFileAttributes attrs) throws IOException {
                        if (!filePath.equals(resultFileAbsolutePath)) {
                            File file = filePath.toFile();
                            if (file.length() > 50) {
                                FileUtils.deleteFile(file);
                            } else {
                                map.put(file.getName(),file);
                            }
                        }
                        return FileVisitResult.CONTINUE;
                    }
                    //при выходе из директории обрабатываем ее следующим образом
                    @Override
                    public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                        //если в директории, при выходе из нее, нет файлов, то удаляем ее, чтобы не оставалось пустых папок
                        if (dir.toFile().list().length == 0) {
                            dir.toFile().delete();
                        }

                        return FileVisitResult.CONTINUE;
                    }
                }
                );

        //вайлы из отсортированного Мара переписываем в лист и возвращаем его.
        ArrayList<File> list = new ArrayList<>();
        for (Map.Entry<String, File> pair : map.entrySet()){
            list.add(pair.getValue());
        }

        return list;
    }

    //этот метод был изначально при загрузке задания
    public static void deleteFile(File file) {
        if (!file.delete()) System.out.println("Can not delete file with name " + file.getName());
    }
}
