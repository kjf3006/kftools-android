package com.appverlag.kf.kftools.network;

import android.graphics.BitmapFactory;

public class ImageSize {

    public int width;
    public int height;

    public ImageSize(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public ImageSize(BitmapFactory.Options options) {
        this.width = options.outWidth;
        this.height = options.outHeight;
    }
}
