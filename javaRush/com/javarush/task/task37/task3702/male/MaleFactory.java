package com.javarush.task.task37.task3702.male;

import com.javarush.task.task37.task3702.AbstractFactory;
import com.javarush.task.task37.task3702.Human;

/**
 * Created by Ru on 29.03.2017.
 */
public class MaleFactory implements AbstractFactory{

    public Human getPerson(int age){
        Human newHuman = null;

        if (age > 19)
            newHuman = new Man();
        if (age > 12 && age <20)
            newHuman = new TeenBoy();
        if (age < 13)
            newHuman = new KidBoy();

        return newHuman;
    }
}
