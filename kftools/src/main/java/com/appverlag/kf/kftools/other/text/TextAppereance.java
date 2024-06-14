package com.appverlag.kf.kftools.other.text;

import com.appverlag.kf.kftools.R;

public enum TextAppereance {

    BODY(R.style.KFTools_TextAppearance_Body),
    HEADLINE(R.style.KFTools_TextAppearance_Headline),
    TITLE1(R.style.KFTools_TextAppearance_Title1),
    TITLE2(R.style.KFTools_TextAppearance_Title2),
    TITLE3(R.style.KFTools_TextAppearance_Title3),
    CALLOUT(R.style.KFTools_TextAppearance_Callout),
    CAPTION1(R.style.KFTools_TextAppearance_Caption1),
    CAPTION2(R.style.KFTools_TextAppearance_Caption2),
    SUBHEADLINE(R.style.KFTools_TextAppearance_Subheadline),
    FOOTNOTE(R.style.KFTools_TextAppearance_Footnote);

    private final int value;

    TextAppereance(int value) {
        this.value = value;
    }
    public int getValue() {
        return value;
    }
}
