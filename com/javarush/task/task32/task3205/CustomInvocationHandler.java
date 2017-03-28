package com.javarush.task.task32.task3205;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * Created by Ru on 11.03.2017.
 */
public class CustomInvocationHandler implements InvocationHandler {
    SomeInterfaceWithMethods someInterfaceWithMethodsOrigonal;

    public CustomInvocationHandler(SomeInterfaceWithMethods someInterfaceWithMethods) {
        this.someInterfaceWithMethodsOrigonal = someInterfaceWithMethods;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        System.out.println(method.getName() + " in");
        method.invoke(someInterfaceWithMethodsOrigonal, args);
        System.out.println(method.getName() + " out");
        return method.invoke(someInterfaceWithMethodsOrigonal, args);
    }
}
