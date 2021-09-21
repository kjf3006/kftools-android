package com.appverlag.kf.kftools.ui.picker;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.content.DialogInterface.OnClickListener;
import android.view.ViewGroup;
import android.widget.NumberPicker;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.appverlag.kf.kftools.R;

import java.util.Locale;


/**
 * A dialog that prompts the user for the time of day using a
 * {@link TimePicker}.
 */
public class KFTimePickerDialog extends AlertDialog implements OnClickListener, TimePicker.OnTimeChangedListener {

    private static final String HOUR = "hour";
    private static final String MINUTE = "minute";
    private static final String IS_24_HOUR = "is24hour";

    private final TimePicker mTimePicker;
    private final TimePickerDialog.OnTimeSetListener mTimeSetListener;

    private NumberPicker mNumberPickerMinute, mNumberPickerHour;

    private final int mInitialHourOfDay;
    private final int mInitialMinute;
    private final boolean mIs24HourView;
    private int mMinuteInterval = 1;


    /**
     * The callback interface used to indicate the user is done filling in
     * the time (e.g. they clicked on the 'OK' button).
     */
    public interface OnTimeSetListener {
        /**
         * Called when the user is done setting a new time and the dialog has
         * closed.
         *
         * @param view the view associated with this listener
         * @param hourOfDay the hour that was set
         * @param minute the minute that was set
         */
        void onTimeSet(TimePicker view, int hourOfDay, int minute);
    }


    /**
     * Creates a new time picker dialog.
     *
     * @param context the parent context
     * @param listener the listener to call when the time is set
     * @param hourOfDay the initial hour
     * @param minute the initial minute
     * @param is24HourView whether this is a 24 hour view or AM/PM
     */
    public KFTimePickerDialog(Context context, TimePickerDialog.OnTimeSetListener listener, int hourOfDay, int minute,
                            boolean is24HourView) {
        this(context, null, listener, hourOfDay, minute, is24HourView);
    }


    /**
     * Creates a new time picker dialog with the specified theme.
     * <p>
     * The theme is overlaid on top of the theme of the parent {@code context}.
     * If {@code themeResId} is 0, the dialog will be inflated using the theme
     * specified by the
     * {@link android.R.attr#timePickerDialogTheme android:timePickerDialogTheme}
     * attribute on the parent {@code context}'s theme.
     *
     * @param context the parent context
     * @param title title to apply to this dialog
     * @param listener the listener to call when the time is set
     * @param hourOfDay the initial hour
     * @param minute the initial minute
     * @param is24HourView Whether this is a 24 hour view, or AM/PM.
     */
    public KFTimePickerDialog(Context context, String title, TimePickerDialog.OnTimeSetListener listener,
                            int hourOfDay, int minute, boolean is24HourView) {
        super(context, 0);

        mTimeSetListener = listener;
        mInitialHourOfDay = hourOfDay;
        mInitialMinute = minute;
        mIs24HourView = is24HourView;

        final Context themeContext = getContext();
        final LayoutInflater inflater = LayoutInflater.from(themeContext);
        final View view = inflater.inflate(R.layout.kftools_time_picker_dialog, null);
        setView(view);
        setButton(BUTTON_POSITIVE, "OK", this);
        setButton(BUTTON_NEGATIVE, "Abbrechen", this);
//        setButtonPanelLayoutHint(LAYOUT_HINT_SIDE);

        if (title != null) {
            setTitle(title);
        }

        mTimePicker = view.findViewById(R.id.timePicker);
        mTimePicker.setIs24HourView(mIs24HourView);
        mTimePicker.setCurrentHour(mInitialHourOfDay);
        mTimePicker.setCurrentMinute(mInitialMinute);
        mTimePicker.setOnTimeChangedListener(this);

        findNumberPickers(mTimePicker);
    }

    /**
     * @return the time picker displayed in the dialog
     */
    public TimePicker getTimePicker() {
        return mTimePicker;
    }

