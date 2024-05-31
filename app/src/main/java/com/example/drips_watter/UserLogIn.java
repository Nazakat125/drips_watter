package com.example.drips_watter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;

public class UserLogIn extends AppCompatActivity {
EditText number,password;
Button login;
TextView forgotPassword,register;
ImageView backArrow;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_log_in);
        init();
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserLogIn.this, UserRegistration.class);
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
                                String balance = dataSnapshot.child("Balance").getValue(String.class);
                                if(password.equals(pass) || userType.equals("Customer") ){
                                    Intent intent = new Intent(UserLogIn.this,UserBottomNavigationBar.class);
                                    intent.putExtra("number",phone);
                                    intent.putExtra("name",name);
                                    intent.putExtra("image",image);
                                    intent.putExtra("token",token);
                                    intent.putExtra("balance",balance);
                                    startActivity(intent);
                                    progressDialog.dismiss();
                                }else{
                                    if(!userType.equals("Customer")){
                                        Toast.makeText(UserLogIn.this,"User Not Allowed",Toast.LENGTH_LONG).show();
                                        progressDialog.dismiss();
                                    }else{
                                        Toast.makeText(UserLogIn.this,"Invalid Password",Toast.LENGTH_LONG).show();
                                        progressDialog.dismiss();
                                    }
                                }

                            }else{
                                Toast.makeText(UserLogIn.this,"Invalid Number",Toast.LENGTH_LONG).show();
                                progressDialog.dismiss();

                            }

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(UserLogIn.this,e.toString(),Toast.LENGTH_LONG).show();
                            progressDialog.dismiss();
                        }
                    });
                }
            }
        });
    }
    void init(){
        number = findViewById(R.id.user_number);
        password = findViewById(R.id.user_password);
        login = findViewById(R.id.user_logIn);
        forgotPassword = findViewById(R.id.user_forgotPassword);
        register = findViewById(R.id.user_register);
        backArrow = findViewById(R.id.user_back);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);
    }
}