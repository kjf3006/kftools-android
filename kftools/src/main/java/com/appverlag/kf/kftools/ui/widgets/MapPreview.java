package com.appverlag.kf.kftools.ui.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.appverlag.kf.kftools.R;
import com.appverlag.kf.kftools.images.ImageManager;
import com.appverlag.kf.kftools.images.MapSnapshotOptions;

public class MapPreview extends FrameLayout {

    private ImageView mMapImageView;
    private ImageView mAnnotationImageView;
    private MapSnapshotOptions mOptions;

    public MapPreview(@NonNull Context context) {
        super(context);
        init();
    }

    public MapPreview(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MapPreview(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public MapPreview(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        View.inflate(getContext(), R.layout.kftools_map_preview, this);
        mMapImageView = findViewById(R.id.mapImageView);
        mAnnotationImageView = findViewById(R.id.annotationImageView);

        setBackgroundResource(R.drawable.kftools_widget_default_background);
        setClipToOutline(true);
    }

    private void loadMapSnapShot() {
        if (mOptions == null) {
            mMapImageView.setImageBitmap(null);
            return;
        }
        ImageManager.getInstance().mapSnapshot(mOptions, response -> {
            if (response.success()) {
                mMapImageView.setImageBitmap(response.value);
            }
        });
    }

    public ImageView getMapImageView() {
        return mMapImageView;
    }

    public ImageView getAnnotationImageView() {
        return mAnnotationImageView;
    }

    public MapSnapshotOptions getOptions() {
        return mOptions;
    }

    public void setOptions(MapSnapshotOptions options) {
        this.mOptions = options;
        loadMapSnapShot();
    }
}
