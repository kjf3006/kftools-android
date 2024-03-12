package com.appverlag.kf.kftools.other;

import android.text.NoCopySpan;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;

import androidx.annotation.NonNull;

public class AttributedStringBuilder extends SpannableStringBuilder {

    public AttributedStringBuilder() {
        super("");
    }

    public AttributedStringBuilder newline() {
        return append("\n");
    }

    @NonNull
    @Override
    public AttributedStringBuilder append(CharSequence text) {
        super.append(text);
        return this;
    }

    /**
     * Appends the character sequence {@code text} and spans {@code spans} over the appended part.
     * @param text the character sequence to append.
     * @param spans the object or objects to be spanned over the appended text.
     * @return this {@code KFSpannableStringBuilder}.
     */
    public AttributedStringBuilder append(CharSequence text, Object... spans) {
        append(text);
        for (Object span : spans) {
            setSpan(span, length() - text.length(), length());
        }
        return this;
    }

    /**
     * Mark the specified range of text with the specified object.
     * The flags determine how the span will behave when text is
     * inserted at the start or end of the span's range.
     */
    private void setSpan(Object span, int start, int end) {
        setSpan(span, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
    }
}
