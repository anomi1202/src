package com.javarush.task.task33.task3304;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

/* 
Конвертация из одного класса в другой используя JSON
*/
public class Solution {
    public static void main(String[] args) throws IOException {
        Second s = (Second) convertOneToAnother(new First(), Second.class);
        System.out.println(String.format("Second s = %d %s", s.i, s.name));
        First f = (First) convertOneToAnother(new Second(), First.class);
        System.out.println(String.format("First f = %d %s", f.i, f.name));
    }

    public static Object convertOneToAnother(Object one, Class resultClassObject) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        StringWriter stringWr = new StringWriter();
        mapper.writeValue(stringWr, one);

        String strOut = stringWr.toString();
        String classNameOne = one.getClass().getSimpleName().toLowerCase();
        String classNameTwo = resultClassObject.getSimpleName().toLowerCase();
        strOut = strOut.replaceFirst(classNameOne, classNameTwo);

        StringReader stringRd = new StringReader(strOut);
        return mapper.readValue(stringRd, resultClassObject);
    }

    @JsonTypeInfo(use = JsonTypeInfo.Id.NAME,  property="className")
    @JsonSubTypes(@JsonSubTypes.Type(value=First.class,  name="first"))
    public static class First {
        public int i = 1;
        public String name = "name first";
    }

    @JsonTypeInfo(use = JsonTypeInfo.Id.NAME,  property="className")
    @JsonSubTypes(@JsonSubTypes.Type(value=Second.class, name="second"))
    public static class Second {
        public int i = 2;
        public String name = "name second";
    }
}
