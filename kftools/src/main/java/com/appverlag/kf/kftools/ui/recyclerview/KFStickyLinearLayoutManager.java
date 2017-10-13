//package com.appverlag.kf.kftools.ui.recyclerview;
//
//import android.content.Context;
//import android.support.v4.util.ArrayMap;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
//
//import java.util.List;
//
///**
// * Copyright (C) Kevin Flachsmann - All Rights Reserved
// * Unauthorized copying of this file, via any medium is strictly prohibited
// * Proprietary and confidential
// * Created by kevinflachsmann on 05.10.17.
// */
//public class KFStickyLinearLayoutManager extends LinearLayoutManager {
//
//    private ArrayMap<Integer, Integer> headerPositions;
//    private KFRecyclerViewAdapter adapter;
//
//    public KFStickyLinearLayoutManager(Context context) {
//        this(context, VERTICAL, false);
//    }
//
//    public KFStickyLinearLayoutManager(Context context, int orientation, boolean reverseLayout) {
//        super(context, orientation, reverseLayout);
//    }
//
//
//    @Override
//    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
//        super.onLayoutChildren(recycler, state);
//        cacheHeaderPositions();
//        if (positioner != null) {
//            runPositionerInit();
//        }
//    }
//
//    @Override
//    public int scrollVerticallyBy(int dy, RecyclerView.Recycler recycler, RecyclerView.State state) {
//        int scroll = super.scrollVerticallyBy(dy, recycler, state);
//        if (Math.abs(scroll) > 0) {
//            if (positioner != null) {
//                positioner.updateHeaderState(
//                        findFirstVisibleItemPosition(), getVisibleHeaders(), viewRetriever);
//            }
//        }
//        return scroll;
//    }
//
//    @Override
//    public int scrollHorizontallyBy(int dx, RecyclerView.Recycler recycler, RecyclerView.State state) {
//        int scroll = super.scrollHorizontallyBy(dx, recycler, state);
//        if (Math.abs(scroll) > 0) {
//            if (positioner != null) {
//                positioner.updateHeaderState(
//                        findFirstVisibleItemPosition(), getVisibleHeaders(), viewRetriever);
//            }
//        }
//        return scroll;
//    }
//
//    @Override public void removeAndRecycleAllViews(RecyclerView.Recycler recycler) {
//        super.removeAndRecycleAllViews(recycler);
//        if (positioner != null) {
//            positioner.clearHeader();
//        }
//    }
//
//    @Override
//    public void onAttachedToWindow(RecyclerView view) {
//        Preconditions.validateParentView(view);
//        viewRetriever = new RecyclerViewRetriever(view);
//        positioner = new StickyHeaderPositioner(view);
//        positioner.setElevateHeaders(headerElevation);
//        positioner.setListener(listener);
//        if (headerPositions.size() > 0) {
//            // Layout has already happened and header positions are cached. Catch positioner up.
//            positioner.setHeaderPositions(headerPositions);
//            runPositionerInit();
//        }
//        super.onAttachedToWindow(view);
//    }
//}
