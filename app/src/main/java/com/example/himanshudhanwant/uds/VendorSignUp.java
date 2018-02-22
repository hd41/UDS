package com.example.himanshudhanwant.uds;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class VendorSignUp extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    public final static String url_insert_vendor="https://app-1496457103.000webhostapp.com/PhotoUpload/insertVendor.php"; // uds.epizy.com
    public final static String TAG_SUCCESS="success";
    ProgressDialog pDialog;
    JSONParser jsonParser= new JSONParser();
    String[] SPINNERLIST = {"Choose your Location: ","Complex", "Near Saraswati Hall", "Outside University"};

    EditText name, email,shop, phone, pwd;
    Button btn;
    TextView _LoginLink;
    Spinner spinner;
    String selectedValue="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendor_sign_up);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        name=(EditText)findViewById(R.id.input_name);
        email=(EditText)findViewById(R.id.input_email);
        shop= (EditText)findViewById(R.id.shop_name);
        phone= (EditText)findViewById(R.id.input_phone);
        pwd=(EditText)findViewById(R.id.input_password);
        btn=(Button)findViewById(R.id.btn_signup);
        _LoginLink= (TextView)findViewById(R.id.link_login);

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, SPINNERLIST);
        spinner = (Spinner)findViewById(R.id.android_material_design_spinner);
        spinner.setAdapter(arrayAdapter);
        spinner.setOnItemSelectedListener(this);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                if(isConnected() && validate()) {
                    String vName=name.getText().toString();
                    String vPhone=phone.getText().toString();
                    String vShop=shop.getText().toString();
                    String vMail=email.getText().toString();
                    String vLocation= spinner.getSelectedItem().toString();
                    String merName=vShop.split(" ")[0];
                    String vPwd=pwd.getText().toString();
                    new insertVendor(vName,vPhone,vShop,vMail,vLocation,merName,vPwd).execute();
                }
            }
        });

        Button btn1=(Button)findViewById(R.id.btn_signup);

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isConnected() && validate()) {
                    String vName=name.getText().toString();
                    String vPhone=phone.getText().toString();
                    String vShop=shop.getText().toString();
                    String vMail=email.getText().toString();
                    String vLocation= spinner.getSelectedItem().toString();
                    String merName=vShop.split(" ")[0];
                    String vPwd=pwd.getText().toString();
                    new insertVendor(vName,vPhone,vShop,vMail,vLocation,merName,vPwd).execute();
                }
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        selectedValue= SPINNERLIST[i];
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    class insertVendor extends AsyncTask<String, String, String> {

        String vName,vPhone, vShop, vMail, vLocation, merName,pwd;

        insertVendor(String a, String b, String c, String d,String e,String f, String g){
            vName=a;    vPhone=b;   vShop=c;    vMail=d;    vLocation=e;    merName=f;  pwd=g;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(VendorSignUp.this);
            pDialog.setMessage("Adding ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        protected String doInBackground(String... args) {
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("name",vName));
            params.add(new BasicNameValuePair("phone",vPhone));
            params.add(new BasicNameValuePair("location",vLocation));
            params.add(new BasicNameValuePair("shop",vShop));
            params.add(new BasicNameValuePair("mail",vMail));
            params.add(new BasicNameValuePair("merchant",merName));
            params.add(new BasicNameValuePair("password",pwd));

            JSONObject json =jsonParser.makeHttpRequest(url_insert_vendor,"POST", params);

            try {
                int success = json.getInt(TAG_SUCCESS);
                if (success == 1) {
                    runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(getApplicationContext(), "Added", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Toast.makeText(getApplicationContext(), "Cancelled", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        protected void onPostExecute(String file_url) {
            // dismiss the dialog once done
            pDialog.dismiss();
            Toast.makeText(getApplicationContext(), "Added Successfully", Toast.LENGTH_SHORT).show();
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

    public boolean validate() {
        boolean valid = true;

        String ename = name.getText().toString();
        String eemail = email.getText().toString();
        String epassword = pwd.getText().toString();
        String eshop = shop.getText().toString();
        String ephone = phone.getText().toString();
        String selValue= spinner.getSelectedItem().toString();
        String epwd= pwd.getText().toString();

        if (ename.isEmpty() || ename.length() < 3) {
            name.setError("at least 3 characters");
            valid = false;
        } else {
            name.setError(null);
        }
        if (eemail.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(eemail).matches()) {
            email.setError("enter a valid email address");
            valid = false;
        } else {
            email.setError(null);
        }
        if (epassword.isEmpty() || epassword.length() < 4 || epassword.length() > 10) {
            pwd.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            pwd.setError(null);
        }
        if(eshop.isEmpty() || eshop.length() <4){
            shop.setError("shop name should be atleast of 4 characters");
            valid=false;
        }else {
            shop.setError(null);
        }
        if(selValue.length()>21){
            final AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
            builder.setMessage("Invalid value for Location")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            // Create the AlertDialog object and return it
            builder.create();
            valid= false;
        }
        if(ephone.isEmpty() || ephone.length() <10){
            Log.d("test",ephone.toString());
            phone.setError("Enter a valid Indian no.");
            valid=false;
        }else {
            phone.setError(null);
        }
        if (epwd.isEmpty() || epwd.length() < 6) {
            pwd.setError("at least 3 characters");
            valid = false;
        } else {
            pwd.setError(null);
        }

        return valid;
    }
}
