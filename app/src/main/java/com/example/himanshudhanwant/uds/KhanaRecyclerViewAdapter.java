package com.example.himanshudhanwant.uds;

import android.content.ClipData;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;

public class KhanaRecyclerViewAdapter extends RecyclerView.Adapter<KhanaRecyclerViewAdapter.GridItemViewHolder> {

    private Context context;
    private AdapterView.OnItemClickListener mOnItemClickListener;
    private String[] urls;
    private String[] itemNames;
    private String[] itemCosts;
    private int[] Ids;
//    private Bitmap[] bitmaps;

    public KhanaRecyclerViewAdapter(Context context, String[] urls,String[] itemsName, String[] itemCost,
                                    int[] ids) {
        this.context = context;
        this.urls= urls;
//        this.bitmaps= bitmaps;
        this.itemNames=itemsName;
        this.itemCosts=itemCost;
        this.Ids=ids;
    }

    @Override
    public GridItemViewHolder onCreateViewHolder(ViewGroup parent, int position) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_layout, parent, false);
        return new GridItemViewHolder(itemView, this);
    }

    @Override
    public void onBindViewHolder(GridItemViewHolder holder, int position) {
        holder.tit.setText(itemNames[position]);
        holder.cos.setText(itemCosts[position]);
//        holder.iv.setImageBitmap(bitmaps[position]);

        Glide.with(context).load(urls[position])
                .thumbnail(0.5f)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.iv);
    }

    @Override
    public int getItemCount() {
        return itemNames.length;
    }

    public void setOnItemClickListener(AdapterView.OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    private void onItemHolderClick(GridItemViewHolder itemHolder) {
        if (mOnItemClickListener != null) {
            mOnItemClickListener.onItemClick(null, itemHolder.itemView,
                    itemHolder.getAdapterPosition(), itemHolder.getItemId());
        }
    }


    public class GridItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView tit, cos;
        public ImageView iv;
        public KhanaRecyclerViewAdapter mAdapter;

        public GridItemViewHolder(View itemView, KhanaRecyclerViewAdapter mAdapter) {
            super(itemView);
            this.mAdapter = mAdapter;
            iv=(ImageView)itemView.findViewById(R.id.iv);
            tit = (TextView) itemView.findViewById(R.id.title);
            cos = (TextView) itemView.findViewById(R.id.cost);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mAdapter.onItemHolderClick(this);
        }
    }
}