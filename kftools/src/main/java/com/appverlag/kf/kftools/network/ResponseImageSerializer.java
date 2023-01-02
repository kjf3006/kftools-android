package com.appverlag.kf.kftools.network;

import android.graphics.Bitmap;

import com.appverlag.kf.kftools.images.BitmapSizeEngine;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import okhttp3.Response;
import okhttp3.ResponseBody;

public class ResponseImageSerializer extends ResponseSerializer<Bitmap> {

    @Override
    public Bitmap serialize(Response response) throws Exception {
        ResponseBody body = response.body();
        if (body == null) {
            throw new Exception("Es wurden keine Daten empfangen.");
        }

        InputStream is  = body.byteStream();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        while (true) {
            int r = is.read(buffer);
            if (r == -1) break;
            out.write(buffer, 0, r);
        }
        byte[] data = out.toByteArray();

        return BitmapSizeEngine.resizeBitmap(data);
    }
}
