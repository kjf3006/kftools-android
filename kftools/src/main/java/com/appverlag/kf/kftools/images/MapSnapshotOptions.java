package com.appverlag.kf.kftools.images;

import android.util.Size;

import androidx.annotation.NonNull;

public class MapSnapshotOptions {

    public enum MapType {
        STANDARD, SATTELITE, HYBRID
    }

    private double mLatitude = 0;
    private double mLongitude = 0;
    private double mZoom = 15;
    private Size mSize = new Size(500, 500);

    @NonNull private MapType mMapType = MapType.STANDARD;

    public MapSnapshotOptions(double latitude, double longitude, @NonNull MapType mapType) {
        this.mLatitude = latitude;
        this.mLongitude = longitude;
        this.mMapType = mapType;
    }

    /**
     * Latitude for the center point of the map; a number between -85.0511 and 85.0511.
     * @return latitude
     */
    public double getLatitude() {
        return mLatitude;
    }

    public void setLatitude(double latitude) {
        this.mLatitude = latitude;
    }

    /**
     * Longitude for the center point of the map; a number between -180 and 180.
     * @return longitude
     */
    public double getLongitude() {
        return mLongitude;
    }

    public void setLongitude(double longitude) {
        this.mLongitude = longitude;
    }

    /**
     * Zoom level; a number between 0 and 22. Fractional zoom levels will be rounded to two decimal places.
     * @return zoom level
     */
    public double getZoom() {
        return mZoom;
    }

    public void setZoom(double zoom) {
        this.mZoom = zoom;
    }

    /**
     * The size of the image that you want to create.
     * @return size
     */
    public Size getSize() {
        return mSize;
    }

    public void setSize(Size size) {
        this.mSize = size;
    }

    /**
     * The map’s visual style.
     * @return maptype
     */
    @NonNull
    public MapType getMapType() {
        return mMapType;
    }

    public void setMapType(@NonNull MapType mapType) {
        this.mMapType = mapType;
    }
}
