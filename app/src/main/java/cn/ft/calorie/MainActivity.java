package cn.ft.calorie;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.ft.calorie.event.UserInfoUpdateEvent;
import cn.ft.calorie.ui.LoginActivity;
import cn.ft.calorie.ui.MyProfileActivity;
import cn.ft.calorie.ui.ToolbarActivity;
import cn.ft.calorie.util.RxBus;
import cn.ft.calorie.util.SubscriptionUtils;
import cn.ft.calorie.util.Utils;

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
    @BindView(R.id.aboutBtn)
    RelativeLayout aboutBtn;
    @BindView(R.id.logoutBtn)
    RelativeLayout logoutBtn;
    @BindView(R.id.drawerLayout)
    DrawerLayout drawerLayout;

    ActionBarDrawerToggle actionBarDrawerToggle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void setLayout() {
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @Override
    protected void bindViews() {
        toolbar.setTitle("首页");
        actionBarDrawerToggle = new ActionBarDrawerToggle(this,drawerLayout, toolbar, R.string.open, R.string.close);
        if(Utils.loginUser==null){
            drawerLogin.setVisibility(View.GONE);
            drawerGuest.setVisibility(View.VISIBLE);
        }else{
            drawerLogin.setVisibility(View.VISIBLE);
            drawerGuest.setVisibility(View.GONE);
        }
    }

    @Override
    protected void bindListeners() {
        //menu
        toolbar.setOnMenuItemClickListener(menuItem -> {
            switch (menuItem.getItemId()){
                case R.id.my:
                    if(Utils.isLogin(this))
                        startActivity(new Intent(this,MyProfileActivity.class));
                    break;
            }
            return true;
        });
        //drawertoggle关联
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        //去登录
        loginBtn.setOnClickListener(v -> startActivity(new Intent(this, LoginActivity.class)));
        //注销
        logoutBtn.setOnClickListener(v->
            new AlertDialog.Builder(this)
                    .setTitle("确认退出登录？")
                    .setNegativeButton("取消",null)
                    .setPositiveButton("确认",(dialog,which)->{
                        Utils.toast(this,"已退出登录");
                        Utils.doOnLogout();
                    })
                    .show()
        );
        //登录改变事件
        SubscriptionUtils.register(this,
                RxBus.getDefault().toObservable(UserInfoUpdateEvent.class).subscribe(event->{
                    if(event.getNewUser()==null){//注销
                        drawerLogin.setVisibility(View.GONE);
                        drawerGuest.setVisibility(View.VISIBLE);
                    }else{//登录
                        drawerLogin.setVisibility(View.VISIBLE);
                        drawerGuest.setVisibility(View.GONE);
                        avatar.setImageURI(event.getNewUser().getAvatarDisplayUrl());
                        nicknameTxt.setText(event.getNewUser().getNickname());
                    }

        }));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //toolbar menu
        getMenuInflater().inflate(R.menu.toolbar_main,menu);
        return super.onCreateOptionsMenu(menu);
    }
}
