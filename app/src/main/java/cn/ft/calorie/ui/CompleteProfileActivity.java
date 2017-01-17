package cn.ft.calorie.ui;

import android.os.Bundle;
import android.widget.FrameLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.ft.calorie.ui.fragment.CompleteProfileFragment1;
import cn.ft.calorie.R;
import cn.ft.calorie.ToolbarActivity;
import cn.ft.calorie.util.FragmentUtils;

public class CompleteProfileActivity extends ToolbarActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //FragmentUtils.addFragment(this,R.id.fragmentContainer,new CompleteProfileFragment1());
    }
    @Override
    protected void setLayout() {
        setContentView(R.layout.activity_complete_profile);
    }
    @Override
    protected void bindViews() {
        ButterKnife.bind(this);
    }
    @Override
    protected void bindListeners() {

    }
}
