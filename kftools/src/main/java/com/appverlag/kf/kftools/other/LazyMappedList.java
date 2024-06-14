package com.appverlag.kf.kftools.other;

import android.util.LruCache;

import java.util.AbstractList;
import java.util.List;
import java.util.function.Function;

/**
 * A unmodifiable list lazyly wrapping an other list and mapping elements with a provided function. The elements will be cached by default.
 */
public class LazyMappedList<T> extends AbstractList<T> {

    private static final class _MappedList<Base, Transformed> extends AbstractList<Transformed> {

        private final LruCache<Integer, Transformed> lruCache;

        private final List<Base> baseList;
        private final Function<Base, Transformed> function;

        _MappedList(List<Base> baseList, int cacheSize, Function<Base, Transformed> function) {
            this.lruCache = new LruCache<>(cacheSize);
            this.baseList = baseList;
            this.function = function;
        }

        @Override
        public Transformed get(int index) {
            synchronized (lruCache) {
                Transformed value = lruCache.get(index);
                if (value == null) {
                    value = function.apply(baseList.get(index));
                    lruCache.put(index, value);
                }
                return value;
            }
        }

        @Override
        public int size() {
            return baseList.size();
        }
    }

    private final List<T> baseList;

    public <Base> LazyMappedList(List<Base> baseList, Function<Base, T> function) {
        this(baseList, 60, function);
    }

    public <Base> LazyMappedList(List<Base> baseList, int cacheSize, Function<Base, T> function) {
        this.baseList = new _MappedList<>(baseList, cacheSize, function);
    }

    public LazyMappedList(List<T> baseList) {
        this.baseList = baseList;
    }

    @Override
    public T get(int index) {
        return baseList.get(index);
    }

    @Override
    public int size() {
        return baseList.size();
    }

}
