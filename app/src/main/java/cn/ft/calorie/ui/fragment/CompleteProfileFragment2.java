package cn.ft.calorie.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import cn.ft.calorie.R;
import cn.ft.calorie.ToolbarActivity;
import cn.ft.calorie.pojo.UserInfo;
import cn.ft.calorie.util.Utils;

public class CompleteProfileFragment2 extends Fragment {
    View rootView;
    UserInfo tempUser;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_complete_profile_2, container, false);
        ((ToolbarActivity) getActivity()).toolbar.setTitle("身体信息(2/3)");
        bindViews();
        bindListeners();
        tempUser = (UserInfo) getArguments().getSerializable("tempUser");
        return rootView;
    }
    void bindViews(){

    }
    void bindListeners(){
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
