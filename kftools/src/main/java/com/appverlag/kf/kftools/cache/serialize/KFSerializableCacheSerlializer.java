package com.appverlag.kf.kftools.cache.serialize;

import android.support.annotation.NonNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;

/**
 * Copyright (C) Kevin Flachsmann - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Created by kevinflachsmann on 02.08.20.
 */
public class KFSerializableCacheSerlializer implements KFCacheSerializer {

    @Override
    public Class<?> serializedClass() {
        return Serializable.class;
    }

    @Override
    public void toOutputStream(@NonNull OutputStream outputStream, @NonNull Object object) {
        ObjectOutputStream objectOutput = null;
        try {
            objectOutput = new ObjectOutputStream(outputStream);
            objectOutput.writeObject(object);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            try {
                if(objectOutput != null) {
                    objectOutput.flush();
                    objectOutput.close();
                }
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public Object fromInputStream(@NonNull InputStream inputStream) throws IOException, ClassNotFoundException{
        ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
        return objectInputStream.readObject();
    }
}
