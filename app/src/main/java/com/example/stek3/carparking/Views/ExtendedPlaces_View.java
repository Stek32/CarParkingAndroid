package com.example.stek3.carparking.Views;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.CoordinatorLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.stek3.carparking.Adapters.NearByParksListAdapter;
import com.example.stek3.carparking.Place_Info;
import com.example.stek3.carparking.ParkingMaps;
import com.example.stek3.carparking.Parks.Park;
import com.example.stek3.carparking.R;
import com.example.stek3.carparking.Repository.ParkRepo;

/**
 * Created by Stek3 on 16-Mar-18.
 */

public class ExtendedPlaces_View extends View {

    View ExpandedList;
    Context context;
    CoordinatorLayout coordinatorLayout;

    public ExtendedPlaces_View(Context context) {
        super(context);
        this.context=context;

        LayoutInflater inflater= (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        ExpandedList=inflater.inflate(R.layout.nearbyplaceslist,null);

    }

    public boolean ShowNearByPlaces(){

        if(ExpandedList!=null) {

            final ListView ExpandedListView = (ListView) ExpandedList.findViewById(R.id.ExpandedNearByPlaces);

            if (ParkRepo.NearByParks != null) {
                Park[] parks = new Park[ParkRepo.NearByParks.size()];

                int count = 0;

                for (Park park : ParkRepo.NearByParks) {
                    parks[count] = park;
                    count++;
                }

                NearByParksListAdapter nearByParksListAdapter = new NearByParksListAdapter(context, parks);

                ExpandedListView.setAdapter(nearByParksListAdapter);
            }
                coordinatorLayout = (CoordinatorLayout) ((Activity) context).findViewById(R.id.bgLayout);

                coordinatorLayout.addView(ExpandedList);

                ExpandedListView.setClickable(true);

                ExpandedListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        Park SelectedPark = ParkRepo.NearByParks.get(position);

                        Intent intent = new Intent(((Activity) context), Place_Info.class);
                        intent.putExtra("SelectedPark", SelectedPark.getID());
                        ((Activity) context).startActivity(intent);

                        //Toast.makeText(ExpandedListView.getContext(),"Hello ID: "+String.valueOf(view.getId()),Toast.LENGTH_LONG).show();

                    }
                });

            }




        return true;
    }

    public boolean HideNearByPlaces(){

        coordinatorLayout.removeView(ExpandedList);

        return false;
    }


}