    @Override
    public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
        /* do nothing */
    }

    @Override
    public void show() {
        super.show();
        getButton(BUTTON_POSITIVE).setOnClickListener(view -> {
            KFTimePickerDialog.this.onClick(KFTimePickerDialog.this, BUTTON_POSITIVE);
            // Clearing focus forces the dialog to commit any pending
            // changes, e.g. typed text in a NumberPicker.
            mTimePicker.clearFocus();
            dismiss();
        });
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        switch (which) {
            case BUTTON_POSITIVE:
                // Note this skips input validation and just uses the last valid time and hour
                // entry. This will only be invoked programmatically. User clicks on BUTTON_POSITIVE
                // are handled in show().
                if (mTimeSetListener != null) {
                    mTimeSetListener.onTimeSet(mTimePicker, mTimePicker.getCurrentHour(),
                            mTimePicker.getCurrentMinute()*mMinuteInterval);
                }
                break;
            case BUTTON_NEGATIVE:
                cancel();
                break;
        }
    }

    /**
     * Sets the current time.
     *
     * @param hourOfDay The current hour within the day.
     * @param minuteOfHour The current minute within the hour.
     */
    public void updateTime(int hourOfDay, int minuteOfHour) {
        mTimePicker.setCurrentHour(hourOfDay);
        mTimePicker.setCurrentMinute(minuteOfHour);
    }

    public void setMinuteInterval(int minuteInterval) {
        if (minuteInterval < 1 || minuteInterval > 60) {
            minuteInterval = 1;
        }
        int numberOfValues = 60/minuteInterval;
        String[] values = new String[numberOfValues];
        for (int i = 0; i < numberOfValues; i++) {
            values[i] = String.format(Locale.GERMAN, "%02d", i*minuteInterval);
        }

        if (mNumberPickerMinute != null) {
            mNumberPickerMinute.setMinValue(0);
            mNumberPickerMinute.setMaxValue(numberOfValues-1);
            mNumberPickerMinute.setDisplayedValues(values);
        }
        mMinuteInterval = minuteInterval;
    }

    public void setHourRange(int startHour, int endHour) {
        if (startHour < 0 || startHour > 24) {
            startHour = 0;
        }
        if (endHour < 0 || endHour > 24) {
            endHour = 24;
        }

        int numberOfValues = endHour - startHour + 1;
        String[] values = new String[numberOfValues];
        for (int i = 0; i < numberOfValues; i++) {
            values[i] = String.format(Locale.GERMAN, "%02d", i+startHour);
        }

        if (mNumberPickerHour != null) {
            mNumberPickerHour.setMinValue(startHour);
            mNumberPickerHour.setMaxValue(endHour);
            mNumberPickerHour.setDisplayedValues(values);
        }

    }

    @NonNull
    @Override
    public Bundle onSaveInstanceState() {
        final Bundle state = super.onSaveInstanceState();
        state.putInt(HOUR, mTimePicker.getCurrentHour());
        state.putInt(MINUTE, mTimePicker.getCurrentMinute());
        state.putBoolean(IS_24_HOUR, mTimePicker.is24HourView());
        return state;
    }

    @Override
    public void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        final int hour = savedInstanceState.getInt(HOUR);
        final int minute = savedInstanceState.getInt(MINUTE);
        mTimePicker.setIs24HourView(savedInstanceState.getBoolean(IS_24_HOUR));
        mTimePicker.setCurrentHour(hour);
        mTimePicker.setCurrentMinute(minute);
    }

    private void findNumberPickers(ViewGroup viewGroup) {
        for (int i = 0; i < viewGroup.getChildCount(); i++) {
            View child = viewGroup.getChildAt(i);
            if (child instanceof NumberPicker) {
                NumberPicker numberPicker = (NumberPicker) child;
                if (numberPicker.getMaxValue() == 59) {
                    mNumberPickerMinute = numberPicker;
                }
                else if (numberPicker.getMaxValue() == 23) {
                    mNumberPickerHour = numberPicker;
                }
            }

            if (child instanceof ViewGroup) {
                findNumberPickers((ViewGroup) child);
            }
        }
    }
}
