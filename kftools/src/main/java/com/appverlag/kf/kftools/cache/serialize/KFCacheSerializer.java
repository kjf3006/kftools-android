package com.appverlag.kf.kftools.cache.serialize;

import android.support.annotation.NonNull;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Copyright (C) Kevin Flachsmann - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Created by kevinflachsmann on 02.08.20.
 */
public interface KFCacheSerializer {

    Class<?> serializedClass();
    void toOutputStream(@NonNull OutputStream outputStream, @NonNull Object object);
    Object fromInputStream(@NonNull InputStream inputStream) throws IOException, ClassNotFoundException;
}
