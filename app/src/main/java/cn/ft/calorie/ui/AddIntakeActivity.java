package cn.ft.calorie.ui;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.style.ForegroundColorSpan;
import android.view.Menu;
import android.view.MenuItem;

import com.binaryfork.spanny.Spanny;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.ft.calorie.R;
import cn.ft.calorie.event.HomeRecordUpdateEvent;
import cn.ft.calorie.event.IntakeChosenListUpdateEvent;
import cn.ft.calorie.pojo.IntakeRecord;
import cn.ft.calorie.ui.fragment.AddIntakeChooseFragment;
import cn.ft.calorie.ui.fragment.AddIntakeChosenFragment;
import cn.ft.calorie.util.RxBus;
import cn.ft.calorie.util.SubscriptionUtils;
import cn.ft.calorie.util.Utils;
import cn.ft.calorie.widget.MyFragmentPagerAdapter;

public class AddIntakeActivity extends ToolbarActivity {
    @BindView(R.id.tabLayout)
    TabLayout tabLayout;
    @BindView(R.id.viewPager)
    ViewPager viewPager;

    List<IntakeRecord> chosenList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    protected void setLayout() {
        setContentView(R.layout.activity_add_intake);
    }
    @Override
    protected void bindViews() {
        ButterKnife.bind(this);
        toolbar.setTitle("饮食记录");
        List<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(new AddIntakeChooseFragment());
        fragmentList.add(new AddIntakeChosenFragment());
        List<String> fragmentTitleList = new ArrayList<>();
        fragmentTitleList.add("食物选择");
        fragmentTitleList.add("已选择(0)");
        FragmentPagerAdapter fragmentPagerAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager(),fragmentList,fragmentTitleList);
        viewPager.setAdapter(fragmentPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    protected void bindListeners() {
        SubscriptionUtils.register(this,
                RxBus.getDefault().toObservable(IntakeChosenListUpdateEvent.class)
                        .subscribe(event->{
                            chosenList = event.getDataList();
                            int count = chosenList.size();
                            CharSequence title = "已选择("+count+")";
                            /*if(count > 0){
                            }else{
                            }*/
                            tabLayout.getTabAt(1).setText(title);
                        }));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_add_intake,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.ok:
                if(chosenList==null||chosenList.size()==0){
                    Utils.toast(this,"请选择食物");
                }else{
                    Date currDate = new Date();
                    for(IntakeRecord intakeRecord:chosenList){
                        System.out.println(intakeRecord.getFood().getName()+"----添加");
                        intakeRecord.setUserInfo(Utils.loginUser);
                        intakeRecord.setRecordingTime(currDate);
                        intakeRecord.setCalorie(intakeRecord.getFood().getCalorie()*intakeRecord.getFoodWeight()/100);
                    }
                    SubscriptionUtils.register(this,
                            apiUtils.getApiDataObservable(
                                    apiUtils.getApiServiceImpl()
                                            .mergeIntakeRecords(chosenList))
                            .subscribe(records->{
                                Intent intent = new Intent(this,CompleteIntakeActivity.class);
                                intent.putExtra("intakeRecords", (Serializable) records);
                                //更新主页数据
                                RxBus.getDefault().post(new HomeRecordUpdateEvent());
                                Utils.toast(this,"记录成功");
                                startActivity(intent);
                                finish();
                            },e->{
                                Utils.toast(this,e.getMessage());
                                e.printStackTrace();
                            }));
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
