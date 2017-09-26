package com.appverlag.kf.kftools.images;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.File;

/**
 * Copyright (C) Kevin Flachsmann - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Created by kevinflachsmann on 22.09.17.
 */
public class KFImageManagerBitmapEngine {

    public Bitmap resizeBitmap(final Bitmap bitmap, final int desiredWidth, final int desiredHeight) {
        double sampleSize = 1.0/calculateInSampleSize(bitmap.getWidth(), bitmap.getHeight(), desiredWidth, desiredHeight);
        return Bitmap.createScaledBitmap(bitmap, (int) (bitmap.getWidth()*sampleSize), (int) (bitmap.getHeight()*sampleSize), true);

    }

    public Bitmap resizeBitmap(final File file, final int desiredWidth, final int desiredHeight) {
        if (!file.exists()) return null;

        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(file.getAbsolutePath(), options);

        options.inSampleSize = calculateInSampleSize(options.outWidth, options.outHeight, desiredWidth, desiredHeight);
        options.inJustDecodeBounds = false;

        return BitmapFactory.decodeFile(file.getAbsolutePath(), options);

    }

    public Bitmap resizeBitmap(final byte[] data, final int desiredWidth, final int desiredHeight) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeByteArray(data, 0, data.length, options);

        options.inSampleSize = calculateInSampleSize(options.outWidth, options.outHeight, desiredWidth, desiredHeight);
        options.inJustDecodeBounds = false;
        options.inPreferredConfig = Bitmap.Config.RGB_565;

        return BitmapFactory.decodeByteArray(data, 0, data.length, options);
    }


    private int calculateInSampleSize(final int width, final int height, final int desiredWidth, final int desiredHeight) {

        if (desiredWidth <= 0 && desiredHeight <= 0) return 1;

        int inSampleSize = 1;

        if (height > desiredHeight || width > desiredWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= desiredHeight
                    && (halfWidth / inSampleSize) >= desiredWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

}
