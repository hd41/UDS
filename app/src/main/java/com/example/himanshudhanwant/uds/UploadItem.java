package com.example.himanshudhanwant.uds;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import java.net.HttpURLConnection;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.widget.EditText;
import android.net.Uri;
import java.io.InputStreamReader;
import java.io.OutputStream;
import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedWriter;
import java.util.Map;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.io.OutputStreamWriter;
import java.net.URL;
import android.provider.MediaStore;
import java.io.BufferedReader;
import java.net.URLEncoder;
import java.io.UnsupportedEncodingException;
import android.util.Base64;

import org.json.JSONException;

public class UploadItem extends AppCompatActivity {

    Button CaptureImageFromCamera,UploadImageToServer;
    ImageView ImageViewHolder;
    EditText imageName,cost;
    ProgressDialog progressDialog ;
    Intent intent ;
    TextView tv;
    public  static final int RequestPermissionCode  = 1 ;

    Bitmap bitmap;
    boolean check = true;

    String GetImageNameFromEditText;

    String ImageNameFieldOnServer = "image_name" ;
    String ImagePathFieldOnServer = "image_path" ;
    String ImageUploadPathOnSever ="https://app-1496457103.000webhostapp.com/PhotoUpload/img_to_server.php" ;
    String cost_value;
    SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_item);

        pref = PreferenceManager.getDefaultSharedPreferences(this);
        CaptureImageFromCamera = (Button)findViewById(R.id.button);
        ImageViewHolder = (ImageView)findViewById(R.id.imageView);
        UploadImageToServer = (Button) findViewById(R.id.button2);
        imageName = (EditText)findViewById(R.id.editText);
        cost=(EditText)findViewById(R.id.editText2);
        tv =(TextView)findViewById(R.id.merName);

        tv.setText(pref.getString("loginMer","Login Again"));

        //EnableRuntimePermissionToAccessCamera();

        CaptureImageFromCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), 7);
