// UserHome.java
package com.example.drips_watter;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class UserHome extends Fragment implements CompanyDataAdapter.OnCompanyClickListener {

    String name, number, image,balance;
    ImageView userImage;
    TextView userName, greetings,balanceText;
    ViewFlipper viewFlipper;
    GridView gridView;
    List<CompanyData> companies;
    CompanyDataAdapter adapter;
    ImageButton logout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_home, container, false);
        init(view);
        setGreeting();
        viewFlipper.setInAnimation(getContext(), android.R.anim.slide_in_left);
        viewFlipper.setOutAnimation(getContext(), android.R.anim.slide_out_right);
        viewFlipper.setFlipInterval(3000); // 3 seconds
        viewFlipper.startFlipping();

        name = getArguments().getString("name");
        number = getArguments().getString("number");
        image = getArguments().getString("image");
        balance = getArguments().getString("balance");
        userName.setText(name);
        balanceText.setText(balance);
        Glide.with(this).load(image).into(userImage);



        // Set up Firebase reference
        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference OrderRef = FirebaseDatabase.getInstance().getReference(number).child("Your Order");

        // Attach a listener to retrieve data
        databaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                companies = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Log.d("Data",snapshot.toString());
                    String name = snapshot.child("Profile").child("Name").getValue(String.class);
                    String phone = snapshot.child("Profile").child("Phone").getValue(String.class);
                    String image = snapshot.child("Profile").child("Image").getValue(String.class);
                    String userType = snapshot.child("Profile").child("User Type").getValue(String.class);
                    if(userType.equals("Company")){
                        CompanyData company = new CompanyData(name, image,phone);
                        companies.add(company);
                    }else{


                    }
                }
                // Update adapter with new data
                adapter.setData(companies);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle error
            }
        });

        return view;
    }

    void init(View view) {
        userName = view.findViewById(R.id.user_name_data);
        greetings = view.findViewById(R.id.user_greedings_data);
        userImage = view.findViewById(R.id.user_proflie_image_data);
        viewFlipper = view.findViewById(R.id.user_flipper);
        gridView = view.findViewById(R.id.user_company_grid);
        balanceText = view.findViewById(R.id.user_balence_data);
        balanceText = view.findViewById(R.id.user_balence_data);
        logout = view.findViewById(R.id.company_logout);
        adapter = new CompanyDataAdapter(getActivity(), new ArrayList<>(), this);
        gridView.setAdapter(adapter);
    }

    private void setGreeting() {
        Calendar c = Calendar.getInstance();
        int timeOfDay = c.get(Calendar.HOUR_OF_DAY);

        if (timeOfDay >= 0 && timeOfDay < 12) {
            greetings.setText("Good Morning,");
        } else if (timeOfDay >= 12 && timeOfDay < 16) {
            greetings.setText("Good Afternoon,");
        } else if (timeOfDay >= 16 && timeOfDay < 21) {
            greetings.setText("Good Evening,");
        } else if (timeOfDay >= 21 && timeOfDay < 24) {
            greetings.setText("Good Night,");
        }
    }

    // Method to handle click event from adapter
    @Override
    public void onCompanyClick(int position) {
        CompanyData data = companies.get(position);
        Intent intent = new Intent(getContext(),ProductDetail.class);
        intent.putExtra("key",data.getPhone());
        intent.putExtra("number",number);
        intent.putExtra("logo",data.getLogoUrl());
        startActivity(intent);

    }
}
