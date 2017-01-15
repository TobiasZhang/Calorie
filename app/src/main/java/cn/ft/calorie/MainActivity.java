package cn.ft.calorie;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import cn.ft.calorie.api.ApiUtils;
import cn.ft.calorie.pojo.UserInfo;
import rx.functions.Action1;

public class MainActivity extends AppCompatActivity {
    private ApiUtils apiUtils = ApiUtils.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



    }
    public void xx(View v){
        UserInfo u=  new UserInfo();
        u.setTel("123123");
        u.setPassword("大2b");
        u.setId("587baf9ea18eb302146995bb");
        apiUtils.getApiDataObservable(apiUtils.getApiServiceImpl().mergeUserInfo(u))
                .subscribe(
                        userInfo -> System.out.println(userInfo.getPassword()),
                        e -> System.out.println(e.getMessage())
                );
    }
}
