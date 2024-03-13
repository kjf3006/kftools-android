package com.appverlag.kf.kftools.ui.widgets;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.appverlag.kf.kftools.R;
import com.appverlag.kf.kftools.images.ImageManager;
import com.appverlag.kf.kftools.images.MapSnapshotOptions;

import java.util.Locale;

public class MapPreview extends FrameLayout {

    private ImageView mMapImageView;
    private ImageView mAnnotationImageView;
    private MapSnapshotOptions mOptions;

    private boolean mOpensMapOnSelection = true;
    private String mTitle;
    private OnClickListener mOnClickListener;

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


    private void init() {
        View.inflate(getContext(), R.layout.kftools_map_preview, this);

        setBackgroundResource(R.drawable.kftools_widget_default_background);
        setClipToOutline(true);

        mMapImageView = findViewById(R.id.mapImageView);
        mAnnotationImageView = findViewById(R.id.annotationImageView);

        super.setOnClickListener(v -> {
            if (mOpensMapOnSelection) {
                openGoogleMaps();
            }
            if (mOnClickListener != null) {
                mOnClickListener.onClick(v);
            }
        });
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

    public void openGoogleMaps() {
        String uriString = String.format(Locale.US, "geo:0,0?q=%f,%f", mOptions.getLatitude(), mOptions.getLongitude());
        if (mTitle != null) {
            uriString += String.format("(%s)", mTitle);
        }
        Uri mapUri = Uri.parse(uriString);
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, mapUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        getContext().startActivity(mapIntent);
    }

    @Override
    public void setOnClickListener(@Nullable OnClickListener onClickListener) {
        mOnClickListener = onClickListener;
    }

    public boolean isOpensMapOnSelection() {
        return mOpensMapOnSelection;
    }

    /**
     * If true, the google maps app is opened on selection.
     */
    public void setOpensMapOnSelection(boolean opensMapOnSelection) {
        mOpensMapOnSelection = opensMapOnSelection;
    }

    public String getTitle() {
        return mTitle;
    }

    /**
     * The descriptive title associated with the coordinate.
     * The title will be used when constructing the map-link for opening the google maps when {@link #setOpensMapOnSelection() setOpensMapOnSelection()} is set to true.
     */
    public void setTitle(String title) {
        mTitle = title;
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
