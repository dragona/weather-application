package mg.studio.weatherappdesign;


import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class MainActivity extends AppCompatActivity {
    String[] ADate = new String[]{"MONDAY","TUESDAY","WEDNESDAY","THURSDAY","FRIDAY","SATURDAY","SUNDAY"};
    Pattern p = Pattern.compile("location.{3}([\u4E00-\u9FA5]+).*?cond_txt_d.{3}([\u4E00-\u9FA5]+).*?date.{3}(.{10}).*?tmp_max.{3}(\\d+).*" +
            "?cond_txt_d.{3}([\u4E00-\u9FA5]+).*?date.{3}(.{10}).*?tmp_max.{3}(\\d+).*" +
            "?cond_txt_d.{3}([\u4E00-\u9FA5]+).*?date.{3}(.{10}).*?tmp_max.{3}(\\d+).*");
    Calendar c = Calendar.getInstance();
    String mWay = String.valueOf(c.get(Calendar.DAY_OF_WEEK));
    Integer mwee = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void btnClick(View view) {
        String str = "the function is not designed";
        Toast.makeText(getApplicationContext(), str, Toast.LENGTH_LONG).show();
    }

    public void btnRefresh(View view) {
        boolean success = false;
        ConnectivityManager connManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo.State state = connManager.getNetworkInfo(
                ConnectivityManager.TYPE_WIFI).getState(); // 获取网络连接状态
        if (NetworkInfo.State.CONNECTED == state) { // whether using the WIFI or not
            success = true;
        }

        state = connManager.getNetworkInfo(
                ConnectivityManager.TYPE_MOBILE).getState(); // get the MOBILE net-connection state
        if (NetworkInfo.State.CONNECTED == state) { // whether using the GPRS or not
            success = true;
        }
        if (success) {
            myUploadData();
            new DownloadUpdate().execute();
        } else {
            Toast.makeText(MainActivity.this, "Please check your net-connection", Toast.LENGTH_LONG).show();
        }
    }

    private void myUploadData() {

        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH) + 1;
        int day = c.get(Calendar.DAY_OF_MONTH);
        if ("1".equals(mWay)) {
            mWay = "SUNDAY";
            mwee = 6;
        } else if ("2".equals(mWay)) {
            mWay = "MONDAY";
            mwee = 0;
        } else if ("3".equals(mWay)) {
            mWay = "TUESDAY";
            mwee = 1;
        } else if ("4".equals(mWay)) {
            mWay = "WEDNESDAY";
            mwee = 2;
        } else if ("5".equals(mWay)) {
            mWay = "THURSDAY";
            mwee = 3;
        } else if ("6".equals(mWay)) {
            mWay = "FRIDAY";
            mwee = 4;
        } else if ("7".equals(mWay)) {
            mWay = "SATURDAY";
            mwee = 5;
        }
//        String date = null;
//        if (day > 9 && month > 9) {
//            date = Integer.toString(month) + '/' + Integer.toString(day) + '/' + Integer.toString(year);
//        } else if (day < 10 && month > 9) {
//            date = Integer.toString(month) + '/' + '0' + Integer.toString(day) + '/' + Integer.toString(year);
//        } else if (month < 10 && day > 9) {
//            date = '0' + Integer.toString(month) + '/' + Integer.toString(day) + '/' + Integer.toString(year);
//        } else {
//            date = '0' + Integer.toString(month) + '/' + '0' + Integer.toString(day) + '/' + Integer.toString(year);
//        }
        ((TextView) findViewById(R.id.head_date)).setText(mWay);
//        ((TextView) findViewById(R.id.tv_date)).setText(date);

    }


    private class DownloadUpdate extends AsyncTask<String, Void, String> {


        @Override
        protected String doInBackground(String... strings) {
            String stringUrl = "https://free-api.heweather.com/s6/weather/forecast?";
            HttpURLConnection urlConnection = null;
            BufferedReader reader;
            String location = "location=chongqing";


            String key = "key=411e1bd9c04e4d6c80369ab50bcb63ac";

            stringUrl = stringUrl
                    + location + "&" + key;

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
                return buffer.toString();

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
        /*
        get the data from the json file which I download from hefeng weather
         */
        protected void onPostExecute(String temperature) {
            //Update the temperature displayed
            Matcher m = p.matcher(temperature);
            m = p.matcher(temperature);
            String location = null;
            String temprature = null;
            String weather = null;
            String date = null;
            if (m.find()) {
                location = m.group(1);
                temprature = m.group(4);
                weather = m.group(2);
                date = m.group(3);

            }
            ImageView wea = findViewById(R.id.img_weather_condition);
            TextView text = findViewById(R.id.temperature_of_the_day);//
            myChangeWeather(wea,weather);//change the main image of weather
            myChangeText(text,temprature);//change the temperature of today
            text = findViewById(R.id.tv_location);
            myChangeText(text,location);
            text = findViewById(R.id.tv_date);
            myChangeText(text,date);

            //Change the list below
            myChangeList(m);
        }
    }

    private void myChangeList(Matcher m) {
        String temp = m.group(2);
        ImageView wea = findViewById(R.id.day_one);
        myChangeWeather(wea,temp);

        temp = ADate[mwee].substring(0,3);
        TextView text = findViewById(R.id.text_day_one);
        myChangeText(text,temp);

        wea = findViewById(R.id.day_two);
        temp = m.group(5);
        myChangeWeather(wea,temp);

        temp = ADate[(mwee+1)%7].substring(0,3);
        text = findViewById(R.id.text_day_tow);
        myChangeText(text,temp);

        temp = m.group(8);
        wea = findViewById(R.id.day_thr);
        myChangeWeather(wea,temp);

        temp = ADate[(mwee+2)%7].substring(0,3);
        text = findViewById(R.id.text_day_thr);
        myChangeText(text,temp);

        temp = ADate[(mwee+3)%7].substring(0,3);
        text = findViewById(R.id.text_day_fou);
        myChangeText(text,temp);
    }

    private void myChangeText(TextView viewById, String location) {
        viewById.setText(location);
    }

    private void myChangeWeather(ImageView wea, String weather) {
        switch (weather){
            case "阴":
                wea.setImageResource(R.drawable.windy_small);
                break;
            case"小雨":
                wea.setImageResource(R.drawable.rainy_small);
                break;
            case "晴":
                wea.setImageResource(R.drawable.sunny_small);
                break;
            case"阵雨":
                wea.setImageResource(R.drawable.rainy_small);
                break;
            case "多云":
                wea.setImageResource(R.drawable.partly_sunny_small);
                break;
            case "晴间多云":
                wea.setImageResource(R.drawable.partly_sunny_small);
                break;
            default:

                break;
        }
    }

}
