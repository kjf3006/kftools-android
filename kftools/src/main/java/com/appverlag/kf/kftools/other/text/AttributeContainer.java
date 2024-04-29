/*
 * Copyright (C) Kevin Flachsmann - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Created by kevinflachsmann on 29.4.2024.
 */

package com.appverlag.kf.kftools.other.text;

import android.content.Context;
import android.graphics.Typeface;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.text.style.TextAppearanceSpan;

import androidx.annotation.ColorRes;

import java.util.ArrayList;
import java.util.List;

public class AttributeContainer {
    @ColorRes private int foregroundColor;
    @ColorRes private int backgroundColor;
    private int textAppearance;
    private float relativeSize;
    private boolean strikethrough;
    private boolean underline;
    private boolean bullet;
    private boolean url;
    private int typeface = -1;

    public AttributeContainer() {

    }


    public List<Object> getSpans(Context context) {
        List<Object> spans = new ArrayList<>();
        if (foregroundColor != 0) {
            spans.add(new ForegroundColorSpan(context.getResources().getColor(foregroundColor)));
        }
        if (textAppearance != 0) {
            spans.add(new TextAppearanceSpan(context, textAppearance));
        }
        if (typeface != -1) {
            spans.add(new StyleSpan(typeface));
        }
        return spans;
    }

    public @ColorRes int getForegroundColor() {
        return foregroundColor;
    }

    public void setForegroundColor(@ColorRes int foregroundColor) {
        this.foregroundColor = foregroundColor;
    }

    public void setForegroundColor(SystemColor foregroundColor) {
        setForegroundColor(foregroundColor.getValue());
    }

    public @ColorRes int getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(@ColorRes int backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public int getTextAppearance() {
        return textAppearance;
    }

    public void setTextAppearance(int textAppearance) {
        this.textAppearance = textAppearance;
    }

    public void setTextAppearance(TextAppereance textAppearance) {
        this.textAppearance = textAppearance.getValue();
    }

    public float getRelativeSize() {
        return relativeSize;
    }

    public void setRelativeSize(float relativeSize) {
        this.relativeSize = relativeSize;
    }

    public boolean isStrikethrough() {
        return strikethrough;
    }

    public void setStrikethrough(boolean strikethrough) {
        this.strikethrough = strikethrough;
    }

    public boolean isUnderline() {
        return underline;
    }

    public void setUnderline(boolean underline) {
        this.underline = underline;
    }

    public boolean isBullet() {
        return bullet;
    }

    public void setBullet(boolean bullet) {
        this.bullet = bullet;
    }

    public boolean isUrl() {
        return url;
    }

    public void setUrl(boolean url) {
        this.url = url;
    }

    public int getTypeface() {
        return typeface;
    }

    public void setTypeface(int typeface) {
        this.typeface = typeface;
    }

    public static class Builder {

        @ColorRes
        private int foregroundColor;
        @ColorRes private int backgroundColor;
        private int textAppearance;
        private float relativeSize;
        private boolean strikethrough;
        private boolean underline;
        private boolean bullet;
        private boolean url;
        private int typeface = -1;

        public void foregroundColor(int foregroundColor) {
            this.foregroundColor = foregroundColor;
        }

        public void foregroundColor(SystemColor foregroundColor) {
            this.foregroundColor = foregroundColor.getValue();
        }

        public void backgroundColor(int backgroundColor) {
            this.backgroundColor = backgroundColor;
        }

        public void backgroundColor(SystemColor backgroundColor) {
            this.backgroundColor = backgroundColor.getValue();
        }

        public void textAppearance(int textAppearance) {
            this.textAppearance = textAppearance;
        }

        public void textAppearance(TextAppereance textAppearance) {
            this.textAppearance = textAppearance.getValue();
        }

        public void relativeSize(float relativeSize) {
            this.relativeSize = relativeSize;
        }

        public void strikethrough(boolean strikethrough) {
            this.strikethrough = strikethrough;
        }

        public void strikethrough() {
            strikethrough(true);
        }

        public void underline(boolean underline) {
            this.underline = underline;
        }

        public void underline() {
            underline(true);
        }

        public void bullet(boolean bullet) {
            this.bullet = bullet;
        }

        public void bullet() {
            bullet(true);
        }

        public void url(boolean url) {
            this.url = url;
        }

        public void url() {
            url(true);
        }

        public void typeface(int typeface) {
            this.typeface = typeface;
        }

        public void bold() {
            typeface(Typeface.BOLD);
        }

        public void italic() {
            typeface(Typeface.ITALIC);
        }

        public void boldItalic() {
            typeface(Typeface.BOLD_ITALIC);
        }

        public void normal() {
            typeface(Typeface.NORMAL);
        }

        public Builder() {

        }

        public AttributeContainer build() {
            AttributeContainer attributeContainer = new AttributeContainer();
            attributeContainer.setForegroundColor(foregroundColor);
            attributeContainer.setBackgroundColor(backgroundColor);
            attributeContainer.setTextAppearance(textAppearance);
            attributeContainer.setRelativeSize(relativeSize);
            attributeContainer.setStrikethrough(strikethrough);
            attributeContainer.setUnderline(underline);
            attributeContainer.setBullet(bullet);
            attributeContainer.setUrl(url);
            attributeContainer.setTypeface(typeface);
            return attributeContainer;
        }
    }
}
