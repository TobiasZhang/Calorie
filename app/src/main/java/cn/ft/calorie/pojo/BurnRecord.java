package cn.ft.calorie.pojo;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import cn.ft.calorie.pojo.sectionrecyclerview.MyLatLng;
import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by TT on 2017/1/21.
 */
public class BurnRecord extends RealmObject implements Serializable {
    @SerializedName("_id")
    @PrimaryKey
    private String id;
    private UserInfo userInfo;
    private Integer calorie;

    private Date startingTime;
    private Date terminalTime;
    private Integer duration;
    private Integer distance;
    private Integer speed;

    private Integer zoom;
    private MyLatLng center;
    private RealmList<MyLatLng> pointList;
    private String mapImage;

    private Meta meta;

    public MyLatLng getCenter() {
        return center;
    }

    public void setCenter(MyLatLng center) {
        this.center = center;
    }

    public String getMapImage() {
        return mapImage;
    }

    public void setMapImage(String mapImage) {
        this.mapImage = mapImage;
    }

    public List<MyLatLng> getPointList() {
        return pointList;
    }

    public void setPointList(RealmList<MyLatLng> pointList) {
        this.pointList = pointList;
    }

    public Integer getZoom() {
        return zoom;
    }

    public void setZoom(Integer zoom) {
        this.zoom = zoom;
    }

    public Integer getCalorie() {
        return calorie;
    }

    public void setCalorie(Integer calorie) {
        this.calorie = calorie;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Meta getMeta() {
        return meta;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    public Integer getDistance() {
        return distance;
    }

    public void setDistance(Integer distance) {
        this.distance = distance;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public Integer getSpeed() {
        return speed;
    }

    public void setSpeed(Integer speed) {
        this.speed = speed;
    }

    public Date getStartingTime() {
        return startingTime;
    }

    public void setStartingTime(Date startingTime) {
        this.startingTime = startingTime;
    }

    public Date getTerminalTime() {
        return terminalTime;
    }

    public void setTerminalTime(Date terminalTime) {
        this.terminalTime = terminalTime;
    }

    @Override
    public String toString() {
        return "BurnRecord{" +
                "calorie=" + calorie +
                ", id='" + id + '\'' +
                ", userInfo=" + userInfo +
                ", startingTime=" + startingTime +
                ", terminalTime=" + terminalTime +
                ", duration=" + duration +
                ", distance=" + distance +
                ", speed=" + speed +
                ", zoom=" + zoom +
                ", center=" + center +
                ", pointList=" + pointList +
                ", mapImage='" + mapImage + '\'' +
                ", meta=" + meta +
                '}';
    }
}
