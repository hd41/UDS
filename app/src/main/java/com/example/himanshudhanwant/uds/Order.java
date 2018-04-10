/*
 * Copyright (c) 2018. Truiton (http://www.truiton.com/).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Contributors:
 * Mohit Gupt (https://github.com/mohitgupt)
 *
 */

package com.example.himanshudhanwant.uds;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Order extends AppCompatActivity {

    TextView tv1,tv2,tv3,tv4,tv5;
    ProgressDialog pDialog;
    JSONParser jsonParser = new JSONParser();
    String pid;
    Integer pos;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        Intent i2=getIntent();
        Bundle b2=i2.getBundleExtra("order");
        pid=b2.getString("pid");
        String name=b2.getString("name");
        String ord=b2.getString("ord");
        String add=b2.getString("add");
        String phone=b2.getString("phone");
        pos=b2.getInt("position");
        String time=b2.getString("time");

        tv1=(TextView)findViewById(R.id.tv1);
        tv2=(TextView)findViewById(R.id.tv2);
        tv3=(TextView)findViewById(R.id.tv3);
        tv4=(TextView)findViewById(R.id.tv4);
        tv5=(TextView)findViewById(R.id.tv5);

        tv1.setText(pid);
        tv2.setText(name);

        String final_order="";
        String[] ord_tot= ord.split("-");
        String tot_cost=ord_tot[ord_tot.length-1];
        String[] tmp;
        for(int i=0;i<ord_tot.length-1;i++){
            tmp=ord_tot[i].split("/");
            final_order+=tmp[0]+"\t\t"+tmp[1]+"\t"+tmp[2]+"\n";
        }
        final_order+="Total cost: "+tot_cost;

        tv3.setText(final_order);
        tv4.setText(add);
        tv5.setText(phone);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {// Accept wala
                new Accept().execute();
            }
        });

        FloatingActionButton fab1 = (FloatingActionButton) findViewById(R.id.fab1);
        fab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {//Cancel Order
                new Cancel().execute();
            }
        });
    }

    class Cancel extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Order.this);
            pDialog.setMessage("Fetching orders ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected String doInBackground(Void... params) {
            String str = "hd ";
            try {
                List<NameValuePair> params1 = new ArrayList<NameValuePair>();
                Log.d("test",pid.toString());
                params1.add(new BasicNameValuePair("order_id",pid.toString()));

                JSONObject json =jsonParser.makeHttpRequest("https://app-1496457103.000webhostapp.com/PhotoUpload/orderCancel.php",
                        "POST", params1);
                return json.toString().trim();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return str;
        }

        @Override
        protected void onPostExecute(String result1) {
            Log.d("test",result1);
            pDialog.dismiss();
            finish();
        }
    }

    class Accept extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Order.this);
            pDialog.setMessage("Fetching orders ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected String doInBackground(Void... params) {
            String str = "hd ";
            try {
                List<NameValuePair> params1 = new ArrayList<NameValuePair>();
                Log.d("test",pid.toString());
                params1.add(new BasicNameValuePair("order_id",pid.toString()));

                JSONObject json =jsonParser.makeHttpRequest("https://app-1496457103.000webhostapp.com/PhotoUpload/orderAccept.php",
                        "POST", params1);
                return json.toString().trim();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return str;
        }

        @Override
        protected void onPostExecute(String result1) {
            Log.d("test",result1);
            pDialog.dismiss();
            finish();
        }
    }

}
