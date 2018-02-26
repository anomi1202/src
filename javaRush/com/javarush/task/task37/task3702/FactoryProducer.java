package com.javarush.task.task37.task3702;

import com.javarush.task.task37.task3702.female.FemaleFactory;
import com.javarush.task.task37.task3702.male.MaleFactory;

/**
 * Created by Ru on 29.03.2017.
 */
public class FactoryProducer {
    public static enum HumanFactoryType {MALE, FEMALE}

    public static AbstractFactory getFactory(HumanFactoryType humanFactoryType){
        AbstractFactory abstractFactory = null;

        switch (humanFactoryType){
            case MALE:
                abstractFactory = new MaleFactory();
                break;
            case FEMALE:
                abstractFactory = new FemaleFactory();
                break;
        }

        return abstractFactory;
    }
}
