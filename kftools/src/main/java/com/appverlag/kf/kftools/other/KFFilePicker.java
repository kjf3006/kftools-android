package com.appverlag.kf.kftools.other;

import android.app.Activity;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Parcelable;
import android.provider.MediaStore;
import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Copyright (C) Kevin Flachsmann - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Created by kevinflachsmann on 04.05.18.
 */
public class KFFilePicker {

    private static final String TEMP_IMAGE_NAME = "tempImage";


    public static Intent getPickerIntent(Context context, boolean includeCamera, boolean includeDocuments) {

        List<Intent> allIntents = new ArrayList<>();
        PackageManager packageManager = context.getPackageManager();

        if (includeCamera) allIntents.addAll(getCameraIntents(context, packageManager));

        List<Intent> galleryIntents = getGalleryIntents(packageManager, Intent.ACTION_GET_CONTENT, includeDocuments);
        if (galleryIntents.size() == 0) {
            // if no intents found for get-content try pick intent action (Huawei P9).
            galleryIntents = getGalleryIntents(packageManager, Intent.ACTION_PICK, includeDocuments);
        }
        allIntents.addAll(galleryIntents);

        Intent target;
        if (allIntents.isEmpty()) {
            target = new Intent();
        } else {
            target = allIntents.get(allIntents.size() - 1);
            allIntents.remove(allIntents.size() - 1);
        }

        // Create a chooser from the main  intent
        Intent chooserIntent = Intent.createChooser(target, "Datei wählen");

        // Add all other intents
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, allIntents.toArray(new Parcelable[allIntents.size()]));

        return chooserIntent;

    }

    public static Uri getUriFromResult(Context context, int resultCode, Intent imageReturnedIntent) {
        File imageFile = getTempFile(context);
        Uri selectedImage = null;
        if (resultCode == Activity.RESULT_OK) {
            boolean isCamera = (imageReturnedIntent == null || imageReturnedIntent.getData() == null  || imageReturnedIntent.getData().toString().contains(imageFile.toString()));
            if (isCamera) {     /** CAMERA **/
                selectedImage = FileProvider.getUriForFile(context, context.getPackageName() + ".kftools.fileprovider", getTempFile(context));
            }
            else {            /** ALBUM **/
                selectedImage = imageReturnedIntent.getData();
            }
        }
        return selectedImage;
    }

    public static File getFileFromResult(Context context, int resultCode, Intent imageReturnedIntent) {
        Uri uri = getUriFromResult(context, resultCode, imageReturnedIntent);

        File file = null;
        if (uri.toString().contains("kftools.fileprovider")) {
            file = getTempFile(context);
        }
        else if (uri.getScheme().equals("content")) {
            ContentResolver contentResolver = context.getContentResolver();
            file = getTempFile(context);
            try {
                FileOutputStream out = new FileOutputStream(file);
                InputStream in = contentResolver.openInputStream(uri);
                byte[] buffer = new byte[1024];
                int read;
                while ((read = in.read(buffer)) != -1) {
                    out.write(buffer, 0, read);
                }
                out.close();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        else if (uri.getScheme().equals("file")) {
            file = new File(uri.getPath());
        }

        return file;
    }


    private static List<Intent> getCameraIntents(
            @NonNull Context context, @NonNull PackageManager packageManager) {

        List<Intent> allIntents = new ArrayList<>();

        // Determine Uri of camera image to  save.
        Uri outputFileUri = FileProvider.getUriForFile(context, context.getPackageName() + ".kftools.fileprovider", getTempFile(context));

        Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        List<ResolveInfo> listCam = packageManager.queryIntentActivities(captureIntent, 0);
        for (ResolveInfo res : listCam) {
            Intent intent = new Intent(captureIntent);
            intent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
            intent.setPackage(res.activityInfo.packageName);
            if (outputFileUri != null) {
                intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
            }
            allIntents.add(intent);
        }

        return allIntents;
    }

    /**
     * Get all Gallery intents for getting image from one of the apps of the device that handle
     * images.
     */
    private static List<Intent> getGalleryIntents(
            @NonNull PackageManager packageManager, String action, boolean includeDocuments) {
        List<Intent> intents = new ArrayList<>();
        Intent galleryIntent =
                action == Intent.ACTION_GET_CONTENT
                        ? new Intent(action)
                        : new Intent(action, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        galleryIntent.setType("image/*");
        List<ResolveInfo> listGallery = packageManager.queryIntentActivities(galleryIntent, 0);
        for (ResolveInfo res : listGallery) {
            Intent intent = new Intent(galleryIntent);
            intent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
            intent.setPackage(res.activityInfo.packageName);
            intents.add(intent);
        }

        // remove documents intent
        if (!includeDocuments) {
            for (Intent intent : intents) {
                if (intent
                        .getComponent()
                        .getClassName()
                        .equals("com.android.documentsui.DocumentsActivity")) {
                    intents.remove(intent);
                    break;
                }
            }
        }
        return intents;
    }

    private static File getTempFile(Context context) {
        File imageFile = new File(context.getCacheDir().getAbsolutePath() + "/image-cache/", TEMP_IMAGE_NAME);
        imageFile.getParentFile().mkdirs();
        return imageFile;
    }
}
