package com.appverlag.kf.kftools.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;

import com.appverlag.kf.kftools.network.KFImageManager;


/**
 * Created by kevinflachsmann on 10.03.16.
 */
public class KFImageView extends AppCompatImageView {

    private static final String GOOGLE_MAPS_URL = "https://maps.googleapis.com/maps/api/staticmap";

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

    public void setMapSnapshotForOptions(final double latitude, final double longitude, final Integer placeholder) {
        String url = GOOGLE_MAPS_URL + "?center=" + latitude + "," + longitude + "&zoom=18&size=640x640";
        setImageWithURL(url, placeholder);
    }

    public void setImageWithYoutubeID(final String youtubeID, final Integer placeholder) {
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
