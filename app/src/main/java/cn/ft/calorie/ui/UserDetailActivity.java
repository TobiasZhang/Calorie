package cn.ft.calorie.ui;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.core.ImagePipeline;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.ft.calorie.R;
import cn.ft.calorie.event.UserInfoUpdateEvent;
import cn.ft.calorie.pojo.UserInfo;
import cn.ft.calorie.util.RxBus;
import cn.ft.calorie.util.SubscriptionUtils;
import cn.ft.calorie.util.TakePictureUtils;
import cn.ft.calorie.util.Utils;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class UserDetailActivity extends ToolbarActivity {
    @BindView(R.id.avatar)
    SimpleDraweeView avatar;
    @BindView(R.id.avatarBtn)
    LinearLayout avatarBtn;
    @BindView(R.id.nicknameTxt)
    TextView nicknameTxt;
    @BindView(R.id.nicknameBtn)
    LinearLayout nicknameBtn;
    @BindView(R.id.sexTxt)
    TextView sexTxt;
    @BindView(R.id.sexBtn)
    LinearLayout sexBtn;
    @BindView(R.id.birthdayTxt)
    TextView birthdayTxt;
    @BindView(R.id.birthdayBtn)
    LinearLayout birthdayBtn;

    UserInfo loginUser = Utils.loginUser;

    EditText nickEdit;
    boolean wantToUploadAvatar;
    String newNick;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected void setLayout() {
        setContentView(R.layout.activity_user_detail);
    }
    @Override
    protected void bindViews() {
        ButterKnife.bind(this);
        toolbar.setTitle("个人资料");
        avatar.setImageURI(loginUser.getAvatarDisplayUrl());
        nicknameTxt.setText(loginUser.getNickname());
        birthdayTxt.setText(loginUser.getBirthdayFormatStr());
        sexTxt.setText(loginUser.getSex().equals("男")?"♂":"♀");
    }

    @Override
    protected void bindListeners() {
        //修改昵称
        nicknameBtn.setOnClickListener(v -> {
            FrameLayout nickLayout = new FrameLayout(this);
            nickEdit = new EditText(this);
            FrameLayout.LayoutParams nickLP = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            nickLP.setMargins(30,0,30,0);
            nickEdit.setLayoutParams(nickLP);
            nickLayout.addView(nickEdit);

            nickEdit.setText(loginUser.getNickname());
            nickEdit.setSelection(loginUser.getNickname().length());
            new AlertDialog.Builder(this)
                    .setTitle("修改昵称")
                    .setView(nickLayout)
                    .setNegativeButton("取消",null)
                    .setPositiveButton("确定",(dialog,i)->{
                        String newNick = nickEdit.getText().toString().trim();
                        if(!TextUtils.isEmpty(newNick) && !newNick.equals(loginUser.getNickname())){
                            nicknameTxt.setText(newNick);
                            this.newNick = newNick;
                        }
                    })
                    .show();
        });
        //修改头像
        avatarBtn.setOnClickListener(v -> {
            new AlertDialog.Builder(this)
                    .setTitle("头像")
                    .setItems(new String[]{"拍照", "本地相册"}, (dialogInterface,index) -> {
                        switch (index){
                            case 0:
                                TakePictureUtils.takePhoto(this);
                                break;
                            case 1:
                                TakePictureUtils.takeAblum(this);
                                break;
                        }})
                    .show();
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_user_detail,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.ok://确认修改
                Observable<UserInfo> o;
                if(this.newNick!=null){
                    loginUser.setNickname(newNick);
                    o = apiUtils.getApiDataObservable(apiUtils.getApiServiceImpl().mergeUserInfo(loginUser));
                }else{
                    o = Observable.just(loginUser).subscribeOn(Schedulers.io());
                }
                SubscriptionUtils.register(this,
                                o
                                .observeOn(Schedulers.io())
                                .flatMap(userInfo -> {
                                    Utils.loginUser = userInfo;
                                    if(wantToUploadAvatar){
                                        RequestBody body = RequestBody.create(MediaType.parse("application/octet-stream"), TakePictureUtils.tempPicFile);
                                        MultipartBody.Part part = MultipartBody.Part.createFormData("avatarFile",TakePictureUtils.tempPicFile.getName(),body);
                                        return apiUtils.getApiServiceImpl().uploadAvatar(part,userInfo.getId());
                                    }else{
                                        return Observable.just("okWithoutAvatar");
                                    }
                                })
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(url->{
                                            if(!url.equals("okWithoutAvatar")){
                                                Utils.loginUser.setAvatar(url.toString());
                                                //TakePictureUtils.tempPicFile.delete(); onDestroy已做
                                            }
                                            RxBus.getDefault().post(new UserInfoUpdateEvent(Utils.loginUser));
                                            Utils.toast(this,"保存成功");
                                            finish();
                                        },
                                        e->{
                                            e.printStackTrace();
                                            Utils.toast(this,e.getMessage());
                                            finish();
                                        })

                );
                break;
        }
        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        TakePictureUtils.handlerPicture(this,requestCode,resultCode,data)
                .subscribe(picFile->{
                    wantToUploadAvatar = true;
                    ImagePipeline imagePipeline = Fresco.getImagePipeline();
                    imagePipeline.evictFromCache(Uri.parse("file://"+picFile.getPath()));
                    avatar.setImageURI("file://"+picFile.getPath());
                },e->{
                    Utils.toast(this,e.getMessage());
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(wantToUploadAvatar)
            TakePictureUtils.tempPicFile.delete();
    }
}
