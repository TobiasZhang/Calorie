package cn.ft.calorie.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.ft.calorie.R;
import cn.ft.calorie.ToolbarActivity;
import cn.ft.calorie.listener.OnPasswordEyeListenerImpl;
import cn.ft.calorie.pojo.UserInfo;
import cn.ft.calorie.util.Utils;

public class LoginActivity extends ToolbarActivity {
    @BindView(R.id.telTxt)
    EditText telTxt;
    @BindView(R.id.passwordTxt)
    EditText passwordTxt;
    @BindView(R.id.passwordEye)
    CheckBox passwordEye;
    @BindView(R.id.forgotPassword)
    TextView forgotPassword;
    @BindView(R.id.toRegisterBtn)
    TextView toRegisterBtn;
    @BindView(R.id.loginBtn)
    Button loginBtn;

    @Override
    protected void setLayout() {
        setContentView(R.layout.activity_login);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
     protected void bindViews() {
        ButterKnife.bind(this);
        toolbar.setTitle("登录");
    }
     protected void bindListeners() {
        //密码是否可见
        passwordEye.setOnCheckedChangeListener(new OnPasswordEyeListenerImpl(passwordTxt));
        //登录
        loginBtn.setOnClickListener(v -> {
            String tel = telTxt.getText().toString().trim();
            if(!Utils.isTelValid(tel,telTxt)) return;
            String password = passwordTxt.getText().toString().trim();
            if(!Utils.isPasswordValid(password,passwordTxt)) return;
            UserInfo u = new UserInfo();
            u.setTel(tel);
            u.setPassword(password);
            apiUtils.getApiDataObservable(apiUtils.getApiServiceImpl().login(u)).subscribe(
                    loginUser -> {
                        Utils.loginUser = loginUser;
                        System.out.println(loginUser);//// TODO: 2017/1/16  
                    },
                    err -> Utils.toast(this,err.getMessage())
            );
        });
        //去注册页
        toRegisterBtn.setOnClickListener(v -> startActivity(new Intent(this,RegisterActivity.class)));

    }



}
