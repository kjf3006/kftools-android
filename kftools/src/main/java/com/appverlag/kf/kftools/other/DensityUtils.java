package com.appverlag.kf.kftools.other;

import android.content.res.Resources;
import android.util.TypedValue;

/**
 * Copyright (C) Kevin Flachsmann - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Created by kevinflachsmann on 2020-02-06.
 */
public class DensityUtils {

    /**
     * Convert density-independent pixels to pixels.
     * @param dp Density-independent pixels
     * @return corresponding pixels on screen
     */
    public static int dpToPx(int dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }

    /**
     * Convert pixels to density-independent pixels.
     * @param px pixels
     * @return Corresponding density-independent pixel value
     */
    public static int pxToDp(int px) {
        return (int) (px / Resources.getSystem().getDisplayMetrics().density);
    }

    /**
     * Convert scale-independent pixels to pixels.
     * @param sp Scale-independent pixels
     * @return Corresponding pixels on screen
     */
    public static int spToPx(int sp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, Resources.getSystem().getDisplayMetrics());
    }

    /**
     * Convert density-independent pixels to scale-independent pixels.
     * @param dp Density-independent pixels
     * @return Corresponding scale-independent pixel value
     */
    public static int dpToSp(int dp) {
        return (int) (dpToPx(dp) / Resources.getSystem().getDisplayMetrics().scaledDensity);
    }
}
