package com.javarush.task.task37.task3702.female;

import com.javarush.task.task37.task3702.AbstractFactory;
import com.javarush.task.task37.task3702.Human;
import com.javarush.task.task37.task3702.male.TeenBoy;

/**
 * Created by Ru on 29.03.2017.
 */
public class FemaleFactory implements AbstractFactory {

    public Human getPerson(int age){
        Human newHuman = null;

        if (age > 19)
            newHuman = new Woman();
        if (age > 12 && age <20)
            newHuman = new TeenGirl();
        if (age < 13)
            newHuman = new KidGirl();

        return newHuman;
    }
}
