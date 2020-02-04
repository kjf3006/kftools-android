package com.appverlag.kf.kftools.other;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ImageSpan;

/**
 * Copyright (C) Kevin Flachsmann - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Created by kevinflachsmann on 22.04.18.
 */
public class KFSpannableStringBuilder extends SpannableStringBuilder {

    private int flag = Spannable.SPAN_EXCLUSIVE_EXCLUSIVE;

    public KFSpannableStringBuilder() {
        super("");
    }

    public KFSpannableStringBuilder(CharSequence text) {
        super(text);
    }

    public KFSpannableStringBuilder(CharSequence text, Object... spans) {
        super(text);
        for (Object span : spans) {
            setSpan(span, 0, length());
        }
    }

    public KFSpannableStringBuilder(CharSequence text, Object span) {
        super(text);
        setSpan(span, 0, text.length());
    }

    /**
     * Appends the character sequence {@code text} and spans {@code spans} over the appended part.
     * @param text the character sequence to append.
     * @param spans the object or objects to be spanned over the appended text.
     * @return this {@code KFSpannableStringBuilder}.
     */
    public KFSpannableStringBuilder append(CharSequence text, Object... spans) {
        append(text);
        for (Object span : spans) {
            setSpan(span, length() - text.length(), length());
        }
        return this;
    }

    public KFSpannableStringBuilder append(CharSequence text, Object span) {
        append(text);
        setSpan(span, length() - text.length(), length());
        return this;
    }

    /**
     * Add the ImageSpan to the start of the text.
     * @return this {@code KFSpannableStringBuilder}.
     */
    public KFSpannableStringBuilder append(CharSequence text, ImageSpan imageSpan) {
        text = "." + text;
        append(text);
        setSpan(imageSpan, length() - text.length(), length() - text.length() + 1);
        return this;
    }

    /**
     * Append plain text.
     * @return this {@code KFSpannableStringBuilder}.
     */
    @Override public KFSpannableStringBuilder append(CharSequence text) {
        super.append(text);
        return this;
    }

    /**
     * @deprecated use {@link #append(CharSequence text)}
     */
    @Deprecated public KFSpannableStringBuilder appendText(CharSequence text) {
        append(text);
        return this;
    }

    /**
     * Change the flag. Default is SPAN_EXCLUSIVE_EXCLUSIVE.
     * The flags determine how the span will behave when text is
     * inserted at the start or end of the span's range
     * @param flag see {@link Spanned}.
     */
    public void setFlag(int flag) {
        this.flag = flag;
    }

    /**
     * Mark the specified range of text with the specified object.
     * The flags determine how the span will behave when text is
     * inserted at the start or end of the span's range.
     */
    private void setSpan(Object span, int start, int end) {
        setSpan(span, start, end, flag);
    }

    /**
     * Sets a span object to all appearances of specified text in the spannable.
     * A new instance of a span object must be provided for each iteration
     * because it can't be reused.
     *
     * @param textToSpan Case-sensitive text to span in the current spannable.
     * @param getSpan    Interface to get a span for each spanned string.
     * @return {@code KFSpannableStringBuilder}.
     */
    public KFSpannableStringBuilder findAndSpan(CharSequence textToSpan, GetSpan getSpan) {
        int lastIndex = 0;
        while (lastIndex != -1) {
            lastIndex = toString().indexOf(textToSpan.toString(), lastIndex);
            if (lastIndex != -1) {
                setSpan(getSpan.getSpan(), lastIndex, lastIndex + textToSpan.length());
                lastIndex += textToSpan.length();
            }
        }
        return this;
    }

    /**
     * Interface to return a new span object when spanning multiple parts in the text.
     */
    public interface GetSpan {

        /**
         * @return A new span object should be returned.
         */
        Object getSpan();
    }

    /**
     * Sets span objects to the text. This is more efficient than creating a new instance of KFSpannableStringBuilder
     * or SpannableStringBuilder.
     * @return {@code SpannableString}.
     */
    public static SpannableString spanText(CharSequence text, Object... spans) {
        SpannableString spannableString = new SpannableString(text);
        for (Object span : spans) {
            spannableString.setSpan(span, 0, text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return spannableString;
    }

    public static SpannableString spanText(CharSequence text, Object span) {
        SpannableString spannableString = new SpannableString(text);
        spannableString.setSpan(span, 0, text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannableString;
    }

    public void appendImage(Context context, final int drawableRes) {
        Drawable image = ContextCompat.getDrawable(context, drawableRes);
        if (image != null) {
            image.setBounds(0, 0, image.getIntrinsicWidth(), image.getIntrinsicHeight());
            ImageSpan imageSpan = new KFCenteredImageSpan(image);
            SpannableString spFilterWithIcon = new SpannableString(" ");
            spFilterWithIcon.setSpan(imageSpan, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            append(spFilterWithIcon);
        }
    }
}
