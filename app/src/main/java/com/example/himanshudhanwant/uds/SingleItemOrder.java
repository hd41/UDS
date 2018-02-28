package com.example.himanshudhanwant.uds;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;
import java.util.List;

public class SingleItemOrder extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    int qty=0;
    Spinner spinner;
    int cos=0;
    ImageView iv;

    TextView cost1,name1;
    GetAllItems getAllItems = new GetAllItems();
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
        setContentView(R.layout.activity_single_item_order);

        CardView cardView= (CardView)findViewById(R.id.card_view);
        final Button addToCart=(Button)findViewById(R.id.addToCart);
        cost1=(TextView)findViewById(R.id.cost);
        iv=(ImageView)findViewById(R.id.img) ;
        name1=(TextView)findViewById(R.id.name);

        Intent i2=getIntent();
        Bundle b2=i2.getBundleExtra("bun");
        final String name=b2.getString("name");
        final String cost=b2.getString("cost");
        final String mer=b2.getString("mer");
        final int pos=b2.getInt("pos");

//        iv.setImageBitmap(getAllItems.bitmaps[pos]);
        Log.d("test",getAllItems.getAllUrls(pos));
        Glide.with(getApplicationContext()).load(getAllItems.getAllUrls(pos))
                .thumbnail(0.5f)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(iv);
        name1.setText(name);
        cost1.setText("Rs. "+cost);
        cos=Integer.parseInt(cost);

        spinner = (Spinner)findViewById(R.id.qty);
        spinner.setOnItemSelectedListener(this);
        List<Integer> lis= new ArrayList<Integer>();
        for (int i=1;i<10;i++){
            lis.add(i);
        }
        ArrayAdapter<Integer> dataAdapter = new ArrayAdapter<Integer>(this, android.R.layout.simple_spinner_item, lis);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);

        addToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String final_order=name+"/"+cost+"/"+qty+"-";
                Intent i= new Intent(getApplicationContext(),Details.class);
                Bundle b1=new Bundle();
                b1.putString("final_order",final_order);
                b1.putInt("tot",cos*qty);
                b1.putString("mer",mer);
                i.putExtra("bun",b1);
                startActivity(i);
            }
        });
    }
}
