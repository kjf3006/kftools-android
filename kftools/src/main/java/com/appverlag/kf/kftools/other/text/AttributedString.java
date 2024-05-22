/*
 * Copyright (C) Kevin Flachsmann - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Created by kevinflachsmann on 29.4.2024.
 */

package com.appverlag.kf.kftools.other.text;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableString;

import androidx.annotation.NonNull;

public class AttributedString {

    @NonNull
    private AttributeContainer attributeContainer;

    private final CharSequence text;

    public AttributedString(CharSequence text) {
        this.text = text;
        this.attributeContainer = new AttributeContainer();
    }

    public AttributedString(CharSequence text, @NonNull AttributeContainer attributeContainer) {
        this.text = text;
        this.attributeContainer = attributeContainer;
    }

    public AttributedString(CharSequence text, @NonNull AttributeContainer.Builder builder) {
        this.text = text;
        this.attributeContainer = builder.build();
    }

    @NonNull
    public AttributeContainer getAttributeContainer() {
        return attributeContainer;
    }

    public void setAttributeContainer(@NonNull AttributeContainer attributeContainer) {
        this.attributeContainer = attributeContainer;
    }

    public CharSequence getText(Context context) {
        SpannableString spannableString = new SpannableString(text);
        for (Object span : attributeContainer.getSpans(context)) {
            spannableString.setSpan(span, 0, text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return spannableString;
    }
}
