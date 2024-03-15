package com.appverlag.kf.kftools.other;

import android.content.Context;
import android.graphics.Typeface;
import android.text.style.BackgroundColorSpan;
import android.text.style.BulletSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StrikethroughSpan;
import android.text.style.StyleSpan;
import android.text.style.TextAppearanceSpan;
import android.text.style.URLSpan;
import android.text.style.UnderlineSpan;

import androidx.annotation.ColorInt;

import com.appverlag.kf.kftools.framework.ContextProvider;

import java.util.ArrayList;
import java.util.List;

public class SpanBuilder {

    private final List<Object> spans = new ArrayList<>();

    private final Context context;

    public SpanBuilder(Context context) {
        this.context = context;
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
        spans.add(new ForegroundColorSpan(color.getValue(context)));
        return this;
    }

    public SpanBuilder backgroundColor(@ColorInt int color) {
        spans.add(new BackgroundColorSpan(color));
        return this;
    }

    public SpanBuilder backgroundColor(SystemColor color) {
        spans.add(new BackgroundColorSpan(color.getValue(context)));
        return this;
    }

    public SpanBuilder textAppearance(int appearance) {
        spans.add(new TextAppearanceSpan(ContextProvider.getApplicationContext(), appearance));
        return this;
    }

    public SpanBuilder textAppearance(TextAppereance appearance) {
        return textAppearance(appearance.getValue());
    }

    public SpanBuilder relativeSize(float relativeSize) {
        spans.add(new RelativeSizeSpan(relativeSize));
        return this;
    }

    public SpanBuilder strikethrough() {
        spans.add(new StrikethroughSpan());
        return this;
    }

    public SpanBuilder underline() {
        spans.add(new UnderlineSpan());
        return this;
    }

    public SpanBuilder url(String url) {
        spans.add(new URLSpan(url));
        return this;
    }

    public SpanBuilder bullet() {
        spans.add(new BulletSpan());
        return this;
    }
}
