package com.appverlag.kf.kftools.ui.formcomponents;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.appverlag.kf.kftools.R;
import com.appverlag.kf.kftools.other.KFDensityTool;
import com.appverlag.kf.kftools.ui.picker.KFColorPickerDialog;

public class KFFormColorComponent extends KFFormComponent<Integer>{

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
        int size = KFDensityTool.dpToPx(50);
        addView(button, new ViewGroup.LayoutParams(size, size));

        button.setOnClickListener(v -> showPicker(v.getContext()));
    }

    @Override
    public Integer getValue() {
        return selectedColor;
    }

    private void showPicker(Context context) {
        KFColorPickerDialog colorPicker = new KFColorPickerDialog(context);
        colorPicker.setOnColorSelectedListender((picker, color) -> {
            selectedColor = color;
            button.setBackgroundTintList(ColorStateList.valueOf(color));
        });
        colorPicker.show();
    }
}
