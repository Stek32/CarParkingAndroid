package com.example.stek3.carparking.Repository;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.example.stek3.carparking.Adapters.NearByParksAdapter;
import com.example.stek3.carparking.Fragments.Contacts_Fragment;
import com.example.stek3.carparking.Interfaces.ParkRepoResponse;
import com.example.stek3.carparking.ParkingMaps;
import com.example.stek3.carparking.Parks.Park;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.zip.Inflater;

import com.example.stek3.carparking.Place_Info;
import com.example.stek3.carparking.Processors.ImageDecoder;
import com.example.stek3.carparking.R;
import com.example.stek3.carparking.Sync.*;
/**
 * Created by Stek3 on 14-Mar-18.
 */

public class ParkRepo
{

    Park park = new Park();
    Park finalPark;
    ParkingMaps parkingMaps;
    public static List<Park> NearByParks;
    List<Park> MyParks=new ArrayList<>();

    public ParkRepo() {



    }

    public boolean SynchronizeParks(Context context) {

        SyncParks syncParks=new SyncParks(context);
        syncParks.execute();

        return  true;
    }

    private class SyncParks extends AsyncTask<Void, Integer, List<Park>> {

        Context context;

        public SyncParks(Context context) {

            this.context=context;
        }

        @Override
        protected List<Park> doInBackground(Void... params) {

            List<Park> Parks = new ArrayList<>();
            Park finalPark;

            HttpURLConnection Con = null;
            JSONObject ParkObject = new JSONObject();
            StringBuilder sb = new StringBuilder();

            Log.e("Run", "Running");

            try {

                URL url = new URL("http://192.168.43.164/dcss/api/Parking/Parks?lat=0.347596&lng=32.582520");
                Con = (HttpURLConnection) url.openConnection();

                Con.setRequestMethod("GET");
                Con.setRequestProperty("Content-Type", "application/json");
                Con.setRequestProperty("Accept", "application/json");

                //OutputStreamWriter outputStreamWriter = new OutputStreamWriter(Con.getOutputStream());
                //outputStreamWriter.write(ParkObject.toString());
                //outputStreamWriter.flush();

                Log.e("Response Status: ", Con.getResponseMessage());

                sb = new StringBuilder();
                if (Con.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    Log.e("Run", "Running");
                    InputStreamReader streamReader = new InputStreamReader(Con.getInputStream());
                    BufferedReader bufferedReader = new BufferedReader(streamReader);
                    String response = null;
                    while ((response = bufferedReader.readLine()) != null) {
                        sb.append(response);
                    }
                    bufferedReader.close();

                    Log.e("My Parks JSON:", sb.toString());

                    //Configure JSON DATA Here

                    JSONObject Data = new JSONObject(sb.toString());
                    JSONArray DataValues = Data.getJSONArray("values");


                    for (int count = 0; count < DataValues.length(); count++) {
                        JSONObject FinalParkObject = DataValues.getJSONObject(count);

                        finalPark=new Park();

                        finalPark.setName(FinalParkObject.getString("name"));
                        finalPark.setReference(FinalParkObject.getString("reference"));
                        finalPark.setPlaceID(FinalParkObject.getString("place_id"));


                        JSONObject ParkData = FinalParkObject.getJSONObject("Park Data");

                        finalPark.setID(ParkData.getInt("park_id"));

                        ImageDecoder imageDecoder=new ImageDecoder();
                        finalPark.setImage(imageDecoder.DecodeImage(FinalParkObject.getString("place image")));

                        Log.e("Final Image: ", FinalParkObject.getString("place image"));

                        JSONObject FinalPlaceObject = ParkData.getJSONObject("Location");

                        Park.Location finalParkLocation = new Park.Location();
                        finalParkLocation.setVicinity(FinalPlaceObject.getString("name"));
                        finalParkLocation.setLocationID(FinalPlaceObject.getInt("id"));
                        finalParkLocation.setLatitude(FinalParkObject.getJSONObject("geometry").getJSONObject("location").getDouble("lat"));
                        finalParkLocation.setLongitude(FinalParkObject.getJSONObject("geometry").getJSONObject("location").getDouble("lng"));


                        JSONObject FinalSpaceObject = ParkData.getJSONObject("Space");

                        Park.Spaces finalParkSpace = new Park.Spaces();
                        finalParkSpace.setSpaceID(FinalSpaceObject.getInt("space id"));
                        finalParkSpace.setSpaceCount(FinalSpaceObject.getInt("space count"));
                        finalParkSpace.setUsedSpaces(FinalSpaceObject.getInt("used spaces"));

                        finalPark.setLocation(finalParkLocation);
                        finalPark.setSpaces(finalParkSpace);


                        Log.e("re", finalPark.getName());

                        Parks.add(finalPark);
                    }

                } else {
                    Log.e("testResponse", Con.getResponseMessage() + " " + String.valueOf(Con.getResponseCode()));
                    return null;
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                if (Con != null) {
                    Con.disconnect();
                }
            }

            NearByParks=new ArrayList<>();

            NearByParks=Parks;
            return Parks;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);


            Log.e("Progress",String.valueOf(values[0]));

        }

        @Override
        protected void onPostExecute(List<Park> resultPark) {
            super.onPostExecute(resultPark);

            ProgressBar pgb=(ProgressBar)((Activity)context).findViewById(R.id.loadlistprogress);

            if(pgb!=null)
            {

                pgb.setVisibility(View.GONE);
            }

            ListView lv = (ListView) ((Activity)context).findViewById(R.id.nearByParkingList);
            Park[] parks = new Park[(resultPark.size())];

            int i=0;
            for (Park p:resultPark)
            {

                parks[i]=p;

                i++;

            }

            NearByParksAdapter npa = new NearByParksAdapter(context, parks);
            lv.setAdapter(npa);
            lv.setNestedScrollingEnabled(true);

            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Park SelectedPark = ParkRepo.NearByParks.get(position);

                    Intent intent = new Intent(((Activity) context), Place_Info.class);
                    intent.putExtra("SelectedPark", SelectedPark.getID());
                    ((Activity) context).startActivity(intent);
                }
            });


        }
    }

}
