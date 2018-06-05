package com.example.stek3.carparking.Fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.stek3.carparking.Parks.Park;
import com.example.stek3.carparking.Place_Info;
import com.example.stek3.carparking.R;
import com.google.android.gms.location.places.Place;

/**
 * Created by Stek3 on 22-Mar-18.
 */

public class Places_fragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.places_fragment_layout,container,false);

        TextView SpaceCount=(TextView)view.findViewById(R.id.CountView);
        Log.e("Spacees",String.valueOf(Place_Info.Park_Info.getSpaces().getUsedSpaces()));

       if(Place_Info.Park_Info.getSpaces()!=null) {
           SpaceCount.setText(String.valueOf(Place_Info.Park_Info.getSpaces().getUsedSpaces()) + " Used of " + String.valueOf(Place_Info.Park_Info.getSpaces().getSpaceCount()));
       }

        return view;



    }


}
