package com.example.drips_watter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class WellcomeScreen extends AppCompatActivity {
CardView company,user,delivery;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wellcome_screen);
        init();
        user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WellcomeScreen.this, UserLogIn.class);
                startActivity(intent);
            }
        });
        company.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WellcomeScreen.this, CompanyLogin.class);
                startActivity(intent);
            }
        });
        delivery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WellcomeScreen.this, DeliveryLogin.class);
                startActivity(intent);
            }
        });


    }
    void init(){
        company = findViewById(R.id.company);
        user = findViewById(R.id.user);
        delivery = findViewById(R.id.delivery);
    }
}