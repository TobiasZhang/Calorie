package cn.ft.calorie.ui;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

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
import cn.ft.calorie.ui.adapter.MyWeightAdapter;
import cn.ft.calorie.util.SubscriptionUtils;
import cn.ft.calorie.util.Utils;
import cn.ft.calorie.widget.SectionedExpandableGridAdapter;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MyWeightActivity extends ToolbarActivity {
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;

    MyWeightAdapter adapter;
    @Override
    protected void setLayout() {
        setContentView(R.layout.activity_my_weight);
    }
    @Override
    protected void bindViews() {
        ButterKnife.bind(this);
        toolbar.setTitle("我的体重");
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,1);
        adapter = new MyWeightAdapter(this);
        recyclerView.setLayoutManager(linearLayoutManager);
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

        SubscriptionUtils.register(this,apiUtils.getApiDataObservable(
                apiUtils.getApiServiceImpl().getWeigthRecords(options))
                .subscribe(records->{
                    adapter.addItems(records);
                    swipeRefreshLayout.setRefreshing(false);
                },e->{
                    e.printStackTrace();
                    Utils.toast(this,e.getMessage());
                })
        );
    }
}
