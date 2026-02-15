package com.example.weatherapp;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    EditText etCity;
    Button btnGetWeather;
    TextView tvResult;

    // OpenWeather API Key
    String API_KEY = "1fc1ee457527a0ce89852d9f45531915";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etCity = findViewById(R.id.etCity);
        btnGetWeather = findViewById(R.id.btnGetWeather);
        tvResult = findViewById(R.id.tvResult);

        btnGetWeather.setOnClickListener(v -> {
            String city = etCity.getText().toString().trim();
            if (!city.isEmpty()) {
                fetchWeather(city);
            } else {
                tvResult.setText("📍 Please enter a city name 🏙️");
            }
        });
    }

    private void fetchWeather(String city) {

        String url = "https://api.openweathermap.org/data/2.5/weather?q="
                + city + "&units=metric&appid=" + API_KEY;

        RequestQueue queue = Volley.newRequestQueue(this);

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET, url, null,
                response -> {
                    try {
                        JSONObject main = response.getJSONObject("main");
                        double temp = main.getDouble("temp");
                        int humidity = main.getInt("humidity");

                        JSONArray weatherArray = response.getJSONArray("weather");
                        String condition = weatherArray.getJSONObject(0)
                                .getString("main");

                        tvResult.setText(
                                "🌍 City: " + city + "\n\n" +
                                        "🌡️ Temperature: " + temp + " °C\n" +
                                        "💧 Humidity: " + humidity + "%\n" +
                                        "🌦️ Condition: " + condition + "\n\n" +
                                        "😊 Have a great day!"
                        );

                    } catch (Exception e) {
                        tvResult.setText("⚠️ Oops! Error reading weather data 😕");
                    }
                },
                error -> tvResult.setText("❌ Ahh! City not found 🏙️😢")
        );

        queue.add(request);
    }
}
