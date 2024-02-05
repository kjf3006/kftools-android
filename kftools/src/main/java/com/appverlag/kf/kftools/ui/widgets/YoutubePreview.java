package com.appverlag.kf.kftools.ui.widgets;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.AttributeSet;
import android.util.Size;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;

import com.appverlag.kf.kftools.R;
import com.appverlag.kf.kftools.images.ImageManager;
import com.appverlag.kf.kftools.other.KFDensityTool;
import com.appverlag.kf.kftools.other.youtube.YoutubeVideo;

public class YoutubePreview extends FrameLayout {

    private YoutubeVideo mYoutubeVideo;

    private ImageView mThumbnailImageView;
    private TextView mTextViewTitle;
    private boolean mOpensVideoOnClick = true;
    private OnClickListener mOnClickListener;

    public YoutubePreview(@NonNull Context context) {
        super(context);
        init();
    }

    public YoutubePreview(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public YoutubePreview(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        View.inflate(getContext(), R.layout.kftools_youtube_preview, this);

        setBackgroundResource(R.drawable.kftools_widget_default_background);
        setClipToOutline(true);

        mThumbnailImageView = findViewById(R.id.thumbnailImageView);
        mTextViewTitle = findViewById(R.id.textViewTitle);
    }

    private void loadVideo() {
        if (mYoutubeVideo == null) {
            mThumbnailImageView.setImageBitmap(null);
            mTextViewTitle.setText(null);
            return;
        }

        int imageSize = KFDensityTool.dpToPx(500);
        ImageManager.getInstance().image(mYoutubeVideo.getThumbnailUrl(), new Size(imageSize, imageSize), response -> {
            if (response.success()) {
                mThumbnailImageView.setImageBitmap(response.value);
            }
        });

        mYoutubeVideo.getData(data -> {
            if (data != null) {
                mTextViewTitle.setText(data.getTitle());
            }
        });

        super.setOnClickListener(v -> {
            if (mOpensVideoOnClick) {
                openYoutube();
            }
            if (mOnClickListener != null) {
                mOnClickListener.onClick(v);
            }
        });
    }

    private void openYoutube() {
        Uri videoUri = Uri.parse(mYoutubeVideo.getYoutubeUrl());
        Intent intent = new Intent(Intent.ACTION_VIEW, videoUri);
        getContext().startActivity(intent);
    }

    @Override
    public void setOnClickListener(@Nullable OnClickListener onClickListener) {
        mOnClickListener = onClickListener;
    }

    public YoutubeVideo getYoutubeVideo() {
        return mYoutubeVideo;
    }

    public void setYoutubeVideo(YoutubeVideo youtubeVideo) {
        this.mYoutubeVideo = youtubeVideo;
        loadVideo();
    }

    public boolean isOpensVideoOnClick() {
        return mOpensVideoOnClick;
    }

    public void setOpensVideoOnClick(boolean opensVideoOnClick) {
        this.mOpensVideoOnClick = opensVideoOnClick;
    }
}
