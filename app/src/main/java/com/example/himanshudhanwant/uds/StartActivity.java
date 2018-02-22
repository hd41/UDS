package com.example.himanshudhanwant.uds;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ImageView;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class StartActivity extends Activity {

    public static final String GET_IMAGE_URL="https://app-1496457103.000webhostapp.com/PhotoUpload/getAllItems.php";
    GetAllItems getAllItems;
    int flag=0;

    Thread splashTread;
    ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);
        imageView = (ImageView)findViewById(R.id.splash);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        //int[] ids = new int[]{R.drawable.s_img,R.drawable.s_image_black, R.drawable.s_image_black2};
        //Random randomGenerator = new Random();
        //int r= randomGenerator.nextInt(ids.length);
        //this.imageView.setImageDrawable(getResources().getDrawable(ids[r]));

        getURLs();
        splashTread = new Thread() {
            @Override
            public void run() {
                try {
                    int waited = 0;
                    // Splash screen pause time
                    while ( flag==0) {
                        sleep(100);
                        waited += 100;
                    }
                    Intent intent = new Intent(StartActivity.this,
                            Home.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);
                    StartActivity.this.finish();
                } catch (InterruptedException e) {
                    // do nothing
                } finally {
                    StartActivity.this.finish();
                }

            }
        };
        splashTread.start();
    }


    private void getImages(){
        class GetImages extends AsyncTask<Void,Void,Void> {
            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
//                loading = ProgressDialog.show(Splashscreen.this,"Downloading images...","Please wait...",false,false);
            }

            @Override
            protected void onPostExecute(Void v) {
                super.onPostExecute(v);
//                loading.dismiss();
                flag=1;
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
//                loading = ProgressDialog.show(Splashscreen.this,"Loading...","Please Wait...",true,true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
//                loading.dismiss();
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


}