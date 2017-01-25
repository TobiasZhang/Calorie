package cn.ft.calorie.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.ft.calorie.R;
import cn.ft.calorie.api.ApiUtils;
import cn.ft.calorie.event.IntakeChosenAddEvent;
import cn.ft.calorie.event.IntakeChosenListUpdateEvent;
import cn.ft.calorie.pojo.IntakeRecord;
import cn.ft.calorie.ui.ToolbarActivity;
import cn.ft.calorie.ui.adapter.IntakeChosenAdapter;
import cn.ft.calorie.util.RxBus;
import cn.ft.calorie.util.SubscriptionUtils;

/**
 * Created by TT on 2017/1/24.
 */
public class AddIntakeChosenFragment extends Fragment {
    ApiUtils apiUtils;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.hintView)
    TextView hintView;

    IntakeChosenAdapter adapter;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_add_intake_chosen, container, false);
        apiUtils = ((ToolbarActivity) getActivity()).apiUtils;
        ButterKnife.bind(this, rootView);
        bindListeners();

        adapter = new IntakeChosenAdapter(getActivity());
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);

        return rootView;
    }

    void bindListeners() {
        SubscriptionUtils.register(getActivity(), RxBus.getDefault().toObservable(IntakeChosenAddEvent.class)
                .subscribe(event -> {
                    if(event.getData()!=null){
                        IntakeRecord intakeRecord = event.getData();
                        adapter.addItem(intakeRecord);
                    }
                    int count = adapter.getItemCount();
                    if(count==0){
                        hintView.setVisibility(View.VISIBLE);
                    }else{
                        hintView.setVisibility(View.GONE);
                    }
                    //数量改变
                    RxBus.getDefault().post(new IntakeChosenListUpdateEvent(adapter.getDataList()));
                }));
    }
}
