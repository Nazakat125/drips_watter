package com.example.drips_watter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.List;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserOrderListAdapter extends RecyclerView.Adapter<UserOrderListAdapter.ViewHolder> {

    private Context context;
    private List<UserOrderListData> userOrderList;

    public UserOrderListAdapter(Context context, List<UserOrderListData> userOrderList) {
        this.context = context;
        this.userOrderList = userOrderList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_order_placed, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        UserOrderListData data = userOrderList.get(position);
        holder.userName.setText(data.getName());
        Glide.with(context).load(data.getImage()).into(holder.userImage);
        holder.btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,UserTrackOrder.class);
                intent.putExtra("number",data.getNumber());
                intent.putExtra("key",data.getKey());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return userOrderList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        CircleImageView userImage;
        TextView userName;
        CardView btn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            userImage = itemView.findViewById(R.id.user_company_image);
            userName = itemView.findViewById(R.id.user_company_title);
            btn = itemView.findViewById(R.id.user_order);
        }
    }
}
