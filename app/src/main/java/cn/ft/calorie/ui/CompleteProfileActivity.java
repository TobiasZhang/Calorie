package cn.ft.calorie.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import butterknife.ButterKnife;
import cn.ft.calorie.R;
import cn.ft.calorie.event.FragmentPopupEvent;
import cn.ft.calorie.ui.fragment.CompleteProfileFragment1;
import cn.ft.calorie.util.FragmentUtils;
import cn.ft.calorie.util.RxBus;
import cn.ft.calorie.util.TakePictureUtils;
import cn.ft.calorie.util.Utils;

public class CompleteProfileActivity extends ToolbarActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FragmentUtils.addFragment(this,R.id.fragmentContainer,new CompleteProfileFragment1());
    }
    @Override
    protected void setLayout() {
        setContentView(R.layout.activity_complete_profile);
    }
    @Override
    protected void bindViews() {
        ButterKnife.bind(this);
        toolbar.setTitle("身体信息(1/3)");
    }
    @Override
    protected void bindListeners() {
        getSupportFragmentManager().addOnBackStackChangedListener(()->{
            int count = getSupportFragmentManager().getBackStackEntryCount();
            toolbar.setTitle("身体信息("+(count+1)+"/3)");
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Fragment f = getSupportFragmentManager().findFragmentByTag("completeProfile2");
        f.onActivityResult(requestCode,resultCode,data);
    }
}
