package com.example.drips_watter;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
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

import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ProductDetail extends AppCompatActivity {
ImageView backbtn,noData;
TextView watterPrice,totalLiter,totalBill;
ImageButton subtract ,add;
Button placeOrder;
ProgressDialog progressDialog;
LinearLayout showData;
int numberOfLiter = 0;
int bill = 0;
int price = 0;
int balance = 0;

String companyTokan,name,userToken,userName,address,image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);
        init();
        totalLiter.setText("0");
        totalBill.setText("0");
        String key = getIntent().getStringExtra("key");
        String number = getIntent().getStringExtra("number");
        String logo = getIntent().getStringExtra("logo");
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        FirebaseDatabase db = FirebaseDatabase.getInstance();
        progressDialog.show();
        DatabaseReference ref = db.getReference(key).child("Water Price");
        ref.get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    noData.setVisibility(View.GONE);
                    showData.setVisibility(View.VISIBLE);
                    String priceInt = dataSnapshot.child("Price").getValue(String.class);
                    price = Integer.parseInt(priceInt);
                    watterPrice.setText(String.valueOf(price));
                    progressDialog.dismiss();


                    DatabaseReference ref2 = db.getReference(key).child("Profile");
                    ref2.get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                        @Override
                        public void onSuccess(DataSnapshot dataSnapshot) {
                             companyTokan = dataSnapshot.child("FCM token").getValue(String.class);
                             name = dataSnapshot.child("Name").getValue(String.class);
                        }
                    });
                    DatabaseReference ref3 = db.getReference(number).child("Profile");
                    ref3.get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                        @Override
                        public void onSuccess(DataSnapshot dataSnapshot) {
                             userToken = dataSnapshot.child("FCM token").getValue(String.class);
                             userName = dataSnapshot.child("Name").getValue(String.class);
                             address = dataSnapshot.child("Address").getValue(String.class);
                             image = dataSnapshot.child("Image").getValue(String.class);
                          String  getbalance = dataSnapshot.child("Balance").getValue(String.class);
                            balance = Integer.parseInt(getbalance);
                        }
                    });

                }else{
                    showData.setVisibility(View.GONE);
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
        subtract.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (numberOfLiter > 0) { // Check if the number of liters is greater than 0
                    numberOfLiter--;
                    bill = price * numberOfLiter;
                    totalLiter.setText(String.valueOf(numberOfLiter));
                    totalBill.setText(String.valueOf(bill));
                }
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                numberOfLiter++;
                bill = price * numberOfLiter;
                totalLiter.setText(String.valueOf(numberOfLiter));
                totalBill.setText(String.valueOf(bill));
            }
        });
        placeOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();
                Map<String , Object> userData = new HashMap<>();
                Map<String , Object> CompanyData = new HashMap<>();
                userData.put("number",key);
                userData.put("Name",name);
                userData.put("FCM Token",companyTokan);
                userData.put("Total Liter",numberOfLiter);
                userData.put("Total Bill",bill);
                userData.put("Address",address);
                userData.put("logo",logo);
                userData.put("Accepted","False");
                userData.put("Ready for Delivery","False");
                userData.put("Delivered","False");
                userData.put("Compeleted","False");



                CompanyData.put("Number",number);
                CompanyData.put("Name",userName);
                CompanyData.put("Image",image);
                CompanyData.put("FCM Token",userToken);
                CompanyData.put("Address",address);
                CompanyData.put("Total Liter",numberOfLiter);
                CompanyData.put("Total Bill",bill);
                CompanyData.put("Accepted","False");
                CompanyData.put("Ready for Delivery","False");
                CompanyData.put("Delivered","False");
                CompanyData.put("Compeleted","False");
                CompanyData.put("logo",logo);
                String randomKey = UUID.randomUUID().toString();
                DatabaseReference placeOrderRef = db.getReference(key).child("Orders").child(randomKey);
                placeOrderRef.setValue(CompanyData).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        DatabaseReference yourOrder = db.getReference(number).child("Your Order").child(randomKey);
                        yourOrder.setValue(userData).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                DatabaseReference updateBalance = db.getReference(number).child("Profile");
                                int newBalence = balance - bill;
                                Map<String , Object> update = new HashMap<>();
                                update.put("Balance",String.valueOf(newBalence));
                                updateBalance.updateChildren(update);
                                sendNotifcation(userName + " place Order of " + numberOfLiter + " Liters",companyTokan);
                                Toast.makeText(ProductDetail.this,"Order Placed Scessfully",Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(ProductDetail.this, WellcomeScreen.class);
                                intent.putExtra("name",userName);
                                intent.putExtra("number",number);
                                intent.putExtra("image",image);
                                intent.putExtra("token",userToken);
                                intent.putExtra("balance",newBalence);
                                progressDialog.dismiss();
                                startActivity(intent);

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                progressDialog.dismiss();
                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                    }
                });




            }
        });
    }
    void init(){
        backbtn = findViewById(R.id.watter_back);
        watterPrice = findViewById(R.id.watter_price);
        subtract = findViewById(R.id.subtract_watter);
        totalLiter = findViewById(R.id.total_watter_liter);
        add = findViewById(R.id.add_watter);
        totalBill = findViewById(R.id.total_price);
        placeOrder = findViewById(R.id.watter_place_order);
        showData = findViewById(R.id.productDetailWithData);
        noData = findViewById(R.id.productDetailNoData);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);
    }
    void sendNotifcation(String message,String token){
        try {
            JSONObject jsonObject = new JSONObject();
            JSONObject notificationObject = new JSONObject();
            JSONObject dataObject = new JSONObject();
            notificationObject.put("title","New Order");
            notificationObject.put("body",message);
            dataObject.put("userId","");
            jsonObject.put("notification",notificationObject);
            jsonObject.put("data",dataObject);
            jsonObject.put("to",token);
            callApi(jsonObject);
        }catch (Exception e){

        };
    }
    void callApi (JSONObject jsonObject){
        final MediaType JSON = MediaType.get("application/json");
        OkHttpClient client = new OkHttpClient();
        String url = "https://fcm.googleapis.com/fcm/send";
        RequestBody requestBody = RequestBody.create(jsonObject.toString(),JSON);
        Request request = new  Request.Builder()
                .url(url).
                post(requestBody).
                header("Authorization","Bearer AAAAq8pjeYc:APA91bGsWw1ewyj3HsjQxFeiHdc8UhWieVSoqv9tuKkokcZCvNk2uHu2a6KsibAKYly3z4PX6jtY7ilQGvig-OPjgxAmO3_c9SaE64F9ohJrCJmZcBKFynlQE_cEi86SVggAflPzJpLx")
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {

            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {

            }
        });

    }
}