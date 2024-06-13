package com.example.myweather;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.text.DecimalFormat;

public class InformationActivity extends AppCompatActivity {

    EditText city,country;
    Button btnSearch;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);

        city = findViewById(R.id.city);
        country = findViewById(R.id.country);
        btnSearch = findViewById(R.id.btnSearch);

        btnSearch.setOnClickListener(v -> {
            String ecity = city.getText().toString().trim();
            String ecountry = country.getText().toString().trim();

            Bundle bundle = new Bundle();
            bundle.putString("city", ecity);
            bundle.putString("country", ecountry);

            Intent i = new Intent(InformationActivity.this, MainActivity.class);
            i.putExtras(bundle);
            startActivity(i);
        });

    }

}