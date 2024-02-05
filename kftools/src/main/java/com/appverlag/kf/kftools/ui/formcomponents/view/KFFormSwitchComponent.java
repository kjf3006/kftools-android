package com.appverlag.kf.kftools.ui.formcomponents.view;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;

public class KFFormSwitchComponent extends KFFormComponent<Boolean, SwitchCompat> {

    private SwitchCompat aSwitch;

    public KFFormSwitchComponent(@NonNull Context context) {
        super(context);
        setupView(context, null);
    }

    public KFFormSwitchComponent(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setupView(context, attrs);
    }

    private void setupView(@NonNull Context context, @Nullable AttributeSet attrs) {
        aSwitch = new SwitchCompat(context, attrs);
        addView(aSwitch);
    }

    @Override
    public Boolean getValue() {
        return aSwitch.isChecked();
    }

    @Override
    public void setValue(Boolean value) {
        aSwitch.setChecked(value);
    }

    @Override
    public SwitchCompat getControl() {
        return aSwitch;
    }

    @Deprecated(forRemoval = true)
    public SwitchCompat getSwitch() {
        return aSwitch;
    }
}
