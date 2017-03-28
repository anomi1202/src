package com.javarush.task.task33.task3308;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Ru on 23.03.2017.
 */
@XmlRootElement
@XmlType(name = "shop")
public class Shop {

    public int count;
    public double profit;
    public String[] secretData;
    public Goods goods = new Goods();

    @XmlRootElement
    @XmlType(name = "goods")
    public static class Goods{
        public List<String> names;
    }

    @Override
    public String toString() {
        return "Shop{" +
                "count=" + count +
                ", profit=" + profit +
                ", secretData=" + Arrays.toString(secretData) +
                ", goods=" + goods.names.toString() +
                '}';
    }
}
