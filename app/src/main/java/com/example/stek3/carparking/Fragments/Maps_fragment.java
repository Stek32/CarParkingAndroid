package com.example.stek3.carparking.Fragments;

import android.app.Activity;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.example.stek3.carparking.ParkingMaps;
import com.example.stek3.carparking.Parks.Park;
import com.example.stek3.carparking.Place_Info;
import com.example.stek3.carparking.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by Stek3 on 22-Mar-18.
 */

public class Maps_fragment extends Fragment implements OnMapReadyCallback {

    ParkingMaps parkingMaps;
    MapFragment mapFragment;
    private static View view;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        if (view != null)
        {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null)
                parent.removeView(view);
        }

        try
        {

            view = inflater.inflate(R.layout.maps_fragment_layout, container, false);

            parkingMaps = new ParkingMaps(view.getContext());

            mapFragment = new MapFragment();
            mapFragment = (MapFragment) ((Activity) view.getContext()).getFragmentManager().findFragmentById(R.id.mapFragment);
            mapFragment.getMapAsync(this);
        }

        catch (Exception ex){

        }

        Switch routeSwitch=(Switch)view.findViewById(R.id.routeSwitch);
        routeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isOn) {

                if(isOn)
                {

                    buttonView.setText("ON");
                }
                else
                {
                    parkingMaps.closePakingRoute();
                    buttonView.setText("OFF");
                }



            }

        });

        return view;

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {

        parkingMaps.setmMap(googleMap);

        parkingMaps.updateLocationUI();

        parkingMaps.getDeviceLocation();

        BitmapDescriptor ParkIcon= BitmapDescriptorFactory.fromResource(R.mipmap.ic_parking);


        googleMap.addMarker(new MarkerOptions().position(new LatLng(Place_Info.Park_Info.getLocation().getLatitude(),Place_Info.Park_Info.getLocation().getLongitude()
        )).title(Place_Info.Park_Info.getName()).icon(ParkIcon));

if(Place_Info.Park_Info.getLocation().getRoute()!=null){

    Log.e("Park Line options", Place_Info.Park_Info.getName());
    googleMap.addPolyline(Place_Info.Park_Info.getLocation().getRoute());

}
else {
    Log.e("Park Line Options","Is Null");
}



        //parkingMaps.showCurrentPlace();



    }


}
