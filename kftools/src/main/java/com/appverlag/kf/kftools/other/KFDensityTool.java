package com.appverlag.kf.kftools.other;

import android.content.res.Resources;
import android.util.TypedValue;

/**
 * Copyright (C) Kevin Flachsmann - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Created by kevinflachsmann on 2020-02-06.
 */
public class KFDensityTool {

    public static int dpToPx(int dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }

    public static int pxToDp(int px) {
        return (int) (px / Resources.getSystem().getDisplayMetrics().density);
    }

    public static int spToPx(int sp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, Resources.getSystem().getDisplayMetrics());
    }

    public static int dpToSp(int dp) {
        return (int) (dpToPx(dp) / Resources.getSystem().getDisplayMetrics().scaledDensity);
    }
}
