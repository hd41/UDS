package com.example.himanshudhanwant.uds;

        import android.app.Activity;
        import android.app.ProgressDialog;
        import android.net.ConnectivityManager;
        import android.net.NetworkInfo;
        import android.os.AsyncTask;
        import android.os.Bundle;
        import android.support.v7.app.AppCompatActivity;
        import android.util.Log;

        import android.content.Intent;
        import android.view.View;
        import android.widget.Button;
        import android.widget.EditText;
        import android.widget.TextView;
        import android.widget.Toast;

        import org.apache.http.NameValuePair;
        import org.apache.http.message.BasicNameValuePair;
        import org.json.JSONArray;
        import org.json.JSONException;
        import org.json.JSONObject;

        import java.util.ArrayList;
        import java.util.List;

//        import butterknife.ButterKnife;
//        import butterknife.InjectView;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private static final int REQUEST_SIGNUP = 0;
    private final static String URL_login="https://app-1496457103.000webhostapp.com/PhotoUpload/login.php";
    ProgressDialog pDialog;

    JSONParser jsonParser= new JSONParser();
    EditText _emailText,_passwordText;
    Button _loginButton;
    TextView _signupLink;

    int flag;
    String usrName, usrMail, usrPhone;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //ButterKnife.inject(this);
        _emailText= (EditText)findViewById(R.id.input_email);
        _passwordText= (EditText)findViewById(R.id.input_password);
        _loginButton=(Button)findViewById(R.id.btn_login);
        _signupLink= (TextView)findViewById(R.id.link_signup);

        _loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validate() && isConnected()){
                    String mail=_emailText.getText().toString();
                    String pwd= _passwordText.getText().toString();
                    new Login(mail,pwd).execute();
                }
            }
        });

        _signupLink.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Start the Signup activity
                Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
        startActivityForResult(intent, REQUEST_SIGNUP);
    }
});
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SIGNUP) {
            if (resultCode == RESULT_OK) {

                // TODO: Implement successful signup logic here
                // By default we just finish the Activity and log them in automatically
                this.finish();
            }
        }
    }

    @Override
    public void onBackPressed() {
        // disable going back to the MainActivity
        moveTaskToBack(true);
    }

    class Login extends AsyncTask<String, String, String> {

        String vMail, vPwd;

        Login(String a, String b){
            vMail=a;    vPwd=b;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(LoginActivity.this);
            pDialog.setMessage("Logging In ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        protected String doInBackground(String... args) {
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("usr",vMail));
            params.add(new BasicNameValuePair("password",vPwd));

            JSONObject json =jsonParser.makeHttpRequest(URL_login,"POST", params);

            try{
                String no=json.getString("no");
                if(no.matches("1")){
//                    JSONObject jsonObj = new JSONObject(json.toString());
                    JSONArray result = json.getJSONArray("result");
                    for (int i = 0; i < result.length(); i++) {
                        JSONObject c = result.getJSONObject(i);
                        usrName = c.getString("name");
                        usrMail = c.getString("mail");
                        usrPhone = c.getString("phone");
                        flag =1;
                        Log.d("test",usrName);
                    }
                }else if(no.matches("2")){
//                    JSONObject jsonObj = new JSONObject(temp[1]);
                    JSONArray result = json.getJSONArray("result");
                    for (int i = 0; i < result.length(); i++) {
                        JSONObject c = result.getJSONObject(i);
                        usrName = c.getString("name");
                        usrMail = c.getString("email");
                        usrPhone = c.getString("phone");
                        flag =2;
                    }
                }else{
                    flag=3;
                }
            }catch (Exception ex){
                ex.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(String file_url) {
            // dismiss the dialog once done
            pDialog.dismiss();
            switch (flag){
                case 1: Intent in = new Intent(getApplicationContext(),UploadItem.class);
                    startActivity(in);
                    break;
                case 2: Intent in2= new Intent(getApplicationContext(),Home.class);
                    startActivity(in2);
                    break;
                case 3: _emailText.setError("Email or password incorrect");
                    break;
            }

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

        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

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

        return valid;
    }
}