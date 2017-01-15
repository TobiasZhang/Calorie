package cn.ft.calorie.pojo;

import java.io.Serializable;
import java.util.Date;

import io.realm.RealmObject;

/**
 * Created by TT on 2017/1/16.
 */
public class Meta extends RealmObject implements Serializable {
    private Date createdAt;
    private Date updatedAt;

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }
}
