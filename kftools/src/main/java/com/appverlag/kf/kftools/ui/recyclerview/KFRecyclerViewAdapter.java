package com.appverlag.kf.kftools.ui.recyclerview;

import android.support.annotation.IntRange;
import android.support.annotation.RestrictTo;
import android.support.v4.util.ArrayMap;
import android.support.v7.widget.RecyclerView;

/**
 * Copyright (C) Kevin Flachsmann - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Created by kevinflachsmann on 04.10.17.
 */
public abstract class KFRecyclerViewAdapter<VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {

    protected static final int VIEW_TYPE_HEADER = -2;
    protected static final int VIEW_TYPE_ITEM = -1;
    private static final String LOG_TAG = "KFRecyclerViewAdapter";

    private final ArrayMap<Integer, Integer> headerLocationMap;

    public KFRecyclerViewAdapter() {
        this.headerLocationMap = new ArrayMap<>(0);
    }



    /*
    user functions
     */
    public abstract int getSectionCount();

    public abstract int getItemCount(int section);

    public abstract void onBindHeaderViewHolder(VH holder, int section);

    public abstract void onBindViewHolder(VH holder, KFIndexPath indexPath);



    ////*****
    private boolean isHeader(int position) {
        return headerLocationMap.get(position) != null;
    }



    private KFIndexPath indexPathForPosition(int position) {
        Integer absHeaderLoc = headerLocationMap.get(position);
        if (absHeaderLoc != null) {
            return new KFIndexPath(-1, -1);
        }
        Integer lastSectionIndex = -1;
        for (Integer sectionIndex : headerLocationMap.keySet()) {
            if (position > sectionIndex) {
                lastSectionIndex = sectionIndex;
            } else {
                break;
            }
        }
        return new KFIndexPath(position - lastSectionIndex - 1, headerLocationMap.get(lastSectionIndex));

    }

    public ArrayMap<Integer, Integer> getHeaderPositions() {
        return headerLocationMap;
    }


    @Override
    @Deprecated
    @RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
    public final int getItemCount() {
        int count = 0;
        headerLocationMap.clear();
        for (int s = 0; s < getSectionCount(); s++) {
            int itemCount = getItemCount(s);
            if (itemCount > 0) {
                headerLocationMap.put(count, s);
                count += itemCount + 1;
            }
        }
        return count;
    }



    /**
     *
     * @deprecated
     */
    @Override
    @Deprecated
    @RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
    public final int getItemViewType(int position) {
        if (isHeader(position)) {
            return getHeaderViewType(headerLocationMap.get(position));
        }
        else {
            return getItemViewType(indexPathForPosition(position));
        }
    }

    @IntRange(from = 0, to = Integer.MAX_VALUE)
    public int getHeaderViewType(int section) {
        //noinspection ResourceType
        return VIEW_TYPE_HEADER;
    }

    @IntRange(from = 0, to = Integer.MAX_VALUE)
    public int getItemViewType(KFIndexPath indexPath) {
        //noinspection ResourceType
        return VIEW_TYPE_ITEM;
    }


    /**
     *
     * @deprecated
     */
    @Override
    @Deprecated
    @RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
    public final void onBindViewHolder(VH holder, int position) {

        if (isHeader(position)) {
            onBindHeaderViewHolder(holder, headerLocationMap.get(position));
        }
        else {
            onBindViewHolder(holder, indexPathForPosition(position));
        }
    }

}
