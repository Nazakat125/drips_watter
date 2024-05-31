package com.example.drips_watter;

import android.app.ProgressDialog;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class UserPacageDetail extends Fragment {
    String key;
    ProgressDialog progressDialog;
    RecyclerView recyclerView;
    List<UserOrderListData> userOrders;
    UserOrderListAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_pacage_detail, container, false);
        init(view);
        key = getArguments().getString("number");
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference ref = db.getReference(key).child("Your Order");

        progressDialog.show();

        userOrders = new ArrayList<>();
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new UserOrderListAdapter(getContext(), userOrders);
        recyclerView.setAdapter(adapter);

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userOrders.clear();

                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    String value = ds.getKey().toString();
                    String image = ds.child("logo").getValue(String.class);
                    String name = ds.child("Name").getValue(String.class);
                    UserOrderListData userOrder = new UserOrderListData(image, name,value,key);
                    userOrders.add(userOrder);
                }
                adapter.notifyDataSetChanged();
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), databaseError.toString(), Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
            }
        });

        return view;
    }

    void init(View view) {
        recyclerView = view.findViewById(R.id.user_order_list);
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);
    }
}
