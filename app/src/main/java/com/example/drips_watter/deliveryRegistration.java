package com.example.drips_watter;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
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
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class deliveryRegistration extends AppCompatActivity {
    EditText name, phoneNumber, address, password, confirmPassword,age,vehical;
    RelativeLayout profile;
    ImageView userImage,backButton;
    Button registerBtn;
    TextView loginBtn;
    ProgressDialog progressDialog;
    Uri selectedImageUri;
    String token;
    AutoCompleteTextView joinCompany;
    String selectedCompany;
    private static final int PICK_IMAGE_REQUEST = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_registration);

        init();
        getAndStoreFCMToken();
        FirebaseDatabase databaseRef = FirebaseDatabase.getInstance();
        List<String> userList = new ArrayList<>();
        ArrayAdapter<String> userAdapt = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, userList);
        joinCompany.setAdapter(userAdapt);
        progressDialog.show();
        databaseRef.getReference().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot mainNode : dataSnapshot.getChildren()) {
                    String mainNodeName = mainNode.child("Profile").child("Name").getValue(String.class);
                    String userType = mainNode.child("Profile").child("User Type").getValue(String.class);
                    if(userType.equals("Company")){
                        userList.add(mainNodeName);
                    }

                }
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError e) {
                Toast.makeText(deliveryRegistration.this,e.toString(),Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
            }
        });

        joinCompany.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedCompany =  parent.getItemAtPosition(position).toString();
            }
        });

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(deliveryRegistration.this, DeliveryLogin.class);
                startActivity(intent);
            }
        });

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String na = name.getText().toString().trim();
                String phone = phoneNumber.getText().toString().trim();
                String add = address.getText().toString().trim();
                String agge = age.getText().toString().trim();
                String vehhi = vehical.getText().toString().trim();
                String pass = password.getText().toString().trim();
                String coPass = confirmPassword.getText().toString().trim();
                if(na.isEmpty() || phone.isEmpty() || add.isEmpty() || pass.isEmpty() || coPass.isEmpty()){
                    if(na.isEmpty()){
                        name.setError("Enter Name");
                    }
                    if(phone.isEmpty()){
                        phoneNumber.setError("Enter Number");
                    }
                    if(add.isEmpty()){
                        address.setError("Enter Address");
                    }
                    if(pass.isEmpty()){
                        password.setError("Enter Password");
                    }
                    if(coPass.isEmpty()){
                        confirmPassword.setError("Enter Password Again");
                    }

                }else{

                    databaseRef.getReference(phone).child("Profile").get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                        @Override
                        public void onSuccess(DataSnapshot dataSnapshot) {
                            if(dataSnapshot.exists()){
                                Toast.makeText(deliveryRegistration.this,"User Already Exist",Toast.LENGTH_LONG).show();
                            }else{
                                if(phone.length() == 10 || !pass.equals(coPass) || selectedImageUri == null || pass.length() < 6 || selectedCompany == null){
                                    if(phone.length() == 10){
                                        Toast.makeText(deliveryRegistration.this,"Phone Must be 11 digit long",Toast.LENGTH_LONG).show();
                                    }
                                    if(!pass.equals(coPass)){
                                        Toast.makeText(deliveryRegistration.this,"Password & Confirm Password have same Vales",Toast.LENGTH_LONG).show();
                                    }
                                    if(selectedImageUri == null){
                                        Toast.makeText(deliveryRegistration.this,"Select Profile Picture",Toast.LENGTH_LONG).show();
                                    }
                                    if(pass.length() < 6){
                                        Toast.makeText(deliveryRegistration.this,"Password should be grater than 6 digit",Toast.LENGTH_LONG).show();
                                    }
                                    if(selectedCompany == null){
                                        Toast.makeText(deliveryRegistration.this,"Select One Company",Toast.LENGTH_LONG).show();
                                    }
                                }else{
                                    progressDialog.show();
                                    StorageReference storageRef = FirebaseStorage.getInstance().getReference("Profile Images");
                                    String randomKey = UUID.randomUUID().toString();
                                    StorageReference imageRef = storageRef.child("images/").child(randomKey);
                                    imageRef.putFile(selectedImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                            imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                @Override
                                                public void onSuccess(Uri uri) {
                                                    String imageUri = uri.toString();
                                                    Map<String , Object> data = new HashMap<>();
                                                    data.put("Name", na);
                                                    data.put("Phone", phone);
                                                    data.put("Address", add);
                                                    data.put("Age", agge);
                                                    data.put("Vehical", vehhi);
                                                    data.put("Password", pass);
                                                    data.put("Confirm Password", coPass);
                                                    data.put("Image", imageUri);
                                                    data.put("FCM token", token);
                                                    data.put("User Type", "Delivery boy");
                                                    data.put("Company Joined", selectedCompany);
                                                    data.put("Latitude","25.1972");
                                                    data.put("Longitude","55.2744");
                                                    data.put("Current Address","Burj Khalifa, Dubai, United Arab Emirates");
                                                    databaseRef.getReference(phone).child("Profile").setValue(data).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void unused) {
                                                            progressDialog.dismiss();
                                                            Toast.makeText(deliveryRegistration.this,na +" Account created Sccessfully",Toast.LENGTH_LONG).show();
                                                            Intent intent = new Intent(deliveryRegistration.this, DeliveryLogin.class);
                                                            startActivity(intent);
                                                        }
                                                    }).addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Toast.makeText(deliveryRegistration.this,e.toString(),Toast.LENGTH_LONG).show();
                                                            progressDialog.dismiss();
                                                        }
                                                    });

                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Toast.makeText(deliveryRegistration.this,e.toString(),Toast.LENGTH_LONG).show();
                                                    progressDialog.dismiss();
                                                }
                                            });
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(deliveryRegistration.this,e.toString(),Toast.LENGTH_LONG).show();
                                            progressDialog.dismiss();
                                        }
                                    });



                                }
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(deliveryRegistration.this,e.toString(),Toast.LENGTH_LONG).show();
                            progressDialog.dismiss();
                        }
                    });
                }
            }
        });
    }

    void init() {
        profile = findViewById(R.id.deliveryProfile);
        name = findViewById(R.id.delivery_name_r);
        phoneNumber = findViewById(R.id.delivery_number_r);
        address = findViewById(R.id.delivery_address_r);
        password = findViewById(R.id.delivery_password_r);
        confirmPassword = findViewById(R.id.delivery_passwordAgain_r);
        userImage = findViewById(R.id.delivery_proflie_image);
        registerBtn = findViewById(R.id.delivery_register_r);
        loginBtn = findViewById(R.id.delivery_login_r);
        backButton = findViewById(R.id.delivery_back_r);
        joinCompany = findViewById(R.id.delivery_company_r);
        age = findViewById(R.id.delivery_age_r);
        vehical = findViewById(R.id.delivery_vehical_r);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);
    }

    private void openGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            userImage.setImageURI(data.getData());
            selectedImageUri = data.getData();
        }
    }
    private void getAndStoreFCMToken() {
        // Get the FCM token
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        token = task.getResult();
                    } else {
                        // Handle the error
                    }
                });
    }
}
