package cn.ft.calorie.event;

import java.util.List;

import cn.ft.calorie.pojo.IntakeRecord;

/**
 * Created by TT on 2017/1/19.
 */
public class IntakeChosenListUpdateEvent {
    private List<IntakeRecord> dataList;

    public IntakeChosenListUpdateEvent(List<IntakeRecord> dataList) {
        this.dataList = dataList;
    }

    public List<IntakeRecord> getDataList() {
        return dataList;
    }

    public void setDataList(List<IntakeRecord> dataList) {
        this.dataList = dataList;
    }
}
