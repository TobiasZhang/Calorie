package cn.ft.calorie.event;

import cn.ft.calorie.pojo.UserInfo;

/**
 * Created by TT on 2017/1/19.
 */
public class UserInfoUpdateEvent {
    private UserInfo newUser;

    public UserInfo getNewUser() {
        return newUser;
    }

    public void setNewUser(UserInfo newUser) {
        this.newUser = newUser;
    }

    public UserInfoUpdateEvent(UserInfo newUser) {

        this.newUser = newUser;
    }
}
