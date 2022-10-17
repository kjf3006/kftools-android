package com.appverlag.kf.kftools.ui.formcomponents.view;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.appverlag.kf.kftools.ui.widgets.KFDropDownView;

public class KFFormDropDownComponent extends KFFormComponent<String> {

    private KFDropDownView dropDownView;

    public KFFormDropDownComponent(@NonNull Context context) {
        super(context);
        setupView(context, null);
    }

    public KFFormDropDownComponent(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setupView(context, attrs);
    }

    private void setupView(@NonNull Context context, @Nullable AttributeSet attrs) {
        dropDownView = new KFDropDownView(context, attrs);
        addView(dropDownView);
    }

    @Override
    public String getValue() {
        return dropDownView.getSelectedValue();
    }

    @Override
    public void setValue(String value) {
        dropDownView.setSelectedValue(value);
    }

    public KFDropDownView getDropDownView() {
        return dropDownView;
    }
}
