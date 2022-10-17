package com.appverlag.kf.kftools.ui.formcomponents.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.appverlag.kf.kftools.R;
import com.appverlag.kf.kftools.other.KFDensityTool;

public class KFFormEditTextComponent extends KFFormComponent<String>{

    private EditText editText;

    public KFFormEditTextComponent(@NonNull Context context) {
        super(context);
        setupView(context, null);
    }

    public KFFormEditTextComponent(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setupView(context, attrs);
    }

    private void setupView(@NonNull Context context, @Nullable AttributeSet attrs) {
        editText = new EditText(context, attrs, 0, R.style.KFTools_EditText);
        addView(editText);

        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.KFFormEditTextComponent, 0, 0);
        boolean singleLine = a.getBoolean(R.styleable.KFFormEditTextComponent_android_singleLine, false);
        if (!singleLine) {
            editText.setMinHeight(KFDensityTool.dpToPx(150));
        }
    }

    @Override
    public String getValue() {
        return editText.getText().toString();
    }

    @Override
    public void setValue(String value) {
        editText.setText(value);
    }

    public EditText getEditText() {
        return editText;
    }
}
