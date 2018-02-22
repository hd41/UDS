package com.example.himanshudhanwant.uds;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class OrderByMerchant extends AppCompatActivity {

    private final static  String GET_IMAGE_URL1="https://app-1496457103.000webhostapp.com/PhotoUpload/orderByMerchant.php";

    RecyclerView mRecyclerView;
    MerchantRecyclerViewAdapter mAdapter;
    String merchantName;

    JSONParser jsonParser=new JSONParser();
    GetMerchantItems getMerchantItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_by_merchant);


        mRecyclerView = (RecyclerView)findViewById(R.id.merchant_recycler_view);
        mRecyclerView.setLayoutManager(new GridLayoutManager(OrderByMerchant.this, 2));

        fillmRecyclerView();
    }

    public void fillmRecyclerView(){
        mAdapter = new MerchantRecyclerViewAdapter(OrderByMerchant.this, new GetAllItems().getMerchants());
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                merchantName=new GetAllItems().getMerchants()[position];
                Log.d("test",merchantName+" pressed");
                getURLs();
            }
        });
    }

    private void getImages(){
        class GetImages extends AsyncTask<Void,Void,Void> {
            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(OrderByMerchant.this,"Loading ...","Please wait",false,false);
            }

            @Override
            protected void onPostExecute(Void v) {
                super.onPostExecute(v);
                loading.dismiss();
                Intent in=new Intent(getApplicationContext(),MenuByMerchant.class);
                Bundle b1= new Bundle();
                b1.putString("merName",merchantName);
                in.putExtra("bun",b1);
                startActivity(in);

            }

            @Override
            protected Void doInBackground(Void... voids) {
                try {
                    getMerchantItems.getAllImages();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return null;
            }
        }
        GetImages getImages = new GetImages();
        getImages.execute();
    }

    private void getURLs() {
        class GetURLs extends AsyncTask<String,Void,String> {
            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                 loading = ProgressDialog.show(OrderByMerchant.this,"Loading...","Please Wait...",true,true);
            }

            @Override
            protected String doInBackground(String... strings) {
                BufferedReader bufferedReader = null;
                try {

                    List<NameValuePair> params = new ArrayList<NameValuePair>();
                    params.add(new BasicNameValuePair("name",merchantName));

                    JSONObject json =jsonParser.makeHttpRequest(GET_IMAGE_URL1,
                            "POST", params);
                    return json.toString().trim();

                }catch(Exception e){
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Log.d("test",s);
                getMerchantItems = new GetMerchantItems(s);
                getImages();
            }
        }
        GetURLs gu = new GetURLs();
        gu.execute(GET_IMAGE_URL1);
    }
}
