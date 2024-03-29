package com.appverlag.kf.kftools.network;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Size;

import com.appverlag.kf.kftools.images.BitmapSizeEngine;


import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.BufferedSource;

public class ResponseImageSerializer extends ResponseSerializer<Bitmap> {

    private Size desiredSize;

    public ResponseImageSerializer() {}

    public ResponseImageSerializer(Size desiredSize) {
        this.desiredSize = desiredSize;
    }

    @Override
    public Bitmap serialize(Response response) throws Exception {
        ResponseBody body = response.body();
        if (body == null) {
            throw NetworkException.noDataReceived();
        }

        if (body.contentLength() == 0) {
            body.close();
            throw new NetworkException("Received response with 0 content-length header.");
        }

        BufferedSource bufferedSource = body.source();

        BitmapFactory.Options options = new BitmapFactory.Options();

        if (desiredSize != null) {
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(bufferedSource.peek().inputStream(), null, options);
            options.inSampleSize = BitmapSizeEngine.calculateInSampleSize(new Size(options.outWidth, options.outHeight), desiredSize);
            options.inJustDecodeBounds = false;
        }

        Bitmap bitmap = BitmapFactory.decodeStream(bufferedSource.inputStream(), null, options);
        bufferedSource.close();

        if (bitmap == null) {
            throw NetworkException.invalidDataReceived();
        }
        return bitmap;
    }
}
