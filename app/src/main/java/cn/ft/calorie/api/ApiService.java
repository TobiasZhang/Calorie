package cn.ft.calorie.api;


import cn.ft.calorie.pojo.UserInfo;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by TT on 2016/11/29.
 */
public interface ApiService {
    String API_PATH = "api/";
    String ADMIN_PATH = "admin/";
    /**
     * 用户登录
     */
    @POST(API_PATH + "user/login")
    Observable<ApiResult<UserInfo>> login(@Body UserInfo userInfo);
    /**
     * 验证手机号是否存在
     */
    @POST(API_PATH + "user/validateTel")
    Observable<ApiResult<Void>> validateTel(@Query("tel") String tel);
    /**
     * 用户注册
     */
    @POST(API_PATH + "user/register")
    Observable<ApiResult<UserInfo>> register(@Body UserInfo userInfo);
    /**
     * 修改用户
     */
    @POST(API_PATH + "user/merge")
    Observable<ApiResult<UserInfo>> mergeUserInfo(@Body UserInfo userInfo);
}
