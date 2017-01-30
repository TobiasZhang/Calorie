package cn.ft.calorie.ui;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.SupportMapFragment;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.Polyline;
import com.amap.api.maps.model.PolylineOptions;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.ft.calorie.R;
import cn.ft.calorie.api.AmapStaticMapUtils;
import cn.ft.calorie.api.ApiUtils;
import cn.ft.calorie.event.BurnCompleteEvent;
import cn.ft.calorie.event.HomeRecordUpdateEvent;
import cn.ft.calorie.pojo.BurnRecord;
import cn.ft.calorie.pojo.sectionrecyclerview.MyLatLng;
import cn.ft.calorie.util.RxBus;
import cn.ft.calorie.util.SubscriptionUtils;
import cn.ft.calorie.util.TimeUtils;
import cn.ft.calorie.util.Utils;
import io.realm.RealmList;

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

    ApiUtils apiUtils = ApiUtils.getInstance();

    //此次燃烧掉的卡路里
    int newBurn;
    //总距离/m
    float distance;
    //总时间/s
    int duration;
    //平均速度m/s
    float speed;
    Date startTime,terminalTime;

    //坐标点集合
    List<LatLng> locationList;
    //-myLatLngList
    RealmList<MyLatLng> myLatLngList = new RealmList<>();

    //缩放等级
    int zoom;
    //中心点
    LatLng center;
    MyLatLng myCenter;

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
        startTime = (Date) intent.getSerializableExtra("startTime");
        terminalTime = (Date) intent.getSerializableExtra("terminalTime");
        locationList = intent.getParcelableArrayListExtra("locationList");
        for(int i = 0; i < locationList.size(); i++){
            LatLng  l = locationList.get(i);
            myLatLngList.add(Utils.latLng2MyLatLng(l));
            if(i==0){
                west = east = l.longitude;
                south = north = l.latitude;
            }else{
                if(l.longitude < west)
                    west = l.longitude;
                else  if(l.longitude > east)
                    east = l.longitude;
                if(l.latitude < south)
                    south = l.latitude;
                else if(l.latitude > north)
                    north = l.latitude;
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
        aMap.getUiSettings().setCompassEnabled(true);//指南针
        aMap.setOnCameraChangeListener(new AMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {
            }

            @Override
            public void onCameraChangeFinish(CameraPosition cameraPosition) {
                if(zoom==0){
                    zoom = (int)cameraPosition.zoom;
                }
            }
        });
        Marker startMarker = aMap.addMarker(
                new MarkerOptions()
                        .position(locationList.get(0))
                        .title("起点")
                        .snippet(TimeUtils.getFormatDate(startTime,"HH:mm:ss"))
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))

        );
        Marker endMarker = aMap.addMarker(
                new MarkerOptions()
                        .position(locationList.get(locationList.size()-1))
                        .title("终点")
                        .snippet(TimeUtils.getFormatDate(terminalTime,"HH:mm:ss"))
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE))
        );
        Polyline polyline = aMap.addPolyline(
                new PolylineOptions()
                        .addAll(locationList)
                        .width(10)
                        .useGradient(true)
                        .color(Color.BLUE));
        //设置展示的地图区域
        aMap.moveCamera(CameraUpdateFactory.newLatLngBounds(new LatLngBounds(new LatLng(south,west),new LatLng(north,east)),100));
        //中心点
        center = new LatLng(south+(north-south)/2,west+(east-west)/2);
        myCenter = Utils.latLng2MyLatLng(center);
        aMap.moveCamera(CameraUpdateFactory.changeLatLng(center));


        burnCalorieTxt.setText(newBurn+"");
        distanceTxt.setText((int)distance+"");
        durationTxt.setText(TimeUtils.formatSeconds(duration));
        speedTxt.setText((int)speed+"");
        dateTxt.setText(TimeUtils.getFormatDate(terminalTime,"MM月dd日 HH:mm"));
        nicknameTxt.setText(Utils.loginUser.getNickname());
        avatar.setImageURI(Utils.loginUser.getAvatarDisplayUrl());

    }

    private void bindListeners() {
        //分享
        shareBtn.setOnClickListener(v->{
            // TODO: 2017/1/29
        });
        //完成
        okBtn.setOnClickListener(v->{
            if(zoom==0){
                Utils.toast(this,"正在生成地图信息");
                return;
            }
            BurnRecord b = new BurnRecord();
            b.setUserInfo(Utils.loginUser);
            b.setCalorie(newBurn);
            b.setDistance((int) distance);
            b.setDuration(duration);
            b.setSpeed((int) speed);
            b.setStartingTime(startTime);
            b.setTerminalTime(terminalTime);
            b.setCenter(myCenter);
            b.setPointList(myLatLngList);
            b.setZoom(zoom);
            //b.setMapImage(AmapStaticMapUtils.getStaticMapUrl(center,zoom,locationList));
            SubscriptionUtils.register(this,apiUtils.getApiDataObservable(apiUtils.getApiServiceImpl().mergeBurnRecord(b))
                    .subscribe(burnRecord -> {
                        // TODO: 2017/1/30
                        System.out.println(burnRecord.getMapImage()+"----");
                        RxBus.getDefault().post(new HomeRecordUpdateEvent());
                        RxBus.getDefault().post(new BurnCompleteEvent());
                        finish();
                        Utils.toast(this,"保存成功");
                    },e->{
                        e.printStackTrace();
                        Utils.toast(this,e.getMessage());
                    }));
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
