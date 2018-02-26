package com.javarush.task.task36.task3601;

import java.util.List;

/**
 * Created by Ru on 30.03.2017.
 */
public class Model {
    List<String> getData = new Service().getData();

    public List<String> getStringDataList() {
        return getData;
    }


}
