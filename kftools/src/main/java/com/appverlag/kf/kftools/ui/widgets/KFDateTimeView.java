package com.appverlag.kf.kftools.ui.widgets;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.appverlag.kf.kftools.R;
import com.appverlag.kf.kftools.ui.picker.KFTimePickerDialog;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

@SuppressLint("AppCompatCustomView")
public class KFDateTimeView extends TextView {

    public enum DatePickerMode {
        DatePickerModeDate,
        DatePickerModeTime,
        DatePickerModeDateAndTime
    }

    private DateFormat dateFormat = SimpleDateFormat.getDateTimeInstance();
    private DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT);
    private LocalDateTime localDateTime;
    private String title;
    private DatePickerMode datePickerMode;

    private OnSelectionListener onSelectionListener;
    private OnShowDatePickerListener onShowDatePickerListener;
    private OnShowTimePickerDialogListener onShowTimePickerDialogListener;

    public KFDateTimeView(@NonNull Context context) {
        super(context, null, 0, R.style.KFTools_TextView_Action);
        setupView();
    }

    public KFDateTimeView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs, 0, R.style.KFTools_TextView_Action);
        setupView();

        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.KFDateTimeView, 0, 0);
        title = a.getString(R.styleable.KFDateTimeView_android_title);
        setDatePickerMode(DatePickerMode.values()[a.getInt(R.styleable.KFDateTimeView_datePickerMode, 0)]);
    }

    private void setupView() {
        setSaveEnabled(true);
        setLocalDateTime(LocalDateTime.now());
        setDatePickerMode(DatePickerMode.DatePickerModeDateAndTime);
        setOnClickListener(v -> showPicker());
    }

    private void showPicker() {
        if (datePickerMode == DatePickerMode.DatePickerModeTime) {
            showTimePicker();
        }
        else {
            showDatePicker();
        }
    }

    private void showDatePicker() {

        DatePickerDialog dialog = new DatePickerDialog(getContext(), (view, year, month, dayOfMonth) -> {
            localDateTime = localDateTime.withYear(year).withMonth(month).withDayOfMonth(dayOfMonth);

            if (datePickerMode !=  DatePickerMode.DatePickerModeDate) {
                showTimePicker();
            }
            else {
                didSelectDate();
            }
        }, localDateTime.getYear(), localDateTime.getMonthValue(), localDateTime.getDayOfMonth());

        if (onShowDatePickerListener != null) {
            onShowDatePickerListener.willShowDatePicker(dialog.getDatePicker());
        }

        dialog.show();
    }

    private void showTimePicker() {
        KFTimePickerDialog dialog = new KFTimePickerDialog(getContext(), (view, hourOfDay, minute) -> {
            localDateTime = localDateTime.withHour(hourOfDay).withMinute(minute);
            didSelectDate();
        }, localDateTime.getHour(), localDateTime.getMinute(), true);

        if (onShowTimePickerDialogListener != null) {
            onShowTimePickerDialogListener.willShowTimePickerDialog(dialog);
        }
        dialog.show();
    }

    private void didSelectDate() {
        updateText();
        if (onSelectionListener != null) {
            onSelectionListener.onSelection(localDateTime);
        }
    }

    /*
    listener
     */

    public interface OnSelectionListener {
        void onSelection(LocalDateTime selectedDate);
    }

    public interface OnShowDatePickerListener {
        void willShowDatePicker(DatePicker datePicker);
    }

    public interface OnShowTimePickerDialogListener {
        void willShowTimePickerDialog(KFTimePickerDialog timePickerDialog);
    }

    /*
    getter and setter
     */

    public void setOnShowTimePickerDialogListener(OnShowTimePickerDialogListener onShowTimePickerDialogListener) {
        this.onShowTimePickerDialogListener = onShowTimePickerDialogListener;
    }

    public void setOnShowDatePickerListener(OnShowDatePickerListener onShowDatePickerListener) {
        this.onShowDatePickerListener = onShowDatePickerListener;
    }

    public void setOnSelectionListener(OnSelectionListener onSelectionListener) {
        this.onSelectionListener = onSelectionListener;
    }

    public DatePickerMode getDatePickerMode() {
        return datePickerMode;
    }

    public void setDatePickerMode(DatePickerMode datePickerMode) {
        this.datePickerMode = datePickerMode;

        if (datePickerMode == DatePickerMode.DatePickerModeDateAndTime) {
            dateFormat = SimpleDateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.SHORT);
            setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_baseline_calendar_today_24, 0);
        }
        else if (datePickerMode == DatePickerMode.DatePickerModeDate) {
            dateFormat = SimpleDateFormat.getDateInstance(DateFormat.DEFAULT);
            setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_baseline_calendar_today_24, 0);
        }
        else if (datePickerMode ==  DatePickerMode.DatePickerModeTime) {
            dateFormat = SimpleDateFormat.getTimeInstance(DateFormat.SHORT);
            setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_baseline_watch_later_24, 0);
        }

        updateText();
    }

    private void updateText() {
        setText(localDateTime.format(dateTimeFormatter));
    }

    public DateFormat getDateFormat() {
        return dateFormat;
    }

    public void setDateFormat(DateFormat dateFormat) {
        this.dateFormat = dateFormat;
    }

    public DateTimeFormatter getDateTimeFormatter() {
        return dateTimeFormatter;
    }

    public void setDateTimeFormatter(DateTimeFormatter dateTimeFormatter) {
        this.dateTimeFormatter = dateTimeFormatter;
    }



    public Date getDate() {
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    public void setDate(Date date) {
        setLocalDateTime(LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault()));
    }

    public LocalDateTime getLocalDateTime() {
        return localDateTime;
    }

    public void setLocalDateTime(LocalDateTime localDateTime) {
        this.localDateTime = localDateTime.truncatedTo(ChronoUnit.MINUTES);
        updateText();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    /*
    saved state
     */

    @Override
    public Parcelable onSaveInstanceState() {
        final Parcelable superState = super.onSaveInstanceState();
        final SavedState customViewSavedState = new SavedState(superState);
        customViewSavedState.localDateTime = localDateTime;
        return customViewSavedState;
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        final SavedState customViewSavedState = (SavedState) state;
        setLocalDateTime(customViewSavedState.localDateTime);
        super.onRestoreInstanceState(customViewSavedState.getSuperState());
    }

    private static class SavedState extends BaseSavedState {

        LocalDateTime localDateTime;

        public static final Parcelable.Creator<SavedState> CREATOR = new Creator<>() {
            @Override
            public SavedState createFromParcel(Parcel source) {
                return new SavedState(source);
            }

            @Override
            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };

        public SavedState(Parcelable superState) {
            super(superState);
        }

        private SavedState(Parcel source) {
            super(source);
            localDateTime = (LocalDateTime) source.readSerializable();
        }

        @Override public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeSerializable(localDateTime);
        }
    }
}
