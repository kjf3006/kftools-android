package com.appverlag.kf.kftools.ui.formcomponents.view;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.appverlag.kf.kftools.ui.widgets.KFDateTimeView;

import java.time.LocalDateTime;
import java.util.Date;

public class KFFormDateTimeComponent extends KFFormComponent<LocalDateTime, KFDateTimeView> {

    private KFDateTimeView dateTimeView;

    public KFFormDateTimeComponent(@NonNull Context context) {
        super(context);
        setupView(context, null);
    }

    public KFFormDateTimeComponent(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setupView(context, attrs);
    }

    private void setupView(@NonNull Context context, @Nullable AttributeSet attrs) {
        dateTimeView = new KFDateTimeView(context, attrs);
        addView(dateTimeView);
    }

    @Override
    public LocalDateTime getValue() {
        return dateTimeView.getLocalDateTime();
    }

    @Override
    public void setValue(LocalDateTime value) {
        dateTimeView.setLocalDateTime(value);
    }

    @Override
    public KFDateTimeView getControl() {
        return dateTimeView;
    }

    /**
     *
     * @return dateTimeView
     * @Deprecated Use {@link #getControl() getControl()} instead.
     */
    @Deprecated(forRemoval = true)
    public KFDateTimeView getDateTimeView() {
        return dateTimeView;
    }
}
