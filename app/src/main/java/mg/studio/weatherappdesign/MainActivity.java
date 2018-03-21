package mg.studio.weatherappdesign;


import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
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
            new DownloadUpdate().execute();
            myUploadData();
        } else {
            Toast.makeText(MainActivity.this, "Please check your net-connection", Toast.LENGTH_LONG).show();

        }
    }

    private void myUploadData() {
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH) + 1;
        int day = c.get(Calendar.DAY_OF_MONTH);

        String mWay = String.valueOf(c.get(Calendar.DAY_OF_WEEK));
        if ("1".equals(mWay)) {
            mWay = "SUNDAY";
        } else if ("2".equals(mWay)) {
            mWay = "MONDAY";
        } else if ("3".equals(mWay)) {
            mWay = "TUESDAY";
        } else if ("4".equals(mWay)) {
            mWay = "WEDNESDAY";
        } else if ("5".equals(mWay)) {
            mWay = "THURSDAY";
        } else if ("6".equals(mWay)) {
            mWay = "FRIDAY";
        } else if ("7".equals(mWay)) {
            mWay = "SATURDAY";
        }
        String date = null;
        if (day > 9 && month > 9) {
            date = Integer.toString(month) + '/' + Integer.toString(day) + '/' + Integer.toString(year);
        } else if (day < 10 && month > 9) {
            date = Integer.toString(month) + '/' + '0' + Integer.toString(day) + '/' + Integer.toString(year);
        } else if (month < 10 && day > 9) {
            date = '0' + Integer.toString(month) + '/' + Integer.toString(day) + '/' + Integer.toString(year);
        } else {
            date = '0' + Integer.toString(month) + '/' + '0' + Integer.toString(day) + '/' + Integer.toString(year);
        }
        ((TextView) findViewById(R.id.head_date)).setText(mWay);
        ((TextView) findViewById(R.id.tv_date)).setText(date);

    }


    private class DownloadUpdate extends AsyncTask<String, Void, String> {


        @Override
        protected String doInBackground(String... strings) {
            String stringUrl = "https://free-api.heweather.com/s6/weather/now?";
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
            Pattern p = Pattern.compile("location.{3}([\u4E00-\u9FA5]+).*tmp.{3}(\\d+)\"");
            Matcher m = p.matcher(temperature);
            m = p.matcher(temperature);
            String location = null;
            String temprature = null;
            if (m.find()) {
                location = m.group(1);
                temprature = m.group(2);
            }
            ((TextView) findViewById(R.id.temperature_of_the_day)).setText(temprature);
            ((TextView) findViewById(R.id.tv_location)).setText(location);

        }


    }

}
