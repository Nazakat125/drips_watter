package com.example.drips_watter;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class DeliveryBoyOrdersList extends AppCompatActivity implements DeliveryBoyOrdersAdapter.OrderButtonClickListener {
    private String phone;
    private RecyclerView recyclerView;
    private DeliveryBoyOrdersAdapter adapter;
    private List<DeliveryBoyOrdersData> deliveryBoyOrdersList;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_boy_orders_list);
        phone = getIntent().getStringExtra("key");
        recyclerView = findViewById(R.id.delivery_boy_order_recy);
        deliveryBoyOrdersList = new ArrayList<>();
        adapter = new DeliveryBoyOrdersAdapter(this, deliveryBoyOrdersList, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);
        // Setup Firebase Database reference
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference(phone).child("Orders");
        progressDialog.show();
        // Attach ValueEventListener to retrieve data
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Clear previous data

                deliveryBoyOrdersList.clear();
                // Iterate through each child node
                progressDialog.dismiss();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    String deliverykey = ds.getKey();
                    String companyPhone = ds.child("Company Phone").getValue(String.class);
                    String companyToken = ds.child("Company token").getValue(String.class);
                    String customerAddress = ds.child("Customer Address").getValue(String.class);
                    String customerImage = ds.child("Customer Image").getValue(String.class);
                    String customerName = ds.child("Customer Name").getValue(String.class);
                    String customerPhone = ds.child("Customer Phone").getValue(String.class);
                    String customerToken = ds.child("Customer token").getValue(String.class);
                    String completed = ds.child("Delivered").getValue(String.class);
                    String orderKey = ds.child("Order Key").getValue(String.class);
                    String readyForDelivery = ds.child("Ready for Delivery").getValue(String.class);
                    String totalLiters = ds.child("Total Liters").getValue(String.class);
                    if(completed.equals("false")){
                        progressDialog.dismiss();
                        DeliveryBoyOrdersData data = new DeliveryBoyOrdersData(customerImage, customerName, customerPhone, customerAddress, totalLiters, orderKey, readyForDelivery,completed,companyPhone,companyToken,customerToken,deliverykey);
                        deliveryBoyOrdersList.add(data);
                    }

                }
                // Notify adapter of data changes
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                progressDialog.dismiss();
            }
        });
    }

    @Override
    public void onReadyForDeliveryClick(int position) {
        DeliveryBoyOrdersData orderData = deliveryBoyOrdersList.get(position);
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference customerRef = db.getReference(orderData.getNumber()).child("Your Order").child(orderData.getKey());
        Map<String , Object> customerUpdate = new HashMap<>();
        customerUpdate.put("Ready for Delivery","true");
        customerRef.updateChildren(customerUpdate).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                sendNotifcation("Delivery Update","Your Pacakge is ready for Delivery",orderData.getCustomerToken());
                DatabaseReference companyRef = db.getReference(orderData.getCompanyPhone()).child("Orders").child(orderData.getKey());
                Map<String , Object> companyUpdate = new HashMap<>();
                companyUpdate.put("Ready for Delivery","true");
                companyRef.updateChildren(companyUpdate).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        sendNotifcation(orderData.getName() + "Delivery Update","Pacakge is Ready for Delivery",orderData.getCompanyToken());
                        DatabaseReference deliveryRef = db.getReference(phone).child("Orders").child(orderData.getDeliveryKey());
                        Map<String , Object> deliveryUpdate = new HashMap<>();
                        deliveryUpdate.put("Ready for Delivery","true");
                        deliveryRef.updateChildren(deliveryUpdate).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(DeliveryBoyOrdersList.this,"Order Accepted",Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                });
            }
        });

    }

    @Override
    public void onCompletedClick(int position) {
        // Handle completed button click
        DeliveryBoyOrdersData orderData = deliveryBoyOrdersList.get(position);
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference customerRef = db.getReference(orderData.getNumber()).child("Your Order").child(orderData.getKey());
        Map<String , Object> customerUpdate = new HashMap<>();
        customerUpdate.put("Delivered","true");
        customerRef.updateChildren(customerUpdate).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                sendNotifcation("Delivery Delivered","Your Pacakge has been Delivered",orderData.getCustomerToken());
                DatabaseReference companyRef = db.getReference(orderData.getCompanyPhone()).child("Orders").child(orderData.getKey());
                Map<String , Object> companyUpdate = new HashMap<>();
                companyUpdate.put("Delivered","true");
                companyRef.updateChildren(companyUpdate).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        sendNotifcation(orderData.getName() + "Delived","Pacakge is Delived",orderData.getCompanyToken());
                        DatabaseReference deliveryRef = db.getReference(phone).child("Orders").child(orderData.getDeliveryKey());
                        Map<String , Object> deliveryUpdate = new HashMap<>();
                        deliveryUpdate.put("Delivered","true");
                        deliveryRef.updateChildren(deliveryUpdate).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(DeliveryBoyOrdersList.this,"Pacakge Delived",Toast.LENGTH_LONG).show();
                                startActivity(new Intent(DeliveryBoyOrdersList.this, WellcomeScreen.class));
                            }
                        });
                    }
                });
            }
        });
    }
    void sendNotifcation(String title,String message,String token){
        try {
            JSONObject jsonObject = new JSONObject();
            JSONObject notificationObject = new JSONObject();
            JSONObject dataObject = new JSONObject();
            notificationObject.put("title",title);
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
