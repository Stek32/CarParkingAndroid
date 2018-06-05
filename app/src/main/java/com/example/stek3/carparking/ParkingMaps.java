package com.example.stek3.carparking;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Location;
import android.media.Image;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.stek3.carparking.Adapters.NearByParksAdapter;
import com.example.stek3.carparking.Parks.Park;
import com.example.stek3.carparking.Processors.RouteDataParser;
import com.example.stek3.carparking.Processors.RouteDataParserTask;
import com.example.stek3.carparking.Repository.ParkRepo;
import com.example.stek3.carparking.Processors.DataParser;
import com.example.stek3.carparking.Processors.DownloadUrl;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.PlaceDetectionClient;
import com.google.android.gms.location.places.PlaceLikelihood;
import com.google.android.gms.location.places.PlaceLikelihoodBufferResponse;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by Stek3 on 06-Mar-18.
 */

public class ParkingMaps {

    private static final float DEFAULT_ZOOM = 16;
    private static final int M_MAX_ENTRIES = 5;
    public final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION =1;
    private int PROXIMITY_RADIUS = 10000 ;
    private final String API_KEY= "AIzaSyBG8KUYh-_Zm7iEjPD5VPrI90fz6QTrAEY";
    public static  String Tester;
    private static Location CurrentLocation;
    private Context mContext;

    private GoogleMap mMap;

    GeoDataClient mGeoDataClient;
    PlaceDetectionClient mPlaceDetectionClient;
    FusedLocationProviderClient mFusedLocationProviderClient;
    boolean mLocationPermissionGranted=false;
    private Location mLastKnownLocation,mDefaultLocation;

    Park park=new Park();

    public static List<Park> NearByParks =new ArrayList<Park>();
    private static String currentPlace;
    private String URLResult;
    private List<HashMap<String, String>> nearbyPlacesList;

    public List<HashMap<String, String>> getNearbyPlacesList() {
        return nearbyPlacesList;
    }

    public void setNearbyPlacesList(List<HashMap<String, String>> nearbyPlacesList) {
        this.nearbyPlacesList = nearbyPlacesList;
    }

    public Location getCurrentLocation() {
        return CurrentLocation;
    }

    public void setCurrentLocation(Location currentLocation) {
        this.CurrentLocation = currentLocation;
    }

    public void setCurrentPlace(String currentPlace) {
        this.currentPlace = currentPlace;
    }



    public ParkingMaps(Context context){

        if (ContextCompat.checkSelfPermission(context,Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            mLocationPermissionGranted=false;
            ActivityCompat.requestPermissions((Activity)context,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);

        }
        else {
            mLocationPermissionGranted = true;


            mContext = context;

            // Construct a GeoDataClient.
            mGeoDataClient = Places.getGeoDataClient(context, null);

            // Construct a PlaceDetectionClient.
            mPlaceDetectionClient = Places.getPlaceDetectionClient(context, null);

            // Construct a FusedLocationProviderClient.
            mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context);
        }

    }

    public ParkingMaps(){

    }

    public GoogleMap getmMap() {
        return mMap;
    }

    public void setmMap(GoogleMap mMap) {
        this.mMap = mMap;
    }



    public void updateLocationUI() {
        if (mMap == null) {
            return;
        }
        else {
            try {
                if (mLocationPermissionGranted) {
                    mMap.setMyLocationEnabled(true);
                    mMap.getUiSettings().setMyLocationButtonEnabled(true);

                } else {
                    mMap.setMyLocationEnabled(false);
                    mMap.getUiSettings().setMyLocationButtonEnabled(false);
                    mLastKnownLocation = null;
                    //getLocationPermission();
                }
            } catch (SecurityException e) {
                Log.e("Exception: %s", e.getMessage());
            }
        }
    }

