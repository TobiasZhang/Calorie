package cn.ft.calorie.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.shawnlin.numberpicker.NumberPicker;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.ft.calorie.R;
import cn.ft.calorie.api.ApiUtils;
import cn.ft.calorie.event.IntakeChosenAddEvent;
import cn.ft.calorie.pojo.Food;
import cn.ft.calorie.pojo.IntakeRecord;
import cn.ft.calorie.ui.FoodCustomActivity;
import cn.ft.calorie.ui.ToolbarActivity;
import cn.ft.calorie.ui.adapter.IntakeChooseAdapter;
import cn.ft.calorie.util.RxBus;
import cn.ft.calorie.util.SubscriptionUtils;
import cn.ft.calorie.util.Utils;

/**
 * Created by TT on 2017/1/24.
 */
public class AddIntakeChooseFragment extends Fragment {
    @BindView(R.id.searchBtn)
    Button searchBtn;
    @BindView(R.id.searchContentTxt)
    EditText searchContentTxt;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.hintView)
    TextView hintView;

    @BindView(R.id.customFoodBtn)
    Button customFoodBtn;
    @BindView(R.id.customFoodView)
    FrameLayout customFoodView;

    ApiUtils apiUtils;
    IntakeChooseAdapter adapter;

    String searchContent;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_add_intake_choose, container, false);

        apiUtils = ((ToolbarActivity) getActivity()).apiUtils;
        ButterKnife.bind(this, rootView);
        customFoodView.setVisibility(View.GONE);
        hintView.setVisibility(View.VISIBLE);

        adapter = new IntakeChooseAdapter(getActivity());
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);

        bindListeners();
        return rootView;
    }

    void bindListeners() {
        //搜索
        searchBtn.setOnClickListener(v -> {
            searchContent = searchContentTxt.getText().toString().trim();
            if (TextUtils.isEmpty(searchContent)) return;
            SubscriptionUtils.register(getActivity(),
                    apiUtils.getApiDataObservable(apiUtils.getApiServiceImpl().getFoods(searchContent))
                            .subscribe(foods -> {
                                adapter.updateItems(foods);
                                if (adapter.getItemCount() > 0) {
                                    hintView.setVisibility(View.GONE);
                                    customFoodView.setVisibility(View.GONE);
                                } else {
                                    hintView.setVisibility(View.VISIBLE);
                                    customFoodView.setVisibility(View.VISIBLE);
                                }
                            }, e -> {
                                e.printStackTrace();
                                Utils.toast(getActivity(), e.getMessage());
                            }));
        });
        //点击食物
        adapter.setOnItemClickLietener(((position, data, viewType) -> {
            // TODO: 2017/1/24
            View pickerView = getActivity().getLayoutInflater().inflate(R.layout.alertdiaolg_food_weight, null);
            TextView pickerTitleTxt = (TextView) pickerView.findViewById(R.id.pickerTitleTxt);
            TextView weightNumTxt = (TextView) pickerView.findViewById(R.id.weightNumTxt);
            NumberPicker foodWeightPicker = (NumberPicker) pickerView.findViewById(R.id.foodWeightPicker);
            Button okBtn = (Button) pickerView.findViewById(R.id.okBtn);

            int initValue = 100;
            weightNumTxt.setText(initValue + "/g");
            pickerTitleTxt.setText("请选择 " + data.getName() + " 的重量");
            foodWeightPicker.setValue(initValue / 10);
            foodWeightPicker.setOnValueChangedListener((picker, oldVal, newVal) -> {
                weightNumTxt.setText(newVal * 10 + "/g");
            });

            AlertDialog dialog = new AlertDialog.Builder(getActivity(), R.style.FullScreenDialog).create();
            Window dialogWindow = dialog.getWindow();
            dialogWindow.setGravity(Gravity.BOTTOM);
            dialogWindow.setWindowAnimations(R.style.DialogAnim);
            dialog.show();

            dialogWindow.setContentView(pickerView);
            dialogWindow.getDecorView().setPadding(0, 0, 0, 0);
            dialogWindow.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

            //确认
            okBtn.setOnClickListener(v -> {
                Food food = data;
                int foodWeight = foodWeightPicker.getValue() * 10;
                IntakeRecord intakeRecord = new IntakeRecord();
                intakeRecord.setFood(food);
                intakeRecord.setFoodWeight(foodWeight);
                intakeRecord.setFoodUnitCount(foodWeight);
                intakeRecord.setFoodUnit("g");
                //将选择发送
                RxBus.getDefault().post(new IntakeChosenAddEvent(intakeRecord));
                dialog.dismiss();
                Utils.toast(getActivity(), "已添加至选择列表");
            });
        }));
        //自定义食物
        customFoodBtn.setOnClickListener(v->{
            Intent intent = new Intent(getActivity(), FoodCustomActivity.class);
            intent.putExtra("customFoodName",searchContent);
            startActivity(intent);
        });
    }


}
