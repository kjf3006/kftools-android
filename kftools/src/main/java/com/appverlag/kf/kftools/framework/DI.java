package com.appverlag.kf.kftools.framework;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.HashMap;

public class DI {

    private static final HashMap<Class<?>, Object> dependencies = new HashMap<>();

    public static void set(@NonNull Object instance) {
        set(instance.getClass(), instance);
    }

    public static void set(@NonNull Class<?> clazz, @NonNull Object instance) {
        if (!clazz.isInstance(instance)) {
            throw new RuntimeException("Object " + instance.getClass() + " not instance of " + clazz);
        }
        dependencies.put(clazz, instance);
    }

    @SuppressWarnings("unchecked")
    @Nullable
    public static <T> T opt(Class<T> clazz) {
        Object dependency = dependencies.get(clazz);
        if (dependency != null && clazz.isInstance(dependency)) {
            return (T) dependency;
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    @NonNull
    public static <T> T get(Class<T> clazz) {
        Object dependency = dependencies.get(clazz);
        if (dependency != null && clazz.isInstance(dependency)) {
            return (T) dependency;
        }
        throw new RuntimeException("No object of type " + clazz + " registered. Use DI.set(...) to register an object.");
    }
}
