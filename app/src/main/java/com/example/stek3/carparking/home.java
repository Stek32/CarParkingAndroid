package com.example.stek3.carparking;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.stek3.carparking.Adapters.NearByParksAdapter;
import com.example.stek3.carparking.Parks.Park;
import com.example.stek3.carparking.Repository.ParkRepo;
import com.example.stek3.carparking.Repository.UserRepo;
import com.example.stek3.carparking.SQLite.DbHelper;
import com.example.stek3.carparking.Sync.Sync;
import com.example.stek3.carparking.Sync.SyncParks;
import com.example.stek3.carparking.Views.ExtendedPlaces_View;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.zip.Inflater;

public class home extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,OnMapReadyCallback
{
    private static final int PLACE_PICKER_REQUEST =1 ;

    public final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION =1;

    boolean ExpandedList_Opened=false;
    boolean mLocationPermissionGranted=false,LocationRetrieved=false;

    ParkingMaps parkingMaps;
    ExtendedPlaces_View extendedPlaces_view;
    MapFragment mapFragment;
    CoordinatorLayout coordinatorLayout;
    View ExpandedList;

    TextView NameView,LocationView,UserNameView,EmailView;
    LinearLayout HomeContainer;

    private BottomSheetBehavior mBottomSheetBehavior;


    private String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line).append('\n');
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return sb.toString();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        NameView=(TextView)findViewById(R.id.nameView);
        UserNameView=(TextView)findViewById(R.id.userNameView);
        LocationView=(TextView)findViewById(R.id.locationView);



        parkingMaps = new ParkingMaps(this);

        mapFragment = (MapFragment) getFragmentManager()
              .findFragmentById(R.id.fragment);
        mapFragment.getMapAsync(this);

        Sync s=new Sync(this);
        s.execute();

        //mapFragment.getMapAsync(home.this);

        ParkRepo parkRepo=new ParkRepo();
        parkRepo.SynchronizeParks(this);


        BackgroundWorker worker=new BackgroundWorker(this);
        worker.execute();


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();


            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);



    }

    private void GetNearByPlaces(List<Park> source){
        while (source == null){

            GetNearByPlaces(source);
        }


    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(data, this);
                String toastMsg = String.format("Place: %s", place.getName());
                Toast.makeText(this, toastMsg, Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        else if(ExpandedList_Opened){

            ExpandedList_Opened=extendedPlaces_view.HideNearByPlaces();
        }
        else {
          //  super.onBackPressed();
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if(id == R.id.nearbyParking){

            if(ExpandedList_Opened==false)
            {

                extendedPlaces_view=new ExtendedPlaces_View(this);
                ExpandedList_Opened=extendedPlaces_view.ShowNearByPlaces();

            }

        }
        else if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        parkingMaps=new ParkingMaps();

        parkingMaps.setmMap(googleMap);

        parkingMaps.getDeviceLocation();
        parkingMaps.updateLocationUI();

        if(parkingMaps.getNearbyPlacesList()!=null)
        {
            for (int i = 0; i < parkingMaps.getNearbyPlacesList().size(); i++)
            {

                Park yard = new Park();

                HashMap<String, String> googlePlace = new HashMap<String, String>();

                yard.setName(googlePlace.get("place_name"));


            }



        }

        else
        {



        }
    }

    public void DisplayMapUI()
        {


        }



    private class BackgroundWorker extends AsyncTask<Void,Integer,Users>{

        Context context;

        public  BackgroundWorker(Context context){

            this.context=context;
        }

    Users user;

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    @Override
    protected Users doInBackground(Void... params) {

        UserRepo repo=new UserRepo(context);

        Users User=new Users();

        DbHelper dbHelper=new DbHelper(context);

        if(dbHelper.getUser()!=null) {
            User = dbHelper.getUser();
        }
        else {
            User=new Users();
        }

        User.setLocation(parkingMaps.getCurrentPlace());

        return User;

    }


        @Override
    protected void onPostExecute(Users result){


            Log.e("from",result.getFirstName().toString());

            NameView.setText(result.getFirstName() + " " + result.getLastName());
            UserNameView.setText(result.getUserName());


            UserNameView = (TextView) findViewById(R.id.headerUserNameView);
            UserNameView.setText(result.getUserName());

            EmailView = (TextView) findViewById(R.id.headerEmailView);
            EmailView.setText(result.getEmail());

            LocationView=(TextView) findViewById(R.id.locationView);
            LocationView.setText(result.getLocation());

    }
}

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        mLocationPermissionGranted = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true;
                }

            }
        }

    }



}