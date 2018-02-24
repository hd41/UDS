package com.example.himanshudhanwant.uds;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class Cart extends AppCompatActivity implements CartRecyclerAdapter.ItemClickListener{

    CartRecyclerAdapter adapter;
    dataHelper dh;
    String merchantName;

    ArrayList<Item_row> cart_items=new ArrayList<Item_row>();
    Button pro;
    TextView tv;
    String final_order="";  int total=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        dh=new dataHelper(getApplicationContext());
        cart_items=getDataSet();
        pro=(Button)findViewById(R.id.proceed);
        tv=(TextView)findViewById(R.id.totCost);


        for(int i=0;i<cart_items.size();i++){
            total+=cart_items.get(i).getCost1()*cart_items.get(i).getQuant();
        }
        tv.setText(""+total);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.cart_recycler_view);
        LinearLayoutManager layout=new LinearLayoutManager(getApplicationContext());
        layout.setOrientation(LinearLayout.VERTICAL);
        recyclerView.setLayoutManager(layout);
        adapter = new CartRecyclerAdapter(this, cart_items);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);

        pro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for(int i=0;i<cart_items.size();i++){
                    final_order+=cart_items.get(i).getItem1()+"/"+cart_items.get(i).getCost1()+"/"+cart_items.get(i).getQuant()+"-";
                }
                Intent i= new Intent(getApplicationContext(),Details.class);
                Bundle b1=new Bundle();
                b1.putString("final_order",final_order);
                b1.putInt("tot",total);
                b1.putString("mer",merchantName);
                i.putExtra("bun",b1);
                startActivity(i);
            }
        });
    }

    @Override
    public void onItemClick(View view, int position) {
        String written=tv.getText().toString();
        String[] writt=written.split(" ");
        int sub=cart_items.get(position).getCost1()*cart_items.get(position).getQuant();
        total=Integer.parseInt(writt[writt.length-1])-sub;
        tv.setText(""+total);
        dh.delete2(cart_items.get(position).getId());
    }

    private ArrayList<Item_row> getDataSet() {
        ArrayList results = new ArrayList<Item_row>();
        Cursor c=dh.getData();
        c.moveToFirst();
        merchantName=c.getString(c.getColumnIndex("merchant"));
        while(c.isAfterLast()==false){
            int id=c.getInt(c.getColumnIndex("id"));
            String item=c.getString(c.getColumnIndex("item"));
            int cost=c.getInt(c.getColumnIndex("cost"));
            int quant=(int)(c.getFloat(c.getColumnIndex("quantity")));

            c.moveToNext();

            results.add(new Item_row(id,item,cost,quant));
        }
        c.close();

        return results;
    }
}
