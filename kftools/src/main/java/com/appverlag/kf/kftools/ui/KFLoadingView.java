package com.appverlag.kf.kftools.ui;

import android.content.Context;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.view.View;
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
        ((ProgressBar) findViewById(R.id.progressBar)).getIndeterminateDrawable().setColorFilter(0xFF000000, PorterDuff.Mode.MULTIPLY);

        findViewById(R.id.errorButton).setOnClickListener(view -> {
            findViewById(R.id.errorView).setVisibility(GONE);
            findViewById(R.id.progressBar).setVisibility(VISIBLE);
            if (listener != null) listener.onClick();
        });
        setVisibility(GONE);
    }


    public void showProgress() {
        setVisibility(VISIBLE);
        findViewById(R.id.errorView).setVisibility(GONE);
        findViewById(R.id.progressBar).setVisibility(VISIBLE);
    }

    public void hide() {
        setVisibility(GONE);
    }

    public void showError() {
        findViewById(R.id.errorView).setVisibility(VISIBLE);
        findViewById(R.id.progressBar).setVisibility(GONE);
    }

    public void setListener(KFLoadingViewErrorListener listener) {
        this.listener = listener;
    }

    public interface KFLoadingViewErrorListener {
        void onClick();
    }

}
