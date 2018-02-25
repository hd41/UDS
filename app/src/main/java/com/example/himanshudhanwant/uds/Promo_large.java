package com.example.himanshudhanwant.uds;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;
import java.util.List;

public class Promo_large extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    TextView tv,cost1;
    ImageView iv;
    GetAllItems getAllItems = new GetAllItems();
    Spinner spinner;
    Button addToCart;
    Integer qty=0;

    dataHelper dh;

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
        qty = Integer.parseInt(parent.getItemAtPosition(position).toString());

        // Showing selected spinner item
        Toast.makeText(parent.getContext(), "Selected: " + qty, Toast.LENGTH_LONG).show();

    }

    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_promo_large);

        dh=new dataHelper(getApplication());
        tv=(TextView)findViewById(R.id.name);
        iv=(ImageView)findViewById(R.id.img);
        spinner=(Spinner)findViewById(R.id.qty);
        addToCart=(Button)findViewById(R.id.addToCart);
        cost1=(TextView)findViewById(R.id.cost);

        Intent in=getIntent();
        Bundle bun1=in.getBundleExtra("bun");
        String name=bun1.getString("names");
        String cos=bun1.getString("cost");
        final String merName= bun1.getString("merName");
        final int pos=bun1.getInt("pos");

        Log.d("test: promo_large",name+cos+merName+pos);

        cost1.setText("Rs. "+cos);
        tv.setText(name);
//        iv.setImageBitmap(getAllItems.bitmaps[pos]);
        Log.d("test",getAllItems.getAllUrls(pos));
        Glide.with(getApplicationContext()).load(getAllItems.getAllUrls(pos))
                .thumbnail(0.5f)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(iv);

        spinner.setOnItemSelectedListener(this);
        List <Integer> lis= new ArrayList<Integer>();
        for (int i=1;i<10;i++){
            lis.add(i);
        }
        ArrayAdapter<Integer> dataAdapter = new ArrayAdapter<Integer>(this, android.R.layout.simple_spinner_item, lis);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);


        addToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("test","click");
                if(!dh.find(getAllItems.Ids[pos])){
                    dh.insert(getAllItems.Ids[pos],getAllItems.itemNames[pos],Integer.parseInt(getAllItems.itemCosts[pos]),qty, merName);
                }else{
                    dh.update(getAllItems.Ids[pos],getAllItems.itemNames[pos],Integer.parseInt(getAllItems.itemCosts[pos]),qty, merName);
                }
                addToCart.setEnabled(false);
                addToCart.setBackgroundColor(0xFFFF4444);
                addToCart.setText("Added to Cart");
            }
        });
    }
}
