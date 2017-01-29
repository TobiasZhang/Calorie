package cn.ft.calorie.ui;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdate;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.SupportMapFragment;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;
import com.amap.api.maps.model.Polyline;
import com.amap.api.maps.model.PolylineOptions;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.ft.calorie.R;
import cn.ft.calorie.util.TimeUtils;
import cn.ft.calorie.util.Utils;

public class BurnCompleteActivity extends AppCompatActivity {
    @BindView(R.id.okBtn)
    Button okBtn;
    @BindView(R.id.shareBtn)
    Button shareBtn;
    @BindView(R.id.avatar)
    SimpleDraweeView avatar;
    @BindView(R.id.burnCalorieTxt)
    TextView burnCalorieTxt;
    @BindView(R.id.dateTxt)
    TextView dateTxt;
    @BindView(R.id.nicknameTxt)
    TextView nicknameTxt;
    @BindView(R.id.distanceTxt)
    TextView distanceTxt;
    @BindView(R.id.durationTxt)
    TextView durationTxt;
    @BindView(R.id.speedTxt)
    TextView speedTxt;

    //此次燃烧掉的卡路里
    int newBurn;
    //总距离/m
    float distance;
    //总时间/s
    int duration;
    //平均速度m/s
    float speed;
    //坐标点集合
    List<LatLng> locationList;

    AMap aMap;

    //显示区域
    double west,south,east,north;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_burn_complete);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        newBurn = intent.getIntExtra("newBurn",0);
        distance = intent.getFloatExtra("distance",0);
        duration = intent.getIntExtra("duration",0);
        speed = intent.getFloatExtra("speed",0);
        locationList = intent.getParcelableArrayListExtra("locationList");
        for(int i = 0; i < locationList.size(); i++){
            LatLng  l = locationList.get(i);
            if(i==0){
                west = l.longitude;
                east = l.longitude +1;
                south = l.latitude;
                north = l.latitude +1;
            }else{
                if(l.longitude < west)
                    west = l.latitude;
                else  if(l.longitude > east)
                    east = l.latitude;
                if(l.latitude < south) west = l.longitude;
                if(l.latitude > north) north = l.longitude;
            }
            System.out.println(west+"-w--"+south+"-s--"+east+"-e--"+north+"-n--");
        }


        bindViews();
        bindListeners();
    }

    private void bindViews() {
        if (aMap == null) {
            aMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
        }
        //设置希望展示的地图缩放级别
        aMap.moveCamera(CameraUpdateFactory.newLatLngBounds(new LatLngBounds(new LatLng(south,west),new LatLng(north,east)),10));


        burnCalorieTxt.setText(newBurn+"");
        distanceTxt.setText(distance+"");
        durationTxt.setText(duration+"");
        speedTxt.setText(speed+"");
        dateTxt.setText(TimeUtils.getFormatDate(new Date(),"MM月dd日 HH:mm"));
        nicknameTxt.setText(Utils.loginUser.getNickname());
        avatar.setImageURI(Utils.loginUser.getAvatarDisplayUrl());

        Polyline polyline = aMap.addPolyline(
                new PolylineOptions()
                        .addAll(locationList)
                        .width(5)
                      //.useGradient(true)
                        .color(Color.BLUE));
    }

    private void bindListeners() {
        //分享
        shareBtn.setOnClickListener(v->{
            // TODO: 2017/1/29
        });
        //完成
        okBtn.setOnClickListener(v->{

        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
