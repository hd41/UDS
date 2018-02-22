package com.example.himanshudhanwant.uds;

import android.app.Activity;
import android.app.ProgressDialog;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

import butterknife.ButterKnife;
import butterknife.InjectView;

public class SignupActivity extends AppCompatActivity {
    private static final String TAG = "SignupActivity";
    ProgressDialog pDialog;
    JSONParser jsonParser=new JSONParser();
    private static final String URL_insert_customer="https://app-1496457103.000webhostapp.com/PhotoUpload/insertCustomer.php";

    @InjectView(R.id.input_name) EditText _nameText;
    @InjectView(R.id.input_phone) EditText _phone;
    @InjectView(R.id.input_email) EditText _emailText;
    @InjectView(R.id.input_password) EditText _passwordText;
    @InjectView(R.id.input_rpassword) EditText _rpassword;
    @InjectView(R.id.btn_signup) Button _signupButton;
    @InjectView(R.id.link_login) TextView _loginLink;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ButterKnife.inject(this);

        _signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validate() && isConnected()){
                    String name=_nameText.getText().toString();
                    String phone=_phone.getText().toString();
                    String email=_emailText.getText().toString();
                    String pass= _passwordText.getText().toString();
                    new insertCustomer(name,phone,email,pass).execute();
                }
            }
        });

        _loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish the registration screen and return to the Login activity
                finish();
            }
        });
    }

    class insertCustomer extends AsyncTask<String, String, String> {

        String vName,vPhone, vMail, vPwd;

        insertCustomer(String a, String b, String c, String d){
            vName=a;    vPhone=b;    vMail=c;    vPwd=d;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(SignupActivity.this);
            pDialog.setMessage("Adding ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        protected String doInBackground(String... args) {
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("name",vName));
            params.add(new BasicNameValuePair("phone",vPhone));
            params.add(new BasicNameValuePair("mail",vMail));
            params.add(new BasicNameValuePair("password",vPwd));

            JSONObject json =jsonParser.makeHttpRequest(URL_insert_customer,"POST", params);

            try {
                int success = json.getInt("success");
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

        String name = _nameText.getText().toString();
        String phone= _phone.getText().toString();
        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();
        String rpassword = _rpassword.getText().toString();

        if (name.isEmpty() || name.length() < 3) {
            _nameText.setError("at least 3 characters");
            valid = false;
        } else {
            _nameText.setError(null);
        }
        if (phone.isEmpty() || phone.length() < 10) {
            _phone.setError("Phone must contain 10 digits");
            valid = false;
        } else {
            _phone.setError(null);
        }
        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError("enter a valid email address");
            valid = false;
        } else {
            _emailText.setError(null);
        }
        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            _passwordText.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            _passwordText.setError(null);
        }
        if (rpassword.isEmpty() || rpassword.length() < 4 || rpassword.length() > 10) {
            _rpassword.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            _rpassword.setError(null);
        }
        if(!rpassword.equals(password)){
            _rpassword.setError("Password doesn't match with retyped password");
            valid = false;
        }

        return valid;
    }
}