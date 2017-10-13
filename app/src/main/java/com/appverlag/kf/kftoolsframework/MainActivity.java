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
import com.appverlag.kf.kftools.network.KFConnectionManager;
import com.appverlag.kf.kftools.network.KFConnectionManagerCallback;
import com.appverlag.kf.kftools.network.KFConnectionManagerJSONCallback;
import com.appverlag.kf.kftools.other.KFRemoteLogger;
import com.appverlag.kf.kftools.other.KFImagePicker;
import com.appverlag.kf.kftools.permission.KFRunntimePermissionManager;
import com.appverlag.kf.kftools.ui.KFImageView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import okhttp3.Request;
import okhttp3.Response;

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

        findViewById(R.id.buttonSection).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, KFSectionedRecyclerViewActivity.class);
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
                KFRunntimePermissionManager.check(MainActivity.this, new String[]{Manifest.permission.CAMERA}, new KFRunntimePermissionManager.KFRunntimePermissionManagerCallback() {
                    @Override
                    public void onSuccess(boolean success) throws SecurityException {
                        if (success) {
                            Intent intent = KFImagePicker.getPickImageIntent(MainActivity.this);
                            startActivityForResult(intent, 100);
                        }
                    }
                });


            }
        });

        findViewById(R.id.buttonPermission).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                KFRunntimePermissionManager.check(MainActivity.this, new String[]{Manifest.permission.READ_CONTACTS, Manifest.permission.CAMERA}, new KFRunntimePermissionManager.KFRunntimePermissionManagerCallback() {
                    @Override
                    public void onSuccess(boolean success) {
                        System.out.println("permission success");
                    }
                });
            }
        });

        imageView = (KFImageView) findViewById(R.id.imageView);
        imageView.setMapSnapshotForOptions(47.2, 10.7, true, 0, 0);


//        Request request = new Request.Builder().url("http://login-auf-polizei.at/api/events").build();
//        KFConnectionManager.getInstance().sendRequest(request, false, new KFConnectionManagerJSONCallback() {
//            @Override
//            public void onResponse(JSONObject response) {
//                System.out.println(response);
//            }
//
//            @Override
//            public void onFailure() {
//
//            }
//        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 100 && resultCode == RESULT_OK) {
            Bitmap bitmap = KFImagePicker.getImageFromResult(this, resultCode, data);
            imageView.setImageBitmap(bitmap);
        }
    }
}
