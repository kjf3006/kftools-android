package com.appverlag.kf.kftools.permission;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.WindowManager;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Copyright (C) Kevin Flachsmann - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Created by kevinflachsmann on 25.09.17.
 */
public class KFRunntimePermissionManager {

    private KFRunntimePermissionManager() {

    }

    public static void check(Context context, String [] permissions, final KFRunntimePermissionManagerCallback callback) {

        List<String> openPermissions = new ArrayList<>();

        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) openPermissions.add(permission);
        }

        if (openPermissions.size() == 0) {
            callback.onSuccess();
            return;
        }

        String requestID = UUID.randomUUID().toString();

        IntentFilter filter = new IntentFilter();
        filter.addAction(requestID);

        BroadcastReceiver receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                callback.onSuccess();
            }
        };

        context.registerReceiver(receiver, filter);

        Intent intent = new Intent(context, KFRunntimePermissionManagerActivity.class);
        intent.putExtra("permissions", openPermissions.toArray(new String[openPermissions.size()]));
        intent.putExtra("requestID", requestID);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }


    public interface KFRunntimePermissionManagerCallback {
        void onSuccess() throws SecurityException;
    }



}
