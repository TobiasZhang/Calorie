package cn.ft.calorie.event;

import cn.ft.calorie.pojo.BurnRecord;
import cn.ft.calorie.pojo.IntakeRecord;
import cn.ft.calorie.pojo.WeightRecord;

/**
 * Created by TT on 2017/1/19.
 */
public class IntakeRecordUpdateEvent {
    private IntakeRecord intakeRecord;
    public IntakeRecordUpdateEvent(IntakeRecord intakeRecord) {

        this.intakeRecord = intakeRecord;
    }
}
