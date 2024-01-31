package com.appverlag.kf.kftools.images;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import androidx.annotation.DrawableRes;

import java.io.Serializable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.Request;

/**
 * Copyright (C) Kevin Flachsmann - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Created by kevinflachsmann on 22.07.18.
 */
public class ImageContainer implements Serializable {

    public enum Type { KEY, URL, RES, BMP, MAP }


    private String key, url, youtubeID;
    private Type type = Type.KEY;
    private @DrawableRes int placeholderRes;
    private Bitmap placeholderBitmap, bitmap;
    private @DrawableRes int resId;
    private Request request;

    private MapSnapshotOptions mapOptions;

    /*
    res
     */

    public static ImageContainer res(@DrawableRes int resId) {
        ImageContainer container = new ImageContainer();
        container.resId = resId;
        container.type = Type.RES;
        return container;
    }

    /*
    BMP
     */

    public static ImageContainer bmp(Bitmap bitmap) {
        ImageContainer container = new ImageContainer();
        container.bitmap = bitmap;
        container.type = Type.BMP;
        return container;
    }



    /*
    key
     */
    public static ImageContainer key(String key) {
        return key(key, 0);
    }

    public static ImageContainer key(String key, @DrawableRes int placeholder) {
        ImageContainer container = new ImageContainer();
        container.key = key;
        container.type = Type.KEY;
        container.placeholderRes = placeholder;
        return container;
    }

    public static ImageContainer key(String key, Bitmap placeholder) {
        ImageContainer container = new ImageContainer();
        container.key = key;
        container.type = Type.KEY;
        container.placeholderBitmap = placeholder;
        return container;
    }

    /*
    url
     */
    public static ImageContainer url(String url) {
        return url(url, 0);
    }

    public static ImageContainer url(String url, @DrawableRes int placeholder) {
        ImageContainer container = new ImageContainer();
        container.url = url;
        container.type = Type.URL;
        container.placeholderRes = placeholder;
        return container;
    }

    public static ImageContainer url(String url, Bitmap placeholder) {
        ImageContainer container = new ImageContainer();
        container.url = url;
        container.type = Type.URL;
        container.placeholderBitmap = placeholder;
        return container;
    }

    public static ImageContainer url(Request request, Bitmap placeholder) {
        ImageContainer container = new ImageContainer();
        container.request = request;
        container.type = Type.URL;
        container.placeholderBitmap = placeholder;
        return container;
    }

    /*
    youtube
     */
    public static ImageContainer youtubeID(String youtubeID) {
        return youtubeID(youtubeID, 0);
    }

    public static ImageContainer youtubeID(String youtubeID, Bitmap placeholder) {
        ImageContainer container = new ImageContainer();
        container.url = "https://img.youtube.com/vi/" + youtubeID + "/hqdefault.jpg";
        container.type = Type.URL;
        container.placeholderBitmap = placeholder;
        return container;
    }

    public static ImageContainer youtubeID(String youtubeID, @DrawableRes int placeholder) {
        ImageContainer container = new ImageContainer();
        container.url = "https://img.youtube.com/vi/" + youtubeID + "/hqdefault.jpg";
        container.type = Type.URL;
        container.placeholderRes = placeholder;
        return container;
    }

    public static ImageContainer youtubeURL(String youtubeURL) {
        return youtubeID(youtubeIDFormURL(youtubeURL), 0);
    }

    public static ImageContainer youtubeURL(String youtubeURL, @DrawableRes int placeholder) {
        return youtubeID(youtubeIDFormURL(youtubeURL), placeholder);
    }

    public static ImageContainer youtubeURL(String youtubeURL, Bitmap placeholder) {
        return youtubeID(youtubeIDFormURL(youtubeURL), placeholder);
    }

    public Bitmap getPlaceholder(Context context) {
        if (placeholderBitmap != null) {
            return placeholderBitmap;
        }
        return BitmapFactory.decodeResource(context.getResources(), placeholderRes);
    }

    /*
    getter & setter
     */

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Type getType() {
        return type;
    }

    public String getYoutubeID() {
        return youtubeID;
    }

    public void setYoutubeID(String youtubeID) {
        this.youtubeID = youtubeID;
    }

    public Integer getPlaceholderRes() {
        return placeholderRes;
    }

    public void setPlaceholderRes(Integer placeholderRes) {
        this.placeholderRes = placeholderRes;
    }

    public Bitmap getPlaceholderBitmap() {
        return placeholderBitmap;
    }

    public void setPlaceholderBitmap(Bitmap placeholderBitmap) {
        this.placeholderBitmap = placeholderBitmap;
    }

    public int getResId() {
        return resId;
    }

    public void setResId(int resId) {
        this.resId = resId;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    /*
    helper
     */

    private static String youtubeIDFormURL(String url) {
        Matcher m = Pattern.compile("((?<=(v|V)/)|(?<=be/)|(?<=(\\?|\\&)v=)|(?<=embed/))([\\w-]++)")
                .matcher(url);
        String youtubeID = "";
        if (m.find()) {
            youtubeID = m.group();
        }
        return youtubeID;
    }
}
