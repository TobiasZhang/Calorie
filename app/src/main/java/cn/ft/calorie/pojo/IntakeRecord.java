package cn.ft.calorie.pojo;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import io.realm.RealmObject;

/**
 * Created by TT on 2017/1/21.
 */
public class IntakeRecord extends RealmObject implements Serializable {
    @SerializedName("_id")
    private String id;
    private UserInfo userInfo;
    private Integer calorie;
    private Food food;
    private Integer foodCount;
    private String foddUnit;
    private Integer foodWeight;
    private Meta meta;

    public Integer getCalorie() {
        return calorie;
    }

    public void setCalorie(Integer calorie) {
        this.calorie = calorie;
    }

    public String getFoddUnit() {
        return foddUnit;
    }

    public void setFoddUnit(String foddUnit) {
        this.foddUnit = foddUnit;
    }

    public Food getFood() {
        return food;
    }

    public void setFood(Food food) {
        this.food = food;
    }

    public Integer getFoodCount() {
        return foodCount;
    }

    public void setFoodCount(Integer foodCount) {
        this.foodCount = foodCount;
    }

    public Integer getFoodWeight() {
        return foodWeight;
    }

    public void setFoodWeight(Integer foodWeight) {
        this.foodWeight = foodWeight;
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
}
