package cn.ft.calorie.api;


import cn.ft.calorie.pojo.UserInfo;
import retrofit2.http.Body;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by TT on 2016/11/29.
 */
public interface ApiService {
    String API_PATH = "api/";
    String ADMIN_PATH = "admin/";
    @POST(API_PATH+"user/merge")
    Observable<ApiResult<UserInfo>> mergeUserInfo(@Body UserInfo userInfo);
 /*   @POST("user/getall/")
    Observable<ApiResult<List<UserInfo>>> getTest();*/
}
