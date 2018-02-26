package com.example.himanshudhanwant.uds;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Details extends AppCompatActivity {

    EditText et1,et2,et3;
    TextView tv1;
    Button button;
    ProgressDialog pDialog;
    JSONParser jsonParser=new JSONParser();

    String addr,result,phone;
    public static final String url_insert_menu="https://app-1496457103.000webhostapp.com/PhotoUpload/insertOrder.php";
    public static String name;
    public static String merchant;

    SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        pref = PreferenceManager.getDefaultSharedPreferences(this);
        final SmsManager smsManager = SmsManager.getDefault();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent i2=getIntent();
        Bundle b2=i2.getBundleExtra("bun");
        String res=b2.getString("final_order");
        Log.d("test: res ",res);
        merchant=b2.getString("mer");
        int tot=b2.getInt("tot");

        et1=(EditText)findViewById(R.id.name);
        et2=(EditText)findViewById(R.id.phone);
        et3=(EditText)findViewById(R.id.address);
        tv1=(TextView)findViewById(R.id.tot);
        button=(Button)findViewById(R.id.fnl);

        //TODO : After login credentials take name and merchant's name and merchant's phone no
        final String phoneNo = "9467587898";
        et1.setText(pref.getString("loginName",""));
        et2.setText(pref.getString("loginPhone",""));
        tv1.setText("Rs." +tot);
        tv1.setTextSize(32);
        addr =et2.getText().toString();
        result=res+ tot;

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Sure to place your Order?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        name =et1.getText().toString();
                        addr =et2.getText().toString();
                        phone=et3.getText().toString();
                        new insertMenu(name,addr,phone,merchant).execute();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });


        final AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setMessage("Not Connected to Internet.Proceed to message?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        name =et1.getText().toString();
                        addr =et2.getText().toString();
                        final String message = "Name:"+ name + " Add:" + addr + " Res:" + result;
                        Log.d("Create Response", "" + message);
                        smsManager.sendTextMessage(phoneNo, null, message, null, null);
                        String tmp=("Hi " + et1.getText().toString() + ", your order has been placed.");
                        Toast.makeText(getApplicationContext(),tmp,Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        final AlertDialog.Builder builder2 = new AlertDialog.Builder(this);
        builder2.setMessage("Fill all the paticulars.")
                .setCancelable(false)
                .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isConnected()){
                    if(isEmpty(et1) || isEmpty(et2) || isEmpty(et3)){
                        AlertDialog alert = builder2.create();
                        alert.setTitle("Alert");
                        alert.show();
                    }
                    else{
                        AlertDialog alert = builder.create();
                        alert.setTitle("Confirming Order");
                        alert.show();
                    }

                }
                else{
                    if(isEmpty(et1) || isEmpty(et2) || isEmpty(et3)){
                        AlertDialog alert = builder2.create();
                        alert.setTitle("Alert");
                        alert.show();
                    }
                    else {
                        AlertDialog alert1 = builder1.create();
                        alert1.setTitle("Message:");
                        alert1.show();
                    }
                }


            }
        });
    }

    class insertMenu extends AsyncTask<String, String, String> {

        String customer;
        String address,phone1;
        String merchant;

        insertMenu(String name,String addr,String phone,String mer){
            customer = name;
            address=addr;
            phone1=phone;
            merchant=mer;
        }

        //Cart c=new Cart();
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Details.this);
            pDialog.setMessage("Ordering ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        protected String doInBackground(String... args) {
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("name",customer));
            params.add(new BasicNameValuePair("result", result));
            params.add(new BasicNameValuePair("addr", address));
            params.add(new BasicNameValuePair("phone",phone1));
            params.add(new BasicNameValuePair("merchant",merchant));

            JSONObject json=new JSONObject();
            json =jsonParser.makeHttpRequest(url_insert_menu,
                    "POST", params);

            return null;
        }

        protected void onPostExecute(String file_url) {
            // dismiss the dialog once done
            pDialog.dismiss();
            Toast.makeText(getApplicationContext(), "Ordered Successfully", Toast.LENGTH_SHORT).show();
            String tmp=("Hi "+et1.getText().toString()+", your order has been placed.");
            Toast.makeText(getApplicationContext(),tmp,Toast.LENGTH_SHORT).show();
        }
    }

    public boolean isConnected(){
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Activity.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected())
            return true;
        else
            return false;
    }

    private boolean isEmpty(EditText etText) {
        return etText.getText().toString().trim().length() == 0;
    }
}
