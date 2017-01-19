package cn.ft.calorie.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import cn.ft.calorie.R;
import cn.ft.calorie.api.ApiUtils;
import cn.ft.calorie.event.FragmentPopupEvent;
import cn.ft.calorie.util.RxBus;
import cn.ft.calorie.util.SubscriptionUtils;

/**
 * Created by TT on 2017/1/16.
 */
public abstract class ToolbarActivity extends AppCompatActivity {
    public ApiUtils apiUtils = ApiUtils.getInstance();
    public Toolbar toolbar;
    //protected TextView titleTxt;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLayout();
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        //titleTxt = (TextView) findViewById(R.id.title);
        setSupportActionBar(toolbar);
        //<-箭头
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //去除action标题,让toolbar的标题正常显示
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        bindViews();
        bindListeners();

        /*toolbar.setNavigationOnClickListener(v->{
            if(getSupportFragmentManager().popBackStackImmediate())
                RxBus.getDefault().post(new FragmentPopupEvent("update"));
            else
                finish();
        });*/
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home://返回
                if(!getSupportFragmentManager().popBackStackImmediate())
                    finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    protected abstract void setLayout();
    protected abstract void bindViews();
    protected abstract void bindListeners();

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SubscriptionUtils.unregister(this);
    }
}
