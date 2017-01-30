package cn.ft.calorie.ui;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.os.SystemClock;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.ft.calorie.R;
import cn.ft.calorie.util.TimeUtils;
import cn.ft.calorie.util.Utils;

/**
 * Created by TT on 2017/1/26.
 */
public class AddBurnActivity extends ToolbarActivity {
    @BindView(R.id.burnCalorieTxt)
    TextView burnCalorieTxt;
    @BindView(R.id.intakeCalorieTxt)
    TextView intakeCalorieTxt;
    @BindView(R.id.startBtn)
    Button startBtn;
    @BindView(R.id.pauseBtn)
    Button pauseBtn;
    @BindView(R.id.finishBtn)
    Button finishBtn;
    @BindView(R.id.distanceTxt)
    TextView distanceTxt;
    @BindView(R.id.durationTxt)
    TextView durationTxt;
    @BindView(R.id.speedTxt)
    TextView speedTxt;
    @BindView(R.id.countDownTimerTxt)
    TextView countDownTimerTxt;

    int currBurn;
    int currIntake;
    int newBurn;
    boolean isFinish = false;
    boolean isStarting = false;

    //坐标点集合
    List<LatLng> locationList = new ArrayList<>();
    //总距离/m
    float distance;
    //总时间/s
    int duration;
    //平均速度m/s
    float speed;
    //开始时间
    Date startTime;
    //终止时间
    Date terminalTime;

