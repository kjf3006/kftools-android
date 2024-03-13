package com.appverlag.kf.kftools.ui.formcomponents.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.appverlag.kf.kftools.R;
import com.appverlag.kf.kftools.other.DensityUtils;

/**
 *
 * @deprecated Use plain View with KFTools.Separator style
 */
@Deprecated(forRemoval = true)
public class KFFormSeparatorComponent extends KFFormComponent<Object, View> {

    View separator;

    public KFFormSeparatorComponent(@NonNull Context context) {
        super(context);
        setupView(context);
    }

    public KFFormSeparatorComponent(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setupView(context);
    }

    private void setupView(@NonNull Context context) {
        separator = new View(context, null, 0, R.style.KFTools_Separator);
        addView(separator, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, DensityUtils.dpToPx(2)));
    }

    @Override
    public Object getValue() {
        return null;
    }

    @Override
    public void setValue(Object value) {

    }

    @Override
    public View getControl() {
        return separator;
    }
}
