package cn.ft.calorie.ui.fragment;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.core.ImagePipeline;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.ft.calorie.R;
import cn.ft.calorie.pojo.UserInfo;
import cn.ft.calorie.util.FragmentUtils;
import cn.ft.calorie.util.TakePictureUtils;
import cn.ft.calorie.util.Utils;

public class CompleteProfileFragment2 extends Fragment {
    View rootView;
    UserInfo tempUser;
    @BindView(R.id.avatar)
    SimpleDraweeView avatar;
    @BindView(R.id.nicknameTxt)
    EditText nicknameTxt;
    @BindView(R.id.dataPicker)
    DatePicker dataPicker;
    @BindView(R.id.okBtn)
    Button okBtn;

    boolean wantToUploadAvatar;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_complete_profile_2, container, false);
        bindViews();
        bindListeners();
        tempUser = (UserInfo) getArguments().getSerializable("tempUser");

        return rootView;
    }

    void bindViews() {
        ButterKnife.bind(this, rootView);
        dataPicker.updateDate(1990,0,1);
        if(TakePictureUtils.tempPicFile.exists()){
            wantToUploadAvatar = true;
            avatar.setImageURI("file://"+TakePictureUtils.tempPicFile.getPath());
        }
    }
    void bindListeners() {
        //欲上传头像
        avatar.setOnClickListener(v->{
             new AlertDialog.Builder(getActivity())
                    .setTitle("头像")
                    .setItems(new String[]{"拍照", "本地相册"}, (dialogInterface,index) -> {
                        switch (index){
                            case 0:
                                TakePictureUtils.takePhoto(getActivity());
                                break;
                            case 1:
                                TakePictureUtils.takeAblum(getActivity());
                                break;
                        }})
                    .show();
        });
        okBtn.setOnClickListener(v->{
            //昵称
            String nickname = nicknameTxt.getText().toString().trim();
            if(TextUtils.isEmpty(nickname)){nicknameTxt.setError("请输入昵称");nicknameTxt.requestFocus();return;}
            tempUser.setNickname(nickname);
            //生日
            Calendar calendar = Calendar.getInstance();
            calendar.set(dataPicker.getYear(),dataPicker.getMonth(),dataPicker.getDayOfMonth());
            tempUser.setBirthday(calendar.getTime());

            Bundle args = new Bundle();
            args.putSerializable("tempUser",tempUser);
            args.putBoolean("wantToUploadAvatar",wantToUploadAvatar);
            FragmentUtils.addFragment(getActivity(),R.id.fragmentContainer,new CompleteProfileFragment3(),args,true,"completeProfile3");
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        TakePictureUtils.handlerPicture(getActivity(),requestCode,resultCode,data)
                .subscribe(picFile->{
                    wantToUploadAvatar = true;
                    ImagePipeline imagePipeline = Fresco.getImagePipeline();
                    imagePipeline.evictFromCache(Uri.parse("file://"+picFile.getPath()));
                    avatar.setImageURI("file://"+picFile.getPath());
                },e->{
                    Utils.toast(getActivity(),e.getMessage());
                });
    }
}
