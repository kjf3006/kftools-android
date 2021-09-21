package com.appverlag.kf.kftools.ui.widgets;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.res.TypedArray;
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
    private Date date;
    private String title;
    private DatePickerMode datePickerMode;

    private OnSelectionListener onSelectionListener;
    private OnShowDatePickerListener onShowDatePickerListener;
    private OnShowTimePickerDialogListener onShowTimePickerDialogListener;

    public KFDateTimeView(@NonNull Context context) {
        super(context, null, 0, R.style.KFTools_TextView_Action);
        setupView(context);
    }

    public KFDateTimeView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs, 0, R.style.KFTools_TextView_Action);
        setupView(context);

        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.KFDateTimeView, 0, 0);
        title = a.getString(R.styleable.KFDateTimeView_android_title);
        setDatePickerMode(DatePickerMode.values()[a.getInt(R.styleable.KFDateTimeView_datePickerMode, 0)]);
    }

    private void setupView(@NonNull Context context) {
        setDate(new Date());
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
        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        DatePickerDialog dialog = new DatePickerDialog(getContext(), (view, year, month, dayOfMonth) -> {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            date = calendar.getTime();
            if (datePickerMode !=  DatePickerMode.DatePickerModeDate) {
                showTimePicker();
            }
            else {
                didSelectDate();
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        dialog.setTitle(title);

        if (onShowDatePickerListener != null) {
            onShowDatePickerListener.willShowDatePicker(dialog.getDatePicker());
        }

        dialog.show();
    }

    private void showTimePicker() {
        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        KFTimePickerDialog dialog = new KFTimePickerDialog(getContext(), (view, hourOfDay, minute) -> {
            calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
            calendar.set(Calendar.MINUTE, minute);
            date = calendar.getTime();
            didSelectDate();
        }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true);
        dialog.setTitle(title);

        if (onShowTimePickerDialogListener != null) {
            onShowTimePickerDialogListener.willShowTimePickerDialog(dialog);
        }
        dialog.show();
    }

    private void didSelectDate() {
        setText(dateFormat.format(date));
        if (onSelectionListener != null) {
            onSelectionListener.onSelection(date);
        }
    }

    /*
    listener
     */

    public interface OnSelectionListener {
        void onSelection(Date selectedDate);
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

        setText(dateFormat.format(date));
    }

    public DateFormat getDateFormat() {
        return dateFormat;
    }

    public void setDateFormat(DateFormat dateFormat) {
        this.dateFormat = dateFormat;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        this.date = calendar.getTime();
        setText(dateFormat.format(this.date));
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
