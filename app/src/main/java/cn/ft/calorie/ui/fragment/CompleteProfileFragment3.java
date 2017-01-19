package cn.ft.calorie.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.shawnlin.numberpicker.NumberPicker;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.ft.calorie.R;
import cn.ft.calorie.api.ApiUtils;
import cn.ft.calorie.pojo.UserInfo;
import cn.ft.calorie.ui.ToolbarActivity;
import cn.ft.calorie.util.SubscriptionUtils;
import cn.ft.calorie.util.TakePictureUtils;
import cn.ft.calorie.util.Utils;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class CompleteProfileFragment3 extends Fragment {
    @BindView(R.id.heightPicker)
    NumberPicker heightPicker;
    @BindView(R.id.weightPicker)
    NumberPicker weightPicker;
    @BindView(R.id.okBtn)
    Button okBtn;

    View rootView;
    UserInfo tempUser;
    boolean wantToUploadAvatar;

    ToolbarActivity toolbarActivity;
    ApiUtils apiUtils;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_complete_profile_3, container, false);
        bindViews();
        bindListeners();
        tempUser = (UserInfo) getArguments().getSerializable("tempUser");
        wantToUploadAvatar = getArguments().getBoolean("wantToUploadAvatar", false);
        return rootView;
    }

    void bindViews() {
        ButterKnife.bind(this, rootView);
        toolbarActivity = ((ToolbarActivity) getActivity());
        heightPicker.setValue(160);
        weightPicker.setValue(60);
        apiUtils = toolbarActivity.apiUtils;
    }

    void bindListeners() {
        okBtn.setOnClickListener(v->{
            int height = heightPicker.getValue();
            int weight = weightPicker.getValue();
            tempUser.setHeight(height);
            tempUser.setWeight(weight);
            SubscriptionUtils.register(getActivity(),
                toolbarActivity.apiUtils
                    .getApiDataObservable(apiUtils.getApiServiceImpl().mergeUserInfo(tempUser))
                        .observeOn(Schedulers.io())
                        .flatMap(user->{
                            System.out.println(Thread.currentThread().getName());

                            Utils.loginUser = user;
                            if(!wantToUploadAvatar)
                                return Observable.just("okWithoutAvatar");
                            RequestBody body = RequestBody.create(MediaType.parse("application/octet-stream"), TakePictureUtils.tempPicFile);
                            return apiUtils.getApiServiceImpl().uploadAvatar(body,user.getId());
                     })
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                            url->{
                                System.out.println(Thread.currentThread().getName()+"--------subscribe----");
                                if(!url.equals("okWithoutAvatar")){
                                    Utils.toast(getActivity(),"头像上传成功");
                                    Utils.loginUser.setAvatar(url.toString());
                                    TakePictureUtils.tempPicFile.delete();
                                }
                                Utils.toast(getActivity(),"身份信息填写完成");
                        },
                            e->{
                                e.printStackTrace();
                                Utils.toast(getActivity(),e.getMessage());
                        })
            );
        });
    }

}
