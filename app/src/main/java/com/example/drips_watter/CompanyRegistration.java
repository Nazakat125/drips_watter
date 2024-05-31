package com.example.drips_watter;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
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
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CompanyRegistration extends AppCompatActivity {
    EditText name, phoneNumber, address, password, confirmPassword;
    RelativeLayout profile;
    ImageView userImage,backButton;
    Button registerBtn;
    TextView loginBtn;
    ProgressDialog progressDialog;
    Uri selectedImageUri;
    String token;
    private static final int PICK_IMAGE_REQUEST = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_registration);

        init();
        getAndStoreFCMToken();
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
                Intent intent = new Intent(CompanyRegistration.this, CompanyLogin.class);
                startActivity(intent);
            }
        });

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String na = name.getText().toString().trim();
                String phone = phoneNumber.getText().toString().trim();
                String add = address.getText().toString().trim();
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
                    FirebaseDatabase databaseRef = FirebaseDatabase.getInstance();
                    databaseRef.getReference(phone).child("Profile").get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                        @Override
                        public void onSuccess(DataSnapshot dataSnapshot) {
                            if(dataSnapshot.exists()){
                                Toast.makeText(CompanyRegistration.this,"User Already Exist",Toast.LENGTH_LONG).show();
                            }else{
                                if(phone.length() == 10 || !pass.equals(coPass) || selectedImageUri == null || pass.length() < 6){
                                    if(phone.length() == 10){
                                        Toast.makeText(CompanyRegistration.this,"Phone Must be 11 digit long",Toast.LENGTH_LONG).show();
                                    }
                                    if(!pass.equals(coPass)){
                                        Toast.makeText(CompanyRegistration.this,"Password & Confirm Password have same Vales",Toast.LENGTH_LONG).show();
                                    }
                                    if(selectedImageUri == null){
                                        Toast.makeText(CompanyRegistration.this,"Select Profile Picture",Toast.LENGTH_LONG).show();
                                    }
                                    if(pass.length() < 6){
                                        Toast.makeText(CompanyRegistration.this,"Password should be grater than 6 digit",Toast.LENGTH_LONG).show();
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
                                                    data.put("Password", pass);
                                                    data.put("Confirm Password", coPass);
                                                    data.put("Image", imageUri);
                                                    data.put("FCM token", token);
                                                    data.put("User Type", "Company");
                                                    databaseRef.getReference(phone).child("Profile").setValue(data).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void unused) {
                                                            progressDialog.dismiss();
                                                            Toast.makeText(CompanyRegistration.this,na +" Account created Sccessfully",Toast.LENGTH_LONG).show();
                                                            Intent intent = new Intent(CompanyRegistration.this, CompanyLogin.class);
                                                            startActivity(intent);
                                                        }
                                                    }).addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Toast.makeText(CompanyRegistration.this,e.toString(),Toast.LENGTH_LONG).show();
                                                            progressDialog.dismiss();
                                                        }
                                                    });

                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Toast.makeText(CompanyRegistration.this,e.toString(),Toast.LENGTH_LONG).show();
                                                    progressDialog.dismiss();
                                                }
                                            });
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(CompanyRegistration.this,e.toString(),Toast.LENGTH_LONG).show();
                                            progressDialog.dismiss();
                                        }
                                    });



                                }
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(CompanyRegistration.this,e.toString(),Toast.LENGTH_LONG).show();
                            progressDialog.dismiss();
                        }
                    });
                }
            }
        });
    }

    void init() {
        profile = findViewById(R.id.companyProfile);
        name = findViewById(R.id.company_name_r);
        phoneNumber = findViewById(R.id.company_number_r);
        address = findViewById(R.id.company_address_r);
        password = findViewById(R.id.company_password_r);
        confirmPassword = findViewById(R.id.company_passwordAgain_r);
        userImage = findViewById(R.id.company_proflie_image);
        registerBtn = findViewById(R.id.company_register_r);
        loginBtn = findViewById(R.id.company_login_r);
        backButton = findViewById(R.id.company_back_r);
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
