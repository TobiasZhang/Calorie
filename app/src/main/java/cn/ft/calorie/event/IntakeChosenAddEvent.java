package cn.ft.calorie.event;

import java.util.List;

import cn.ft.calorie.pojo.Food;
import cn.ft.calorie.pojo.IntakeRecord;

/**
 * Created by TT on 2017/1/19.
 */
public class IntakeChosenAddEvent {
    private IntakeRecord data;

    public IntakeChosenAddEvent(IntakeRecord data) {

        this.data = data;
    }

    public IntakeRecord getData() {
        return data;
    }

    public void setData(IntakeRecord data) {
        this.data = data;
    }
}
