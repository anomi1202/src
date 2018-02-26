package com.javarush.task.task33.task3310.strategy;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Created by Ru on 28.04.2017.
 */
public class FileBucket {
    private Path path;

    public FileBucket() {
        try {
            path = Files.createTempFile(null,null);
            Files.deleteIfExists(path);
            Files.createFile(path);
        } catch (IOException e) {}

        path.toFile().deleteOnExit();
    }

    /**
     * он должен возвращать размер файла на который указывает path.
     * @return
     */
    public long getFileSize() {
        long size = 0;
        try {
            size = Files.size(path);
        } catch (IOException e) {}

        return size;
    }

    /**
     * должен сериализовывать переданный entry в файл. Учти, каждый entry может содержать еще один entry.
     * @param entry
     */
    public void putEntry(Entry entry){
        try {
            ObjectOutputStream oos = new ObjectOutputStream(Files.newOutputStream(path));
            oos.writeObject(entry);
            oos.close();
        } catch (IOException e) {}
    }

    /**
     * должен забирать entry из файла. Если файл имеет нулевой размер, вернуть null.
     * @return
     */
    public Entry getEntry(){
        Entry entry = null;
        if (getFileSize() != 0) {
            try {
                ObjectInputStream ois = new ObjectInputStream(Files.newInputStream(path));
                entry = (Entry) ois.readObject();
            } catch (Exception e) {}
        }
        return entry;
    }
    /**
     * удалять файл на который указывает path.
     */
    public void remove(){
        try {
            Files.delete(path);
        } catch (IOException e) {}
    }
}
