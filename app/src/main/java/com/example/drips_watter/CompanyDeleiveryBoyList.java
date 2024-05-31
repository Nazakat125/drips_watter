package com.example.drips_watter;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CompanyDeleiveryBoyList extends AppCompatActivity {
RecyclerView deliveryBoyList;
    List<CompanyDeliveryBoyData> deliveryBoys;
ImageView back;
    String name, number, image;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_deleivery_boy_list);
        init();
        name = getIntent().getStringExtra("name");
        number = getIntent().getStringExtra("number");
        image = getIntent().getStringExtra("image");
        progressDialog.show();
        Log.d("data",name.toString());
        deliveryBoys = new ArrayList<>();
        deliveryBoyList.setLayoutManager(new LinearLayoutManager(this));
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                deliveryBoys.clear();

                Log.d("data",dataSnapshot.toString());
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    progressDialog.dismiss();
                    Log.d("data",ds.toString());
                    String image = ds.child("Profile").child("Image").getValue(String.class);
                    String na = ds.child("Profile").child("Name").getValue(String.class);
                    String number = ds.child("Profile").child("Phone").getValue(String.class);
                    String address = ds.child("Profile").child("Address").getValue(String.class);
                    String age = ds.child("Profile").child("Age").getValue(String.class);
                    String vehicle = ds.child("Profile").child("Vehical").getValue(String.class);
                    String userType = ds.child("Profile").child("User Type").getValue(String.class);
                    if(userType.equals("Delivery boy")){
                        String currentAddress = ds.child("Profile").child("Current Address").getValue(String.class);
                        String companyJoin = ds.child("Profile").child("Company Joined").getValue(String.class);
                        Double latitude = ds.child("Profile").child("Latitude").getValue(Double.class);
                        Double longitude = ds.child("Profile").child("Longitude").getValue(Double.class);
                        if(name.equals(companyJoin)){
                            Log.d("Data",name.toString());
                            Log.d("Data",companyJoin.toString());
                            CompanyDeliveryBoyData deliveryBoy = new CompanyDeliveryBoyData(image, na, number, address, age, vehicle,currentAddress,longitude,latitude);
                            deliveryBoys.add(deliveryBoy);
                        }

                    }else{

                    }


                }
                CompanyDeliveryBoyAdapter adapter = new CompanyDeliveryBoyAdapter(deliveryBoys);
                deliveryBoyList.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(CompanyDeleiveryBoyList.this,databaseError.toString(),Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
            }
        });


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CompanyDeleiveryBoyList.this, CompanyBottomNavigatinBar.class);
                intent.putExtra("number",number);
                intent.putExtra("name",name);
                intent.putExtra("image",image);
                startActivity(intent);
            }
        });




    }

    void  init(){
        deliveryBoyList = findViewById(R.id.Company_delivery_recy__);
        back = findViewById(R.id.company_delivery_boy_back);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);
    }
}