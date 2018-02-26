package com.javarush.task.task36.task3601;

import java.util.List;

/**
 * Created by Ru on 30.03.2017.
 */
public class Controller {
    List<String> getStringDataList = new Model().getStringDataList();

    public List<String> onDataListShow() {
        return getStringDataList;
    }
}
