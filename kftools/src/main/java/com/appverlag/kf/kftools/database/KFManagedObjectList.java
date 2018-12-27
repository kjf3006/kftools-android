package com.appverlag.kf.kftools.database;

import android.database.Cursor;
import android.util.SparseArray;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Copyright (C) Kevin Flachsmann - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Created by kevinflachsmann on 15.05.17.
 */
public class KFManagedObjectList<T extends KFManagedObject> implements Iterable<T> {

    private Class<T> clazz;
    private SparseArray<T> objects;
    private Cursor cursor;

    public KFManagedObjectList(Cursor cursor, Class<T> clazz) {
        this.objects = new SparseArray<>();
        this.cursor = cursor;
        this.clazz = clazz;
    }

    public T getObject(int index) {
        if (index >= getCount()) return null;
        T object = objects.get(index, null);
        if(object == null) {
            cursor.moveToPosition(index);
            try {
                object = clazz.newInstance();
                object.onCreate(cursor);
                objects.put(index, object);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            if (objects.size() == getCount()) close();
        }

        return object;
    }

    public int getCount() {
        return cursor.getCount();
    }

    public List<T> getAllObjects() {
        List<T> objects = new ArrayList<>();
        for (int i = 0; i < getCount(); i++) {
            objects.add(getObject(i));
        }
        close();
        return objects;
    }

    public void close() {
        cursor.close();
    }

    @Override
    public void finalize() {
        cursor.close();
        try {
            super.finalize();
        }
        catch (Throwable e) {
            e.printStackTrace();
        }
    }

    @Override
    public Iterator<T> iterator() {
        return new KFManagedObjectListIterator();
    }

    private class KFManagedObjectListIterator implements Iterator<T> {

        private int index = 0;

        public boolean hasNext() {
            return index < getCount();
        }

        public T next() {
            return getObject(index++);
        }

        public void remove() {
            throw new UnsupportedOperationException("not supported yet");
        }

    }
}