    public void getDeviceLocation() {
   
        try {
            if (mLocationPermissionGranted)
            {
                Task locationResult = mFusedLocationProviderClient.getLastLocation();

                locationResult.addOnCompleteListener((Activity) mContext,new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {

                        if (task.isSuccessful()) {
                            // Set the map's camera position to the current location of the device.
                            mLastKnownLocation = (Location) task.getResult();
                            setCurrentLocation(mLastKnownLocation);
                            if(mMap!=null) {

                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                        new LatLng(mLastKnownLocation.getLatitude(),
                                                mLastKnownLocation.getLongitude()), DEFAULT_ZOOM));
                                mMap.addMarker(new MarkerOptions().position(new LatLng(mLastKnownLocation.getLatitude(), mLastKnownLocation.getLongitude())));
                                setCurrentLocation(mLastKnownLocation);
                                mMap.getUiSettings().setMyLocationButtonEnabled(true);
                            }

                        }

                        else
                            {
                            Log.d("TAG", "Current location is null. Using defaults.");
                            Log.e("TAG", "Exception: %s", task.getException());
                                if(mMap!=null) {
                                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mDefaultLocation.getLatitude(), mDefaultLocation.getLongitude()), DEFAULT_ZOOM));
                                    mMap.getUiSettings().setMyLocationButtonEnabled(false);
                                    mMap.addMarker(new MarkerOptions().position(new LatLng(mDefaultLocation.getLatitude(), mDefaultLocation.getLongitude())));
                                    setCurrentLocation(mDefaultLocation);
                                    mMap.getUiSettings().setMyLocationButtonEnabled(true);
                                }
                                }





                    }
                });


            }
        } catch(SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    public String getCurrentPlace(){

       getDeviceLocation();

        if (mLocationPermissionGranted) {
            // Get the likely places - that is, the businesses and other points of interest that
            // are the best match for the device's current location.
            @SuppressWarnings("MissingPermission") final
            Task<PlaceLikelihoodBufferResponse> placeResult =
                    mPlaceDetectionClient.getCurrentPlace(null);
            placeResult.addOnCompleteListener
                    (new OnCompleteListener<PlaceLikelihoodBufferResponse>() {
                        @Override
                        public void onComplete(@NonNull Task<PlaceLikelihoodBufferResponse> task) {
                            if (task.isSuccessful() && task.getResult() != null) {
                                PlaceLikelihoodBufferResponse likelyPlaces = task.getResult();

                                // Set the count, handling cases where less than 5 entries are returned.
                                int count;
                                if (likelyPlaces.getCount() < M_MAX_ENTRIES) {
                                    count = likelyPlaces.getCount();
                                } else {
                                    count = M_MAX_ENTRIES;
                                }

                                int i = 0;
                                String[] mLikelyPlaceNames = new String[count];
                                String[] mLikelyPlaceAddresses = new String[count];
                                String[] mLikelyPlaceAttributions = new String[count];
                                LatLng[] mLikelyPlaceLatLngs = new LatLng[count];

                                for (PlaceLikelihood placeLikelihood : likelyPlaces) {
                                    // Build a list of likely places to show the user.
                                    mLikelyPlaceNames[i] = (String) placeLikelihood.getPlace().getName();
                                    mLikelyPlaceAddresses[i] = (String) placeLikelihood.getPlace()
                                            .getAddress();
                                    mLikelyPlaceAttributions[i] = (String) placeLikelihood.getPlace()
                                            .getAttributions();
                                    mLikelyPlaceLatLngs[i] = placeLikelihood.getPlace().getLatLng();

                                    i++;
                                    if (i > (count - 1)) {
                                        break;
                                    }
                                }


                                // Release the place likelihood buffer, to avoid memory leaks.
                                likelyPlaces.release();


                                setCurrentPlace(mLikelyPlaceNames[0]);

                                    Tester=mLikelyPlaceNames[0];

                                TextView LocationView=(TextView)((Activity)mContext).findViewById(R.id.locationView);

                                if(LocationView!=null)
                                {

                                    LocationView.setText(mLikelyPlaceAddresses[0]);

                                }


                            } else {
                                Log.e("TAG", "Exception: %s", task.getException());
                            }

                        }
                    });

            Log.e("Tester",String.valueOf(Tester));
        }

        else {
            // The user has not granted permission.
            Log.i("TAG", "The user did not grant location permission.");


        }

        return currentPlace;
}

    public void showCurrentPlace() {
        CurrentPlace cp=new CurrentPlace();
        cp.execute();

    }

    public void closePakingRoute() {

    }

    //public double getOverallDistance() {}

//    public List<HashMap<String,String>> getRouteDistances() throws ExecutionException, InterruptedException {
//
//        if(URLResult==null){
//
//        }
//
//        RouteDataParserTask parserTask=new RouteDataParserTask(getmMap());
//        List<List<HashMap<String, String>>> ListResult= parserTask.execute(URLResult).get();
//
//
//
//
//    }

    public PolylineOptions getParkingRoute(Park.Location Origin,Park.Location Destination) throws ExecutionException, InterruptedException {

        Park.Location [] RoutePoints=new Park.Location[2];
        RoutePoints[0]=Origin;
        RoutePoints[1]=Destination;

        ParkingRoute route=new ParkingRoute(mContext);
        URLResult=route.execute(RoutePoints).get();

        RouteDataParserTask parserTask=new RouteDataParserTask(getmMap());
        List<List<HashMap<String, String>>> ListResult= parserTask.execute(URLResult).get();

        for (String Direction:GetDirections())
        {
        Log.e("Direction",String.valueOf(Direction));
        }

        ArrayList<LatLng> points;
        PolylineOptions lineOptions = null;

        // Traversing through all the routes
        for (int i = 0; i < ListResult.size(); i++) {
            points = new ArrayList<>();
            lineOptions = new PolylineOptions();

            // Fetching i-th route
            List<HashMap<String, String>> path = ListResult.get(i);

            // Fetching all the points in i-th route
            for (int j = 0; j < path.size(); j++) {
                HashMap<String, String> point = path.get(j);

                if(point!=null && !point.containsKey("Direction")) {
                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);

                    points.add(position);
                }
            }


            // Adding all the points in the route to LineOptions
            lineOptions.addAll(points);
            lineOptions.width(10);
            lineOptions.color(Color.BLUE);
        }

       return lineOptions;

    }

    private String getUrl(double latitude, double longitude, String nearbyPlace) {

       //longitude=-122.3212928;
        //latitude=37.4018118;

        StringBuilder googlePlacesUrl = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        googlePlacesUrl.append("location=" + latitude + "," + longitude);
        googlePlacesUrl.append("&radius=" + PROXIMITY_RADIUS);
        googlePlacesUrl.append("&type=" + nearbyPlace);
        googlePlacesUrl.append("&sensor=true");
        googlePlacesUrl.append("&key=" + "AIzaSyBG8KUYh-_Zm7iEjPD5VPrI90fz6QTrAEY");
        Log.d("getUrl", googlePlacesUrl.toString());
        return (googlePlacesUrl.toString());

    }

    public List<String> GetDirections() throws ExecutionException, InterruptedException {

        RouteDataParserTask parserTask=new RouteDataParserTask(getmMap());
        List<List<HashMap<String, String>>> ListResult= parserTask.execute(URLResult).get();

        List<String> res=new ArrayList<>();

        for (List<HashMap<String,String>> L:ListResult) {

            for (HashMap<String,String> Direction:L)
            {
                if(Direction.containsKey("Direction"))
                {
                    res.add(Direction.get("Direction"));
                }
                }
        }

        return res;

    }

    private void getLocationPermission() {
    /*
     * Request location permission, so that we can get the location of the
     * device. The result of the permission request is handled by a callback,
     * onRequestPermissionsResult.
     */
        if (ContextCompat.checkSelfPermission(mContext,
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions((Activity)mContext,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    public class CurrentPlace extends AsyncTask<Void,Void,Void>{

        @Override
        protected Void doInBackground(Void... params) {

            if (mMap == null) {
                return null;
            }

            getDeviceLocation();

            if (mLocationPermissionGranted) {
                // Get the likely places - that is, the businesses and other points of interest that
                // are the best match for the device's current location.
                @SuppressWarnings("MissingPermission") final
                Task<PlaceLikelihoodBufferResponse> placeResult =
                        mPlaceDetectionClient.getCurrentPlace(null);
                placeResult.addOnCompleteListener
                        (new OnCompleteListener<PlaceLikelihoodBufferResponse>() {
                            @Override
                            public void onComplete(@NonNull Task<PlaceLikelihoodBufferResponse> task) {
                                if (task.isSuccessful() && task.getResult() != null) {
                                    PlaceLikelihoodBufferResponse likelyPlaces = task.getResult();

                                    // Set the count, handling cases where less than 5 entries are returned.
                                    int count;
                                    if (likelyPlaces.getCount() < M_MAX_ENTRIES) {
                                        count = likelyPlaces.getCount();
                                    } else

                                    {
                                        count = M_MAX_ENTRIES;
                                    }

                                    int i = 0;
                                    String[] mLikelyPlaceNames = new String[count];
                                    String[] mLikelyPlaceAddresses = new String[count];
                                    String[] mLikelyPlaceAttributions = new String[count];
                                    LatLng[] mLikelyPlaceLatLngs = new LatLng[count];

                                    for (PlaceLikelihood placeLikelihood : likelyPlaces) {
                                        // Build a list of likely places to show the user.
                                        mLikelyPlaceNames[i] = (String) placeLikelihood.getPlace().getName();
                                        mLikelyPlaceAddresses[i] = (String) placeLikelihood.getPlace()
                                                .getAddress();
                                        mLikelyPlaceAttributions[i] = (String) placeLikelihood.getPlace()
                                                .getAttributions();
                                        mLikelyPlaceLatLngs[i] = placeLikelihood.getPlace().getLatLng();

                                        i++;
                                        if (i > (count - 1)) {
                                            break;
                                        }
                                    }
                                    // Release the place likelihood buffer, to avoid memory leaks.
                                    likelyPlaces.release();

                                    // Show a dialog offering the user the list of likely places, and add a
                                    // marker at the selected place.
                                    // openPlacesDialog();

                                    int which=0;

                                    LatLng markerLatLng = mLikelyPlaceLatLngs[which];


                                    String markerSnippet = mLikelyPlaceAddresses[which];
                                    if (mLikelyPlaceAttributions[which] != null) {
                                        markerSnippet = markerSnippet + "\n" + mLikelyPlaceAttributions[which];
                                    }

                                    //CurrentLocation.setLatitude(markerLatLng.latitude);
                                    //CurrentLocation.setLongitude(markerLatLng.longitude);

                                    mMap.addMarker(new MarkerOptions()
                                            .title(mLikelyPlaceNames[which])
                                            .position(markerLatLng)
                                            .snippet(markerSnippet));

                                    // Position the map's camera at the location of the marker.
                                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(markerLatLng,DEFAULT_ZOOM));

                                    //  mMap.clear();
                                    String url = getUrl(markerLatLng.latitude,markerLatLng.longitude,"parking");
                                    Object[] DataTransfer = new Object[2];
                                    DataTransfer[0] = mMap;
                                    DataTransfer[1] = url;
                                    GetNearbyPlacesData getNearbyPlacesData = new GetNearbyPlacesData(mContext);
                                    getNearbyPlacesData.execute(DataTransfer);


                                } else {
                                    Log.e("TAG", "Exception: %s", task.getException());
                                }
                            }
                        });
            } else {
                // The user has not granted permission.
                Log.i("TAG", "The user did not grant location permission.");


            }

            return null;
        }



    }

    public class backgroundParser extends AsyncTask<String,Void,Void>{

        @Override
        protected Void doInBackground(String... params) {

            List<HashMap<String, String>> nearbyPlacesList = null;
            DataParser dataParser = new DataParser();
            nearbyPlacesList = dataParser.parse(params[0]);
           // ShowNearbyPlaces(nearbyPlacesList);


            Log.e("Data Parsed", String.valueOf(nearbyPlacesList));

            return null;
        }
    }

    private class GetNearbyPlacesData extends AsyncTask<Object, Integer, String> {

        Context globalcontext;

        ProgressBar pgb = (ProgressBar) ((Activity) mContext).findViewById(R.id.loadlistprogress);

        public GetNearbyPlacesData(Context context) {

            globalcontext = context;
        }

        String googlePlacesData;
        GoogleMap mMap;
        String url;
        List<HashMap<String, String>> nearbyPlacesList = null;
        @Override
        protected String doInBackground(Object... params) {

            try {

                Log.d("GetNearbyPlacesData", "doInBackground entered");
                mMap = (GoogleMap) params[0];
                url = (String) params[1];
                DownloadUrl downloadUrl = new DownloadUrl();
                googlePlacesData = downloadUrl.readUrl(url);
                Log.d("GooglePlacesReadTask", "doInBackground Exit");


                DataParser dataParser = new DataParser();
                nearbyPlacesList = dataParser.parse(googlePlacesData);


                Log.e("Data Parsed", String.valueOf(nearbyPlacesList));


            } catch (Exception e) {
                Log.e("GooglePlacesReadTask", e.toString());
            }
            return googlePlacesData;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            if(pgb!=null) {
                pgb.setVisibility(View.VISIBLE);
            }
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            pgb.setProgress(values[0]);
        }

        @Override
        protected void onPostExecute(String result)
        {
            if(pgb!=null) {
                pgb.setVisibility(View.GONE);
            }
            Toast.makeText(mContext,"Getting Near By Places",Toast.LENGTH_LONG).show();

            try {
                ShowNearbyPlaces(nearbyPlacesList);
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            Log.d("GooglePlacesReadTask", "onPostExecute Entered");

            Log.d("GooglePlacesReadTask", "onPostExecute Exit");

        }


        private void ShowNearbyPlaces(List<HashMap<String, String>> nearbyPlacesList) throws ExecutionException, InterruptedException
        {
            for (HashMap<String, String> place : nearbyPlacesList)
            {
                Log.e("NearBy Place", place.get("place_name"));
            }

            Park[] parkingyards = new Park[nearbyPlacesList.size()];

            setNearbyPlacesList(nearbyPlacesList);

            for (int i = 0; i < nearbyPlacesList.size(); i++)
            {

                Log.e("onPostExecute", "Entered into showing locations");
                MarkerOptions markerOptions = new MarkerOptions();
                HashMap<String, String> googlePlace = nearbyPlacesList.get(i);
                double lat = Double.parseDouble(googlePlace.get("lat"));
                double lng = Double.parseDouble(googlePlace.get("lng"));
                String placeName = googlePlace.get("place_name");
                String vicinity = googlePlace.get("vicinity");
                String Photo = googlePlace.get("photo");
                String PlaceID = googlePlace.get("place_id");
                String PlaceReference = googlePlace.get("reference");

                LatLng latLng = new LatLng(lat, lng);
                markerOptions.position(latLng);
                markerOptions.title(placeName + " : " + vicinity);
                mMap.addMarker(markerOptions);
                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));

                Park park = new Park();
                park.setName(placeName);
                park.setPlaceID(PlaceID);
                park.setReference(PlaceReference);

                Park.Location Location = new Park.Location();
                Location.setVicinity(vicinity);
                Location.setLongitude(lng);
                Location.setLatitude(lat);

                park.setLocation(Location);

                NearByPlaceImage im = new NearByPlaceImage();

                String[] photoArr = new String[2];
                photoArr[0] = Photo;

                try {

                    Bitmap result = im.execute(photoArr).get();
                    park.setImage(result);

                }

                catch (InterruptedException e) {
                    e.printStackTrace();
                }

                catch (ExecutionException e) {

                    e.printStackTrace();
                }


                parkingyards[i] = park;
                NearByParks.add(park);

                Log.e("Options before send:",String.valueOf(park.getLocation().getRoute()));

                //move map camera
                //mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                //mMap.animateCamera(CameraUpdateFactory.zoomTo(DEFAULT_ZOOM));

            }

            Log.e("Place count", String.valueOf(parkingyards.length));

            ListView NearByPlacesListView = (ListView) ((Activity) mContext).findViewById(R.id.nearByParkingList);

            if (parkingyards != null) {

                NearByParks = new ArrayList<Park>();
                NearByParksAdapter nearByParksAdapter = new NearByParksAdapter(mContext, parkingyards);

            if(NearByPlacesListView!=null)
            {
                NearByPlacesListView.setAdapter(nearByParksAdapter);
                NearByPlacesListView.setNestedScrollingEnabled(true);
            }
                for (Park yard : parkingyards) {

                    ParkRepo parkRepo = new ParkRepo();
                    //yard=parkRepo.SynchronizePark(yard);

                  // Park.Location CurrentLocation=new Park.Location();
                   //CurrentLocation.setLatitude(getCurrentLocation().getLatitude());
                    //CurrentLocation.setLongitude(getCurrentLocation().getLongitude());

//                    Park.Location YardLocation=yard.getLocation();
                   // YardLocation.setRoute(getParkingRoute(CurrentLocation,YardLocation));

                    //yard.setLocation(YardLocation);

                    NearByParks.add(yard);

//                    Log.e("Show me: ",String.valueOf(parkRepo.SynchronizePark(yard).getSpaces().getUsedSpaces()));
                }

            }
        }


        class NearByPlaceImage extends AsyncTask<String, String, Bitmap> {

            @Override
            protected Bitmap doInBackground(String... url) {

                Image result;
                String basseurl = "https://maps.googleapis.com/maps/api/place/photo?";
                String maxWidth = "400";
                String minWidth = "400";
                String api = API_KEY;
                String photoreference = url[0];

                try {
                    URL urlConnection = new URL(basseurl + "maxwidth=" + maxWidth + "&minwidth=" + minWidth + "&photoreference=" + photoreference + "&key=" + api);

                    HttpURLConnection conn = (HttpURLConnection) urlConnection.openConnection();
                    conn.setRequestMethod("GET");

                    InputStream in = conn.getInputStream();

                    Bitmap bm = BitmapFactory.decodeStream(in);

                    return bm;

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (ProtocolException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                return null;
            }


            @Override
            protected void onPostExecute(Bitmap image) {
                super.onPostExecute(image);

                if (image != null) {

                    Log.e("Boolean Image", String.valueOf(true));
                } else {
                    Log.e("Boolean Image", String.valueOf(false));
                }

if(pgb!=null) {
    pgb.setVisibility(View.GONE);
}
            }

        }


    }

    public class ParkingRoute extends AsyncTask<Park.Location,Void,String> {

        public ParkingRoute(Context context){

            context=mContext;

        }

        @Override
        protected String doInBackground(Park.Location... params) {

            Park.Location Destination=params[1];

            DownloadUrl url=new DownloadUrl();
            String Routes= null;
            try {

                getDeviceLocation();

                Routes = url.readUrl("https://maps.googleapis.com/maps/api/directions/json?origin=" +String.valueOf(getCurrentLocation().getLatitude())+ "," + String.valueOf(getCurrentLocation().getLongitude()) + "&destination="+String.valueOf(Destination.getLatitude())+','+String.valueOf(Destination.getLongitude())+"&radius=10000&type=parking&sensor=true&key=AIzaSyBG8KUYh-_Zm7iEjPD5VPrI90fz6QTrAEY");

              // Log.e("Last Location:",String.valueOf(originlat)+String.valueOf(originlng));


            }
            catch (IOException e)
            {
                e.printStackTrace();
            }

            return Routes;
        }

        @Override
        protected void onPostExecute(String result) {




        }
    }
}


