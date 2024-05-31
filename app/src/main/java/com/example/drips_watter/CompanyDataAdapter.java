// CompanyDataAdapter.java
package com.example.drips_watter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.cardview.widget.CardView;
import com.bumptech.glide.Glide;
import java.util.List;

public class CompanyDataAdapter extends BaseAdapter {
    private Context mContext;
    private List<CompanyData> mCompanies;
    private OnCompanyClickListener mListener; // Listener for handling click events

    public CompanyDataAdapter(Context context, List<CompanyData> companies, OnCompanyClickListener listener) {
        mContext = context;
        mCompanies = companies;
        mListener = listener;
    }

    @Override
    public int getCount() {
        return mCompanies.size();
    }

    @Override
    public Object getItem(int position) {
        return mCompanies.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.company_list, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.companyName = convertView.findViewById(R.id.user_company_name);
            viewHolder.companyClick = convertView.findViewById(R.id.user_company_click);           viewHolder.companyLogo = convertView.findViewById(R.id.user_company_logo);
   // Assuming your CardView id is "card_view"
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        CompanyData company = mCompanies.get(position);
        viewHolder.companyName.setText(company.getName());
        Glide.with(mContext).load(company.getLogoUrl()).into(viewHolder.companyLogo);

        // Set click listener to CardView
        viewHolder.companyClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Notify listener about the click event
                if (mListener != null) {
                    mListener.onCompanyClick(position);
                }
            }
        });

        return convertView;
    }

    private static class ViewHolder {
        ImageView companyLogo;
        TextView companyName;
        CardView companyClick;
    }

    // Method to update data in the adapter
    public void setData(List<CompanyData> companies) {
        mCompanies.clear();
        mCompanies.addAll(companies);
        notifyDataSetChanged();
    }

    // Interface for handling click events
    public interface OnCompanyClickListener {
        void onCompanyClick(int position);
    }
}
