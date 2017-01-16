package cn.ft.calorie;

import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginActivity extends ToolbarActivity {
    @BindView(R.id.telTxt)
    EditText telTxt;
    @BindView(R.id.passwordTxt)
    EditText passwordTxt;
    @BindView(R.id.passwordEye)
    CheckBox passwordEye;
    @BindView(R.id.forgotPassword)
    TextView forgotPassword;
    @BindView(R.id.toRegister)
    TextView toRegister;
    @BindView(R.id.thirdPartyBlock)
    LinearLayout thirdPartyBlock;
    @BindView(R.id.loginBtn)
    Button loginBtn;

    @Override
    void setLayout() {
        setContentView(R.layout.activity_login);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        bindListeners();
        titleTxt.setText("登录");
    }


    private void bindListeners() {
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
