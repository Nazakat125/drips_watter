package com.example.drips_watter;

import android.content.Intent;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Calendar;


public class CompanyHome extends Fragment {

    String name, number, image;
    ImageView companyImage;
    TextView companyName, greetings;
    ViewFlipper viewFlipper;
    RelativeLayout priceUpdate;
    CardView deliveryBoyList;
    ImageButton logout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_company_home, container, false);
        init(view);
        setGreeting();
        viewFlipper.setInAnimation(getContext(), android.R.anim.slide_in_left);
        viewFlipper.setOutAnimation(getContext(), android.R.anim.slide_out_right);
        viewFlipper.setFlipInterval(3000); // 3 seconds
        viewFlipper.startFlipping();
        name = getArguments().getString("name");
        number = getArguments().getString("number");
        image = getArguments().getString("image");
        companyName.setText(name);
        Glide.with(this).load(image).into(companyImage);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), WellcomeScreen.class));
            }
        });
        priceUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(),CompanyPriceUpdate.class);
                intent.putExtra("number",number);
                intent.putExtra("name",name);
                intent.putExtra("image",image);
                startActivity(intent);
            }
        });
        deliveryBoyList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), CompanyDeleiveryBoyList.class);
                intent.putExtra("number",number);
                intent.putExtra("name",name);
                intent.putExtra("image",image);
                startActivity(intent);
            }
        });

        return  view;
    }
    void init(View view) {
        companyName = view.findViewById(R.id.company_name_data);
        greetings = view.findViewById(R.id.company_greedings_data);
        companyImage = view.findViewById(R.id.company_proflie_image_data);
        viewFlipper = view.findViewById(R.id.company_flipper);
        priceUpdate = view.findViewById(R.id.company_price_update);
        deliveryBoyList = view.findViewById(R.id.company_delivery_boy_detail);
        logout = view.findViewById(R.id.company_logout);
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