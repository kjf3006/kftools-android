package com.appverlag.kf.kftools.ui.formcomponents;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.appverlag.kf.kftools.R;

public class KFFormCheckboxComponent extends KFFormComponent<Boolean> {

    private CheckBox checkBox;

    public KFFormCheckboxComponent(@NonNull Context context) {
        super(context);
        setupView(context, null);
    }

    public KFFormCheckboxComponent(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setupView(context, attrs);
    }

    private void setupView(@NonNull Context context, @Nullable AttributeSet attrs) {
        checkBox = new CheckBox(context, attrs);
        addView(checkBox);
    }

    @Override
    public Boolean getValue() {
        return checkBox.isChecked();
    }

    public CheckBox getCheckBox() {
        return checkBox;
    }
}
