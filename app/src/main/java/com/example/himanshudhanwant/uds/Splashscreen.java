package com.example.himanshudhanwant.uds;


import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.ImageView;

import java.util.Random;


public class Splashscreen extends Activity {

    Thread splashTread;
    ImageView imageView;

    SharedPreferences pref;
    private boolean isConnected = true;
    private Context con;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);
        imageView = (ImageView)findViewById(R.id.splash);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);

        //implementing network state action
        ConnectivityManager connectivityManager = (ConnectivityManager)con.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo ni = connectivityManager.getActiveNetworkInfo();
            if (ni.getState() != NetworkInfo.State.CONNECTED) {
                // record the fact that there is not connection
                isConnected = false;
            }
        }

        if(isConnected){
            pref = PreferenceManager
                    .getDefaultSharedPreferences(this);

            splashTread = new Thread() {
                @Override
                public void run() {
                    try {
                        int waited = 0;
                        // Splash screen pause time
                        while (waited < 2000) {
                            sleep(100);
                            waited += 100;
                        }



                        String check=pref.getString("loginName", null);
                        if(check != null){ //logged in
                            Intent intent = new Intent(Splashscreen.this,
                                    HomeWithLogin.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                            startActivity(intent);
                        }else{
                            Intent intent = new Intent(Splashscreen.this,
                                    Home.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                            startActivity(intent);
                        }
                        Splashscreen.this.finish();
                    } catch (InterruptedException e) {
                        // do nothing
                        Intent in = new Intent(getApplicationContext(),Error.class);
                        startActivity(in);
                    } finally {
                        Splashscreen.this.finish();
                    }

                }
            };
            splashTread.start();
        }else {
            Intent in = new Intent(getApplicationContext(),Error.class);
            startActivity(in);
        }


    }

}