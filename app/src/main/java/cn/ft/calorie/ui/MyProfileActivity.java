package cn.ft.calorie.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.ft.calorie.R;
import cn.ft.calorie.event.UserInfoUpdateEvent;
import cn.ft.calorie.pojo.UserInfo;
import cn.ft.calorie.util.RxBus;
import cn.ft.calorie.util.SubscriptionUtils;
import cn.ft.calorie.util.Utils;

public class MyProfileActivity extends ToolbarActivity {

    @BindView(R.id.avatar)
    SimpleDraweeView avatar;
    @BindView(R.id.nicknameTxt)
    TextView nicknameTxt;
    @BindView(R.id.userDetailBtn)
    RelativeLayout userDetailBtn;
    @BindView(R.id.burnRecordBtn)
    RelativeLayout burnRecordBtn;
    @BindView(R.id.intakeRecordBtn)
    RelativeLayout intakeRecordBtn;
    @BindView(R.id.weightRecordBtn)
    RelativeLayout weightRecordBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected void setLayout() {
        setContentView(R.layout.activity_my_profile);
        ButterKnife.bind(this);
    }

    @Override
    protected void bindViews() {
        toolbar.setTitle("我的");
        avatar.setImageURI(Utils.loginUser.getAvatarDisplayUrl());
        nicknameTxt.setText(Utils.loginUser.getNickname());
    }

    @Override
    protected void bindListeners() {
        //个人信息页
        userDetailBtn.setOnClickListener(v->{
            startActivity(new Intent(this, UserDetailActivity.class));
        });
        //用户更新event
        SubscriptionUtils.register(this,
                RxBus.getDefault().toObservable(UserInfoUpdateEvent.class)
                .subscribe(event -> {
                    UserInfo u = event.getNewUser();
                    System.out.println("----update event-------"+u.getAvatarDisplayUrl());
                    avatar.setImageURI(u.getAvatarDisplayUrl());
                    nicknameTxt.setText(u.getNickname());
                }));

    }
}
