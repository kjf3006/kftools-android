package com.appverlag.kf.kftools.framework;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class DI {

//    private static final DI defaultInstance = new DI();


    private static final Map<Class<?>, Object> dependencies = new ConcurrentHashMap<>();
    private static final Map<Class<?>, Factory> factories = new ConcurrentHashMap<>();

    private DI() { }

    public static void set(@NonNull Object instance) {
        set(instance.getClass(), instance);
    }

    public static void set(@NonNull Class<?> clazz, @NonNull Object instance) {
        if (!clazz.isInstance(instance)) {
            throw new RuntimeException("Object " + instance.getClass() + " not instance of " + clazz);
        }
        dependencies.put(clazz, instance);
    }

    public static void set(@NonNull Class<?> clazz, @NonNull Factory factory) {
       factories.put(clazz, factory);
    }

    @Nullable
    public static <T> T opt(Class<T> clazz) {
        return resolve(clazz);
    }

    @NonNull
    public static <T> T get(Class<T> clazz) {
        T dependency = opt(clazz);
        if (dependency == null) {
            throw new RuntimeException("No object of type " + clazz + " registered. Use DI.set(...) to register an object.");
        }
        return dependency;
    }

    @Nullable
    private static <T> T resolve(Class<T> clazz) {
        T dependency = resolveObject(clazz);
        return dependency != null ? dependency : resolveFactory(clazz);
    }

    @SuppressWarnings("unchecked")
    @Nullable
    private static <T> T resolveObject(Class<T> clazz) {
        Object dependency = dependencies.get(clazz);
        if (dependency != null && clazz.isInstance(dependency)) {
            return (T) dependency;
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    @Nullable
    private static <T> T resolveFactory(Class<T> clazz) {
        Factory factory = factories.get(clazz);
        if (factory == null) {
            return null;
        }

        Object dependency = factory.provide();
        if (dependency != null && clazz.isInstance(dependency)) {
            return (T) dependency;
        }
        return null;
    }

    public interface Factory {
        Object provide();
    }

}
