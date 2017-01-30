package cn.ft.calorie.pojo.sectionrecyclerview;

import java.io.Serializable;

import io.realm.RealmObject;

/**
 * Created by TT on 2017/1/30.
 */
public class MyLatLng extends RealmObject implements Serializable {
    private double latitude;
    private double longitude;

    public MyLatLng(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public MyLatLng() {
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    @Override
    public String toString() {
        return "MyLatLng{" +
                "latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }
}