    Handler timerHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            System.out.println("有msg----" + msg.what);
            if (msg.what == 1) {
                duration++;
                durationTxt.setText(TimeUtils.formatSeconds(duration));
                timerHandler.sendEmptyMessageDelayed(1, 1000);
            }
        }
    };
    CountDownTimer countDownTimer;
    AMapLocationClient mLocationClient;

    Ringtone finishRingtone;
    Vibrator finishVibrator;

    private PendingIntent alarmPendingIntent;
    private AlarmManager alarm;

    //唤醒receiver
    private BroadcastReceiver alarmReceiver = new BroadcastReceiver(){
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals("ALARM_LOCATION")){
                System.out.println("-----alarmReceiver------");
                if(null != mLocationClient){
                    mLocationClient.startLocation();
                }
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 创建Intent对象，action为LOCATION
        Intent alarmIntent = new Intent();
        alarmIntent.setAction("ALARM_LOCATION");

        // 定义一个PendingIntent对象，PendingIntent.getBroadcast包含了sendBroadcast的动作。
        // 也就是发送了action 为"LOCATION"的intent
        alarmPendingIntent = PendingIntent.getBroadcast(this, 0, alarmIntent, 0);
        alarm = (AlarmManager) getSystemService(ALARM_SERVICE);

        //动态注册一个广播
        IntentFilter filter = new IntentFilter();
        filter.addAction("ALARM_LOCATION");
        registerReceiver(alarmReceiver, filter);


        //声明AMapLocationClient类对象
        mLocationClient = new AMapLocationClient(getApplicationContext());
        //声明定位回调监听器
        AMapLocationListener mLocationListener = new AMapLocationListener() {
            @Override
            public void onLocationChanged(AMapLocation aMapLocation) {
                if (aMapLocation != null) {
                    if (aMapLocation.getErrorCode() == 0) {
                        System.out.println("定位了--------------");
                        handleLocation(aMapLocation);
                        //可在其中解析amapLocation获取相应内容。
                    } else {
                        //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
                        Log.e("AmapError", "location Error, ErrCode:"
                                + aMapLocation.getErrorCode() + ", errInfo:"
                                + aMapLocation.getErrorInfo());
                    }
                }
            }
        };
        //设置定位回调监听
        mLocationClient.setLocationListener(mLocationListener);

        //声明AMapLocationClientOption对象
        AMapLocationClientOption mLocationOption = new AMapLocationClientOption();
        //设置定位模式为AMapLocationMode.Hight_Accuracy，高精度模式。
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);

        //获取最近3s内精度最高的一次定位结果：
        //设置setOnceLocationLatest(boolean b)接口为true，启动定位时SDK会返回最近3s内精度最高的一次定位结果。如果设置其为true，setOnceLocation(boolean b)接口也会被设置为true，反之不会，默认为false。
        //mLocationOption.setOnceLocationLatest(true);
        mLocationOption.setInterval(1000);

        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);

    }

    @Override
    protected void setLayout() {
        setContentView(R.layout.activity_add_burn);
        this.currBurn = getIntent().getIntExtra("currBurn", 0);
        this.currIntake = getIntent().getIntExtra("currIntake", 0);
        //设置完成铃声
        Uri notificationRingtone = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        finishRingtone = RingtoneManager.getRingtone(getApplicationContext(), notificationRingtone);
        //设置完成震动
        finishVibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
    }

    @Override
    protected void bindViews() {
        ButterKnife.bind(this);
        toolbar.setTitle("跑步锻炼");
        burnCalorieTxt.setText(this.currBurn + "");
        intakeCalorieTxt.setText(this.currIntake + "");
        distanceTxt.setText("0");
        speedTxt.setText("0");
        durationTxt.setText("00:00:00");

        startBtn.setVisibility(View.VISIBLE);
        finishBtn.setVisibility(View.GONE);
        pauseBtn.setTag("pause");
        pauseBtn.setVisibility(View.GONE);
        
        countDownTimer = new CountDownTimer(4000,1000){
            @Override
            public void onTick(long millisUntilFinished) {
                int s = (int) ((millisUntilFinished-1000)/1000);
                if(s==0){
                    countDownTimerTxt.setText("GO!");
                }else{
                    countDownTimerTxt.setText(s+"");
                }
            }

            @Override
            public void onFinish() {
                startLocation();
                isStarting = true;
                startTime = new Date();
                countDownTimerTxt.setVisibility(View.GONE);
            }
        };
    }

    @Override
    protected void bindListeners() {
        //开始
        startBtn.setOnClickListener(v -> {
            mLocationClient.startLocation();//启动定位
            startBtn.setVisibility(View.GONE);
            pauseBtn.setVisibility(View.VISIBLE);

            countDownTimerTxt.setVisibility(View.VISIBLE);
            countDownTimer.start();
        });
        //暂停/继续
        pauseBtn.setOnClickListener(v -> {
            switch (pauseBtn.getTag().toString()) {
                case "pause":
                    terminalTime = new Date();

                    mLocationClient.stopLocation();//停止定位
                    timerHandler.removeMessages(1);
                    //停止定位的时候取消闹钟
                    if(null != alarm){
                        alarm.cancel(alarmPendingIntent);
                    }

                    pauseBtn.setTag("continue");
                    pauseBtn.setText("继续");
                    finishBtn.setVisibility(View.VISIBLE);
                    break;
                case "continue":
                    startLocation();
                    pauseBtn.setTag("pause");
                    pauseBtn.setText("暂停");
                    finishBtn.setVisibility(View.GONE);
                    break;

            }
        });
        //结束
        finishBtn.setOnClickListener(v -> {
            if(locationList.size() < 2){
                Utils.toast(this,"客官，您倒是走两步啊！");
                return;
            }
            Bundle bundle = new Bundle();
            bundle.putInt("newBurn",newBurn);
            bundle.putFloat("distance",distance);
            bundle.putInt("duration",duration);
            bundle.putFloat("speed",speed);
            bundle.putSerializable("startTime",startTime);
            bundle.putSerializable("terminalTime",terminalTime);
            bundle.putParcelableArrayList("locationList", (ArrayList<? extends Parcelable>) locationList);
            Intent completeIntent = new Intent(this,BurnCompleteActivity.class);
            completeIntent.putExtras(bundle);
            startActivity(completeIntent);
//            Utils.toast(this,"结束");
        });
    }

    //处理定位坐标点
    private void handleLocation(AMapLocation aMapLocation) {
        if(!isStarting)
            return;
        LatLng newLatLng = new LatLng(aMapLocation.getLatitude(), aMapLocation.getLongitude());
        if(locationList.size()==0){//第一次记录坐标
            locationList.add(newLatLng);
            return;
        }
        float dDistance = AMapUtils.calculateLineDistance(newLatLng, locationList.get(locationList.size() - 1));
        System.out.println(dDistance+"---------------------ddistance");
        if(dDistance > 0){
            distance += dDistance;
            distanceTxt.setText((int) distance + "");

            //计算跑步热量（kcal）＝体重（kg）×距离（公里）×1.036
            //tempBurn cal
            newBurn = (int)(Utils.loginUser.getWeight()/10 * distance * 1.036);
            int tempBurn = currBurn + newBurn;// TODO: 2017/1/29  体重kg
            burnCalorieTxt.setText(tempBurn + "");
            if (!isFinish && tempBurn >= currIntake) {//当锻炼到达目标
                isFinish = true;
                System.out.println("-----------isfinish==="+isFinish);
                finishRingtone.play();
                finishVibrator.vibrate(300);

                // TODO: 2017/1/29
            }
            locationList.add(newLatLng);
        }
        speed = distance / duration;
        speedTxt.setText((int) speed + "");
    }
    //开始定位
    private void startLocation(){
        mLocationClient.startLocation();//启动定位
        timerHandler.sendEmptyMessageDelayed(1, 1000);
        if(null != alarm){
            //设置一个闹钟，2秒之后每隔一段时间执行启动一次定位程序
            alarm.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + 2*1000,
                    5 * 1000, alarmPendingIntent);
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        System.out.println("--------ondestroy");
        // mapView.onDestroy();
        if (null != mLocationClient) {
            mLocationClient.onDestroy();
            mLocationClient = null;
        }
        if (finishRingtone != null && finishRingtone.isPlaying())
            finishRingtone.stop();
        if(null != alarmReceiver){
            unregisterReceiver(alarmReceiver);
            alarmReceiver = null;
        }
        //停止定位的时候取消闹钟
        if(null != alarm){
            alarm.cancel(alarmPendingIntent);
        }
        timerHandler.removeMessages(1);
        timerHandler = null;
    }
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (finishRingtone != null && finishRingtone.isPlaying())
            finishRingtone.stop();
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public void onBackPressed() {
        if(startTime!=null){
            new AlertDialog.Builder(this)
                    .setTitle("提示")
                    .setMessage("返回之后此次的锻炼记录就作废了")
                    .setNegativeButton("取消",null)
                    .setPositiveButton("确认",(b,i)->{
                        super.onBackPressed();
                    })
                    .show();
        }else
            super.onBackPressed();
    }
}
