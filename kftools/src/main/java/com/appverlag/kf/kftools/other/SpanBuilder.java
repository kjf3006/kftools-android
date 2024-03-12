package com.appverlag.kf.kftools.other;

import android.graphics.Color;
import android.graphics.Typeface;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.text.style.TextAppearanceSpan;

import androidx.annotation.ColorInt;

import com.appverlag.kf.kftools.framework.ContextProvider;

import java.util.ArrayList;
import java.util.List;

public class SpanBuilder {

    private final List<Object> spans = new ArrayList<>();

    public SpanBuilder() {

    }

    public List<Object> getSpans() {
        return spans;
    }

    // region style span

    public SpanBuilder style(int style) {
        spans.add(new StyleSpan(style));
        return this;
    }

    public SpanBuilder bold() {
        return style(android.graphics.Typeface.BOLD);
    }

    public SpanBuilder normal() {
        return style(Typeface.NORMAL);
    }

    public SpanBuilder italic() {
        return style(Typeface.ITALIC);
    }

    public SpanBuilder boldItalic() {
        return style(Typeface.BOLD_ITALIC);
    }

    // endregion

    public SpanBuilder foregroundColor(@ColorInt int color) {
        spans.add(new ForegroundColorSpan(color));
        return this;
    }

    public SpanBuilder foregroundColor(SystemColor color) {
        spans.add(new ForegroundColorSpan(color.getValue()));
        return this;
    }

    public SpanBuilder textAppearance(int appearance) {
        spans.add(new TextAppearanceSpan(ContextProvider.getApplicationContext(), appearance));
        return this;
    }

    public SpanBuilder textAppearance(TextAppereance appearance) {
        return textAppearance(appearance.getValue());
    }
}
