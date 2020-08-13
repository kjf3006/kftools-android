package com.appverlag.kf.kftools.images;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Copyright (C) Kevin Flachsmann - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Created by kevinflachsmann on 22.07.18.
 */
public class KFImageContainer {

    public enum Type { KEY, URL, RES }
    public enum Size {SMALL, MEDIUM, LARGE, ORIGINAL}; //100x100 500x500 1000x1000 0x0


    private String key, url, youtubeID;
    private Type type = Type.KEY;
    private @DrawableRes int placeholder;
    private Bitmap placeholderBitmap;
    private @DrawableRes int resId;

    /*
    res
     */

    public static KFImageContainer res(@DrawableRes int resId) {
        return res(resId, 0);
    }

    public static KFImageContainer res(@DrawableRes int resId, @DrawableRes int placeholder) {
        KFImageContainer container = new KFImageContainer();
        container.resId = resId;
        container.type = Type.RES;
        container.placeholder = placeholder;
        return container;
    }

    public static KFImageContainer res(@DrawableRes int resId, Bitmap placeholder) {
        KFImageContainer container = new KFImageContainer();
        container.resId = resId;
        container.type = Type.RES;
        container.placeholderBitmap = placeholder;
        return container;
    }


    /*
    key
     */
    public static KFImageContainer key(String key) {
        return key(key, 0);
    }

    public static KFImageContainer key(String key, @DrawableRes int placeholder) {
        KFImageContainer container = new KFImageContainer();
        container.key = key;
        container.type = Type.KEY;
        container.placeholder = placeholder;
        return container;
    }

    public static KFImageContainer key(String key, Bitmap placeholder) {
        KFImageContainer container = new KFImageContainer();
        container.key = key;
        container.type = Type.KEY;
        container.placeholderBitmap = placeholder;
        return container;
    }

    /*
    url
     */
    public static KFImageContainer url(String url) {
        return url(url, 0);
    }

    public static KFImageContainer url(String url, @DrawableRes int placeholder) {
        KFImageContainer container = new KFImageContainer();
        container.url = url;
        container.type = Type.URL;
        container.placeholder = placeholder;
        return container;
    }

    public static KFImageContainer url(String url, Bitmap placeholder) {
        KFImageContainer container = new KFImageContainer();
        container.url = url;
        container.type = Type.URL;
        container.placeholderBitmap = placeholder;
        return container;
    }

    /*
    youtube
     */
    public static KFImageContainer youtubeID(String youtubeID) {
        return youtubeID(youtubeID, 0);
    }

    public static KFImageContainer youtubeID(String youtubeID, Bitmap placeholder) {
        KFImageContainer container = new KFImageContainer();
        container.url = "https://img.youtube.com/vi/" + youtubeID + "/hqdefault.jpg";
        container.type = Type.URL;
        container.placeholderBitmap = placeholder;
        return container;
    }

    public static KFImageContainer youtubeID(String youtubeID, @DrawableRes int placeholder) {
        KFImageContainer container = new KFImageContainer();
        container.url = "https://img.youtube.com/vi/" + youtubeID + "/hqdefault.jpg";
        container.type = Type.URL;
        container.placeholder = placeholder;
        return container;
    }

    public static KFImageContainer youtubeURL(String youtubeURL) {
        return youtubeID(youtubeIDFormURL(youtubeURL), 0);
    }

    public static KFImageContainer youtubeURL(String youtubeURL, @DrawableRes int placeholder) {
        return youtubeID(youtubeIDFormURL(youtubeURL), placeholder);
    }

    public static KFImageContainer youtubeURL(String youtubeURL, Bitmap placeholder) {
        return youtubeID(youtubeIDFormURL(youtubeURL), placeholder);
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

    public Integer getPlaceholder() {
        return placeholder;
    }

    public void setPlaceholder(Integer placeholder) {
        this.placeholder = placeholder;
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
