package cn.ft.calorie;

import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
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
    @BindView(R.id.toRegister)
    TextView toRegister;
    @BindView(R.id.loginBtn)
    Button loginBtn;

    @Override
    void setLayout() {
        setContentView(R.layout.activity_login);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bindViews();
        bindListeners();
    }

    private void bindViews() {
        ButterKnife.bind(this);
        getSupportActionBar().setTitle("登录");
    }

    private void bindListeners() {
        //密码是否可见
        passwordEye.setOnCheckedChangeListener((compoundButton, isChecked) -> {
            if (isChecked)
                passwordTxt.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            else
                passwordTxt.setTransformationMethod(PasswordTransformationMethod.getInstance());
            passwordTxt.setSelection(passwordTxt.length());
        });
        //登录
        loginBtn.setOnClickListener(v -> {
            String tel = telTxt.getText().toString().trim();
            if(!isTelValid(tel)) return;
            String password = passwordTxt.getText().toString().trim();
            if(!isPasswordValid(password)) return;
            UserInfo u = new UserInfo();
            u.setTel(tel);
            u.setPassword(password);
            apiUtils.getApiDataObservable(apiUtils.getApiServiceImpl().login(u)).subscribe(
                    loginUser -> {
                        System.out.println(loginUser);//// TODO: 2017/1/16  
                    },
                    err -> Utils.toast(this,err.getMessage())
            );
        });
    }

    private boolean isTelValid(String tel){
        return true;
    }
    private boolean isPasswordValid(String password){
        boolean isValid = false;
        if(TextUtils.isEmpty(password)){
            passwordTxt.setError("密码长度位于6~12位之间",null);
        }else if(false){
            // TODO: 2017/1/16
        }else{
            isValid = true;
        }
        if(!isValid)
            passwordTxt.requestFocus();
        return isValid;
    }

}
