package cn.ft.calorie.ui;

import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.ft.calorie.R;
import cn.ft.calorie.util.Utils;

public class AboutUsActivity extends ToolbarActivity {
    @BindView(R.id.appIcon)
    SimpleDraweeView appIcon;
    @BindView(R.id.appNameTxt)
    TextView appNameTxt;
    @BindView(R.id.disclaimerBtn)
    Button disclaimerBtn;
    @BindView(R.id.appVersionBtn)
    Button appVersionBtn;

    @Override
    protected void setLayout() {
        setContentView(R.layout.activity_about_us);
    }

    @Override
    protected void bindViews() {
        ButterKnife.bind(this);
        String appName = getString(getApplicationInfo().labelRes);
        toolbar.setTitle("关于"+appName);
        appNameTxt.setText(appName);
        appIcon.setImageURI("res://"+getPackageName()+"/" + getApplicationInfo().icon);
        appVersionBtn.setText("版本号 "+ Utils.getVersionName(this));
    }

    @Override
    protected void bindListeners() {
        disclaimerBtn.setOnClickListener(v -> {
            View txtView = getLayoutInflater().inflate(R.layout.alertdialog_disclaimer,null);
            Button okBtn = (Button) txtView.findViewById(R.id.okBtn);
            AlertDialog alertDialog = new AlertDialog.Builder(this)
                    .setView(txtView)
                    .show();
            okBtn.setOnClickListener(v1->alertDialog.dismiss());
        });
    }

}
