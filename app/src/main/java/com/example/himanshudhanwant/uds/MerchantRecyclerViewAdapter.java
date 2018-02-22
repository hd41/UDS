package com.example.himanshudhanwant.uds;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Himanshu Dhanwant on 08-Jan-18.
 */

public class MerchantRecyclerViewAdapter extends RecyclerView.Adapter<MerchantRecyclerViewAdapter.GridItemViewHolder> {

    private Context context;
    private AdapterView.OnItemClickListener mOnItemClickListener;
    private String[] merchantList;

    public MerchantRecyclerViewAdapter(Context context, String[] merchants) {
        this.context = context;
        this.merchantList=merchants;
    }

    @Override
    public MerchantRecyclerViewAdapter.GridItemViewHolder onCreateViewHolder(ViewGroup parent, int position) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.merchant_layout, parent, false);
        return new MerchantRecyclerViewAdapter.GridItemViewHolder(itemView, this);
    }

    @Override
    public void onBindViewHolder(MerchantRecyclerViewAdapter.GridItemViewHolder holder, int position) {
        holder.name.setText(merchantList[position]);
    }

    @Override
    public int getItemCount() {
        return merchantList.length;
    }

    public void setOnItemClickListener(AdapterView.OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    private void onItemHolderClick(MerchantRecyclerViewAdapter.GridItemViewHolder itemHolder) {
        if (mOnItemClickListener != null) {
            mOnItemClickListener.onItemClick(null, itemHolder.itemView,
                    itemHolder.getAdapterPosition(), itemHolder.getItemId());
        }
    }


    public class GridItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView name;
        public MerchantRecyclerViewAdapter mAdapter;

        public GridItemViewHolder(View itemView, MerchantRecyclerViewAdapter mAdapter) {
            super(itemView);
            this.mAdapter = mAdapter;
            name = (TextView) itemView.findViewById(R.id.merName);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mAdapter.onItemHolderClick(this);
        }
    }
}
