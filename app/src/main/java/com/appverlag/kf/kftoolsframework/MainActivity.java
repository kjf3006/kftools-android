package com.appverlag.kf.kftoolsframework;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.appverlag.kf.kftools.ui.KFImageView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
