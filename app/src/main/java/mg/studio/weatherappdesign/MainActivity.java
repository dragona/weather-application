package mg.studio.weatherappdesign;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import java.util.*;
import org.json.JSONObject;


public class MainActivity extends AppCompatActivity {

    private static final int msgKey1 = 1;
    private TextView mData;
    private TextView mWeek;
    private TextView mLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mData = (TextView) findViewById(R.id.tv_date);
        mWeek = (TextView) findViewById(R.id.tv_week);
        mLocation = (TextView) findViewById(R.id.tv_location);

        new DownloadUpdate().execute();
    }


    //获得当前年月日
    public String getData(){
        final Calendar c = Calendar.getInstance();
        c.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        String mYear = String.valueOf(c.get(Calendar.YEAR)); // 获取当前年份
        String mMonth = String.valueOf(c.get(Calendar.MONTH) + 1);// 获取当前月份
        String mDay = String.valueOf(c.get(Calendar.DAY_OF_MONTH));// 获取当前月份的日期号码

        //统一格式
        if(Integer.parseInt(mMonth)<10){mMonth="0"+mMonth;}
        if(Integer.parseInt(mDay)<10){mDay="0"+mDay;}

        return mMonth + "/" +mDay + "/" + mYear;
    }

    //获得当前星期
    public String getWeek(){
        final Calendar c = Calendar.getInstance();
        c.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        String mWay = String.valueOf(c.get(Calendar.DAY_OF_WEEK));

        if("1".equals(mWay)){
            mWay ="SUNDAY";
        }else if("2".equals(mWay)){
            mWay ="MONDAY";
        }else if("3".equals(mWay)){
            mWay ="TUESDAY";
        }else if("4".equals(mWay)){
            mWay ="WEDNESDAY";
        }else if("5".equals(mWay)){
            mWay ="THURSDAY";
        }else if("6".equals(mWay)){
            mWay ="FRIDAY";
        }else if("7".equals(mWay)){
            mWay ="SATURDAY";
        }

        return mWay;
    }


    public void btnClick(View view) {
        
        new DownloadUpdate().execute();
    }


    private class DownloadUpdate extends AsyncTask<String, Void, String> {


        @Override
        protected String doInBackground(String... strings) {
            String stringUrl= "http://v.juhe.cn/weather/index?format=2&cityname=重庆&key=3f915b0cad6ae92729f8eb1f0446b66d";
            HttpURLConnection urlConnection = null;
            BufferedReader reader;
            mData.setText(getData());
            mWeek.setText(getWeek());

            try {
                URL url = new URL(stringUrl);

                // Create the request to get the information from the server, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();

                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Mainly needed for debugging
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }
                //The temperature
                return buffer.toString().substring(66,68);

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String temperature) {
            //Update the temperature displayed
            ((TextView) findViewById(R.id.temperature_of_the_day)).setText(temperature);
        }
    }
}
