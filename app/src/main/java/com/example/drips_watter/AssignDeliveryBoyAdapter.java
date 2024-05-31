package com.example.drips_watter;

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

public class AssignDeliveryBoyAdapter extends RecyclerView.Adapter<AssignDeliveryBoyAdapter.ViewHolder> {

    private List<AssignDeliveryBoyData> dataList;
    private OnAssignButtonClickListener listener;

    public AssignDeliveryBoyAdapter(List<AssignDeliveryBoyData> dataList, OnAssignButtonClickListener listener) {
        this.dataList = dataList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.assign_delivery_boys_screen, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        AssignDeliveryBoyData data = dataList.get(position);
        holder.bind(data);
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;
        private TextView nameTextView;
        private TextView numberTextView;
        private TextView ageTextView;
        private TextView vehicleTextView;
        private TextView addressTextView;
        private TextView currentAddressTextView;
        private Button assign;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.company_assign_delivery_boy_image);
            nameTextView = itemView.findViewById(R.id.company_assign_delivery_boy_name);
            numberTextView = itemView.findViewById(R.id.company_assign_delivery_boy_number);
            ageTextView = itemView.findViewById(R.id.company_assign_delivery_boy_age);
            vehicleTextView = itemView.findViewById(R.id.company_assign_delivery_boy_vehical);
            addressTextView = itemView.findViewById(R.id.company_assign_delivery_boy_address);
            currentAddressTextView = itemView.findViewById(R.id.company_assign_delivery_boy_current_location);
            assign = itemView.findViewById(R.id.company_assign_delivery_boy_assign_btn);

            // Set onClickListener for the assign button
            assign.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION && listener != null) {
                        listener.onAssignButtonClick(position);
                    }
                }
            });
        }

        public void bind(AssignDeliveryBoyData data) {
            Glide.with(itemView.getContext())
                    .load(data.getImage())
                    .into(imageView);
            nameTextView.setText(data.getName());
            numberTextView.setText(data.getNumber());
            ageTextView.setText(data.getAge());
            vehicleTextView.setText(data.getVehicle());
            addressTextView.setText(data.getAddress());
            currentAddressTextView.setText(data.getCurrentAddress());
        }
    }

    public interface OnAssignButtonClickListener {
        void onAssignButtonClick(int position);
    }
}
