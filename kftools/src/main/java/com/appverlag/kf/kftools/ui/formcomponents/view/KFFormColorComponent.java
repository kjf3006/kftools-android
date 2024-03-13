package com.appverlag.kf.kftools.ui.formcomponents.view;

import android.content.Context;
import android.content.res.ColorStateList;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.appverlag.kf.kftools.R;
import com.appverlag.kf.kftools.other.DensityUtils;
import com.appverlag.kf.kftools.ui.picker.KFColorPickerDialog;

public class KFFormColorComponent extends KFFormComponent<Integer, View>{

    private Button button;
    private int selectedColor;

    public KFFormColorComponent(@NonNull Context context) {
        super(context);
        setupView(context, null);
    }

    public KFFormColorComponent(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setupView(context, attrs);
    }

    private void setupView(@NonNull Context context, @Nullable AttributeSet attrs) {
        button = new Button(context, attrs, 0, R.style.KFTools_Button_ColorPicker);
        int size = DensityUtils.dpToPx(50);
        addView(button, new ViewGroup.LayoutParams(size, size));

        button.setOnClickListener(v -> showPicker(v.getContext()));
    }

    @Override
    public Integer getValue() {
        return selectedColor;
    }

    @Override
    public void setValue(Integer value) {
        selectedColor = value;
        button.setBackgroundTintList(ColorStateList.valueOf(value));
    }

    @Override
    public View getControl() {
        return button;
    }

    private void showPicker(Context context) {
        KFColorPickerDialog colorPicker = new KFColorPickerDialog(context);
        colorPicker.setOnColorSelectedListender((picker, color) -> {
            setValue(color);
        });
        colorPicker.show();
    }
}
