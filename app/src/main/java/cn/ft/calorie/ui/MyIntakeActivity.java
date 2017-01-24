package cn.ft.calorie.ui;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.ft.calorie.R;
import cn.ft.calorie.pojo.IntakeRecord;
import cn.ft.calorie.pojo.sectionrecyclerview.MyIntakeSection;
import cn.ft.calorie.ui.adapter.MyIntakeAdapter;
import cn.ft.calorie.util.SubscriptionUtils;
import cn.ft.calorie.util.TimeUtils;
import cn.ft.calorie.util.Utils;
import cn.ft.calorie.widget.SectionedExpandableGridAdapter;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MyIntakeActivity extends ToolbarActivity {
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;


    MyIntakeAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected void setLayout() {
        setContentView(R.layout.activity_my_intake);
    }

    @Override
    protected void bindViews() {
        ButterKnife.bind(this);
        toolbar.setTitle("我的饮食");
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,1);
        adapter = new MyIntakeAdapter(
                this,
                gridLayoutManager);
        adapter.setOnItemClickLietener((position, data, viewType)->{
            if(viewType==adapter.VIEW_TYPE_ITEM){
                IntakeRecord r = (IntakeRecord) data;
                Utils.toast(this,r.getId()+"-"+r.getFood().getName());
            }else{
                adapter.sectionStateChanged((SectionedExpandableGridAdapter.Section) data);
            }

        });
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(adapter);
        swipeRefreshLayout.setOnRefreshListener(()->{
            adapter.removeAll();
            httpRequest();
        });

        httpRequest();
    }

    @Override
    protected void bindListeners() {

    }

    void httpRequest(){
        Map<String,String> options = new HashMap<>();
        options.put("userId", Utils.loginUser.getId());
        options.put("startDate",new Date((2016-1900),0,21).toGMTString());
        options.put("endDate",new Date((2017-1900),2,1).toGMTString());

        LinkedHashMap<MyIntakeSection,List<IntakeRecord>> recordLinkedMap = new LinkedHashMap<>();
        Map<String,MyIntakeSection> sectionMap = new HashMap<>();

        SubscriptionUtils.register(this,apiUtils.getApiDataObservable(
                apiUtils.getApiServiceImpl().getIntakeRecords(options))
                .observeOn(Schedulers.io())
                .map(intakeRecords -> {
                    for(IntakeRecord intakeRecord:intakeRecords){
                        String dataStr = TimeUtils.getFormatDate(intakeRecord.getRecordingTime(),"yyyy-MM-dd");
                        MyIntakeSection targetSection = sectionMap.get(dataStr);
                        List<IntakeRecord> targetList = recordLinkedMap.get(targetSection);
                        if(targetSection == null){
                            MyIntakeSection section = new MyIntakeSection();
                            section.setDate(dataStr);

                            sectionMap.put(section.getSectionName(),section);

                            targetSection = section;
                            targetList = new ArrayList<>();
                            recordLinkedMap.put(section,targetList);
                        }
                        targetList.add(intakeRecord);
                        targetSection.setCalorie(targetSection.getCalorie()+intakeRecord.getCalorie());

                    }
                    for(Map.Entry<MyIntakeSection,List<IntakeRecord>> entry:recordLinkedMap.entrySet()){
                        adapter.addSection(entry.getKey(),entry.getValue());
                    }
                    return null;
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(aVoid->{
                    adapter.myNotifyDataSetChanged();
                    swipeRefreshLayout.setRefreshing(false);
                })
        );
    }
}
