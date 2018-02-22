package com.example.himanshudhanwant.uds;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Himanshu Dhanwant on 26-Nov-17.
 */

public class CartRecyclerAdapter extends RecyclerView.Adapter<CartRecyclerAdapter.ViewHolder> {


    ArrayList<Item_row> mData=new ArrayList<Item_row>();
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;

    public CartRecyclerAdapter(Context context,  ArrayList<Item_row> cart_items) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = cart_items;
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.cart_item_layout, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    // binds the data to the textview in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.name.setText(mData.get(position).getItem1());
        holder.cost.setText("Rs. "+mData.get(position).getCost1());
        holder.qty.setText(""+mData.get(position).getQuant());
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return mData.size();
    }
    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView name,qty,cost;

        public ViewHolder(View itemView) {
            super(itemView);
            name=(TextView)itemView.findViewById(R.id.name);
            qty=(TextView)itemView.findViewById(R.id.qty);
            cost=(TextView)itemView.findViewById(R.id.cost);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
            mData.remove(getAdapterPosition());
            notifyItemRemoved(getAdapterPosition());
            notifyItemRangeChanged(getAdapterPosition(),mData.size());
        }
    }

    // convenience method for getting data at click position
    public int getItem(int id) {
        return mData.get(id).getId();
    }

    // allows clicks events to be caught
    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }

}
