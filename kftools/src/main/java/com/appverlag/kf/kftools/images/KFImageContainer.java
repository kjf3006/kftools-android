package com.appverlag.kf.kftools.images;

/**
 * Copyright (C) Kevin Flachsmann - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Created by kevinflachsmann on 22.07.18.
 */
public class KFImageContainer {

    public enum Type { KEY, URL };


    private String key, url;
    private Type type = Type.KEY;

    public static KFImageContainer key(String key) {
        KFImageContainer container = new KFImageContainer();
        container.key = key;
        container.type = Type.KEY;
        return container;
    }

    public static KFImageContainer url(String url) {
        KFImageContainer container = new KFImageContainer();
        container.url = url;
        container.type = Type.URL;
        return container;
    }

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
}
