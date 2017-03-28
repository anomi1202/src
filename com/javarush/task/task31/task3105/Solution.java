package com.javarush.task.task31.task3105;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/*
Добавление файла в архив
*/
public class Solution {
    public static void main(String[] args) throws IOException {
        if (args.length < 2)
            return;
        Path filePath = Paths.get(args[0]);
        String folderToZip = args[1];
        HashMap<String, ByteArrayOutputStream> enrtysMap = new HashMap<>();

        //считываем файлы из архива
        ZipInputStream zipIn = new ZipInputStream(new FileInputStream(folderToZip));
        ZipEntry zipTemp = null;
        while ((zipTemp = zipIn.getNextEntry()) != null) {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];

            while (zipIn.read(buffer) > 0) {
                byteArrayOutputStream.write(buffer);
            }
            enrtysMap.put(zipTemp.getName(), byteArrayOutputStream);
            zipIn.closeEntry();

        }
        zipIn.close();


        //записываем файлы обратно в архив
        ZipOutputStream zipOut = new ZipOutputStream(new FileOutputStream(folderToZip));
        String fileName = filePath.toString().substring(filePath.toString().lastIndexOf("\\")+1);

        for (Map.Entry<String, ByteArrayOutputStream> pair : enrtysMap.entrySet()){
            String currentFileName = pair.getKey().substring(pair.getKey().lastIndexOf("/") + 1);
            zipOut.putNextEntry(new ZipEntry(pair.getKey()));
            if (!currentFileName.equals(fileName)) {
                zipOut.write(pair.getValue().toByteArray());
                zipOut.closeEntry();
            }
            pair.getValue().close();
        }

        zipOut.close();
    }
}
