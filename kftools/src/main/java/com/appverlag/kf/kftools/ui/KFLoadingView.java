package com.appverlag.kf.kftools.ui;

import android.content.Context;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import com.appverlag.kf.kftools.R;

/**
 * Copyright (C) Kevin Flachsmann - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Created by kevinflachsmann on 02.03.17.
 */
public class KFLoadingView extends FrameLayout {

    private KFLoadingViewErrorListener listener;
    private ProgressBar progressBar;
    private View errorView;

    private boolean dynamiclyAdded;

    public KFLoadingView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView(context);
    }

    public KFLoadingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public KFLoadingView(Context context) {
        super(context);
        initView(context);
    }

    private void initView(Context context) {
        View.inflate(context, R.layout.kftools_loading_container, this);
        errorView = findViewById(R.id.errorView);
        View errorButton = findViewById(R.id.errorButton);
        progressBar = findViewById(R.id.progressBar);
        progressBar.getIndeterminateDrawable().setColorFilter(0xFF000000, PorterDuff.Mode.MULTIPLY);

        errorButton.setOnClickListener(view -> {
            errorView.setVisibility(GONE);
            progressBar.setVisibility(VISIBLE);
            if (listener != null) listener.onClick();
        });
        setVisibility(GONE);
    }

    public void showInView(ViewGroup viewGroup) {
        dynamiclyAdded = true;

        ViewGroup.LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        viewGroup.addView(this, layoutParams);
        showProgress();
    }

    public void showProgress() {
        setVisibility(VISIBLE);
        errorView.setVisibility(GONE);
        progressBar.setVisibility(VISIBLE);
    }

    public void hide() {
        setVisibility(GONE);

        if (dynamiclyAdded) {
            ((ViewGroup) getParent()).removeView(this);
        }
    }

    public void showError() {
        errorView.setVisibility(VISIBLE);
        progressBar.setVisibility(GONE);
    }

    public void setListener(KFLoadingViewErrorListener listener) {
        this.listener = listener;
    }

    public interface KFLoadingViewErrorListener {
        void onClick();
    }

}
