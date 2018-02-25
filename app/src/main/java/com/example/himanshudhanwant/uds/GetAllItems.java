package com.example.himanshudhanwant.uds;


import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class GetAllItems {

    public static int[] Ids;
    public static String[] imageURLs;
    public static String[] itemNames;
    public static String[] itemCosts;
    public static String[] itemMerchant;
//    public static Bitmap[] bitmaps;

    public static ArrayList<String> merchants=new ArrayList<String>();

    public static final String JSON_ARRAY="result";
    public static final String ITEM_ID="id";
    public static final String IMAGE_URL = "url";
    public static final String ITEM_NAME = "name";
    public static final String ITEM_COST = "cost";
    public static final String MERCHANT = "merchant";
    private String json;
    private JSONArray urls;

    public GetAllItems(String json){
        this.json = json;
        try {
            JSONObject jsonObject = new JSONObject(json);
            urls = jsonObject.getJSONArray(JSON_ARRAY);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public GetAllItems(){
        //mauja maaro
    }

    public String getAllUrls(int pos){
        return imageURLs[pos];
    }

    public void putMerchants(ArrayList<String> a){
        merchants=a;
    }
    public String[] getMerchants(){
        String[] tmp= new String[merchants.size()];
        for(int i=0;i<merchants.size();i++){
            tmp[i]=merchants.get(i);
        }
        return tmp;
    }

    private Bitmap getImage(JSONObject jo){
        URL url = null;
        Bitmap image = null;
        try {
            url = new URL(jo.getString(IMAGE_URL));

            image = BitmapFactory.decodeStream(url.openConnection().getInputStream());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return image;
    }

    public void getAllImages() throws JSONException {
//        bitmaps = new Bitmap[urls.length()];
        Ids=new int[urls.length()];
        imageURLs = new String[urls.length()];
        itemNames= new String[urls.length()];
        itemCosts= new String[urls.length()];
        itemMerchant = new String[urls.length()];

        for(int i=0;i<urls.length();i++){
            Ids[i]=urls.getJSONObject(i).getInt(ITEM_ID);
            imageURLs[i] = urls.getJSONObject(i).getString(IMAGE_URL);
            itemNames[i]=urls.getJSONObject(i).getString(ITEM_NAME);
            itemCosts[i]=urls.getJSONObject(i).getString(ITEM_COST);
            itemMerchant[i]=urls.getJSONObject(i).getString(MERCHANT);
            Log.d("test",imageURLs[i]+" : "+itemNames[i]+" : "+itemCosts[i]);
            JSONObject jsonObject = urls.getJSONObject(i);
//            bitmaps[i]=getImage(jsonObject);
        }
    }
}