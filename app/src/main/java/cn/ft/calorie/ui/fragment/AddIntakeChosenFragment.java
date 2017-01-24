package cn.ft.calorie.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import cn.ft.calorie.R;

/**
 * Created by TT on 2017/1/24.
 */
public class AddIntakeChosenFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_add_intake_chosen, container, false);
        ButterKnife.bind(this, rootView);
        return rootView;
    }
}
