package cn.ft.calorie.ui;

import android.os.Bundle;

import cn.ft.calorie.R;
import cn.ft.calorie.ui.fragment.ResetPasswordFragment1;
import cn.ft.calorie.util.FragmentUtils;

public class ResetPasswordActivity extends ToolbarActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void setLayout() {
        setContentView(R.layout.activity_forgot_password);
        FragmentUtils.addFragment(this,R.id.fragmentContainer,new ResetPasswordFragment1());
    }

    @Override
    protected void bindViews() {
        toolbar.setTitle("找回密码(1/2)");
    }

    @Override
    protected void bindListeners() {
        getSupportFragmentManager().addOnBackStackChangedListener(()->{
            int count = getSupportFragmentManager().getBackStackEntryCount();
            switch (count){
                case 0:toolbar.setTitle("找回密码(1/2)");break;
                case 1:toolbar.setTitle("找回密码(2/2)");break;
            }
        });

    }
}
