/*
 * Copyright (c) 2017. Truiton (http://www.truiton.com/).
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

package com.example.himanshudhanwant.uds;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class ItemTwoFragment extends Fragment {

    TextView tv1,tv2,tv3,tv4,tv5;

    SharedPreferences pref;
    public static ItemTwoFragment newInstance() {
        ItemTwoFragment fragment = new ItemTwoFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pref = PreferenceManager
                .getDefaultSharedPreferences(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v= inflater.inflate(R.layout.fragment_item_two, container, false);

        tv1= (TextView)v.findViewById(R.id.tv1);
        tv2= (TextView)v.findViewById(R.id.tv2);
        tv3= (TextView)v.findViewById(R.id.tv3);
        tv4= (TextView)v.findViewById(R.id.tv4);
        tv5= (TextView)v.findViewById(R.id.tv5);

        tv1.setText(pref.getString("loginName","Rakesh Bhaiya"));
        tv2.setText(pref.getString("loginMail","Kya kroge jaanke"));
        tv3.setText(pref.getString("loginPhone","9211420100"));
        tv4.setText(pref.getString("loginMer","HD"));
        tv5.setText(pref.getString("loginMer","Goldy Bhai ki Shop"));
        return v;
    }
}
