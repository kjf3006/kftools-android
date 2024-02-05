package com.appverlag.kf.kftools.ui.formcomponents.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class KFFormCheckboxComponent extends KFFormComponent<Boolean, CheckBox> {

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

    @Override
    public void setValue(Boolean value) {
        checkBox.setChecked(value);
    }

    @Override
    public CheckBox getControl() {
        return checkBox;
    }

    @Deprecated(forRemoval = true)
    public CheckBox getCheckBox() {
        return checkBox;
    }
}
