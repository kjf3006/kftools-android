package com.appverlag.kf.kftools.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

/**
 * Copyright (C) Kevin Flachsmann - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Created by kevinflachsmann on 22.07.18.
 */
public class KFFixedAspectRatioFrameLayout extends FrameLayout {

    private float aspectRatio = 0;

    public KFFixedAspectRatioFrameLayout(Context context) {
        super(context);
    }

    public KFFixedAspectRatioFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public KFFixedAspectRatioFrameLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }



    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //   let the default measuring occur, then force the desired aspect ratio
        //   on the view (not the drawable).
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (aspectRatio != 0) {
            int width = getMeasuredWidth();
            int height = Math.round(width * aspectRatio);
            setMeasuredDimension(width, height);
        }
    }

    public float getAspectRatio() {
        return aspectRatio;
    }

    public void setAspectRatio(float aspectRatio) {
        this.aspectRatio = aspectRatio;
    }
}
