package com.example.himanshudhanwant.uds;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

public class MenuByMerchant extends AppCompatActivity {

    KhanaRecyclerViewAdapter mAdapter;
    RecyclerView mRecyclerView;
    String merName;
    dataHelper dh;

    GetMerchantItems getMerchantItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_by_merchant);

        dh= new dataHelper(getApplication());
        Intent in=getIntent();
        Bundle bun1=in.getBundleExtra("bun");
        merName=bun1.getString("merName");

        mRecyclerView = (RecyclerView)findViewById(R.id.merchant_item_recycler_view);
        mRecyclerView.setLayoutManager(new GridLayoutManager(MenuByMerchant.this, 2));

        fillmRecyclerView();
    }

    public void fillmRecyclerView(){
        mAdapter = new KhanaRecyclerViewAdapter(MenuByMerchant.this, GetMerchantItems.imageURLs,
                GetMerchantItems.itemNames,GetMerchantItems.itemCosts,GetMerchantItems.Ids);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent in =new Intent(getApplicationContext(),Promo_large.class);
                Bundle bu=new Bundle();
                bu.putInt("pos",position);
                bu.putString("names",getMerchantItems.itemNames[position]);
                bu.putString("cost",getMerchantItems.itemCosts[position]);
                bu.putString("merName",merName);
                in.putExtra("bun",bu);
                startActivity(in);
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.cart_icon, menu);
        // return true so that the menu pop up is opened
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.action_cart) {
            if(dh.numberOfRows()>0){
                Intent intent = new Intent(this,Cart.class);
                startActivity(intent);
            }else{
                Toast.makeText(getApplicationContext(),"Insert some items to cart to check out",Toast.LENGTH_SHORT).show();
            }
            return true;
        }
        return true;
    }
}
