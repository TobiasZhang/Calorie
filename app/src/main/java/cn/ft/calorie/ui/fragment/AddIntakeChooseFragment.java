package cn.ft.calorie.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.ft.calorie.R;
import cn.ft.calorie.api.ApiUtils;
import cn.ft.calorie.event.IntakeChooseListUpdateEvent;
import cn.ft.calorie.event.IntakeRecordUpdateEvent;
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

    ApiUtils apiUtils;
    IntakeChooseAdapter adapter;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_add_intake_choose, container, false);

        apiUtils = ((ToolbarActivity)getActivity()).apiUtils;
        ButterKnife.bind(this, rootView);
//        hintView.setVisibility(View.VISIBLE);
        adapter = new IntakeChooseAdapter(getActivity());
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);

        bindListeners();
        return rootView;
    }
    void bindListeners(){
        //搜索
        searchBtn.setOnClickListener(v->{
            String searchContent = searchContentTxt.getText().toString().trim();
            if(TextUtils.isEmpty(searchContent))return;
            SubscriptionUtils.register(getActivity(),
                    apiUtils.getApiDataObservable(apiUtils.getApiServiceImpl().getFoods(searchContent))
                            .subscribe(foods->{
                                adapter.updateItems(foods);
                                RxBus.getDefault().post(new IntakeChooseListUpdateEvent(foods));
                            },e->{
                                e.printStackTrace();
                                Utils.toast(getActivity(),e.getMessage());
                            }));
        });
        //点击食物
        adapter.setOnItemClickLietener(((position, data, viewType) -> {
            // TODO: 2017/1/24  
        }));
        //食物列表改变事件
        SubscriptionUtils.register(getActivity(),
                RxBus.getDefault().toObservable(IntakeChooseListUpdateEvent.class)
                        .subscribe(foods->{
                            if (adapter.getItemCount()>0){
                                hintView.setVisibility(View.GONE);
                            }else{
                                hintView.setVisibility(View.VISIBLE);
                            }
                        },e->{
                            e.printStackTrace();
                            Utils.toast(getActivity(),"操作失败");
                        }));
    }


}
