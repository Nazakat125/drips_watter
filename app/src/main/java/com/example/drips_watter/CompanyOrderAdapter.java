package com.example.drips_watter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class CompanyOrderAdapter extends RecyclerView.Adapter<CompanyOrderAdapter.ViewHolder> {
    private List<CompanyOrderData> orderList;
    private Context context;
    private OnAssignDriverClickListener listener;

    public interface OnAssignDriverClickListener {
        void onAssignDriverClick(int position);
    }

    public CompanyOrderAdapter(List<CompanyOrderData> orderList, Context context, OnAssignDriverClickListener listener) {
        this.orderList = orderList;
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.company_order_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CompanyOrderData orderData = orderList.get(position);
        holder.companyName.setText(orderData.getName());
        holder.companyNumber.setText(orderData.getNumber());
        holder.companyTotalLiters.setText(orderData.getTotalLiter());
        holder.companyTotalBill.setText(orderData.getTotalBill());
        holder.companyAddress.setText(orderData.getAddress());
        Glide.with(context).load(orderData.getImage()).into(holder.companyImage);
        if(orderData.getAccepted().equals("true")){
            holder.driverAssigned.setVisibility(View.VISIBLE);
            holder.assignDriver.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView companyName, companyNumber, companyTotalLiters, companyTotalBill, companyAddress;
        ImageView companyImage;
        Button assignDriver,driverAssigned;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            companyName = itemView.findViewById(R.id.company_user_name);
            companyImage = itemView.findViewById(R.id.company_user_image);
            companyNumber = itemView.findViewById(R.id.company_user_number);
            companyTotalLiters = itemView.findViewById(R.id.company_user_total_liters);
            companyTotalBill = itemView.findViewById(R.id.company_user_total_Bill);
            companyAddress = itemView.findViewById(R.id.company_user_address);
            assignDriver = itemView.findViewById(R.id.company_assign_driver);
            driverAssigned = itemView.findViewById(R.id.company_assigned_driver);

            assignDriver.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onAssignDriverClick(position);
                        }
                    }
                }
            });
        }
    }
}
