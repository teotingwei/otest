package com.example.ocbcpayment.view.adapter;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.ocbcpayment.R;
import com.example.ocbcpayment.model.User;
import com.example.ocbcpayment.view.activity.PayActivity;

import java.util.ArrayList;
import java.util.List;

import static com.example.ocbcpayment.base.Constants.EXTRA_MOBILE_RECEIVER;
import static com.example.ocbcpayment.base.Constants.EXTRA_MOBILE_RECEIVER_NAME;
import static com.example.ocbcpayment.base.Constants.PAY_REQUEST;


public class PayerAdapter extends RecyclerView.Adapter<PayerAdapter.MyViewHolder> implements Filterable {
    private List<User> originalData;
    private List<User> filteredData;
    private LayoutInflater inflater;
    private Context context;
    private ItemFilter mFilter = new ItemFilter();

    public PayerAdapter(Context context, List<User> mylist) {
        inflater = LayoutInflater.from(context);
        this.context = context;
        this.originalData = mylist;
        this.filteredData = mylist;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.user_item, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.payer_name.setText(filteredData.get(position).getName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, PayActivity.class);
                intent.putExtra(EXTRA_MOBILE_RECEIVER, filteredData.get(position).getPhone());
                intent.putExtra(EXTRA_MOBILE_RECEIVER_NAME, filteredData.get(position).getName());
                ((Activity) context).startActivityForResult(intent, PAY_REQUEST);
            }
        });
    }

    @Override
    public int getItemCount() {
        return filteredData.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView payer_name;

        public MyViewHolder(View itemView) {
            super(itemView);
            payer_name = (TextView) itemView.findViewById(R.id.text_view_name);
        }
    }

    public Filter getFilter() {
        return mFilter;
    }

    private class ItemFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            String filterString = constraint.toString().toLowerCase();

            FilterResults results = new FilterResults();

            final List<User> list = originalData;

            int count = list.size();
            final ArrayList<User> nlist = new ArrayList<User>(count);

            String filterableString;

            for (int i = 0; i < count; i++) {
                filterableString = list.get(i).getName();
                if (filterableString.toLowerCase().contains(filterString)) {
                    nlist.add(list.get(i));
                }
            }

            results.values = nlist;
            results.count = nlist.size();

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            filteredData = (ArrayList<User>) results.values;
            notifyDataSetChanged();
        }

    }
}
