package com.appverlag.kf.kftools.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;

import com.appverlag.kf.kftools.network.KFImageManager;


/**
 * Created by kevinflachsmann on 10.03.16.
 */
public class KFImageView extends AppCompatImageView {

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
        if (placeholder == 0) {
            setImageBitmap(getPlaceholderImage());
        }
        else setImageResource(placeholder);

        savedURL = url;

        KFImageManager.getInstance(getContext()).imageForURL(url, new KFImageManager.KFImageManagerCompletionHandler() {
            @Override
            public void onComplete(Bitmap bitmap) {
                if (url == null || !url.equals(savedURL) || bitmap == null) return;
                setImageBitmap(bitmap);
            }
        });

    }

    public void setImageWithURL(final String url, final Bitmap placeholder) {
        if (placeholder == null) {
            setImageBitmap(getPlaceholderImage());
        }
        else setImageBitmap(placeholder);

        savedURL = url;

        KFImageManager.getInstance(getContext()).imageForURL(url, new KFImageManager.KFImageManagerCompletionHandler() {
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
        setMapSnapshotForOptions(latitude, longitude, 0, placeholder);
    }

    public void setMapSnapshotForOptions(final double latitude, final double longitude, final Integer annotationImage, final Integer placeholder) {
        if (placeholder == 0) {
            setImageBitmap(getPlaceholderImage());
        }
        else setImageResource(placeholder);

        final String identifier = "ms_" + latitude + "-" + longitude + "_" + annotationImage;
        savedURL = identifier;

        Bitmap annotationPin = BitmapFactory.decodeResource(getResources(), annotationImage);

        KFImageManager.getInstance(getContext()).mapSnapshotForOptions(latitude, longitude, annotationPin, new KFImageManager.KFImageManagerCompletionHandler() {
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



    @Override protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
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
