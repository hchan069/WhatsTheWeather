package com.spaga.whatstheweather;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    EditText cityName;
    Button findWeather;
    TextView resultTextView;

    public void findWeather(View view) {
        Log.i("cityName", cityName.getText().toString());

        DownloadTask task = new DownloadTask();
        task.execute("http://samples.openweathermap.org/data/2.5/weather?q= " +
                cityName.getText().toString() + "&appid=b6907d289e10d714a6e88b30761fae22");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cityName = findViewById(R.id.cityName);
        findWeather = findViewById(R.id.findWeather);
        resultTextView = findViewById(R.id.resultTextView);
    }

    public class DownloadTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
            String result = "";
            URL url;
            try {
                url = new URL(urls[0]);
                HttpURLConnection connection = (HttpURLConnection)url.openConnection();
                InputStream inputStream = connection.getInputStream();
                InputStreamReader reader = new InputStreamReader(inputStream);

                int data = reader.read();
                while (data != -1) {
                    char current = (char) data;
                    result += current;
                    data = reader.read();
                }
                return result;

            } catch (Exception e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            try {
                String message = "";
                JSONObject jsonObject = new JSONObject(result);
                String weatherInfo = jsonObject.getString("weather");

                Log.i("Weather Content: ", weatherInfo);

                JSONArray array = new JSONArray(weatherInfo);

                for (int i = 0; i < array.length(); i++) {
                    JSONObject jsonPart = array.getJSONObject(i);
                    String main = "";
                    String description = "";

                    main = jsonPart.getString("main");
                    description = jsonPart.getString("description");

                    if (main != "" && description != "")
                        message += main + ": " + description + "\r\n";
                }

                if (message != "")
                    resultTextView.setText(message);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
