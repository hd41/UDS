/*
 * Copyright (c) 2017. Truiton (http://www.truiton.com/).
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

package com.truiton.bottomnavigation;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ItemOneFragment extends Fragment {

    public ArrayList<String> order_id= new ArrayList<String>();
    public ArrayList<String> order_name= new ArrayList<String>();
    public ArrayList<String> order_res= new ArrayList<String>();
    public ArrayList<String> order_add= new ArrayList<String>();
    public ArrayList<String> order_phone= new ArrayList<String>();
    public ArrayList<String> order_time= new ArrayList<String>();
    public ArrayList<Integer> order_tick= new ArrayList<Integer>();

    TextView tv;
    ListView lv;
    BaseAdapter2 adapter;
    ProgressDialog pDialog;
    JSONParser jsonParser = new JSONParser();

    AllOrder ao = new AllOrder();

    public static ItemOneFragment newInstance() {
        ItemOneFragment fragment = new ItemOneFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_item_one, container, false);

        lv = (ListView)v.findViewById(R.id.listView);
        new TheTask().execute();
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent in = new Intent(getContext(),Order.class);
                Bundle bu=new Bundle();
                bu.putString("pid",order_id.get(position));
                bu.putString("name",order_name.get(position));
                bu.putString("ord",order_res.get(position));
                bu.putString("add",order_add.get(position));
                bu.putString("phone",order_phone.get(position));
                bu.putString("time",order_time.get(position));
                bu.putInt("position",position);
                in.putExtra("order",bu);
                startActivity(in);
            }
        });
        return v;
    }

    class TheTask extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(getContext());
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
                params1.add(new BasicNameValuePair("name","Jingle"));

                JSONObject json =jsonParser.makeHttpRequest("https://app-1496457103.000webhostapp.com/PhotoUpload/allMenuByMerchant.php",
                        "POST", params1);
                return json.toString().trim();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return str;
        }

        @Override
        protected void onPostExecute(String result1) {

            String[] trial=result1.split("<");
            String result=trial[0];
            try {
                JSONObject object=new JSONObject(result);
                JSONArray new_array = object.getJSONArray("menu");

                for (int i = 0, count = new_array.length(); i < count; i++) {
                    try {
                        JSONObject jsonObject = new_array.getJSONObject(i);

                        AllOrder so= new AllOrder(jsonObject.getString("order_id"),jsonObject.getString("name"),
                                jsonObject.getString("result"),jsonObject.getString("addr"),jsonObject.getString("phone"),
                                jsonObject.getString("date"),0);

                        order_id.add(jsonObject.getString("order_id"));
                        order_name.add(jsonObject.getString("name"));
                        order_res.add(jsonObject.getString("result"));
                        order_add.add(jsonObject.getString("phone"));
                        order_phone.add(jsonObject.getString("addr"));
                        order_time.add(jsonObject.getString("date"));
                        order_tick.add(0);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            pDialog.dismiss();


            if (getActivity()!=null){
                adapter = new BaseAdapter2(getActivity(),order_name,order_phone,order_tick);
                adapter.notifyDataSetChanged();
                lv.setAdapter(adapter);
            }
        }
    }
}
