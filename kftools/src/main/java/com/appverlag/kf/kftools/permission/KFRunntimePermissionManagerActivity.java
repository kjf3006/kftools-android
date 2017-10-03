package com.appverlag.kf.kftools.permission;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.view.WindowManager;

import com.appverlag.kf.kftools.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright (C) Kevin Flachsmann - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Created by kevinflachsmann on 25.09.17.
 */
public class KFRunntimePermissionManagerActivity extends Activity implements ActivityCompat.OnRequestPermissionsResultCallback {

    private String requestID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

        String [] permissions = getIntent().getStringArrayExtra("permissions");
        requestID = getIntent().getStringExtra("requestID");

        ActivityCompat.requestPermissions(this, permissions, 100);
    }

    private void showRequestPermissionRationale(List<String> permissions) {

        String message = "";
        for (String permission : permissions) {
            message += "\n" + permission;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.kftools_AlertDialog);
        builder.setCancelable(false);
        builder.setTitle("Der Zugriff auf folgende Funktionen wird benöntigt:");
        builder.setMessage(message);
        builder.setNeutralButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                KFRunntimePermissionManagerActivity.this.finish();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        boolean success = true;

        for (int result : grantResults) {
            if (result != PackageManager.PERMISSION_GRANTED) success = false;
        }

        Intent broadcast = new Intent();
        broadcast.setAction(requestID);
        broadcast.putExtra("success", success);
        LocalBroadcastManager.getInstance(this).sendBroadcast(broadcast);

        finish();
    }
}