package com.example.drips_watter;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CompanyOrderDetail extends Fragment implements CompanyOrderAdapter.OnAssignDriverClickListener {

    RecyclerView recyclerView;
    String key,token;
    ProgressDialog progressDialog;
    List<CompanyOrderData> orderList;
    CompanyOrderAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_company_order_detail, container, false);
        init(view);
        key = getArguments().getString("number");
        token = getArguments().getString("token");

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        orderList = new ArrayList<>();
        adapter = new CompanyOrderAdapter(orderList, getContext(), this);
        recyclerView.setAdapter(adapter);

        progressDialog.show();
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference ref = db.getReference(key).child("Orders");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                orderList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String key = snapshot.getKey();
                    String image = snapshot.child("Image").getValue(String.class);
                    String name = snapshot.child("Name").getValue(String.class);
                    String number = snapshot.child("Number").getValue(String.class);
                    String address = snapshot.child("Address").getValue(String.class);
                    String fcmToken = snapshot.child("FCM Token").getValue(String.class);
                    String accepted = snapshot.child("Accepted").getValue(String.class);
                    String delivered = snapshot.child("Delivered").getValue(String.class).toLowerCase();
                    Long totalBill = snapshot.child("Total Bill").getValue(Long.class);
                    Long totalLiter = snapshot.child("Total Liter").getValue(Long.class);
                    if(delivered.equals("false")){
                        CompanyOrderData orderData = new CompanyOrderData(image, name, number, String.valueOf(totalBill) + " RS",String.valueOf(totalLiter) + " Liters",address,key,fcmToken,accepted);
                        orderList.add(orderData);
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

        return view;
    }

    void init(View view) {
        recyclerView = view.findViewById(R.id.company_order_list);
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Loading orders...");
        progressDialog.setCancelable(false);
    }

    @Override
    public void onAssignDriverClick(int position) {
        CompanyOrderData clickedItem = orderList.get(position);
        Intent intent = new Intent(getContext(), CompanyAssignDliveryBoy.class);
        intent.putExtra("userImage",clickedItem.getImage());
        intent.putExtra("userName",clickedItem.getName());
        intent.putExtra("userPhone",clickedItem.getNumber());
        intent.putExtra("userAddress",clickedItem.getAddress());
        intent.putExtra("key",key);
        intent.putExtra("token",token);
        intent.putExtra("orderKey",clickedItem.getKey());
        intent.putExtra("userToken",clickedItem.getToken());
        intent.putExtra("userTotalLiter",clickedItem.getTotalLiter());
        startActivity(intent);

    }
}
