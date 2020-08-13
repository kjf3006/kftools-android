package com.appverlag.kf.kftools.ui;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ShapeDrawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.appverlag.kf.kftools.images.KFImageContainer;

import java.util.List;

/**
 * Copyright (C) Kevin Flachsmann - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Created by kevinflachsmann on 06.07.18.
 */
public class KFMultipleImageView extends FrameLayout {

    private LinearLayout container;
    private OnImageClickListener onImageClickListener;

    public KFMultipleImageView(Context context) {
        super(context);
        setupView();
    }

    public KFMultipleImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setupView();
    }

    public KFMultipleImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setupView();
    }

    private void setupView() {
        container = new LinearLayout(getContext());
        container.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        addView(container,layoutParams);
    }

    public void setImages(List<KFImageContainer> images) {
        container.removeAllViews();

        int px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, getResources().getDisplayMetrics());

        ShapeDrawable divider = new ShapeDrawable();
        divider.setIntrinsicWidth(px);
        divider.setIntrinsicHeight(px);
        divider.getPaint().setColor(Color.TRANSPARENT);

        container.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
        container.setDividerDrawable(divider);

        LinearLayout containerView = null;
        for (int i = 0; i < images.size(); i++) {
            if (i%2 == 0) {
                containerView = new LinearLayout(getContext());
                containerView.setOrientation(images.size() < 3 ? LinearLayout.VERTICAL : LinearLayout.HORIZONTAL);
                containerView.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
                containerView.setDividerDrawable(divider);

                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                container.addView(containerView,layoutParams);
            }

            KFImageView imageView = new KFImageView(getContext());
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setImage(images.get(i));
            imageView.setAspectRatio(0.56f);
            imageView.setTag(i);

            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onImageClickListener != null) {
                        onImageClickListener.onClick(v, (int) v.getTag());
                    }
                }
            });

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 1);
            containerView.addView(imageView, layoutParams);

        }
    }


    /*
    click handler
     */

    public interface OnImageClickListener {
        void onClick(View v, int index);
    }

    public OnImageClickListener getOnClickListener() {
        return onImageClickListener;
    }

    public void setOnImageClickListener(OnImageClickListener onImageClickListener) {
        this.onImageClickListener = onImageClickListener;
    }
}
