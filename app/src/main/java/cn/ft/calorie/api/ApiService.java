package cn.ft.calorie.api;


import java.util.List;
import java.util.Map;

import cn.ft.calorie.pojo.BurnRecord;
import cn.ft.calorie.pojo.IntakeRecord;
import cn.ft.calorie.pojo.UserInfo;
import cn.ft.calorie.pojo.WeigthRecord;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
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
    @POST(API_PATH + "user/isTelExist")
    Observable<ApiResult<Boolean>> isTelExist(@Query("tel") String tel);
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
    /**
     * 上传头像
     */
    @Multipart
    @POST(API_PATH + "user/uploadAvatar")
    Observable<ApiResult<String>> uploadAvatar(@Part MultipartBody.Part avatarBody, @Part("uid")String uid);
    /**
     * 重置密码
     */
    @POST(API_PATH + "user/resetPassword")
    Observable<ApiResult<UserInfo>> resetPassword(@Body UserInfo userInfo);

    /**
     * 摄入记录
     */
    @GET(API_PATH + "intakeRecord")
    Observable<ApiResult<List<IntakeRecord>>> getIntakeRecords(@QueryMap Map<String,String> options);
    /**
     * 锻炼记录
     */
    @GET(API_PATH + "burnRecord")
    Observable<ApiResult<List<BurnRecord>>> getBurnRecords(@QueryMap Map<String,String> options);
    /**
     * 体重记录
     */
    @GET(API_PATH + "weightRecord")
    Observable<ApiResult<List<WeigthRecord>>> getWeigthRecords(@QueryMap Map<String,String> options);

}
