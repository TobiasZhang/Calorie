package cn.ft.calorie.event;

import cn.ft.calorie.pojo.UserInfo;

/**
 * Created by TT on 2017/1/19.
 */
public class UserInfoUpdateEvent {
    private UserInfo newUser;
    private boolean isUserChanged;

    public UserInfo getNewUser() {
        return newUser;
    }

    public void setNewUser(UserInfo newUser) {
        this.newUser = newUser;
    }

    public boolean isUserChanged() {
        return isUserChanged;
    }

    public void setUserChanged(boolean userChanged) {
        isUserChanged = userChanged;
    }

    public UserInfoUpdateEvent(boolean isUserChanged, UserInfo newUser) {
        this.newUser = newUser;
        this.isUserChanged = isUserChanged;

    }
}
