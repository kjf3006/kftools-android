package com.appverlag.kf.kftools.ui.widgets;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Parcel;
import android.os.Parcelable;
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
        setSaveEnabled(true);
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
        setSelectedIndex(selectedIndex);
    }

    public List<String> getValues() {
        return values;
    }

    public void setValues(List<String> values) {
        this.values = values;
        setSelectedIndex(selectedIndex);
    }

    public int getSelectedIndex() {
        return selectedIndex;
    }

    public void setSelectedIndex(int selectedIndex) {
        if (selectedIndex > options.size() || selectedIndex < 0) {
            selectedIndex = 0;
        }
        this.selectedIndex = selectedIndex;
        if (options.size() > 0) {
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

    /*
    saved state
     */

    @Override
    public Parcelable onSaveInstanceState() {
        final Parcelable superState = super.onSaveInstanceState();
        final SavedState customViewSavedState = new SavedState(superState);
        customViewSavedState.selectedIndex = selectedIndex;
        customViewSavedState.values = values;
        customViewSavedState.options = options;
        return customViewSavedState;
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        final SavedState customViewSavedState = (SavedState) state;
        values = customViewSavedState.values;
        options = customViewSavedState.options;
        setSelectedIndex(customViewSavedState.selectedIndex);
        super.onRestoreInstanceState(customViewSavedState.getSuperState());
    }

    private static class SavedState extends BaseSavedState {

        private List<String> options, values;
        private int selectedIndex;

        public static final Parcelable.Creator<SavedState> CREATOR = new Creator<SavedState>() {
            @Override public SavedState createFromParcel(Parcel source) {
                return new SavedState(source);
            }

            @Override public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };

        public SavedState(Parcelable superState) {
            super(superState);
        }

        private SavedState(Parcel source) {
            super(source);
            options = source.createStringArrayList();
            values = source.createStringArrayList();
            selectedIndex = source.readInt();
        }

        @Override public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeStringList(options);
            out.writeStringList(values);
            out.writeInt(selectedIndex);
        }
    }

}
