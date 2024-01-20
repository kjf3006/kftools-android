package com.appverlag.kf.kftools.images;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import androidx.annotation.NonNull;

import com.appverlag.kf.kftools.network.ImageSize;

import java.io.File;
import java.io.InputStream;

public class BitmapSizeEngine {

    public static final int DEFAULT_MAX_WIDTH =  2048;
    public static final int DEFAULT_MAX_HEIGHT =  2048;

    public static Bitmap resizeBitmap(final byte[] data) {
        return resizeBitmap(data, DEFAULT_MAX_WIDTH, DEFAULT_MAX_HEIGHT);
    }

    public static Bitmap resizeBitmap(final byte[] data, final int desiredWidth, final int desiredHeight) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeByteArray(data, 0, data.length, options);

        options.inSampleSize = calculateInSampleSize(options.outWidth, options.outHeight, desiredWidth, desiredHeight);
        options.inJustDecodeBounds = false;
        options.inPreferredConfig = Bitmap.Config.RGB_565;

        return BitmapFactory.decodeByteArray(data, 0, data.length, options);
    }

    public static Bitmap resizeBitmap(final File file, final int desiredWidth, final int desiredHeight) {
        String filePath = file.getAbsolutePath();

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);

        options.inSampleSize = calculateInSampleSize(options.outWidth, options.outHeight, desiredWidth, desiredHeight);
        options.inJustDecodeBounds = false;
        options.inPreferredConfig = Bitmap.Config.RGB_565;

        return BitmapFactory.decodeFile(filePath, options);
    }

    public static Bitmap resizeBitmap(final InputStream inputStream, final int desiredWidth, final int desiredHeight) {

        BitmapFactory.Options options = new BitmapFactory.Options();

        if (desiredWidth != 0 && desiredHeight != 0) {
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(inputStream, null, options);

            options.inSampleSize = calculateInSampleSize(options.outWidth, options.outHeight, desiredWidth, desiredHeight);
        }
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeStream(inputStream, null, options);
    }

    public static int calculateInSampleSize(@NonNull final ImageSize size, @NonNull final ImageSize desiredSize) {
        return calculateInSampleSize(size.width, size.height, desiredSize.width, desiredSize.height);
    }

    private static int calculateInSampleSize(final int width, final int height, final int desiredWidth, final int desiredHeight) {

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
