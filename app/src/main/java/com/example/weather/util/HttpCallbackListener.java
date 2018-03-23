package com.example.weather.util;

/**
 * Created by apple on 2018/3/23.
 */

public interface HttpCallbackListener {
    void onFinish(String response);
    void onError(Exception e);
}
