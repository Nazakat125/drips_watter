package com.example.drips_watter;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UserTrackOrder extends AppCompatActivity {
    LinearLayout withData,noData;
    String key,number;
    ProgressDialog progressDialog;
    TextView companyName,totalLiters,totalBills,userAddress,quality;
    Button orderCompelted;
    Double waterQl;

    LinearLayout watterQuality;

    TextView orderAccepted,orderAcceptedDetail,readyForDeliveryText,readyForDeliveryDetail,myLocation,myLocationDetail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_track_order);
        init();
        key = getIntent().getStringExtra("key");
        number = getIntent().getStringExtra("number");
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        Log.d("data",key.toString());
        DatabaseReference ref = db.getReference(number).child("Your Order").child(key);
        progressDialog.show();
        DatabaseReference ref2 = db.getReference("03058485853").child("sensordata");
        ref2.get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                 waterQl = dataSnapshot.child("tdsvalue").getValue(Double.class);
            }
        });
        ref.get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    withData.setVisibility(View.VISIBLE);
                    noData.setVisibility(View.GONE);
                    progressDialog.dismiss();
                    String name = dataSnapshot.child("Name").getValue(String.class);
                    String address = dataSnapshot.child("Address").getValue(String.class);
                    String fcmToken = dataSnapshot.child("FCM Token").getValue(String.class);
                    String accepted = dataSnapshot.child("Accepted").getValue(String.class);
                    String delivered = dataSnapshot.child("Delivered").getValue(String.class);
                    String readyForDelivery = dataSnapshot.child("Ready for Delivery").getValue(String.class);
                    Long totalBill = dataSnapshot.child("Total Bill").getValue(Long.class);
                    Long totalLiter = dataSnapshot.child("Total Liter").getValue(Long.class);
                    String number = dataSnapshot.child("number").getValue(String.class);

                    companyName.setText(name);
                    totalLiters.setText(String.valueOf(totalLiter) + " Liters");
                    totalBills.setText(String.valueOf(totalBill)+ "+ Rs") ;
                    userAddress.setText(address);
                    if(accepted.equals("true")){
                        orderAccepted.setTextColor(Color.parseColor("#3FBDF1"));
                        orderAcceptedDetail.setTextColor(Color.parseColor("#3FBDF1"));
                    }
                    if(readyForDelivery.equals("true")){
                        readyForDeliveryText.setTextColor(Color.parseColor("#3FBDF1"));
                        readyForDeliveryDetail.setTextColor(Color.parseColor("#3FBDF1"));
                    }
                    if (delivered.equals("true")){
                        myLocation.setTextColor(Color.parseColor("#3FBDF1"));
                        myLocationDetail.setTextColor(Color.parseColor("#3FBDF1"));
                        orderCompelted.setVisibility(View.VISIBLE);
                        watterQuality.setVisibility(View.VISIBLE);
                        quality.setText(String.valueOf(waterQl) + " ppm");

                    }


                }else {
                    withData.setVisibility(View.GONE);
                    noData.setVisibility(View.VISIBLE);
                    progressDialog.dismiss();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
            }
        });
        orderCompelted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ref.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(UserTrackOrder.this,"Order Compelted Sccessfully",Toast.LENGTH_LONG).show();
                        startActivity(new Intent(UserTrackOrder.this, WellcomeScreen.class));
                    }
                });
            }
        });

    }
    void init(){
        noData = findViewById(R.id.user_package_detail_no_data);
        withData = findViewById(R.id.user_package_detail_view);
        companyName = findViewById(R.id.user_orederd_company_name);
        totalLiters = findViewById(R.id.user_orederd_company_total_liters);
        totalBills = findViewById(R.id.user_orederd_company_total_bill);
        userAddress =findViewById(R.id.user_orederd_company_address);
        orderAccepted =findViewById(R.id.user_order_accepted_color);
        orderAcceptedDetail = findViewById(R.id.user_order_accepted_detail_color);
        readyForDeliveryText = findViewById(R.id.user_ready_for_delivery_color);
        readyForDeliveryDetail = findViewById(R.id.user_ready_for_delivery_detial_color);
        myLocation =findViewById(R.id.user_ready_my_location_color);
        myLocationDetail = findViewById(R.id.user_ready_my_location_detail_color);
        orderCompelted = findViewById(R.id.user_order_compelted);
        watterQuality = findViewById(R.id.user_watter_quality_visible);
        quality = findViewById(R.id.user_watter_quality);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);

    }
}