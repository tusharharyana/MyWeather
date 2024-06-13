package com.example.myweather;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {

    TextView result;
    Button btnBack;
    private final String url = "http://api.openweathermap.org/data/2.5/weather";
    private final String apiId = "5668555365ebe9415737ddc4c0101bc4";
    DecimalFormat df = new DecimalFormat("#.##");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        result = findViewById(R.id.result);
        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> {
            Intent i = new Intent(MainActivity.this,InformationActivity.class);
            startActivity(i);
        });

        Bundle bundle = getIntent().getExtras();
        String city = bundle.getString("city");
        String country = bundle.getString("country");
        String tempUrl = "";

        if (city.equals("")) {
            result.setText("City field cannot be empty");
        } else {
            try {
                city = URLEncoder.encode(city, "UTF-8");
                if (!country.equals("")) {
                    country = URLEncoder.encode(country, "UTF-8");
                    tempUrl = url + "?q=" + city + "," + country + "&appid=" + apiId;
                } else {
                    tempUrl = url + "?q=" + city + "&appid=" + apiId;
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            StringRequest stringRequest = new StringRequest(Request.Method.GET, tempUrl,
                    response -> {
//                        Log.d("response", response);
//                        result.setText(response);

                        String output = "";
                        try{
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("weather");

                            JSONObject jsonObjectWeather = jsonArray.getJSONObject(0);
                            String description = jsonObjectWeather.getString("description");

                            JSONObject jsonObjectMain = jsonObject.getJSONObject("main");
                            double temp = jsonObjectMain.getDouble("temp") - 273.15;
                            double feelsLike = jsonObjectMain.getDouble("feels_like")- 273.15;
                            float pressure = jsonObjectMain.getInt("pressure");
                            int humidity = jsonObjectMain.getInt("humidity");

                            JSONObject jsonObjectWind = jsonObject.getJSONObject("wind");
                            String wind = jsonObjectWind.getString("speed");

                            JSONObject jsonObjectCloud = jsonObject.getJSONObject("clouds");
                            String clouds = jsonObjectCloud.getString("all");

                            JSONObject jsonObjectSys = jsonObject.getJSONObject("sys");
                            String countryName = jsonObjectSys.getString("country");
                            String cityName = jsonObject.getString("name");

//                            result.setTextColor(Color.rgb(68,134,199));

                            output += "Current Weather of " + cityName + " ("+ countryName + ")" +"\n"
                                    + "\n Temp: " + df.format(temp)+ " °C"
                                    + "\n Feels Like: " + df.format(feelsLike) + " °C"
                                    + "\n Humidity: " + humidity + "%"
                                    + "\n Description: " + description
                                    + "\n Wind Speed: " + wind + "m/s (meter per second)"
                                    + "\n Cloudiness: " + clouds + "%"
                                    + "\n Pressure: " + pressure + " hPa";

                            result.setText(output);

                        }catch (JSONException e){
                            e.printStackTrace();
                        }

                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(MainActivity.this, error.toString().trim(), Toast.LENGTH_SHORT).show();
                    Log.e("VolleyError", error.toString());
                }
            });

            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
            requestQueue.add(stringRequest);
        }
    }
}
