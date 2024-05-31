package com.example.drips_watter;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class DeliveryLogin extends AppCompatActivity {
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1001;

    private FusedLocationProviderClient fusedLocationClient;
    private Location lastLocation;

    EditText number,password;
    Button login;
    TextView forgotPassword,register;
    ImageView backArrow;
    ProgressDialog progressDialog;
    String phone,token,name,image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_login);

        init();

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DeliveryLogin.this, deliveryRegistration.class);
                startActivity(intent);
            }
        });

        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 phone = number.getText().toString();
                String pass = password.getText().toString();
                if(pass.isEmpty() || phone.isEmpty()){
                    if(pass.isEmpty()){
                        password.setError("Enter Password");
                    }
                    if(phone.isEmpty()){
                        number.setError("Enter Number");
                    }
                }else{
                    progressDialog.show();
                    FirebaseDatabase databaseRef = FirebaseDatabase.getInstance();
                    databaseRef.getReference(phone).child("Profile").get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                        @Override
                        public void onSuccess(DataSnapshot dataSnapshot) {
                            if(dataSnapshot.exists()){
                                String password = dataSnapshot.child("Password").getValue(String.class);
                                 image =    dataSnapshot.child("Image").getValue(String.class);
                                 name =     dataSnapshot.child("Name").getValue(String.class);
                                String userType =     dataSnapshot.child("User Type").getValue(String.class);
                                 token =    dataSnapshot.child("FCM token").getValue(String.class);
                                 phone =    dataSnapshot.child("Phone").getValue(String.class);
                                if(password.equals(pass) || userType.equals("Delivery boy")){
                                    getLastLocation();
                                }else{
                                    Toast.makeText(DeliveryLogin.this,"Invalid Password",Toast.LENGTH_LONG).show();
                                    progressDialog.dismiss();
                                }

                            }else{
                                Toast.makeText(DeliveryLogin.this,"Invalid Number",Toast.LENGTH_LONG).show();
                                progressDialog.dismiss();

                            }

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(DeliveryLogin.this,e.toString(),Toast.LENGTH_LONG).show();
                            progressDialog.dismiss();
                        }
                    });
                }
            }
        });
    }

    void init(){
        number = findViewById(R.id.delivery_number);
        password = findViewById(R.id.delivery_password);
        login = findViewById(R.id.delivery_logIn);
        forgotPassword = findViewById(R.id.delivery_forgotPassword);
        register = findViewById(R.id.delivery_register);
        backArrow = findViewById(R.id.delivery_back);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);
    }

    private void getLastLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, location -> {
                        if (location != null) {

                            lastLocation = location;
                            double latitude = lastLocation.getLatitude();
                            double longitude = lastLocation.getLongitude();
                            String addressText = getAddressFromLocation(latitude, longitude);
                            Toast.makeText(this, "Latitude: " + latitude + ", Longitude: " + longitude +
                                    "\nAddress: " + addressText, Toast.LENGTH_SHORT).show();
                            Map<String , Object> currentLocation = new HashMap<>();
                            currentLocation.put("Latitude",latitude);
                            currentLocation.put("Longitude",longitude);
                            currentLocation.put("Current Address",addressText);
                            DatabaseReference ref = FirebaseDatabase.getInstance().getReference(phone).child("Profile");
                            ref.updateChildren(currentLocation);
                            progressDialog.dismiss();
                            Intent intent = new Intent(DeliveryLogin.this, DeliveryBoyDashboard.class);
                            intent.putExtra("name",name);
                            intent.putExtra("key",phone);
                            intent.putExtra("image",image);
                            intent.putExtra("token",token);
                            startActivity(intent);
                        } else {
                            Toast.makeText(this, "No location available", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }
                    });
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    private String getAddressFromLocation(double latitude, double longitude) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        List<Address> addresses;
        String addressText = "";

        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (addresses != null && !addresses.isEmpty()) {
                Address address = addresses.get(0);
                for (int i = 0; i <= address.getMaxAddressLineIndex(); i++) {
                    addressText += address.getAddressLine(i);
                    if (i < address.getMaxAddressLineIndex()) {
                        addressText += ", ";
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return addressText;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastLocation();
            } else {
                Toast.makeText(this, "Location permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
