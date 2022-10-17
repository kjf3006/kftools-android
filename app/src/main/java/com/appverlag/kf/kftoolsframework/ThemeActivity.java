package com.appverlag.kf.kftoolsframework;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.appverlag.kf.kftools.other.KFLog;
import com.appverlag.kf.kftools.ui.formcomponents.controller.KFFormComponentContoller;
import com.appverlag.kf.kftools.ui.formcomponents.view.KFFormComponent;
import com.appverlag.kf.kftools.ui.formcomponents.view.KFFormDateTimeComponent;
import com.appverlag.kf.kftools.ui.formcomponents.view.KFFormDropDownComponent;

import java.util.Arrays;
import java.util.Date;

import okhttp3.FormBody;
import okhttp3.RequestBody;

public class ThemeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_theme);

        KFFormDropDownComponent dropDownComponent = findViewById(R.id.dropDownComponent);
        dropDownComponent.getDropDownView().setOptions(Arrays.asList("A", "B", "C", "D"));
        dropDownComponent.getDropDownView().setSelectedIndex(2);
//        dropDownComponent.getDropDownView().setEnabled(false);
        dropDownComponent.getDropDownView().setOnSelectionListener((selectedValue, selectedIndex) -> {
            KFLog.d("ThemeActivity", "selected: " + selectedValue + " at index " + selectedIndex);
        });


        KFFormDateTimeComponent dateTimeComponent = findViewById(R.id.dateTimeComponent);
        dateTimeComponent.getDateTimeView().setOnSelectionListener(selectedDate -> {
            KFLog.d("ThemeActivity", "selected: " + selectedDate.toString());
        });
        dateTimeComponent.getDateTimeView().setOnShowDatePickerListener(datePicker -> {
            datePicker.setMinDate(new Date().getTime());
            datePicker.setSpinnersShown(true);
        });
        dateTimeComponent.getDateTimeView().setOnShowTimePickerDialogListener(timePickerDialog -> {
            timePickerDialog.setHourRange(6, 22);
            timePickerDialog.setMinuteInterval(30);
        });

        KFFormComponentContoller contoller = new KFFormComponentContoller(dateTimeComponent);

        Object object = "TEST";
        KFFormComponent<?> component = contoller.getComponent("");
        if (component.getValue().getClass().equals(object.getClass())) {

        }
    }
}
