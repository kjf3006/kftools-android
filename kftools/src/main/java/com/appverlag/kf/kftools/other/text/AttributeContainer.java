/*
 * Copyright (C) Kevin Flachsmann - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Created by kevinflachsmann on 29.4.2024.
 */

package com.appverlag.kf.kftools.other.text;

import android.content.Context;
import android.graphics.Typeface;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StrikethroughSpan;
import android.text.style.StyleSpan;
import android.text.style.TextAppearanceSpan;
import android.text.style.URLSpan;
import android.text.style.UnderlineSpan;

import androidx.annotation.ColorRes;
import androidx.annotation.FloatRange;

import java.util.ArrayList;
import java.util.List;

public class AttributeContainer {
    @ColorRes private int foregroundColor;
    @ColorRes private int backgroundColor;
    private int textAppearance;
    @FloatRange(from = 0) private float relativeSize;
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
        if (backgroundColor != 0) {
            spans.add(new BackgroundColorSpan(context.getResources().getColor(backgroundColor)));
        }
        if (textAppearance != 0) {
            spans.add(new TextAppearanceSpan(context, textAppearance));
        }
        if (relativeSize != 1) {
            spans.add(new RelativeSizeSpan(relativeSize));
        }
        if (typeface != -1) {
            spans.add(new StyleSpan(typeface));
        }
        if (underline) {
            spans.add(new UnderlineSpan());
        }
        if (strikethrough) {
            spans.add(new StrikethroughSpan());
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

    public void setRelativeSize(@FloatRange(from = 0) float relativeSize) {
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
        @FloatRange(from = 0) private float relativeSize;
        private boolean strikethrough;
        private boolean underline;
        private boolean bullet;
        private boolean url;
        private int typeface = -1;

        public Builder foregroundColor(int foregroundColor) {
            this.foregroundColor = foregroundColor;
            return this;
        }

        public Builder foregroundColor(SystemColor foregroundColor) {
            this.foregroundColor = foregroundColor.getValue();
            return this;
        }

        public Builder backgroundColor(int backgroundColor) {
            this.backgroundColor = backgroundColor;
            return this;
        }

        public Builder backgroundColor(SystemColor backgroundColor) {
            this.backgroundColor = backgroundColor.getValue();
            return this;
        }

        public Builder textAppearance(int textAppearance) {
            this.textAppearance = textAppearance;
            return this;
        }

        public Builder textAppearance(TextAppereance textAppearance) {
            this.textAppearance = textAppearance.getValue();
            return this;
        }

        public Builder relativeSize(@FloatRange(from = 0) float relativeSize) {
            this.relativeSize = relativeSize;
            return this;
        }

        public Builder strikethrough(boolean strikethrough) {
            this.strikethrough = strikethrough;
            return this;
        }

        public Builder strikethrough() {
            return strikethrough(true);
        }

        public Builder underline(boolean underline) {
            this.underline = underline;
            return this;
        }

        public Builder underline() {
            return underline(true);
        }

        public Builder bullet(boolean bullet) {
            this.bullet = bullet;
            return this;
        }

        public Builder bullet() {
            return bullet(true);
        }

        public Builder url(boolean url) {
            this.url = url;
            return this;
        }

        public Builder url() {
            return url(true);
        }

        public Builder typeface(int typeface) {
            this.typeface = typeface;
            return this;
        }

        public Builder bold() {
            return typeface(Typeface.BOLD);
        }

        public Builder italic() {
            return typeface(Typeface.ITALIC);
        }

        public Builder boldItalic() {
            return typeface(Typeface.BOLD_ITALIC);
        }

        public Builder normal() {
            return typeface(Typeface.NORMAL);
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
