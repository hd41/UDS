package com.example.himanshudhanwant.uds;

import android.app.ProgressDialog;
import android.content.Intent;
import android.media.Image;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class UpdateItem extends AppCompatActivity {

    ImageView iv;
    EditText et1,et2;
    Button btn,del;

    String updatedName,updatedCost;
    int update_ID;

    JSONParser jsonParser=new JSONParser();

    public static final String UPDATE_ITEM="https://app-1496457103.000webhostapp.com/PhotoUpload/updateItem.php";
    public static final String DELETE_ITEM="https://app-1496457103.000webhostapp.com/PhotoUpload/deleteItem.php";
    private static final String TAG_SUCCESS = "Success";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_item);

        iv=(ImageView)findViewById(R.id.img);
        et1=(EditText)findViewById(R.id.name);
        et2=(EditText)findViewById(R.id.cost);
        btn=(Button)findViewById(R.id.update);
        del=(Button)findViewById(R.id.delete);

        Intent in=getIntent();
        Bundle b2=in.getBundleExtra("bun");
        int pos=b2.getInt("pos");
        update_ID=b2.getInt("id");
        iv.setImageBitmap(GetAllItems.bitmaps[pos]);
        et1.setText(b2.getString("itemName"));
        et2.setText("Rs. "+b2.getString("cost"));

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updatedName=et1.getText().toString();
                updatedCost=et2.getText().toString();
                String[] cost=updatedCost.split(" ");
                updatedCost=cost[cost.length-1];
                update();
            }
        });

        del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                delete();
            }
        });
    }

    private void update() {
        class getUpdate extends AsyncTask<String,Void,String> {
            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                Log.d("test","on PreExecute");
                loading = ProgressDialog.show(UpdateItem.this,"Updating...","Please Wait...",true,true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(getApplicationContext(),"Updated Successfully!!",Toast.LENGTH_SHORT).show();
            }

            @Override
            protected String doInBackground(String... strings) {
                BufferedReader bufferedReader = null;
                String res="";
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("name",updatedName));
                params.add(new BasicNameValuePair("cost",updatedCost));
                params.add(new BasicNameValuePair("id",""+update_ID));

                JSONObject json =jsonParser.makeHttpRequest(strings[0],
                            "POST", params);

//                try {
////                    int success = json.getInt(TAG_SUCCESS);
////
////                    if (success == 1) {
////                        finish();
////                    } else {
////                        //Toast.makeText(getApplicationContext(), "Updated", Toast.LENGTH_SHORT).show();
////                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }

                return null;
            }
        }
        getUpdate gu = new getUpdate();
        gu.execute(UPDATE_ITEM);
    }

    private void delete() {
        class getDelete extends AsyncTask<String,Void,String> {
            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(UpdateItem.this,"Deleting...","Please Wait...",true,true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(getApplicationContext(),"Deleted Successfully!!",Toast.LENGTH_SHORT).show();
            }

            @Override
            protected String doInBackground(String... strings) {
                BufferedReader bufferedReader = null;
                String res="";
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("id",""+update_ID));

                JSONObject json =jsonParser.makeHttpRequest(strings[0],
                        "POST", params);

                try {
                    int success = json.getInt(TAG_SUCCESS);

                    if (success == 1) {
                        finish();
                    } else {
                        //Toast.makeText(getApplicationContext(), "Updated", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                return null;
            }
        }
        getDelete gu = new getDelete();
        gu.execute(DELETE_ITEM);
    }
}
