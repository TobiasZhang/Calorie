package cn.ft.calorie.ui.fragment;

import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.RoundingParams;
import com.facebook.drawee.view.SimpleDraweeView;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.ft.calorie.R;
import cn.ft.calorie.util.RxBus;
import cn.ft.calorie.ui.ToolbarActivity;
import cn.ft.calorie.event.FragmentPopupEvent;
import cn.ft.calorie.pojo.UserInfo;
import cn.ft.calorie.util.FragmentUtils;
import cn.ft.calorie.util.SubscriptionUtils;
import cn.ft.calorie.util.Utils;

public class CompleteProfileFragment1 extends Fragment {
    @BindView(R.id.femaleImg)
    SimpleDraweeView femaleImg;
    @BindView(R.id.maleImg)
    SimpleDraweeView maleImg;
    @BindView(R.id.okBtn)
    Button okBtn;

    View rootView;
    int choiceSex;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_complete_profile_1, container, false);
        bindViews();
        bindListeners();
        return rootView;
    }
    void bindViews(){
        ButterKnife.bind(this, rootView);
        Uri femaleUri = Uri.parse("res://"+getActivity().getPackageName()+"/" + R.drawable.a_complete_profile_female);
        Uri maleUri = Uri.parse("res://"+getActivity().getPackageName()+"/" + R.drawable.a_complete_profile_male);
        femaleImg.setImageURI(femaleUri);
        maleImg.setImageURI(maleUri);
    }
    void bindListeners(){
        GenericDraweeHierarchy femaleGenericDraweeHierarchy = femaleImg.getHierarchy();
        GenericDraweeHierarchy maleGenericDraweeHierarchy = maleImg.getHierarchy();
        RoundingParams femaleRP = femaleGenericDraweeHierarchy.getRoundingParams();
        RoundingParams maleRP = maleGenericDraweeHierarchy.getRoundingParams();
        femaleImg.setOnClickListener(v->{
            femaleRP.setBorderColor(Color.RED);
            maleRP.setBorderColor(Color.BLACK);
            femaleGenericDraweeHierarchy.setRoundingParams(femaleRP);
            maleGenericDraweeHierarchy.setRoundingParams(maleRP);
            choiceSex = 1;
        });
        maleImg.setOnClickListener(v->{
            femaleRP.setBorderColor(Color.BLACK);
            maleRP.setBorderColor(Color.RED);
            femaleGenericDraweeHierarchy.setRoundingParams(femaleRP);
            maleGenericDraweeHierarchy.setRoundingParams(maleRP);
            choiceSex = 2;
        });
        //继续
        okBtn.setOnClickListener(v->{
            if(choiceSex==0){
                Utils.toast(getContext(),"请选择您的性别");
                return;
            }
            UserInfo tempUser = Utils.loginUser;
            tempUser.setSex(choiceSex==1?"女":"男");
            Bundle bundle = new Bundle();
            bundle.putSerializable("tempUser",tempUser);
            FragmentUtils.addFragment(getActivity(),R.id.fragmentContainer,new CompleteProfileFragment2(),bundle,true,"completeProfile2");
        });
    }

}
