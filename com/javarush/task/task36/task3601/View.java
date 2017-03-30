package com.javarush.task.task36.task3601;

import java.util.List;

/**
 * Created by Ru on 30.03.2017.
 */
public class View {
    List<String> onDataListShow = new Controller().onDataListShow();

    public void fireEventShowData() {
        System.out.println(onDataListShow);
    }
}
