package com.example.himanshudhanwant.uds;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ItemList extends AppCompatActivity implements AdapterView.OnItemClickListener{


    private ListView listView;

    public static final String GET_IMAGE_URL="https://app-1496457103.000webhostapp.com/PhotoUpload/getAllItems.php";
    public GetAllItems getAllItems;
    public static final String BITMAP_ID = "BITMAP_ID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_list);

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
                CustomList customList = new CustomList(ItemList.this,GetAllItems.imageURLs,GetAllItems.bitmaps,GetAllItems.itemNames,GetAllItems.itemCosts,GetAllItems.Ids);
                listView.setAdapter(customList);
            }

            @Override
            protected Void doInBackground(Void... voids) {
                try {
                    getAllItems.getAllImages();

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
                getAllItems = new GetAllItems(s);
                getImages();
            }

            @Override
            protected String doInBackground(String... strings) {
                BufferedReader bufferedReader = null;
                try {
                    URL url = new URL(strings[0]);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    StringBuilder sb = new StringBuilder();

                    bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));

                    String json;
                    while((json = bufferedReader.readLine())!= null){
                        sb.append(json+"\n");
                    }

                    return sb.toString().trim();

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
        b1.putInt("id",GetAllItems.Ids[i]);
        b1.putString("itemName",GetAllItems.itemNames[i]);
        b1.putString("cost",GetAllItems.itemCosts[i]);
        intent.putExtra("bun",b1);
        startActivity(intent);
    }
}
