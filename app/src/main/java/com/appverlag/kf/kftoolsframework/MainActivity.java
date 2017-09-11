package com.appverlag.kf.kftoolsframework;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.appverlag.kf.kftools.other.KFRemoteLogger;
import com.appverlag.kf.kftools.ui.KFImageView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        KFRemoteLogger.initialise(getApplicationContext(), "info@example.com", "KFTools", BuildConfig.VERSION_NAME, BuildConfig.VERSION_CODE);

        KFRemoteLogger.log("MainActivity", "Test");
        KFRemoteLogger.log("MainActivity", "Test 2");
        KFRemoteLogger.log("MainActivity", "Test 3");
        KFRemoteLogger.log("MainActivity", "Test 4");
//        if(!(Thread.getDefaultUncaughtExceptionHandler() instanceof KFExceptionHandler)) {
//            Thread.setDefaultUncaughtExceptionHandler(new KFExceptionHandler("KFTools", BuildConfig.VERSION_NAME, BuildConfig.VERSION_CODE));
//        }

        findViewById(R.id.buttonGallery).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, KFGalleryOverviewActivity.class);
                MainActivity.this.startActivity(intent);
            }
        });

        KFImageView imageView = (KFImageView) findViewById(R.id.imageView);
        imageView.setMapSnapshotForOptions(47.2, 10.7, true, 0, 0);
    }
}
