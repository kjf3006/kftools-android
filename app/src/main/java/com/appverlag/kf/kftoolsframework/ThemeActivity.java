package com.appverlag.kf.kftoolsframework;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.appverlag.kf.kftools.images.ImageContainer;
import com.appverlag.kf.kftools.other.KFLog;
import com.appverlag.kf.kftools.ui.ImagePager;
import com.appverlag.kf.kftools.ui.formcomponents.controller.KFFormComponentContoller;
import com.appverlag.kf.kftools.ui.formcomponents.view.KFFormComponent;
import com.appverlag.kf.kftools.ui.formcomponents.view.KFFormDateTimeComponent;
import com.appverlag.kf.kftools.ui.formcomponents.view.KFFormDropDownComponent;

import java.util.Arrays;
import java.util.Date;

public class ThemeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_theme);

        ImagePager imagePager = findViewById(R.id.imagePager);
        imagePager.setImages(Arrays.asList(
                ImageContainer.url("https://media.idownloadblog.com/wp-content/uploads/2021/09/Apple-September-Event-California-Streaming-BasicAppleGuy-iDownloadBlog-6K.png"),
                ImageContainer.url("https://restado.de/wp-content/uploads/test.jpg"),
                ImageContainer.url("https://upload.wikimedia.org/wikipedia/commons/thumb/6/66/SMPTE_Color_Bars.svg/1200px-SMPTE_Color_Bars.svg.png")));

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

    }
}
