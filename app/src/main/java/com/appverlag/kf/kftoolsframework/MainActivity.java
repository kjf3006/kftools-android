package com.appverlag.kf.kftoolsframework;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.android.billingclient.api.Purchase;
import com.appverlag.kf.kftools.billing.KFBillingManager;
import com.appverlag.kf.kftools.other.KFRemoteLogger;
import com.appverlag.kf.kftools.other.KFImagePicker;
import com.appverlag.kf.kftools.ui.KFImageView;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    KFImageView imageView;

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

        KFBillingManager.initialise(getApplicationContext(), "");

        findViewById(R.id.buttonGallery).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, KFGalleryOverviewActivity.class);
                MainActivity.this.startActivity(intent);
            }
        });

        findViewById(R.id.buttonBilling).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(MainActivity.this, KFBillingActivity.class);
//                MainActivity.this.startActivity(intent);
                KFBillingManager.getInstance().initiatePurchaseFlow(MainActivity.this, "android.test.purchased", null, "inapp");
                List<Purchase> purchases = KFBillingManager.getInstance().getPurchases();
                for (Purchase purchase : purchases) {
                    Log.e("TEST", purchase.toString());
                    KFBillingManager.getInstance().consumeAsync(purchase);
                }
            }
        });

        findViewById(R.id.buttonPickImage).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CAMERA}, 0);
                    return;
                }
                Intent intent = KFImagePicker.getPickImageIntent(MainActivity.this);
                startActivityForResult(intent, 100);
            }
        });

        imageView = (KFImageView) findViewById(R.id.imageView);
        imageView.setMapSnapshotForOptions(47.2, 10.7, true, 0, 0);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 100 && resultCode == RESULT_OK) {
            Bitmap bitmap = KFImagePicker.getImageFromResult(this, resultCode, data);
            imageView.setImageBitmap(bitmap);
        }
    }
}
