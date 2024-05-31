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

public class DeliveryBoyOrdersAdapter extends RecyclerView.Adapter<DeliveryBoyOrdersAdapter.ViewHolder> {
    private List<DeliveryBoyOrdersData> deliveryBoyOrdersList;
    private Context context;
    private OrderButtonClickListener buttonClickListener;

    public DeliveryBoyOrdersAdapter(Context context, List<DeliveryBoyOrdersData> deliveryBoyOrdersList, OrderButtonClickListener buttonClickListener) {
        this.context = context;
        this.deliveryBoyOrdersList = deliveryBoyOrdersList;
        this.buttonClickListener = buttonClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.delivery_boy_orders_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DeliveryBoyOrdersData deliveryBoyOrdersData = deliveryBoyOrdersList.get(position);

        holder.deliveryUserName.setText(deliveryBoyOrdersData.getName());
        holder.deliveryUserNumber.setText(deliveryBoyOrdersData.getNumber());
        holder.deliveryUserTotalLiters.setText(deliveryBoyOrdersData.getLiters());
        holder.deliveryUserAddress.setText(deliveryBoyOrdersData.getAddress());
        Glide.with(context).load(deliveryBoyOrdersData.getImage()).into(holder.imageView);

        if (deliveryBoyOrdersData.getReadyForDelivery().equals("true")) {
            holder.readyForDelivery.setVisibility(View.GONE);
            holder.completed.setVisibility(View.VISIBLE);
        }

        holder.readyForDelivery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int adapterPosition = holder.getAdapterPosition();
                if (adapterPosition != RecyclerView.NO_POSITION && buttonClickListener != null) {
                    buttonClickListener.onReadyForDeliveryClick(adapterPosition);
                }
            }
        });

        holder.completed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int adapterPosition = holder.getAdapterPosition();
                if (adapterPosition != RecyclerView.NO_POSITION && buttonClickListener != null) {
                    buttonClickListener.onCompletedClick(adapterPosition);
                }
            }
        });
    }


    @Override
    public int getItemCount() {
        return deliveryBoyOrdersList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView deliveryUserName, deliveryUserNumber, deliveryUserTotalLiters, deliveryUserAddress;
        ImageView imageView;
        Button readyForDelivery, completed;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            deliveryUserName = itemView.findViewById(R.id.delivery_user_name);
            deliveryUserNumber = itemView.findViewById(R.id.delivery_user_number);
            deliveryUserTotalLiters = itemView.findViewById(R.id.delivery_user_total_liters);
            deliveryUserAddress = itemView.findViewById(R.id.delivery_user_address);
            imageView = itemView.findViewById(R.id.delivery_user_image);
            readyForDelivery = itemView.findViewById(R.id.delivery_boy_ready_for_delivery);
            completed = itemView.findViewById(R.id.delivery_boy_order_compleled);
        }
    }

    public interface OrderButtonClickListener {
        void onReadyForDeliveryClick(int position);
        void onCompletedClick(int position);
    }
}
