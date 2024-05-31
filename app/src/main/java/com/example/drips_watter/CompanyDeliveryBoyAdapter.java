package com.example.drips_watter;

import android.content.Intent;
import android.net.Uri;
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

public class CompanyDeliveryBoyAdapter extends RecyclerView.Adapter<CompanyDeliveryBoyAdapter.ViewHolder> {

    private List<CompanyDeliveryBoyData> dataList;

    public CompanyDeliveryBoyAdapter(List<CompanyDeliveryBoyData> dataList) {
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.delivery_boys_screen, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CompanyDeliveryBoyData data = dataList.get(position);
        holder.bind(data);
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView nameTextView;
        private TextView numberTextView;
        private TextView addressTextView;
        private TextView ageTextView;
        private TextView vehicleTextView;
        private TextView currentAddress;
        private ImageView imageView;
        private Button viewLocation;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.company_delivery_boy_name);
            numberTextView = itemView.findViewById(R.id.company_delivery_boy_number);
            addressTextView = itemView.findViewById(R.id.company_delivery_boy_address);
            ageTextView = itemView.findViewById(R.id.company_delivery_boy_age);
            vehicleTextView = itemView.findViewById(R.id.company_delivery_boy_vehical);
            imageView = itemView.findViewById(R.id.company_delivery_boy_image);
            currentAddress = itemView.findViewById(R.id.company_delivery_boy_current_location);
            viewLocation = itemView.findViewById(R.id.company_delivery_boy_view_current_location); // Assuming button id is button_view_location
        }

        public void bind(CompanyDeliveryBoyData data) {
            nameTextView.setText(data.getName());
            numberTextView.setText(data.getNumber());
            addressTextView.setText(data.getAddress());
            ageTextView.setText(data.getAge());
            vehicleTextView.setText(data.getVehicle());
            currentAddress.setText(data.getCurrentAddress());
            Glide.with(itemView.getContext())
                    .load(data.getImage())
                    .into(imageView);
            viewLocation.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Launch Google Maps with specified latitude and longitude
                    double latitude = data.getLatitude();
                    double longitude = data.getLongitude();
                    String uri = "geo:" + latitude + "," + longitude + "?q=" + latitude + "," + longitude + "(" + data.getName() + ")";
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                    intent.setPackage("com.google.android.apps.maps");
                    itemView.getContext().startActivity(intent);
                }
            });
        }
    }
}
