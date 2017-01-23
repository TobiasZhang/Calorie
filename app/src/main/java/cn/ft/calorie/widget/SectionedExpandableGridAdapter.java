package cn.ft.calorie.widget;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import cn.ft.calorie.pojo.IntakeRecord;

/**
 * Created by lenovo on 2/23/2016.
 */
public abstract class SectionedExpandableGridAdapter<SEC extends SectionedExpandableGridAdapter.Section,ITEM,VH extends RecyclerView.ViewHolder> extends BaseRecyclerAdapter<Object,VH> {
    //data list
    protected LinkedHashMap<SEC, List<ITEM>> mSectionDataMap = new LinkedHashMap<>();

    //section map
    //TODO : look for a way to avoid this
    protected HashMap<String, SEC> mSectionMap = new HashMap<>();

    public SectionedExpandableGridAdapter(Context context,
                                          final GridLayoutManager gridLayoutManager
                                          ) {
        super(context);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return isSection(position)?gridLayoutManager.getSpanCount():1;
            }
        });
    }

    protected abstract boolean isSection(int position);

    public void myNotifyDataSetChanged() {
        //TODO : handle this condition such that these functions won't be called if the recycler view is on scroll
        generateDataList();
        notifyDataSetChanged();
    }
    private void generateDataList () {
        mDataList.clear();
        for (Map.Entry<SEC, List<ITEM>> entry : mSectionDataMap.entrySet()) {
            SEC key = entry.getKey();
            mDataList.add(key);
            if (key.isExpanded)
                mDataList.addAll(entry.getValue());
        }

    }
    public void addSection(SEC newSection, List<ITEM> items) {
        mSectionMap.put(newSection.getSectionName(), newSection);
        mSectionDataMap.put(newSection, items);
    }

    public void addSectionItem(String sectionName, ITEM item) {
        mSectionDataMap.get(mSectionMap.get(sectionName)).add(item);
    }

    public void removeSectionItem(String sectionName, ITEM item) {
        mSectionDataMap.get(mSectionMap.get(sectionName)).remove(item);
    }

    public void removeSection(String sectionName) {
        mSectionDataMap.remove(mSectionMap.get(sectionName));
        mSectionMap.remove(sectionName);
    }

    public static abstract class Section{
        public boolean isExpanded;
        public Section() {
            isExpanded = true;
        }
        public abstract String getSectionName();
    }

    public void removeAll(){
        mSectionDataMap.clear();
        mSectionMap.clear();
        mDataList.clear();
    }
}
