package cn.ft.calorie.ui;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.ft.calorie.R;
import cn.ft.calorie.pojo.Feedback;
import cn.ft.calorie.util.SubscriptionUtils;
import cn.ft.calorie.util.Utils;

public class FeedbackActivity extends ToolbarActivity {
    @BindView(R.id.contentTxt)
    EditText contentTxt;
    @BindView(R.id.qqTxt)
    EditText qqTxt;
    @BindView(R.id.okBtn)
    Button okBtn;
    @BindView(R.id.qqGroupBtn)
    Button qqGroupBtn;
    @Override
    protected void setLayout() {
        setContentView(R.layout.activity_feedback);
    }

    @Override
    protected void bindViews() {
        ButterKnife.bind(this);
        toolbar.setTitle("意见反馈");
    }

    @Override
    protected void bindListeners() {
        okBtn.setOnClickListener(v->{
            String content = contentTxt.getText().toString().trim();
            if(TextUtils.isEmpty(content)){
                Utils.toast(this,"反馈内容不可为空");
                return;
            }
            String qq = qqTxt.getText().toString().trim();
            // TODO: 2017/1/22 QQ验证
            Feedback feedback = new Feedback();
            feedback.setContent(content);
            feedback.setQq(qq);
            feedback.setUserInfo(Utils.loginUser);
            SubscriptionUtils.register(
                    this,
                    apiUtils.getApiDataObservable(apiUtils.getApiServiceImpl().addFeedback(feedback))
                            .subscribe(aVoid -> {
                                Utils.toast(this,"已提交，感谢您的反馈");
                                finish();
                            },e->{
                                e.printStackTrace();
                                Utils.toast(this,e.getMessage());
                            }));
            
        });
        qqGroupBtn.setOnClickListener(v->{
            // TODO: 2017/1/22 qq群
        });

    }
}