//                intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
//                startActivityForResult(intent, 7);
            }
        });

        UploadImageToServer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cost_value=cost.getText().toString();
                GetImageNameFromEditText = imageName.getText().toString();
                if(cost_value.isEmpty() || GetImageNameFromEditText.isEmpty())
                {
                    //EditText is empty
                    Toast.makeText(getApplicationContext(),"Pehle sab bharo",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    //EditText is not empty
                    ImageUploadToServerFunction();
                }
            }
        });
    }

    // Star activity for result method to Set captured image on image view after click.
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 7 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri uri = data.getData();
            try {
                // Adding captured image in bitmap.
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                // adding captured image in imageview.
                ImageViewHolder.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // Requesting runtime permission to access camera.
    public void EnableRuntimePermissionToAccessCamera(){
        if (ActivityCompat.shouldShowRequestPermissionRationale(UploadItem.this,
                Manifest.permission.CAMERA))
        {
            // Printing toast message after enabling runtime permission.
            Toast.makeText(UploadItem.this,"CAMERA permission allows us to Access CAMERA app", Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(UploadItem.this,new String[]{Manifest.permission.CAMERA}, RequestPermissionCode);
        }
    }

    // Upload captured image online on server function.
    public void ImageUploadToServerFunction(){
        ByteArrayOutputStream byteArrayOutputStreamObject ;
        byteArrayOutputStreamObject = new ByteArrayOutputStream();

        // Converting bitmap image to jpeg format, so by default image will upload in jpeg format.
        bitmap.compress(Bitmap.CompressFormat.JPEG, 40, byteArrayOutputStreamObject);

        byte[] byteArrayVar = byteArrayOutputStreamObject.toByteArray();
        final String ConvertImage = Base64.encodeToString(byteArrayVar, Base64.DEFAULT);

        class AsyncTaskUploadClass extends AsyncTask<Void,Void,String> {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                // Showing progress dialog at image upload time.
                progressDialog = ProgressDialog.show(UploadItem.this,"Item is Uploading","Please Wait",false,false);
            }

            @Override
            protected void onPostExecute(String string1) {
                super.onPostExecute(string1);
                // Dismiss the progress dialog after done uploading.
                progressDialog.dismiss();

                // Printing uploading success message coming from server on android app.
                Toast.makeText(UploadItem.this,"Uploaded Successfully",Toast.LENGTH_LONG).show();

                // Setting image as transparent after done uploading.
                ImageViewHolder.setImageResource(android.R.color.transparent);
            }

            @Override
            protected String doInBackground(Void... params) {
                ImageProcessClass imageProcessClass = new ImageProcessClass();
                HashMap<String,String> HashMapParams = new HashMap<String,String>();
                HashMapParams.put(ImageNameFieldOnServer, GetImageNameFromEditText);
                HashMapParams.put(ImagePathFieldOnServer, ConvertImage);
                HashMapParams.put("cost",cost_value);
                HashMapParams.put("merch",pref.getString("loginMer","HD"));
                String FinalData = imageProcessClass.ImageHttpRequest(ImageUploadPathOnSever, HashMapParams);
                return FinalData;
            }
        }
        AsyncTaskUploadClass AsyncTaskUploadClassOBJ = new AsyncTaskUploadClass();
        AsyncTaskUploadClassOBJ.execute();
    }

    public class ImageProcessClass{

        public String ImageHttpRequest(String requestURL,HashMap<String, String> PData) {
            StringBuilder stringBuilder = new StringBuilder();

            try {
                URL url;
                HttpURLConnection httpURLConnectionObject ;
                OutputStream OutPutStream;
                BufferedWriter bufferedWriterObject ;
                BufferedReader bufferedReaderObject ;
                int RC ;
                url = new URL(requestURL);
                httpURLConnectionObject = (HttpURLConnection) url.openConnection();
                httpURLConnectionObject.setReadTimeout(19000);
                httpURLConnectionObject.setConnectTimeout(19000);
                httpURLConnectionObject.setRequestMethod("POST");
                httpURLConnectionObject.setDoInput(true);
                httpURLConnectionObject.setDoOutput(true);
                OutPutStream = httpURLConnectionObject.getOutputStream();
                bufferedWriterObject = new BufferedWriter(
                        new OutputStreamWriter(OutPutStream, "UTF-8"));
                bufferedWriterObject.write(bufferedWriterDataFN(PData));
                bufferedWriterObject.flush();
                bufferedWriterObject.close();
                OutPutStream.close();
                RC = httpURLConnectionObject.getResponseCode();
                if (RC == HttpsURLConnection.HTTP_OK) {
                    bufferedReaderObject = new BufferedReader(new InputStreamReader(httpURLConnectionObject.getInputStream()));
                    stringBuilder = new StringBuilder();
                    String RC2;
                    while ((RC2 = bufferedReaderObject.readLine()) != null){
                        stringBuilder.append(RC2);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return stringBuilder.toString();
        }

        private String bufferedWriterDataFN(HashMap<String, String> HashMapParams) throws UnsupportedEncodingException {
            StringBuilder stringBuilderObject;
            stringBuilderObject = new StringBuilder();
            for (Map.Entry<String, String> KEY : HashMapParams.entrySet()) {
                if (check)
                    check = false;
                else
                    stringBuilderObject.append("&");
                stringBuilderObject.append(URLEncoder.encode(KEY.getKey(), "UTF-8"));
                stringBuilderObject.append("=");
                stringBuilderObject.append(URLEncoder.encode(KEY.getValue(), "UTF-8"));
            }
            return stringBuilderObject.toString();
        }
    }

    @Override
    public void onRequestPermissionsResult(int RC, String per[], int[] PResult) {
        switch (RC) {
            case RequestPermissionCode:
                if (PResult.length > 0 && PResult[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(UploadItem.this,"Permission Granted, Now your application can access CAMERA.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(UploadItem.this,"Permission Canceled, Now your application cannot access CAMERA.", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.more_tab_menu, menu);

        // return true so that the menu pop up is opened
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId())
        {
            case R.id.itemList:
                Intent in =new Intent(getApplicationContext(),ItemList.class);
                startActivity(in);
                break;
            case R.id.exit:
                break;
        }
        return true;
    }

    //http://uds.atwebpages.com/PhotoUpload/uploads/4.png
    //https://www.ndtv.com/cooks/images/Honey.chilli.potatoes.jpg
}