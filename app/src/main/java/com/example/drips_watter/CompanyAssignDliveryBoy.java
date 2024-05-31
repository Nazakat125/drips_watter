package com.example.drips_watter;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import java.util.UUID;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class CompanyAssignDliveryBoy extends AppCompatActivity implements AssignDeliveryBoyAdapter.OnAssignButtonClickListener {

    private RecyclerView recyclerView;
    private AssignDeliveryBoyAdapter adapter;
    private List<AssignDeliveryBoyData> dataList;
    private ProgressDialog progressDialog;
    private DatabaseReference databaseReference;
    String userName,userImage,userPhone,userAddress,key,orderKey,userToken,userTotalLiter,companyToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_assign_dlivery_boy);
         userName = getIntent().getStringExtra("userName");
         userImage = getIntent().getStringExtra("userImage");
         userPhone = getIntent().getStringExtra("userPhone");
         userAddress = getIntent().getStringExtra("userAddress");
         key = getIntent().getStringExtra("key");
        orderKey = getIntent().getStringExtra("orderKey");
        userToken = getIntent().getStringExtra("userToken");
        userTotalLiter = getIntent().getStringExtra("userTotalLiter");
        companyToken = getIntent().getStringExtra("token");

        recyclerView = findViewById(R.id.Company_assign_delivery_recy__);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        dataList = new ArrayList<>();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new AssignDeliveryBoyAdapter(dataList, this);
        recyclerView.setAdapter(adapter);

        progressDialog.show();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                dataList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Log.d("data", snapshot.toString());
                    String image = snapshot.child("Profile").child("Image").getValue(String.class);
            
                    String name = snapshot.child("Profile").child("Name").getValue(String.class);
                    String number = snapshot.child("Profile").child("Phone").getValue(String.class);
                    String address = snapshot.child("Profile").child("Address").getValue(String.class);
                    String age = snapshot.child("Profile").child("Age").getValue(String.class);
                    String vehicle = snapshot.child("Profile").child("Vehical").getValue(String.class);
                    String userType = snapshot.child("Profile").child("User Type").getValue(String.class);
                    String currentAddress = snapshot.child("Profile").child("Current Address").getValue(String.class);
                    String token = snapshot.child("Profile").child("FCM token").getValue(String.class);
                    boolean isSelected = false;
                    if (userType != null && userType.equals("Delivery boy")) {
                        AssignDeliveryBoyData deliveryBoy = new AssignDeliveryBoyData(image, name, number, age, vehicle, address, currentAddress, isSelected,token);
                        dataList.add(deliveryBoy);
                    }
                }
                adapter.notifyDataSetChanged();
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                progressDialog.dismiss();
            }
        });
    }

    @Override
    public void onAssignButtonClick(int position) {
        AssignDeliveryBoyData clickedItem = dataList.get(position);
        progressDialog.show();
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference customerRef = db.getReference(userPhone).child("Your Order").child(orderKey);
        Map<String , Object> customerUpdate = new HashMap<>();
        customerUpdate.put("Accepted","true");
        customerRef.updateChildren(customerUpdate).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                sendNotifcation("Order Update",clickedItem.getName() + "is Assign for delivery of your order",userToken);
                DatabaseReference companyRef = db.getReference(key).child("Orders").child(orderKey);

                Map<String , Object> companyUpdate = new HashMap<>();
                companyUpdate.put("Accepted","true");
                companyRef.updateChildren(companyUpdate).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        sendNotifcation("New Order","New Order Appears for divlivery",clickedItem.getToken());
                        String randomKey = UUID.randomUUID().toString();
                        DatabaseReference deliveryRef = db.getReference(clickedItem.getNumber()).child("Orders").child(randomKey);
                        Map<String , Object> deliveryUpdate = new HashMap<>();
                        deliveryUpdate.put("Customer Name",userName);
                        deliveryUpdate.put("Customer Address",userAddress);
                        deliveryUpdate.put("Customer Phone",userPhone);
                        deliveryUpdate.put("Customer Image",userImage);
                        deliveryUpdate.put("Customer token",userToken);
                        deliveryUpdate.put("Total Liters",userTotalLiter);
                        deliveryUpdate.put("Company Phone",key);
                        deliveryUpdate.put("Company token",companyToken);
                        deliveryUpdate.put("Order Key",orderKey);
                        deliveryUpdate.put("Ready for Delivery","false");
                        deliveryUpdate.put("Delivered","false");
                        deliveryRef.setValue(deliveryUpdate);
                        Toast.makeText(CompanyAssignDliveryBoy.this,clickedItem.getName() + " Assign for that Order",Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(CompanyAssignDliveryBoy.this,WellcomeScreen.class);
                        progressDialog.dismiss();
                        startActivity(intent);
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
