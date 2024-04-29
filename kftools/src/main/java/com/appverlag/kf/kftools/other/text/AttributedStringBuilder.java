/*
 * Copyright (C) Kevin Flachsmann - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Created by kevinflachsmann on 29.4.2024.
 */

package com.appverlag.kf.kftools.other.text;

import android.content.Context;
import android.text.SpannableStringBuilder;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

public class AttributedStringBuilder {

    private final List<AttributedString> attributedStrings = new ArrayList<>();

    public AttributedStringBuilder() {

    }

    public AttributedStringBuilder append(AttributedString attributedString) {
        attributedStrings.add(attributedString);
        return this;
    }

    public AttributedStringBuilder append(CharSequence text, @NonNull AttributeContainer attributeContainer) {
        return append(new AttributedString(text, attributeContainer));
    }

    public AttributedStringBuilder append(CharSequence text, @NonNull AttributeContainer.Builder builder) {
        return append(text, builder.build());
    }

    public AttributedStringBuilder append(CharSequence text) {
        return append(new AttributedString(text));
    }

    public AttributedStringBuilder newline() {
        return append("\n");
    }

    public AttributedStringBuilder newline(int count) {
        for (int i = 0; i < count; i++) {
            newline();
        }
        return this;
    }

    public CharSequence getText(Context context) {
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();
        for (AttributedString attributedString : attributedStrings) {
            spannableStringBuilder.append(attributedString.getText(context));
        }
        return spannableStringBuilder;
    }
}
