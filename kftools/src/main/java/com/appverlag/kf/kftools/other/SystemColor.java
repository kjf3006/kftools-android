package com.appverlag.kf.kftools.other;

import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;

import com.appverlag.kf.kftools.R;
import com.appverlag.kf.kftools.framework.ContextProvider;

public enum SystemColor {

    LABEL(R.color.labelColor),
    SECONDARY_LABEL(R.color.secondaryLabelColor),
    TERTIARY_LABEL(R.color.tertiaryLabelColor),
    QUATERNARY_LABEL(R.color.quaternaryLabelColor),
    SYSTEM_RED(R.color.systemRedColor),
    SYSTEM_ORANGE(R.color.systemOrangeColor),
    SYSTEM_YELLOW(R.color.systemYellowColor),
    SYSTEM_GREEN(R.color.systemGreenColor),
    SYSTEM_BLUE(R.color.systemBlueColor);

    private final @ColorInt int value;

    SystemColor(@ColorRes int value) {
        this.value = ContextProvider.getApplicationContext().getResources().getColor(value);
    }
    @ColorInt
    public int getValue() {
        return value;
    }
}
