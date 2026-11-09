package com.appverlag.kf.kftools.permission;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import android.view.WindowManager;

import com.appverlag.kf.kftools.R;

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

        Intent intent = getIntent();
        requestID = intent != null
                ? intent.getStringExtra("requestID") : null;
        String[] permissions = intent != null
                ? intent.getStringArrayExtra("permissions") : null;

        if (permissions == null || permissions.length == 0
                || requestID == null || requestID.isEmpty()) {
            finishWithResult(false);
            return;
        }

        for (String permission : permissions) {
            if (permission == null || permission.isEmpty()) {
                finishWithResult(false);
                return;
            }
        }

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
    public void onRequestPermissionsResult(
            int requestCode,
            @NonNull String[] permissions,
            @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode != 100) return;

        boolean success = permissions.length > 0
                && grantResults.length == permissions.length;

        for (int result : grantResults) {
            if (result != PackageManager.PERMISSION_GRANTED) {
                success = false;
            }
        }

        finishWithResult(success);
    }

    private void finishWithResult(boolean success) {
        if (requestID != null && !requestID.isEmpty()) {
            Intent broadcast = new Intent(requestID);
            broadcast.putExtra("success", success);
            LocalBroadcastManager.getInstance(this).sendBroadcast(broadcast);
        }
        finish();
    }
}
