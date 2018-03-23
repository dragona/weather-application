package com.example.weather.test;

/**
 * Created by apple on 2018/3/23.
 */

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import com.example.weather.util.HttpCallbackListener;
import com.example.weather.util.HttpUtil;

import android.test.AndroidTestCase;

public class WeatherGetTest  extends AndroidTestCase{
    public void testGetData(){
        String cityName;
        try {
            cityName = URLEncoder.encode("重庆", "utf-8");
            String  weatherUrl="http://v.juhe.cn/weather/index?format=2&cityname="+cityName+"&key=3f915b0cad6ae92729f8eb1f0446b66d";

            HttpUtil.sendHttpRequest(weatherUrl, new HttpCallbackListener() {

                public void onFinish(String response) {
                    // TODO Auto-generated method stub
                    System.out.println(response);
                }

                public void onError(Exception e) {
                    // TODO Auto-generated method stub

                }
            });
        }catch (Exception e) {
            // TODO: handle exception
        }

    }
}