package com.appverlag.kf.kftools.ui.formcomponents.view;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.appverlag.kf.kftools.ui.widgets.KFDateTimeView;

import java.util.Date;

public class KFFormDateTimeComponent extends KFFormComponent<Date> {

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
    public Date getValue() {
        return dateTimeView.getDate();
    }

    public KFDateTimeView getDateTimeView() {
        return dateTimeView;
    }
}
