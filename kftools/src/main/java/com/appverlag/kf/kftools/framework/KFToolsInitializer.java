package com.appverlag.kf.kftools.framework;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.startup.Initializer;

import java.util.Collections;
import java.util.List;

public final class KFToolsInitializer implements Initializer<Object> {
    @NonNull
    @Override
    public Object create(@NonNull Context context) {
        ContextProvider.initialize(context);
        return new Object();
    }

    @NonNull
    @Override
    public List<Class<? extends Initializer<?>>> dependencies() {
        return Collections.emptyList();
    }
}
