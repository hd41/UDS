package com.example.himanshudhanwant.uds;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import static android.R.id.list;

public class Home extends AppCompatActivity implements MyRecyclerViewAdapter.ItemClickListener {

    MyRecyclerViewAdapter adapter;
    KhanaRecyclerViewAdapter mAdapter;
    RecyclerView mRecyclerView;
    Button btn;

    public static final String GET_IMAGE_URL="https://app-1496457103.000webhostapp.com/PhotoUpload/getAllItems.php";
    GetAllItems getAllItems;
    ProgressDialog pDialog;

    public ArrayList<String> merchants=new ArrayList<String>();

    ArrayList<Integer> animalNames=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        btn=(Button)findViewById(R.id.order_by_merchant);
        dataHelper dh= new dataHelper(getApplication());
        dh.delete_all();

        for(int i=0;i<5;i++){
            animalNames.add(R.drawable.horse);
        }

        // set up the RecyclerView
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.promo_recycler_view);
        LinearLayoutManager layout=new LinearLayoutManager(getApplicationContext());
        layout.setOrientation(LinearLayout.HORIZONTAL);
        recyclerView.setLayoutManager(layout);
        adapter = new MyRecyclerViewAdapter(this, animalNames);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);

        //Second Recycler View
        mRecyclerView = (RecyclerView)findViewById(R.id.khana_recycler_view);
        mRecyclerView.setLayoutManager(new GridLayoutManager(Home.this, 2));
        //adapter is set at getImages() function

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(Home.this);
                alertDialog.setTitle("Alert");
                alertDialog.setMessage("Login to Proceed");
                alertDialog.setIcon(R.drawable.add_user);

                alertDialog.setPositiveButton("Login", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int which) {

                        // Write your code here to invoke YES event
                        Intent in = new Intent(getApplicationContext(),LoginActivity.class);
                        startActivity(in);
                    }
                });
                alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Write your code here to invoke NO event
                        Toast.makeText(getApplicationContext(), "You clicked on NO", Toast.LENGTH_SHORT).show();
                        dialog.cancel();
                    }
                });
                alertDialog.show();

                //new fetchMerchants().execute();
            }
        });

        fillmRecyclerView();

    }

    class fetchMerchants extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Home.this);
            pDialog.setMessage("Fetching merchants ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected String doInBackground(Void... params) {
            String str = "hd ";
            try {

                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost(
                        "https://app-1496457103.000webhostapp.com/PhotoUpload/getAllVendors.php");
                HttpResponse response = httpclient.execute(httppost);

                str = EntityUtils.toString(response.getEntity());
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            return str;

        }

        @Override
        protected void onPostExecute(String result1) {
            try {

                JSONObject object=new JSONObject(result1);
                JSONArray new_array = object.getJSONArray("result");

                for (int i = 0, count = new_array.length(); i < count; i++) {
                    try {
                        JSONObject jsonObject = new_array.getJSONObject(i);
                        merchants.add(jsonObject.getString("name"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                GetAllItems ga=new GetAllItems();
                ga.putMerchants(merchants);
                Intent in2=new Intent(getApplicationContext(),OrderByMerchant.class);
                startActivity(in2);
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                // tv.setText("error2");
            }

            pDialog.dismiss();

        }
    }

    public void fillmRecyclerView(){
        mAdapter = new KhanaRecyclerViewAdapter(Home.this, GetAllItems.imageURLs,GetAllItems.itemNames,GetAllItems.itemCosts,GetAllItems.Ids);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent in =new Intent(getApplicationContext(),SingleItemOrder.class);
                Bundle bu=new Bundle();
                bu.putInt("pos",position);
                bu.putString("name",getAllItems.itemNames[position]);
                bu.putString("cost",getAllItems.itemCosts[position]);
                bu.putString("mer",getAllItems.itemMerchant[position]);
                in.putExtra("bun",bu);
                startActivity(in);
            }
        });
    }

    @Override
    public void onItemClick(View view, int position) {
        Intent in =new Intent(getApplicationContext(),SingleItemOrder.class);
        Bundle bu=new Bundle();
        Toast.makeText(getApplicationContext(),"you clicked "+position,Toast.LENGTH_SHORT).show();
        bu.putInt("pos",9-2*position-1);
        bu.putString("name",getAllItems.itemNames[9-2*position-1]);
        bu.putString("cost",getAllItems.itemCosts[9-2*position-1]);
        bu.putString("mer",getAllItems.itemMerchant[9-2*position-1]);
        in.putExtra("bun",bu);
        startActivity(in);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.add_user_icon, menu);
        inflater.inflate(R.menu.home_option_menu, menu);
        // return true so that the menu pop up is opened
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId())
        {

            case R.id.loginActivity:
                Intent in1 =new Intent(Home.this,LoginActivity.class);
                startActivity(in1);
                break;
            case R.id.vendorSignUp:
                Intent in2 =new Intent(getApplicationContext(),VendorSignUp.class);
                startActivity(in2);
                break;
            case R.id.exit:
                finish();
                break;
        }
        if(item.getItemId()==R.id.login) {
            Intent intent = new Intent(this,LoginActivity.class);
            startActivity(intent);
            return true;
        }
        return true;
    }
}
