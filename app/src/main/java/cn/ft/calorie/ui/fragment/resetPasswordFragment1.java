package cn.ft.calorie.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.ft.calorie.R;
import cn.ft.calorie.api.ApiException;
import cn.ft.calorie.api.ApiUtils;
import cn.ft.calorie.ui.ToolbarActivity;
import cn.ft.calorie.util.FragmentUtils;
import cn.ft.calorie.util.SubscriptionUtils;
import cn.ft.calorie.util.Utils;

/**
 * Created by TT on 2017/1/19.
 */
public class ResetPasswordFragment1 extends Fragment {
    @BindView(R.id.telTxt)
    EditText telTxt;
    @BindView(R.id.getValidationCodeBtn)
    Button getValidationCodeBtn;
    @BindView(R.id.validationCodeTxt)
    EditText validationCodeTxt;
    @BindView(R.id.okBtn)
    Button okBtn;

    View rootView;
    ApiUtils apiUtils;

    String processTel;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_reset_password_1, container, false);
        bindViews();
        bindListeners();

        return rootView;
    }

    void bindViews() {
        ButterKnife.bind(this, rootView);
        apiUtils = ((ToolbarActivity)getActivity()).apiUtils;
    }

    void bindListeners() {
        //获取验证码
        getValidationCodeBtn.setOnClickListener(v -> {
            String tel = telTxt.getText().toString().trim();
            if(!Utils.isTelValid(tel,telTxt)) return;
            //验证手机号是否存在
            SubscriptionUtils.register(
                    getActivity(),
                    apiUtils.getApiDataObservable(apiUtils.getApiServiceImpl().isTelExist(tel))
                            .subscribe(isTelExist -> {
                                if(!isTelExist)
                                    throw new ApiException("该用户尚未注册");
                                this.processTel = tel;
                                // TODO: 2017/1/17//发送验证码
                            },e->{
                                Utils.toast(getActivity(),e.getMessage());
                            })
            );
        });
        //验证成功，去重置密码
        okBtn.setOnClickListener(v -> {
            String tel = telTxt.getText().toString().trim();
            if(!Utils.isTelValid(tel,telTxt)) return;
            String validationCode = validationCodeTxt.getText().toString().trim();
            // TODO: 2017/1/19 验证码验证
            //if(!isValidationCodeValid(validationCode))return;
            Bundle bundle = new Bundle();
            bundle.putString("processTel",this.processTel);
            FragmentUtils.addFragment(getActivity(),R.id.fragmentContainer,new ResetPasswordFragment2(),bundle,true,"forgotPasswordFragment2");
        });
    }
}
