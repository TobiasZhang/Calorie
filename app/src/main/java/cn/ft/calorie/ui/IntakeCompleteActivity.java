package cn.ft.calorie.ui;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.ft.calorie.R;
import cn.ft.calorie.pojo.IntakeRecord;
import cn.ft.calorie.ui.adapter.IntakeCompleteAdapter;
import cn.ft.calorie.util.Utils;

public class IntakeCompleteActivity extends ToolbarActivity {
    @BindView(R.id.calorieCountTxt)
    TextView calorieCountTxt;
    @BindView(R.id.goHomeBtn)
    Button goHomeBtn;
    @BindView(R.id.toBurnBtn)
    Button toBurnBtn;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    List<IntakeRecord> intakeRecordList;
    IntakeCompleteAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected void setLayout() {
        setContentView(R.layout.activity_intake_complete);
        intakeRecordList = (List<IntakeRecord>) getIntent().getSerializableExtra("intakeRecords");

    }

    @Override
    protected void bindViews() {
        ButterKnife.bind(this);
        toolbar.setTitle("饮食记录完成");
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new IntakeCompleteAdapter(this);
        adapter.addItems(intakeRecordList);
        recyclerView.setAdapter(adapter);
        int count = 0;
        for(IntakeRecord record:intakeRecordList){
            count += record.getCalorie();
        }
        calorieCountTxt.setText(count+"卡路里");//共摄入xxx卡路里
    }

    @Override
    protected void bindListeners() {
        //回首页
        goHomeBtn.setOnClickListener(v->{
            finish();
        });
        //去锻炼
        toBurnBtn.setOnClickListener(v->{
            // TODO: 2017/1/25
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_share, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.share:
                Utils.toast(this, "分享。。。");// TODO: 2017/1/25
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
