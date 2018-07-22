package com.appverlag.kf.kftools.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;

import com.appverlag.kf.kftools.images.KFImageContainer;
import com.appverlag.kf.kftools.images.KFImageManager;
import com.appverlag.kf.kftools.images.KFImageManagerCompletionHandler;


/**
 * Created by kevinflachsmann on 10.03.16.
 */
public class KFImageView extends AppCompatImageView {

    public enum Size {SMALL, MEDIUM, LARGE};


    private String savedURL;
    private float aspectRatio = 0;

    public KFImageView(Context context) {
        super(context);
    }

    public KFImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public KFImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }


    /*
    URL
     */
    public void setImageWithURL(final String url, final Integer placeholder) {
        setImageWithURL(url, 0, 0, placeholder);
    }

    public void setImageWithURL(final String url, final Bitmap placeholder) {
        setImageWithURL(url, 0, 0, placeholder);
    }

    public void setImageWithURL(final String url, final int desiredWidth, final int desiredHeight, final Integer placeholder) {
        Bitmap placeholderBitmap = null;
        if (placeholder != 0) {
            placeholderBitmap = BitmapFactory.decodeResource(getResources(), placeholder);
        }
        setImageWithURL(url, desiredWidth, desiredHeight, placeholderBitmap);
    }

    public void setImageWithURL(final String url, final int desiredWidth, final int desiredHeight, final Bitmap placeholder) {
        if (placeholder == null) {
            setImageBitmap(getPlaceholderImage());
        }
        else setImageBitmap(placeholder);

        savedURL = url;

        KFImageManager.getInstance(getContext()).imageForURL(url, desiredWidth, desiredHeight, new KFImageManagerCompletionHandler() {
            @Override
            public void onComplete(Bitmap bitmap) {
                if (url == null || !url.equals(savedURL) || bitmap == null) return;
                setImageBitmap(bitmap);
            }
        });

    }


    /*
    MAP
     */
    public void setMapSnapshotForOptions(final double latitude, final double longitude, final Integer placeholder) {
        setMapSnapshotForOptions(latitude, longitude, false, 0, placeholder);
    }

    public void setMapSnapshotForOptions(final double latitude, final double longitude, final Integer annotationImage, final Integer placeholder) {
        setMapSnapshotForOptions(latitude, longitude, false, annotationImage, placeholder);
    }

    public void setMapSnapshotForOptions(final double latitude, final double longitude, boolean satellite, final Integer annotationImage, final Integer placeholder) {
        if (placeholder == 0) {
            setImageBitmap(getPlaceholderImage());
        }
        else setImageResource(placeholder);

        final String identifier = "ms_" + latitude + "-" + longitude + "_" + annotationImage + "_" + satellite;
        savedURL = identifier;

        Bitmap annotationPin = null;
        if (annotationImage != 0) {
            annotationPin = BitmapFactory.decodeResource(getResources(), annotationImage);
        }

        KFImageManager.getInstance(getContext()).mapSnapshotForOptions(latitude, longitude, satellite, annotationPin, new KFImageManagerCompletionHandler() {
            @Override
            public void onComplete(Bitmap bitmap) {
                if (!identifier.equals(savedURL) || bitmap == null) return;
                setImageBitmap(bitmap);
            }
        });
    }

    /*
    YOUTUBE
     */
    public void setImageWithYoutubeID(final String youtubeID, final Integer placeholder) {
        String url = "https://img.youtube.com/vi/" + youtubeID + "/hqdefault.jpg";
        setImageWithURL(url, placeholder);
    }
    public void setImageWithYoutubeID(final String youtubeID, final Bitmap placeholder) {
        String url = "https://img.youtube.com/vi/" + youtubeID + "/hqdefault.jpg";
        setImageWithURL(url, placeholder);
    }


    /*
    LOCAL
     */

    public void setImageWithKey(final String key, final Integer placeholder) {
        setImageWithKey(key, 0, 0, placeholder);
    }

    public void setImageWithKey(final String key, final Bitmap placeholder) {
        setImageWithKey(key, 0, 0, placeholder);
    }

    public void setImageWithKey(final String key, final int desiredWidth, final int desiredHeight, final Integer placeholder) {
        Bitmap placeholderBitmap = null;
        if (placeholder != 0) {
            placeholderBitmap = BitmapFactory.decodeResource(getResources(), placeholder);
        }
        setImageWithKey(key, desiredWidth, desiredHeight, placeholderBitmap);
    }


    public void setImageWithKey(final String key, final int desiredWidth, final int desiredHeight, final Bitmap placeholder) {
        if (placeholder == null) {
            setImageBitmap(getPlaceholderImage());
        }
        else setImageBitmap(placeholder);

        savedURL = key;

        KFImageManager.getInstance(getContext()).getImage(key, desiredWidth, desiredHeight, new KFImageManagerCompletionHandler() {
            @Override
            public void onComplete(Bitmap bitmap) {
                if (key == null || !key.equals(savedURL) || bitmap == null) return;
                setImageBitmap(bitmap);
            }
        });

    }


    /*
    container
     */

    public void setImage(KFImageContainer container, Integer placeholder, Size size) {
        switch (container.getType()) {
            case KEY:
                setImageWithKey(container.getKey(), placeholder);
                break;
            case URL:
                setImageWithURL(container.getUrl(), placeholder);
                break;
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        //loadImageIfNecessary(true, null);
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


    private static Bitmap placeholder;
    private Bitmap getPlaceholderImage() {
        if (placeholder == null) {
            Bitmap bitmap = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            Paint paint = new Paint();
            paint.setColor(Color.parseColor("#E9E9E9"));
            canvas.drawRect(0F, 0F, (float) 100, (float) 100, paint);
            placeholder = bitmap;
        }
        return placeholder;
    }
}
