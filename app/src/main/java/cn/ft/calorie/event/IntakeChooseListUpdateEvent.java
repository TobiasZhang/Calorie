package cn.ft.calorie.event;

import java.util.List;

import cn.ft.calorie.pojo.BurnRecord;
import cn.ft.calorie.pojo.Food;

/**
 * Created by TT on 2017/1/19.
 */
public class IntakeChooseListUpdateEvent {
    private List<Food> datas;
    public IntakeChooseListUpdateEvent(List<Food> datas) {

        this.datas = datas;
    }
}
