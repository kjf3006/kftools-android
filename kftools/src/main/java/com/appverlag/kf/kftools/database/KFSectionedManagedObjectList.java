package com.appverlag.kf.kftools.database;

import android.database.Cursor;
import android.util.SparseArray;

import java.util.List;

/**
 * Copyright (C) Kevin Flachsmann - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Created by kevinflachsmann on 18.05.17.
 */
public class KFSectionedManagedObjectList<T extends KFManagedObject> {

    private Class<T> clazz;
    private SparseArray<KFManagedObjectList<T>> sectionObjects;
    private List<String> sectionTitles;
    private List<Cursor> sectionContent;

    public KFSectionedManagedObjectList(List<String> sectionTitles, List<Cursor> sectionContent, Class<T> clazz) {
        this.sectionTitles = sectionTitles;
        this.sectionContent = sectionContent;
        this.clazz = clazz;
        this.sectionObjects = new SparseArray<>();

    }


    public T getObject(int index, int section) {
        return getSectionList(section).getObject(index);
    }

    public int getSectionCount() {
        return sectionTitles.size();
    }

    public String getTitleForSection(int section) {
        return sectionTitles.get(section);
    }

    public int getCount(int section) {
        return getSectionList(section).getCount();
    }

    public KFManagedObjectList<T> getSectionList(int section) {
        KFManagedObjectList<T> sectionList = sectionObjects.get(section);
        if (sectionList == null) {
            sectionList = new KFManagedObjectList<>(sectionContent.get(section), clazz);
            sectionObjects.put(section, sectionList);
        }

        return sectionList;
    }

    public void close() {
        for (Cursor cursor : sectionContent) {
            cursor.close();
        }
    }

    @Override
    public void finalize() {
        close();
        try {
            super.finalize();
        }
        catch (Throwable e) {
            e.printStackTrace();
        }
    }
}
