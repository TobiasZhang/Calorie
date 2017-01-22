package cn.ft.calorie.pojo.sectionrecyclerview;

import cn.ft.calorie.widget.SectionedExpandableGridAdapter;

/**
 * Created by TT on 2017/1/21.
 */
public class MyIntakeSection extends SectionedExpandableGridAdapter.Section{
    private String date;
    private int calorie;

    public int getCalorie() {
        return calorie;
    }

    public void setCalorie(int calorie) {
        this.calorie = calorie;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public String getSectionName() {
        return this.date;
    }
}
