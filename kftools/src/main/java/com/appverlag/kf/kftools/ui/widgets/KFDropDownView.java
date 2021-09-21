package com.appverlag.kf.kftools.ui.widgets;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatTextView;

import com.appverlag.kf.kftools.R;

import java.util.ArrayList;
import java.util.List;

@SuppressLint("AppCompatCustomView")
public class KFDropDownView extends TextView {

    private List<String> options, values;
    private int selectedIndex;
    private String selectedValue, title;

    private OnSelectionListener onSelectionListener;

    public KFDropDownView(@NonNull Context context) {
        super(context, null, 0, R.style.KFTools_TextView_Action_DropDown);
        setupView(context);
    }

    public KFDropDownView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs, 0, R.style.KFTools_TextView_Action_DropDown);
        setupView(context);

        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.KFDropDownView, 0, 0);
        title = a.getString(R.styleable.KFDropDownView_android_title);
    }

    private void setupView(@NonNull Context context) {
        options = new ArrayList<>();
        setSelectedIndex(0);
        setOnClickListener(v -> showSelection());
    }

    private void showSelection() {
        if (options.size() == 0) {
            return;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(title);

        builder.setItems(options.toArray(new CharSequence[0]), (dialog, which) -> {
            setSelectedIndex(which);
            if (onSelectionListener != null) {
                onSelectionListener.onSelection(selectedValue, selectedIndex);
            }
        });

        builder.create().show();
    }

    /*
    listener
     */

    public interface OnSelectionListener {
        void onSelection(String selectedValue, int selectedIndex);
    }


    /*
    getter and setter
     */

    public List<String> getOptions() {
        return options;
    }

    public void setOptions(List<String> options) {
        this.options = options;
        setText(options.get(this.selectedIndex));
    }

    public List<String> getValues() {
        return values;
    }

    public void setValues(List<String> values) {
        this.values = values;
    }

    public int getSelectedIndex() {
        return selectedIndex;
    }

    public void setSelectedIndex(int selectedIndex) {
        this.selectedIndex = selectedIndex;
        if (options.size() > 0) {
            if (this.selectedIndex < 0) this.selectedIndex = 0;
            setText(options.get(this.selectedIndex));
            selectedValue = values != null ? values.get(this.selectedIndex) : options.get(this.selectedIndex);
        }
        else {
            setText(null);
        }
    }

    public String getSelectedValue() {
        return selectedValue;
    }

    public void setSelectedValue(String selectedValue) {
        setSelectedIndex(values != null ? values.indexOf(selectedValue) : options.indexOf(selectedValue));
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setOnSelectionListener(OnSelectionListener onSelectionListener) {
        this.onSelectionListener = onSelectionListener;
    }
}
