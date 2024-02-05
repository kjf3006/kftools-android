package com.appverlag.kf.kftools.ui.formcomponents.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.appverlag.kf.kftools.R;
import com.appverlag.kf.kftools.ui.widgets.KFEmptyHiddenTextView;

public abstract class KFFormComponent <T,C extends View> extends LinearLayout {

    private TextView textViewTitle, textViewDescription;
    private FrameLayout contentView;
    private String identifier;

    private boolean initialisedRoot;

    public KFFormComponent(@NonNull Context context) {
        super(context);
        setupView(context);
    }

    public KFFormComponent(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setupView(context);

        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.KFFormComponent, 0, 0);
        textViewTitle.setText(a.getText(R.styleable.KFFormComponent_android_title));
        textViewDescription.setText(a.getText(R.styleable.KFFormComponent_description));
        identifier = a.getString(R.styleable.KFFormComponent_android_identifier);
    }

    private void setupView(@NonNull Context context) {
        setOrientation(VERTICAL);

        LinearLayout.LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        textViewTitle = new KFEmptyHiddenTextView(context, null, 0, R.style.KFTools_TextView);
        textViewTitle.setTextColor(getResources().getColor(R.color.secondaryLabelColor));
        addView(textViewTitle);

        contentView = new FrameLayout(context);
        addView(contentView, params);

        textViewDescription = new KFEmptyHiddenTextView(context, null, 0, R.style.KFTools_TextView_Secondary);
        addView(textViewDescription, params);

        initialisedRoot = true;
    }


    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        if (!initialisedRoot) {
            super.addView(child, index, params);
        }
        else {
            contentView.addView(child, index, params);
        }
    }

    /*
    value
     */

    public abstract T getValue();

    public abstract void setValue(T value);

    public abstract C getControl();

    @Override
    public void setEnabled(boolean enabled) {
        getControl().setEnabled(enabled);
    }

    @Override
    public boolean isEnabled() {
        return getControl().isEnabled();
    }

    /*
    getter and setter
     */
    public TextView getTextViewTitle() {
        return textViewTitle;
    }

    public TextView getTextViewDescription() {
        return textViewDescription;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

}
