package com.example.drips_watter;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;

import java.util.Calendar;

public class DeliveryBoyDashboard extends AppCompatActivity {
    String name, key, image,token;
    ImageView imageView;
    TextView userName, greetings;
    ViewFlipper viewFlipper;
    CardView orderList;
    ImageButton logout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_boy_dashboard);
        init();
        setGreeting();
        viewFlipper.setInAnimation(this, android.R.anim.slide_in_left);
        viewFlipper.setOutAnimation(this, android.R.anim.slide_out_right);
        viewFlipper.setFlipInterval(3000); // 3 seconds
        viewFlipper.startFlipping();
        name = getIntent().getStringExtra("name");
        key = getIntent().getStringExtra("key");
        image = getIntent().getStringExtra("image");
        token = getIntent().getStringExtra("token");
        Glide.with(this).load(image).into(imageView);
        userName.setText(name);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DeliveryBoyDashboard.this, WellcomeScreen.class));
            }
        });
        orderList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DeliveryBoyDashboard.this,  DeliveryBoyOrdersList.class);
                intent.putExtra("name",name);
                intent.putExtra("key",key);
                intent.putExtra("image",image);
                intent.putExtra("token",token);
                startActivity(intent);
            }
        });
    }
    void init(){
        imageView = findViewById(R.id.delivery_proflie_image_data);
        userName = findViewById(R.id.delivery_greedings_data);
        greetings = findViewById(R.id.delivery_name_data);
        viewFlipper = findViewById(R.id.delivery_flipper);
        orderList = findViewById(R.id.delivery_all_order);
        orderList = findViewById(R.id.delivery_all_order);
        logout = findViewById(R.id.delivery_logout);
    }
    private void setGreeting() {
        Calendar c = Calendar.getInstance();
        int timeOfDay = c.get(Calendar.HOUR_OF_DAY);

        if (timeOfDay >= 0 && timeOfDay < 12) {
            greetings.setText("Good Morning,");
        } else if (timeOfDay >= 12 && timeOfDay < 16) {
            greetings.setText("Good Afternoon,");
        } else if (timeOfDay >= 16 && timeOfDay < 21) {
            greetings.setText("Good Evening,");
        } else if (timeOfDay >= 21 && timeOfDay < 24) {
            greetings.setText("Good Night,");
        }
    }
}