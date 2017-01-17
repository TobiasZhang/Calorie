package cn.ft.calorie;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import cn.ft.calorie.api.ApiUtils;

/**
 * Created by TT on 2017/1/16.
 */
public abstract class ToolbarActivity extends AppCompatActivity {
    protected ApiUtils apiUtils = ApiUtils.getInstance();
    protected Toolbar toolbar;
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
        //去除标题
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }
    abstract void setLayout();
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home://返回
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
