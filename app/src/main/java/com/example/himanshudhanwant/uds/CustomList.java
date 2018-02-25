package com.example.himanshudhanwant.uds;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

/**
 * Created by Himanshu Dhanwant on 23-Nov-17.
 */

public class CustomList extends ArrayAdapter<String> {

    private String[] urls;
    private String[] itemNames;
    private String[] itemCosts;
    private int[] Ids;
//    private Bitmap[] bitmaps;
    private Activity context;

    public CustomList(Activity context, String[] urls,String[] itemsName, String[] itemCost,
                      int[] ids) {
        super(context, R.layout.single_item_vendor, urls);
        this.context = context;
        this.urls= urls;
//        this.bitmaps= bitmaps;
        this.itemNames=itemsName;
        this.itemCosts=itemCost;
        this.Ids=ids;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.single_item_vendor, null, true);
        TextView textViewName = (TextView) listViewItem.findViewById(R.id.itemName);
        TextView textViewCost = (TextView)  listViewItem.findViewById(R.id.itemCost);
        ImageView image = (ImageView) listViewItem.findViewById(R.id.imageDownloaded);

        textViewName.setText(itemNames[position]);
        textViewCost.setText("Rs. "+itemCosts[position]);

//        image.setImageBitmap(Bitmap.createScaledBitmap(bitmaps[position],100,70,false));
        Glide.with(context).load(urls[position])
                .thumbnail(0.5f)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(image);
        return  listViewItem;
    }
}
