package cn.ft.calorie.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.ft.calorie.MainActivity;
import cn.ft.calorie.R;
import cn.ft.calorie.api.ApiUtils;
import cn.ft.calorie.listener.OnPasswordEyeListenerImpl;
import cn.ft.calorie.pojo.UserInfo;
import cn.ft.calorie.ui.ToolbarActivity;
import cn.ft.calorie.util.SubscriptionUtils;
import cn.ft.calorie.util.Utils;

/**
 * Created by TT on 2017/1/19.
 */
public class ResetPasswordFragment2 extends Fragment {
    @BindView(R.id.passwordEye)
    CheckBox passwordEye;
    @BindView(R.id.passwordTxt)
    EditText passwordTxt;
    @BindView(R.id.rePasswordEye)
    CheckBox rePasswordEye;
    @BindView(R.id.rePasswordTxt)
    EditText rePasswordTxt;
    @BindView(R.id.okBtn)
    Button okBtn;

    View rootView;
    ApiUtils apiUtils;
    String processTel;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_reset_password_2, container, false);
        bindViews();
        bindListeners();

        processTel = getArguments().getString("processTel");
        return rootView;
    }

    void bindViews() {
        ButterKnife.bind(this, rootView);
        apiUtils = ((ToolbarActivity) getActivity()).apiUtils;
    }

    void bindListeners() {
        //密码是否可见
        passwordEye.setOnCheckedChangeListener(new OnPasswordEyeListenerImpl(passwordTxt));
        rePasswordEye.setOnCheckedChangeListener(new OnPasswordEyeListenerImpl(rePasswordTxt));
        //完成
        okBtn.setOnClickListener(v->{
            String password = passwordTxt.getText().toString().trim();
            if(!Utils.isPasswordValid(password,passwordTxt))return;
            String rePassword = rePasswordTxt.getText().toString().trim();
            if(!Utils.isPasswordValid(rePassword,rePasswordTxt))return;
            if(!password.equals(rePassword)){
                rePasswordTxt.setError("两次密码不一致");
                rePasswordTxt.requestFocus();
                return;
            }
            UserInfo u = new UserInfo();
            u.setTel(processTel);
            u.setPassword(password);
            SubscriptionUtils.register(
                    getActivity(),
                    apiUtils.getApiDataObservable(apiUtils.getApiServiceImpl().resetPassword(u))
                        .subscribe(
                                user->{
                                    System.out.println(user+"--------------------user");
                                    System.out.println(user.toString());
                                    Utils.loginUser = user;
                                    Utils.toast(getActivity(),"登录成功");

                                    startActivity(new Intent(getActivity(), MainActivity.class));
                                 },
                                e->{
                                    e.printStackTrace();
                                    Utils.toast(getActivity(),e.getMessage());
                                }
                        )
            );
        });
    }
}
