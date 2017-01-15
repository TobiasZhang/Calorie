package cn.ft.calorie.api;

/**
 * Created by TT on 2017/1/16.
 */
public class ApiException extends RuntimeException {
    public ApiException(String message) {
        super(message);
    }
}
