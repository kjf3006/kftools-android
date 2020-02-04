package com.appverlag.kf.kftools.other;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.text.style.ImageSpan;

/**
 * Copyright (C) Kevin Flachsmann - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Created by kevinflachsmann on 2020-02-04.
 */
public class KFCenteredImageSpan extends ImageSpan {

    public KFCenteredImageSpan(Drawable drawable) {
        super(drawable);
    }

    @Override
    public void draw(Canvas canvas, CharSequence text, int start, int end, float x, int top, int y, int bottom, Paint paint) {
        Drawable b = getDrawable();
        canvas.save();

        int transY = bottom - b.getBounds().bottom;
        // this is the key
        transY -= paint.getFontMetricsInt().descent / 2;

        canvas.translate(x, transY);
        b.draw(canvas);
        canvas.restore();
    }
}
