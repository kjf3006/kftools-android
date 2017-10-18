package com.appverlag.kf.kftools.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;

import com.appverlag.kf.kftools.images.KFImageManager;
import com.appverlag.kf.kftools.images.KFImageManagerCompletionHandler;
import com.makeramen.roundedimageview.RoundedImageView;

/**
 * Created by kevinflachsmann on 11.03.16.
 */
public class KFRoundedImageView extends RoundedImageView {

    private String savedURL;

    public KFRoundedImageView(Context context) {
        super(context);
    }

    public KFRoundedImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public KFRoundedImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }


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
