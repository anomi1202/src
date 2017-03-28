package com.javarush.task.task37.task3707;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.*;

/**
 * Created by Ru on 27.03.2017.
 */
public class AmigoSet<E> extends AbstractSet<E> implements Serializable, Cloneable, Set<E> {
    private static final Object PRESENT = new Object();
    private transient HashMap<E,Object> map;

    public AmigoSet() {
        this.map = new HashMap<>();
    }

    @Override
    public Object clone() {
        try {
            AmigoSet<E> copyAmigoSet = (AmigoSet<E>) super.clone();
            copyAmigoSet.map = (HashMap<E, Object>) map.clone();
            return copyAmigoSet;
        }
        catch (Exception e){
            throw new InternalError();
        }
    }

    public AmigoSet(Collection<? extends E> collection){
        this.map = new HashMap<E, Object>(Math.max(16,(int)(collection.size()/.75F + 1)));
        this.addAll(collection);
    }


    @Override
    public boolean add(E e) {
        return null == map.put((E)e,PRESENT);
    }

    @Override
    public Iterator<E> iterator() {
        Iterator<E> iterator = map.keySet().iterator();
        return iterator;
    }

    @Override
    public int size() {
        return map.size();
    }

    @Override
    public boolean isEmpty() {
        return map.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return map.containsKey(o);
    }

    @Override
    public void clear() {
        map.clear();
    }

    @Override
    public boolean remove(Object o) {
        return null == map.remove(o);
    }

    private void writeObject(ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
        out.writeInt((int)HashMapReflectionHelper.callHiddenMethod(map, "capacity"));
        out.writeFloat((float) HashMapReflectionHelper.callHiddenMethod(map, "loadFactor"));
        out.writeInt(map.size());

        for (Map.Entry<E, Object> pair : map.entrySet()) {
            out.writeObject(pair.getKey());
        }
    }



    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        int capacity = in.readInt();
        float loadFactor = in.readFloat();
        int mapSize = in.readInt();

        map = new HashMap<E, Object>(capacity, loadFactor);

        for (int i = 0; i < mapSize; i++) {
            map.put((E) in.readObject(), PRESENT);
        }
    }
}
