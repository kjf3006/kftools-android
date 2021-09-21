package com.appverlag.kf.kftools.ui.widgets;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

/**
 * AppCompatTextView subclass, which sets visibility to GONE, if text is empty
 */
@SuppressLint("AppCompatCustomView")
public class KFEmptyHiddenTextView extends TextView {


    public KFEmptyHiddenTextView(Context context) {
        super(context);
    }

    public KFEmptyHiddenTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public KFEmptyHiddenTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public KFEmptyHiddenTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        super.setText(text, type);
        setVisibility(TextUtils.isEmpty(text) ? GONE : VISIBLE);
    }
}
