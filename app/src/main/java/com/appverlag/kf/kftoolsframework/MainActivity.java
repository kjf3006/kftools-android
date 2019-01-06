package com.appverlag.kf.kftoolsframework;

import android.Manifest;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.android.billingclient.api.Purchase;
import com.appverlag.kf.kftools.billing.KFBillingManager;
import com.appverlag.kf.kftools.location.KFLocationManager;
import com.appverlag.kf.kftools.location.KFLocationManagerRequest;
import com.appverlag.kf.kftools.network.KFConnectionManager;
import com.appverlag.kf.kftools.network.KFConnectionManagerCallback;
import com.appverlag.kf.kftools.network.KFConnectionManagerJSONCallback;
import com.appverlag.kf.kftools.other.KFExceptionHandler;
import com.appverlag.kf.kftools.other.KFFilePicker;
import com.appverlag.kf.kftools.other.KFRemoteLogger;
import com.appverlag.kf.kftools.other.KFImagePicker;
import com.appverlag.kf.kftools.permission.KFRunntimePermissionManager;
import com.appverlag.kf.kftools.ui.KFBottomSheetDialog;
import com.appverlag.kf.kftools.ui.KFImageView;
import com.appverlag.kf.kftools.ui.KFRichTextView;
import com.appverlag.kf.kftools.weather.KFWeatherForecast;
import com.appverlag.kf.kftools.weather.KFWeatherManager;

import org.json.JSONObject;

import java.io.File;
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
//
//        int i = 1/0;

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
                String [] options = {"TEST", "Test 2"};
                KFBottomSheetDialog.Builder builder = new KFBottomSheetDialog.Builder();

                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                KFBottomSheetDialog dialog = builder.create();
                dialog.show(getSupportFragmentManager(), null);

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
                            Intent intent = KFFilePicker.getPickerIntent(MainActivity.this, true, false);
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


        KFConnectionManager.RequestParams p = new KFConnectionManager.RequestParams();
        p.put("test", "akjhdfad");
        Request request = new Request.Builder().url("https://content-appverlag.com/hdr.php?q=123").post(p.getParams()).build();
        KFConnectionManager.getInstance().sendJSONRequest(request, new KFConnectionManager.KFConnectionManagerCompletionHandler() {
            @Override
            public void onSuccess(JSONObject response) {

            }

            @Override
            public void onError() {

            }
        }, false, false);

        KFRunntimePermissionManager.check(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, new KFRunntimePermissionManager.KFRunntimePermissionManagerCallback() {
            @Override
            public void onSuccess(boolean success) throws SecurityException {

            }
        });

        KFLocationManager.getInstance(this).addRequest(new KFLocationManagerRequest(KFLocationManagerRequest.TYPE_SINGLE, KFLocationManagerRequest.ACCURACY_DEFAULT, KFLocationManagerRequest.DISTANCE_FILTER_DEFAULT, new KFLocationManagerRequest.KFLocationManagerRequestCallback() {
            @Override
            public void didUpdateLocation(Location location) {
                KFWeatherManager.getInstance().getWeatherDataForLocation(location, new KFWeatherManager.KFWeatherManagerCompletionHandler() {
                    @Override
                    public void onComplete(KFWeatherForecast forecast) {
                        Log.d("TEST", forecast.toString());
                        Log.d("TEST", forecast.getLocationName());
                    }
                });
            }
        }));

        final KFRichTextView textView = findViewById(R.id.richTextView);
        textView.setOnInitialLoadListener(new KFRichTextView.AfterInitialLoadListener() {
            @Override
            public void onAfterInitialLoad(boolean isReady) {
                if (isReady) textView.setHtml("<p dir=\"ltr\">wir hoffen, dass Du wieder gut Zuhause angekommen bist und viele neue Eindr&#252;cke und tolle Erinnerungen gesammelt hast! Herzlichen Dank f&#252;r Deine <b>Teilnahme</b> an unserem Bergzeit Erlebnis. Wir haben uns gefreut, dass Du dabei warst!</p>\n");
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 100 && resultCode == RESULT_OK) {
            File file = KFFilePicker.getFileFromResult(this, resultCode, data);
            Log.d("TEST", file.toString());

            Uri uri = Uri.fromFile(file);
            ContentResolver cR = getContentResolver();
            String mime = cR.getType(KFFilePicker.getUriFromResult(this, resultCode, data));
            Log.d("TEST", "mime: " + mime + "\n");
        }
    }
}
