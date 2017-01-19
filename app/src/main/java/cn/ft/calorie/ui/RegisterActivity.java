package cn.ft.calorie.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.ft.calorie.R;
import cn.ft.calorie.listener.OnPasswordEyeListenerImpl;
import cn.ft.calorie.pojo.UserInfo;
import cn.ft.calorie.util.SubscriptionUtils;
import cn.ft.calorie.util.Utils;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class RegisterActivity extends ToolbarActivity {
    @BindView(R.id.telTxt)
    EditText telTxt;
    @BindView(R.id.passwordTxt)
    EditText passwordTxt;
    @BindView(R.id.passwordEye)
    CheckBox passwordEye;
    @BindView(R.id.getValidationCodeBtn)
    Button getValidationCodeBtn;
    @BindView(R.id.validationCodeTxt)
    EditText validationCodeTxt;
    @BindView(R.id.registerBtn)
    Button registerBtn;
    @Override
    protected void setLayout() {
        setContentView(R.layout.activity_register);
    }
    @Override
    protected void bindViews() {
        ButterKnife.bind(this);
        toolbar.setTitle("注册");
    }
    @Override
    protected void bindListeners() {
        //密码是否可见
        passwordEye.setOnCheckedChangeListener(new OnPasswordEyeListenerImpl(passwordTxt));
        //获取验证码
        getValidationCodeBtn.setOnClickListener(v -> {
            String tel = telTxt.getText().toString().trim();
            if(!Utils.isTelValid(tel,telTxt)) return;
            //验证手机号是否重复
            SubscriptionUtils.register(this,
                apiUtils.getApiDataObservable(apiUtils.getApiServiceImpl().validateTel(tel))
                        .subscribe(aVoid -> {
                            // TODO: 2017/1/17//发送验证码
                            Utils.toast(this,"发送验证码");
                        },e->{
                            Utils.toast(this,e.getMessage());
                        })
            );
        });
        //注册
        registerBtn.setOnClickListener(v -> {
            String tel = telTxt.getText().toString().trim();
            if(!Utils.isTelValid(tel,telTxt)) return;
            String password = passwordTxt.getText().toString().trim();
            if(!Utils.isPasswordValid(password,passwordTxt)) return;
            String validationCode = validationCodeTxt.getText().toString().trim();
            if(!isValidationCodeValid(validationCode))return;
            //验证手机号是否重复
            SubscriptionUtils.register(this,
                apiUtils.getApiDataObservable(apiUtils.getApiServiceImpl().validateTel(tel))
                        .observeOn(Schedulers.io())
                        .flatMap(aVoid -> {
                            System.out.println(Thread.currentThread().getName()+"--------");
                            UserInfo u = new UserInfo();
                            u.setTel(tel);
                            u.setPassword(password);
                            return apiUtils.getApiDataObservable(apiUtils.getApiServiceImpl().register(u));
                        })
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(user -> {
                            Utils.loginUser = user;
                            Utils.toast(this,"注册成功");
                            startActivity(new Intent(this,CompleteProfileActivity.class));
                            finish();
                        },e->{
                            Utils.toast(this,e.getMessage());
                        })
            );

        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    boolean isValidationCodeValid(String validationCode){
        boolean isValid = false;
        if(TextUtils.isEmpty(validationCode)){
            validationCodeTxt.setError("请填写验证码");
            validationCodeTxt.requestFocus();
        }else{
            isValid = true;
        }
        return isValid;
    }
}
