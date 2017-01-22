package cn.ft.calorie.pojo;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

import io.realm.RealmList;
import io.realm.RealmObject;

/**
 * Created by TT on 2017/1/21.
 */
public class Food extends RealmObject implements Serializable {
    @SerializedName("_id")
    private String id;
    private String name;
    private Integer calorie;
    private RealmList<UnitConvert> unitConvertList;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public RealmList<UnitConvert> getUnitConvertList() {
        return unitConvertList;
    }

    public void setUnitConvertList(RealmList<UnitConvert> unitConvertList) {
        this.unitConvertList = unitConvertList;
    }

}
