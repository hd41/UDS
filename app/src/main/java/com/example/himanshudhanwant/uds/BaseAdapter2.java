/*
 * Copyright (c) 2018. Truiton (http://www.truiton.com/).
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

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
public class BaseAdapter2 extends BaseAdapter {

    private Activity activity;
    // private ArrayList&lt;HashMap&lt;String, String&gt;&gt; data;
    private static ArrayList title,notice,tick1;
    private static LayoutInflater inflater = null;

    public BaseAdapter2(Activity a, ArrayList b, ArrayList bod, ArrayList tick) {
        activity = a;
        this.title = b;
        this.notice=bod;
        this.tick1=tick;

        inflater = (LayoutInflater) activity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    public int getCount() {
        return title.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        if (convertView == null)
            vi = inflater.inflate(R.layout.layout, null);

        Log.d("test",title.get(position)+" "+notice.get(position)+" "+tick1.get(position));

        TextView title2 = (TextView) vi.findViewById(R.id.row); // title
        String song = title.get(position).toString();
        title2.setText(song);


        TextView title22 = (TextView) vi.findViewById(R.id.row2); // notice
        String song2 = notice.get(position).toString();
        title22.setText(song2);

        ImageView iv=(ImageView) vi.findViewById(R.id.imageView);//image
        int kb = (Integer)tick1.get(position);
        Log.d("babes(o)",""+position+"="+kb);
        if(kb==1){
            iv.setImageResource(R.drawable.tick);
        }
        if(kb==0){
            iv.setImageResource(0);
        }
        return vi;
    }
}