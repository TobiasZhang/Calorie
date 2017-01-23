package cn.ft.calorie.api;

import android.widget.Scroller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.text.DateFormat;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by TT on 2017/01/02.
 */
public class ApiUtils {
    public static final String BASE_URL = "http://192.168.1.103:3000";
    private static final String RESULT_OK = "ok";
    private static final int DEFAULT_TIMEOUT = 5;

    private ApiService apiServiceImpl;
    public ApiService getApiServiceImpl() {
        return apiServiceImpl;
    }

    //构造方法私有
    private ApiUtils(){
        //手动创建一个OkHttpClient并设置超时时间
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .build();
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd HH:mm:ss")
                .create();
        Retrofit retrofit = new Retrofit.Builder()
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(BASE_URL)
                .build();
        apiServiceImpl = retrofit.create(ApiService.class);
    }
    //在访问HttpMethods时创建单例
    private static final ApiUtils INSTANCE = new ApiUtils();
    //获取单例
    public static ApiUtils getInstance(){
        return INSTANCE;
    }
    /**
     * 请求api数据
     * @param o
     * @param <T>
     * @return
     */
    public<T> Observable<T> getApiDataObservable(Observable<ApiResult<T>> o){
        return addThreadSchedulers(o.map(new HttpResultFunc<T>()));
    }
    /**
     * 用来统一处理Http的resultCode,并将HttpResult的Data部分剥离出来返回给subscriber
     *
     * @param <T> Subscriber真正需要的数据类型，也就是Data部分的数据类型
     */
    private class HttpResultFunc<T> implements Func1<ApiResult<T>, T> {
        @Override
        public T call(ApiResult<T> apiResult) {
            if (!apiResult.getResult().equals(RESULT_OK)) {
                throw new ApiException(apiResult.getResult());
            }
            return apiResult.getData();
        }
    }
    //添加线程管理
    private<T> Observable<T> addThreadSchedulers(Observable<T> o){
        return o.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
