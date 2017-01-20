package cn.ft.calorie.event;

import cn.ft.calorie.pojo.UserInfo;

/**
 * Created by TT on 2017/1/19.
 */
public class UserInfoUpdateEvent {
    private UserInfo loginUser;

    public UserInfo getLoginUser() {
        return loginUser;
    }

    public void setLoginUser(UserInfo loginUser) {
        this.loginUser = loginUser;
    }

    public UserInfoUpdateEvent(UserInfo loginUser) {

        this.loginUser = loginUser;
    }
}
