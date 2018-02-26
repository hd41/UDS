package com.example.himanshudhanwant.uds;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ItemList extends AppCompatActivity implements AdapterView.OnItemClickListener{


    private ListView listView;

    public static final String GET_IMAGE_URL="https://app-1496457103.000webhostapp.com/PhotoUpload/orderByMerchant.php";
    public GetMerchantItems getMerchantItems;
    public static final String BITMAP_ID = "BITMAP_ID";
    JSONParser jsonParser = new JSONParser();
    SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_list);

        pref = PreferenceManager.getDefaultSharedPreferences(this);
        listView = (ListView) findViewById(R.id.listView);
        listView.setOnItemClickListener(this);
        getURLs();
    }

    private void getImages(){
        class GetImages extends AsyncTask<Void,Void,Void>{
            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(ItemList.this,"Downloading images...","Please wait...",false,false);
            }

            @Override
            protected void onPostExecute(Void v) {
                super.onPostExecute(v);
                loading.dismiss();
                //Toast.makeText(ImageListView.this,"Success",Toast.LENGTH_LONG).show();
                CustomList customList = new CustomList(ItemList.this,getMerchantItems.imageURLs,getMerchantItems.itemNames,getMerchantItems.itemCosts,
                        getMerchantItems.Ids);
                listView.setAdapter(customList);
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
                loading = ProgressDialog.show(ItemList.this,"Loading...","Please Wait...",true,true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                getMerchantItems = new GetMerchantItems(s);
                getImages();
            }

            @Override
            protected String doInBackground(String... strings) {
                BufferedReader bufferedReader = null;
                try {

                    List<NameValuePair> params = new ArrayList<NameValuePair>();
                    params.add(new BasicNameValuePair("name",pref.getString("loginMer","HD")));

                    JSONObject json =jsonParser.makeHttpRequest(GET_IMAGE_URL,"POST", params);
                    return json.toString().trim();

                }catch(Exception e){
                    return null;
                }
            }
        }
        GetURLs gu = new GetURLs();
        gu.execute(GET_IMAGE_URL);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Intent intent = new Intent(this, UpdateItem.class);
//        intent.putExtra(BITMAP_ID,i);
        Bundle b1=new Bundle();
        b1.putInt("pos",i);
        b1.putInt("id",getMerchantItems.Ids[i]);
        b1.putString("itemName",getMerchantItems.itemNames[i]);
        b1.putString("cost",getMerchantItems.itemCosts[i]);
        intent.putExtra("bun",b1);
        startActivity(intent);
    }
}
