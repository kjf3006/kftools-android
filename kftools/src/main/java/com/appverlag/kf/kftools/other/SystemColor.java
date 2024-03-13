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

    PLACEHOLDER_TEXT(R.color.placeholderTextColor),

    SYSTEM_FILL(R.color.systemFillColor),
    SECONDARY_SYSTEM_FILL(R.color.secondarySystemFillColor),
    TERTIARY_SYSTEM_FILL(R.color.tertiarySystemFillColor),
    QUATERNARY_SYSTEM_FILL(R.color.quarernarySystemFillColor),

    SYSTEM_BACKGROUND(R.color.systemBackgroundColor),
    SECONDARY_SYSTEM_BACKGROUND(R.color.secondarySystemBackgroundColor),
    TERTIARY_SYSTEM_BACKGROUND(R.color.tertiarySystemBackgroundColor),

    SYSTEM_GROUPED_BACKGROUND(R.color.systemGroupedBackgroundColor),
    SECONDARY_GROUPED_SYSTEM_BACKGROUND(R.color.secondarySystemGroupedBackgroundColor),
    TERTIARY_GROUPED_SYSTEM_BACKGROUND(R.color.tertiarySystemGroupedBackgroundColor),

    SYSTEM_GRAY(R.color.systemGrayColor),
    SYSTEM_GRAY2(R.color.systemGray2Color),
    SYSTEM_GRAY3(R.color.systemGray3Color),
    SYSTEM_GRAY4(R.color.systemGray4Color),
    SYSTEM_GRAY5(R.color.systemGray5Color),
    SYSTEM_GRAY6(R.color.systemGray6Color),

    SEPARATOR(R.color.separatorColor),
    OPAQUE_SEPARATOR(R.color.opaqueSeparatorColor),

    LINK(R.color.linkColor),

    SYSTEM_RED(R.color.systemRedColor),
    SYSTEM_ORANGE(R.color.systemOrangeColor),
    SYSTEM_YELLOW(R.color.systemYellowColor),
    SYSTEM_GREEN(R.color.systemGreenColor),
    SYSTEM_BLUE(R.color.systemBlueColor),

    BLACK(R.color.blackColor),
    WHITE(R.color.whiteColor),
    CLEAR(R.color.clearColor),
    RED(R.color.redColor);

    private final @ColorRes int value;

    SystemColor(@ColorRes int value) {
        this.value = value;
    }

    @ColorInt
    public int getValue() {
        return ContextProvider.getApplicationContext().getResources().getColor(value);
    }
}
