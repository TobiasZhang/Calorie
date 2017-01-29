package cn.ft.calorie;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.shawnlin.numberpicker.NumberPicker;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.ft.calorie.event.UserInfoUpdateEvent;
import cn.ft.calorie.event.HomeRecordUpdateEvent;
import cn.ft.calorie.pojo.BurnRecord;
import cn.ft.calorie.pojo.IntakeRecord;
import cn.ft.calorie.pojo.WeightRecord;
import cn.ft.calorie.ui.AboutUsActivity;
import cn.ft.calorie.ui.AddBurnActivity;
import cn.ft.calorie.ui.AddIntakeActivity;
import cn.ft.calorie.ui.FeedbackActivity;
import cn.ft.calorie.ui.LoginActivity;
import cn.ft.calorie.ui.MyProfileActivity;
import cn.ft.calorie.ui.MyWeightActivity;
import cn.ft.calorie.ui.ToolbarActivity;
import cn.ft.calorie.ui.adapter.HomeSectionAdapter;
import cn.ft.calorie.util.RxBus;
import cn.ft.calorie.util.SubscriptionUtils;
import cn.ft.calorie.util.TimeUtils;
import cn.ft.calorie.util.Utils;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivity extends ToolbarActivity {
    @BindView(R.id.nicknameTxt)
    TextView nicknameTxt;
    @BindView(R.id.avatar)
    SimpleDraweeView avatar;
    @BindView(R.id.drawerLogin)
    RelativeLayout drawerLogin;
    @BindView(R.id.loginBtn)
    Button loginBtn;
    @BindView(R.id.drawerGuest)
    RelativeLayout drawerGuest;
    @BindView(R.id.clearCacheBtn)
    RelativeLayout clearCacheBtn;
    @BindView(R.id.feedbackBtn)
    RelativeLayout feedbackBtn;
    @BindView(R.id.encourageBtn)
    RelativeLayout encourageBtn;
    @BindView(R.id.shareAppBtn)
    RelativeLayout shareAppBtn;
    @BindView(R.id.aboutUsBtn)
    RelativeLayout aboutUsBtn;
    @BindView(R.id.logoutBtn)
    RelativeLayout logoutBtn;
    @BindView(R.id.drawerLayout)
    DrawerLayout drawerLayout;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.coordinatorLayout)
    CoordinatorLayout coordinatorLayout;

    private List<Map<String,Object>> dataList = new ArrayList<>();
    HomeSectionAdapter homeSectionAdapter;

    int currWeightInteger;
    int currWeightDecimal;

    int currBurn;
    int currIntake;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: 2017/1/22 获取user
        RxBus.getDefault().post(new UserInfoUpdateEvent(true,null));

    }

    @Override
    protected void setLayout() {
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @Override
    protected void bindViews() {
        toolbar.setTitle("首页");
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close);
        //drawertoggle关联
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();


        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        homeSectionAdapter = new HomeSectionAdapter(this);
        recyclerView.setAdapter(homeSectionAdapter);
    }

    @Override
    protected void bindListeners() {
        //去登录
        loginBtn.setOnClickListener(v -> startActivity(new Intent(this, LoginActivity.class)));
        //意见反馈
        feedbackBtn.setOnClickListener(v -> {
            if (Utils.isLogin(this))
                startActivity(new Intent(this, FeedbackActivity.class));
        });
        //关于
        aboutUsBtn.setOnClickListener(v -> startActivity(new Intent(this, AboutUsActivity.class)));
        //注销
        logoutBtn.setOnClickListener(v ->
                new AlertDialog.Builder(this)
                        .setTitle("确认退出登录？")
                        .setNegativeButton("取消", null)
                        .setPositiveButton("确认", (dialog, which) -> {
                            Utils.toast(this, "已退出登录");
                            Utils.doOnLogout();
                        })
                        .show()
        );
        //登录改变事件
        SubscriptionUtils.register(this,
                RxBus.getDefault().toObservable(UserInfoUpdateEvent.class).subscribe(event -> {
                    if(event.isUserChanged()){
                        httpRequest();
                    }
                    if (event.getNewUser() == null) {//注销
                        drawerLogin.setVisibility(View.GONE);
                        drawerGuest.setVisibility(View.VISIBLE);
                        logoutBtn.setVisibility(View.GONE);
                    } else {//登录
                        drawerLogin.setVisibility(View.VISIBLE);
                        drawerGuest.setVisibility(View.GONE);
                        avatar.setImageURI(event.getNewUser().getAvatarDisplayUrl());
                        nicknameTxt.setText(event.getNewUser().getNickname());
                        logoutBtn.setVisibility(View.VISIBLE);
                    }

                }));
        //体重记录添加事件
        SubscriptionUtils.register(this,
                RxBus.getDefault().toObservable(HomeRecordUpdateEvent.class).subscribe(event->{
                    httpRequest();
                }));
        //点击板块
        homeSectionAdapter.setOnItemClickLietener((position,  data, viewType)-> {
            //if(!Utils.isLogin(this))return;
            switch (viewType){
                //添加锻炼记录
                case HomeSectionAdapter.ITEM_HOME_BURN:
                    Intent addBurnIntent = new Intent(this, AddBurnActivity.class);
                    addBurnIntent.putExtra("currBurn",this.currBurn);
                    addBurnIntent.putExtra("currIntake",this.currIntake);
                    startActivity(addBurnIntent);
                    break;
                //添加饮食记录
                case HomeSectionAdapter.ITEM_HOME_INTAKE:
                    startActivity(new Intent(this, AddIntakeActivity.class));
                    break;
                //添加体重记录
                case HomeSectionAdapter.ITEM_HOME_WEIGHT:
                    if(Integer.parseInt(data.get("weight").toString())>0){
                        Snackbar
                                .make(coordinatorLayout,"今日体重已记录",2000)
                                .setAction("查看",v->{
                                    startActivity(new Intent(this,MyWeightActivity.class));
                                })
                                .show();
                        return;
                    }
                    int currWeight = Utils.loginUser.getWeight();
                    currWeightInteger = currWeight/10;
                    currWeightDecimal = currWeight%10;
                    View weightPickerView = getLayoutInflater().inflate(R.layout.alertdiaolg_record_weight,null);
                    TextView weightNumTxt = (TextView) weightPickerView.findViewById(R.id.weightNumTxt);
                    NumberPicker weightIntegerPicker = (NumberPicker) weightPickerView.findViewById(R.id.weightIntegerPicker);
                    NumberPicker weightDecimalPicker = (NumberPicker) weightPickerView.findViewById(R.id.weightDecimalPicker);
                    weightIntegerPicker.setValue(currWeightInteger);
                    weightDecimalPicker.setValue(currWeightDecimal);
                    weightNumTxt.setText(currWeightInteger+"."+currWeightDecimal+"/kg");
                    weightIntegerPicker.setOnValueChangedListener((picker, oldVal, newVal) -> {
                        currWeightInteger = newVal;
                        weightNumTxt.setText(currWeightInteger+"."+currWeightDecimal+"/kg");
                    });
                    weightDecimalPicker.setOnValueChangedListener((picker, oldVal, newVal) -> {
                        currWeightDecimal = newVal;
                        weightNumTxt.setText(currWeightInteger+"."+currWeightDecimal+"/kg");
                    });
                    new AlertDialog.Builder(this)
                            .setTitle("体重记录")
                            .setView(weightPickerView)
                            .setNegativeButton("取消",null)
                            .setPositiveButton("确认",(dialog,i)->{
                                WeightRecord newWeightRecord = new WeightRecord();
                                newWeightRecord.setUserInfo(Utils.loginUser);
                                newWeightRecord.setRecordingTime(new Date());
                                newWeightRecord.setWeight(currWeightInteger*10+currWeightDecimal);
                                SubscriptionUtils.register(this,
                                        apiUtils.getApiDataObservable(apiUtils.getApiServiceImpl().mergeWeightRecord(newWeightRecord))
                                                .subscribe(weightRecord ->{
                                                    System.out.println(weightRecord);
                                                    RxBus.getDefault().post(new HomeRecordUpdateEvent());
                                                    Utils.toast(this,"今日体重已记录");
                                                },e->{
                                                    e.printStackTrace();
                                                    Utils.toast(this,e.getMessage());
                                                }));
                            })
                            .show();

                    break;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //toolbar menu
        getMenuInflater().inflate(R.menu.toolbar_main, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            //menu 我的
            case R.id.my:
                if (Utils.isLogin(this))
                    startActivity(new Intent(this, MyProfileActivity.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    void httpRequest(){
        dataList.clear();
        homeSectionAdapter.removeAll();
        Map<String,Object> intakeMap = new HashMap<>();
        intakeMap.put("foodCategoryCount",0);
        intakeMap.put("intakeCalorie",0);
        Map<String,Object> burnMap = new HashMap<>();
        burnMap.put("burnCalorie",0);
        burnMap.put("intakeCalorie",0);
        Map<String,Object> weightMap = new HashMap<>();
        weightMap.put("weight",0);

        dataList.add(intakeMap);
        dataList.add(burnMap);
        dataList.add(weightMap);

        if(Utils.loginUser==null){
            homeSectionAdapter.updateItems(dataList);
            return;
        }
        String userId = Utils.loginUser.getId();


        Map<String,String> options = new HashMap<>();
        options.put("userId",userId);
        options.put("startDate",TimeUtils.getTodayGMTString());
        options.put("endDate",TimeUtils.getTodayGMTString(1));

        SubscriptionUtils.register(this,apiUtils.getApiDataObservable(
                //饮食记录
                apiUtils.getApiServiceImpl().getIntakeRecords(options))
                        .observeOn(Schedulers.io())
                        .flatMap(intakeRecords->{
                            Set<String> foodDistinctSet = new HashSet<String>();
                            int currFoodCategoryCount = 0;
                            int currIntake = 0;
                            for(IntakeRecord intakeRecord:intakeRecords){
                                String foodId = intakeRecord.getFood().getId();
                                if(!foodDistinctSet.contains(foodId)){
                                    foodDistinctSet.add(foodId);
                                    currFoodCategoryCount++;
                                }
                                currIntake += intakeRecord.getCalorie();
                            }
                            //摄入卡路里存至成员变量
                            this.currIntake = currIntake;
                            intakeMap.put("foodCategoryCount",currFoodCategoryCount);//食物种类
                            intakeMap.put("intakeCalorie",currIntake);//饮食记录摄入卡路里
                            burnMap.put("intakeCalorie",currIntake);//锻炼记录摄入卡路里
                            //锻炼记录
                            return apiUtils.getApiDataObservable(apiUtils.getApiServiceImpl().getBurnRecords(options));
                        })
                        .flatMap(burnRecords -> {
                            int currBurn = 0;
                            for(BurnRecord burnRecord:burnRecords){
                                currBurn += burnRecord.getCalorie();
                            }
                            //今天消耗卡路里存至成员变量
                            this.currBurn = currBurn;
                            //今天消耗
                            burnMap.put("burnCalorie",currBurn);

                            return apiUtils.getApiDataObservable(apiUtils.getApiServiceImpl().getWeigthRecords(options));
                        })
                        .map(weigthRecords -> {
                            int weight = 0;
                            if(weigthRecords.size()>0){
                                weight = weigthRecords.get(0).getWeight();
                            }
                            weightMap.put("weight",weight);
                            return null;
                        })
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                aVoid->{
                                    homeSectionAdapter.updateItems(dataList);
                                },
                                e->{
                                    e.printStackTrace();
                                    Utils.toast(this,e.getMessage());
                                }
                        )
                        );
    }
}
