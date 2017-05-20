package com.appverlag.kf.kftoolsframework;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.appverlag.kf.kftools.KFExceptionHandler;
import com.appverlag.kf.kftools.KFLoadingView;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(!(Thread.getDefaultUncaughtExceptionHandler() instanceof KFExceptionHandler)) {
            Thread.setDefaultUncaughtExceptionHandler(new KFExceptionHandler("KFTools", BuildConfig.VERSION_NAME, BuildConfig.VERSION_CODE));
        }
    }
}
