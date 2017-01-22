package cn.ft.calorie.pojo;

import java.io.Serializable;

import io.realm.RealmObject;

/**
 * Created by TT on 2017/1/22.
 */
public class UnitConvert extends RealmObject implements Serializable {
    private String unitName;
    private Integer gram;

    public Integer getGram() {
        return gram;
    }

    public void setGram(Integer gram) {
        this.gram = gram;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }
}
