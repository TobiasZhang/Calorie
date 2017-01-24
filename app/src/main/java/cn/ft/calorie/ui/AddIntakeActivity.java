package cn.ft.calorie.ui;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.ft.calorie.R;
import cn.ft.calorie.ui.fragment.AddIntakeChooseFragment;
import cn.ft.calorie.ui.fragment.AddIntakeChosenFragment;
import cn.ft.calorie.widget.MyFragmentPagerAdapter;

public class AddIntakeActivity extends ToolbarActivity {
    @BindView(R.id.tabLayout)
    TabLayout tabLayout;
    @BindView(R.id.viewPager)
    ViewPager viewPager;

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
        fragmentTitleList.add("已选择");
        FragmentPagerAdapter fragmentPagerAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager(),fragmentList,fragmentTitleList);
        viewPager.setAdapter(fragmentPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    protected void bindListeners() {

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
                // TODO: 2017/1/24 提交 
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
