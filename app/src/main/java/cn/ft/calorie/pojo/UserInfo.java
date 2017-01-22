package cn.ft.calorie.pojo;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Date;

import cn.ft.calorie.api.ApiUtils;
import cn.ft.calorie.util.TimeUtils;
import cn.ft.calorie.util.Utils;
import io.realm.RealmObject;

/**
 * Created by TT on 2017/1/16.
 */
public class UserInfo extends RealmObject implements Serializable {
    @SerializedName("_id")
    private String id;
    private String tel;
    private String password;
    private String sex;
    private Date birthday;
    private Integer height;
    private Integer weight;
    private String avatar;
    private String nickname;
    private Meta meta;

    private Integer burnTotal;

    public Integer getIntakeTotal() {
        return intakeTotal;
    }

    public void setIntakeTotal(Integer intakeTotal) {
        this.intakeTotal = intakeTotal;
    }

    public Integer getBurnTotal() {
        return burnTotal;
    }

    public void setBurnTotal(Integer burnTotal) {
        this.burnTotal = burnTotal;
    }

    private Integer intakeTotal;

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }


    public String getAvatarDisplayUrl(){
        return ApiUtils.BASE_URL+"/upload_avatars/"+avatar;
    }
    public String getBirthdayFormatStr(){
        return TimeUtils.getFormatDate(birthday,"yyyy-MM-dd");
    }
    @Override
    public String toString() {
        return "UserInfo{" +
                "avatar='" + avatar + '\'' +
                ", id='" + id + '\'' +
                ", tel='" + tel + '\'' +
                ", password='" + password + '\'' +
                ", sex='" + sex + '\'' +
                ", birthday=" + birthday +
                ", height=" + height +
                ", weight=" + weight +
                ", nickname='" + nickname + '\'' +
                ", meta=" + meta +
                ", burnTotal=" + burnTotal +
                ", intakeTotal=" + intakeTotal +
                '}';
    }
}
