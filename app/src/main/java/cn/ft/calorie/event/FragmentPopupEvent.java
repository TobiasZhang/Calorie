package cn.ft.calorie.event;

/**
 * Created by TT on 2017/1/18.
 */
public class FragmentPopupEvent {
    private int backStackEntryCount;

    public int getBackStackEntryCount() {
        return backStackEntryCount;
    }

    public void setBackStackEntryCount(int backStackEntryCount) {
        this.backStackEntryCount = backStackEntryCount;
    }

    public FragmentPopupEvent(int backStackEntryCount) {

        this.backStackEntryCount = backStackEntryCount;
    }
}
