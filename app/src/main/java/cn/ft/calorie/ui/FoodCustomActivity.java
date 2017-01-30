package cn.ft.calorie.ui;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.ft.calorie.R;
import cn.ft.calorie.event.IntakeChosenAddEvent;
import cn.ft.calorie.pojo.Food;
import cn.ft.calorie.pojo.FoodCustom;
import cn.ft.calorie.pojo.IntakeRecord;
import cn.ft.calorie.util.RxBus;
import cn.ft.calorie.util.SubscriptionUtils;
import cn.ft.calorie.util.Utils;

public class FoodCustomActivity extends ToolbarActivity {
    @BindView(R.id.foodNameTxt)
    EditText foodNameTxt;
    /*@BindView(R.id.foodWeightTxt)
    EditText foodWeightTxt;*/
    @BindView(R.id.foodCalorieTxt)
    EditText foodCalorieTxt;
    @BindView(R.id.okBtn)
    Button okBtn;

    String customFoodName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected void setLayout() {
        setContentView(R.layout.activity_food_custom);
        customFoodName = getIntent().getStringExtra("customFoodName");
    }

    @Override
    protected void bindViews() {
        ButterKnife.bind(this);
        toolbar.setTitle("自定义食物");
        foodNameTxt.setText(customFoodName);
    }

    @Override
    protected void bindListeners() {
        okBtn.setOnClickListener(v->{
            String foodName = foodNameTxt.getText().toString();
            if(TextUtils.isEmpty(foodName)){
                Utils.toast(this,"食物名称不能为空哟");
                return;
            }
            /*String foodWeight = foodWeightTxt.getText().toString();
            if(TextUtils.isEmpty(foodWeight)||"0".equals(foodWeight)){
                Utils.toast(this,"请填写正确的食物重量哟");
                return;
            }*/
            String foodCalorie = foodCalorieTxt.getText().toString();
            if(TextUtils.isEmpty(foodCalorie)||"0".equals(foodCalorie)){
                Utils.toast(this,"请填写正确的每100g所含热量哟");
                return;
            }
            FoodCustom customFood = new FoodCustom();
            customFood.setName(foodName);
            customFood.setCalorie(Integer.valueOf(foodCalorie));
            customFood.setCustomBy(Utils.loginUser);
            SubscriptionUtils.register(this,
                    apiUtils.getApiDataObservable(apiUtils.getApiServiceImpl().addFoodCustom(customFood))
                            .subscribe(newCustomFood->{

                               /* IntakeRecord intakeRecord = new IntakeRecord();
                                intakeRecord.setFoodWeight(Integer.valueOf(foodWeight));
                                intakeRecord.setFoodUnitCount(Integer.valueOf(foodWeight));
                                intakeRecord.setFoodUnit("g");*/
                                //将选择发送
                                //RxBus.getDefault().post(new IntakeChosenAddEvent(intakeRecord));
//                                Utils.toast(this, "已添加至选择列表");
                                Utils.toast(this, "已提交至后台");
                                finish();
                            },e->{
                                e.printStackTrace();
                                Utils.toast(this,e.getMessage());
                            }));
        });
    }
}
