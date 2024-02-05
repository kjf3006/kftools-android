package com.appverlag.kf.kftools.ui.formcomponents.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.appverlag.kf.kftools.R;

/**
 *
 * @deprecated Use plain Button with KFTools.Button style
 */
@Deprecated(forRemoval = true)
public class KFFormButtonComponent extends KFFormComponent<Object, Button> {

    private Button button;

    public KFFormButtonComponent(@NonNull Context context) {
        super(context);
        setupView(context, null);
    }

    public KFFormButtonComponent(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setupView(context, attrs);
    }

    private void setupView(@NonNull Context context, @Nullable AttributeSet attrs) {
        button = new Button(context, attrs, 0, R.style.KFTools_Button);
        addView(button, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
    }

    @Override
    public Object getValue() {
        return null;
    }

    @Override
    public void setValue(Object value) {

    }

    @Override
    public Button getControl() {
        return button;
    }
}
