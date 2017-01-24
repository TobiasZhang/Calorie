package cn.ft.calorie.event;

import cn.ft.calorie.pojo.BurnRecord;
import cn.ft.calorie.pojo.IntakeRecord;
import cn.ft.calorie.pojo.UserInfo;
import cn.ft.calorie.pojo.WeightRecord;

/**
 * Created by TT on 2017/1/19.
 */
public class WeightRecordUpdateEvent {
    private WeightRecord weightRecord;

    public WeightRecord getWeightRecord() {
        return weightRecord;
    }

    public void setWeightRecord(WeightRecord weightRecord) {
        this.weightRecord = weightRecord;
    }

    public WeightRecordUpdateEvent(WeightRecord weightRecord) {

        this.weightRecord = weightRecord;
    }
}
