package com.example.drips_watter;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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

public class CompanyLogin extends AppCompatActivity {
    EditText number,password;
    Button login;
    TextView forgotPassword,register;
    ImageView backArrow;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_login);
        init();
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CompanyLogin.this,CompanyRegistration.class);
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
                String phone = number.getText().toString();
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
                                String image = dataSnapshot.child("Image").getValue(String.class);
                                String name = dataSnapshot.child("Name").getValue(String.class);
                                String token = dataSnapshot.child("FCM token").getValue(String.class);
                                String phone = dataSnapshot.child("Phone").getValue(String.class);
                                String userType = dataSnapshot.child("User Type").getValue(String.class);

                                if(password.equals(pass) || userType.equals("Company")){
                                    Intent intent = new Intent(CompanyLogin.this,CompanyBottomNavigatinBar.class);
                                    intent.putExtra("number",phone);
                                    intent.putExtra("name",name);
                                    intent.putExtra("image",image);
                                    intent.putExtra("token",token);
                                    startActivity(intent);
                                    progressDialog.dismiss();
                                }else{
                                    if(userType.equals("Company")){
                                        Toast.makeText(CompanyLogin.this,"User Not Allowed",Toast.LENGTH_LONG).show();
                                        progressDialog.dismiss();
                                    }else{
                                        Toast.makeText(CompanyLogin.this,"Invalid Password",Toast.LENGTH_LONG).show();
                                        progressDialog.dismiss();
                                    }

                                }

                            }else{
                                Toast.makeText(CompanyLogin.this,"Invalid Number",Toast.LENGTH_LONG).show();
                                progressDialog.dismiss();

                            }

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(CompanyLogin.this,e.toString(),Toast.LENGTH_LONG).show();
                            progressDialog.dismiss();
                        }
                    });
                }
            }
        });

    }
    void init(){
        number = findViewById(R.id.company_number);
        password = findViewById(R.id.company_password);
        login = findViewById(R.id.company_logIn);
        forgotPassword = findViewById(R.id.company_forgotPassword);
        register = findViewById(R.id.company_register);
        backArrow = findViewById(R.id.company_back);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);
    }
}