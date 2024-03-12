package com.appverlag.kf.kftools.other;

import com.appverlag.kf.kftools.R;

public enum TextAppereance {

    BODY(R.style.KFTools_TextTextAppearance_Body),
    HEADLINE(R.style.KFTools_TextTextAppearance_Headline),
    TITLE1(R.style.KFTools_TextTextAppearance_Title1),
    TITLE2(R.style.KFTools_TextTextAppearance_Title2),
    TITLE3(R.style.KFTools_TextTextAppearance_Title3),
    CALLOUT(R.style.KFTools_TextTextAppearance_Callout),
    CAPTION1(R.style.KFTools_TextTextAppearance_Caption1),
    CAPTION2(R.style.KFTools_TextTextAppearance_Caption2),
    SUBHEADLINE(R.style.KFTools_TextTextAppearance_Subheadline),
    FOOTNOTE(R.style.KFTools_TextTextAppearance_Footnote);

    private final int value;

    TextAppereance(int value) {
        this.value = value;
    }
    public int getValue() {
        return value;
    }
}
