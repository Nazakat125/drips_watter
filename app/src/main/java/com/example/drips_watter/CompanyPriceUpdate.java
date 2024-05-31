package com.example.drips_watter;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Firebase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class CompanyPriceUpdate extends AppCompatActivity {
    String name, number, image;
    ImageView backBtn;
    EditText priceUpdate;
    Button priceBtn;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_price_update);
        init();
        name = getIntent().getStringExtra("name");
        number = getIntent().getStringExtra("number");
        image = getIntent().getStringExtra("image");
        progressDialog.show();
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference ref = db.getReference(number).child("Water Price");
        ref.get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    progressDialog.dismiss();
                    Log.d("data",dataSnapshot.toString());
                    String price = dataSnapshot.child("Price").getValue(String.class);
                    priceUpdate.setText(price);
                }else{
                    priceUpdate.setText("");
                    progressDialog.dismiss();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(CompanyPriceUpdate.this,e.toString(),Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
            }
        });


        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CompanyPriceUpdate.this, CompanyBottomNavigatinBar.class);
                intent.putExtra("number",number);
                intent.putExtra("name",name);
                intent.putExtra("image",image);
                startActivity(intent);
            }
        });

        priceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();
                Map<String , Object> data = new HashMap<>();
                data.put("Price" ,priceUpdate.getText().toString());
               ref.setValue(data).addOnSuccessListener(new OnSuccessListener<Void>() {
                   @Override
                   public void onSuccess(Void unused) {
                       progressDialog.dismiss();
                       Toast.makeText(CompanyPriceUpdate.this,"Price Updated",Toast.LENGTH_LONG).show();
                       Intent intent = new Intent(CompanyPriceUpdate.this, CompanyBottomNavigatinBar.class);
                       intent.putExtra("number",number);
                       intent.putExtra("name",name);
                       intent.putExtra("image",image);
                       startActivity(intent);
                   }
               }).addOnFailureListener(new OnFailureListener() {
                   @Override
                   public void onFailure(@NonNull Exception e) {
                       Toast.makeText(CompanyPriceUpdate.this,e.toString(),Toast.LENGTH_LONG).show();
                       progressDialog.dismiss();
                   }
               });

            }
        });




    }

    void init(){
        backBtn = findViewById(R.id.company_back_p);
        priceUpdate = findViewById(R.id.company_price_update);
        priceBtn = findViewById(R.id.company_price_update_btn);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);

    }


}