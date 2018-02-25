package com.example.himanshudhanwant.uds;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

public class ConnectionError extends AppCompatActivity {

    ImageView iv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connection_error);

        iv=(ImageView)findViewById(R.id.error);
        iv.setImageResource(R.drawable.nointernet_r_2x);
        iv.setScaleType(ImageView.ScaleType.FIT_XY);
    }
}
