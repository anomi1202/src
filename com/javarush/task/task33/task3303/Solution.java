package com.javarush.task.task33.task3303;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;

/* 
Десериализация JSON объекта
*/
public class Solution {
    public static <T> T convertFromJsonToNormal(String fileName, Class<T> clazz) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(new FileReader(new File(fileName)), clazz);
    }

    public static void main(String[] args) throws IOException {
        String fileName = "C:\\Users\\Ru\\Desktop\\JavaRushHomeWork\\JavaRushTasks\\4.JavaCollections\\src\\com\\javarush\\task\\task33\\task3303\\test";
        Cat cat = convertFromJsonToNormal(fileName, Cat.class);
        System.out.println(cat);
    }

    //{"name":"Murka","age":5,"weight":3}
    public static class Cat{
        public Cat(){}
        public String name;
        public int age;
        public int weight;

        @Override
        public String toString() {
            return "Cat{" +
                    "name='" + name + '\'' +
                    ", age=" + age +
                    ", weight=" + weight +
                    '}';
        }
    }
}
