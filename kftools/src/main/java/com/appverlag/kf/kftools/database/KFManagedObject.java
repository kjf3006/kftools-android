package com.appverlag.kf.kftools.database;

import android.content.ContentValues;
import android.database.Cursor;

/**
 * Copyright (C) Kevin Flachsmann - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Created by kevinflachsmann on 15.05.17.
 */
public abstract class KFManagedObject {

    protected long id;

    protected abstract void onCreate(Cursor cursor);
    protected abstract ContentValues getContentValues();

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        return String.valueOf(id).hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof KFManagedObject &&(obj == this || ((KFManagedObject) obj).getId() == id);
    }
}
