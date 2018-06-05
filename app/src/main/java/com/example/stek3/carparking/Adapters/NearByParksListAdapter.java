package com.example.stek3.carparking.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.stek3.carparking.Parks.Park;
import com.example.stek3.carparking.R;

import java.util.zip.Inflater;

/**
 * Created by Stek3 on 15-Mar-18.
 */

public class NearByParksListAdapter extends ArrayAdapter<Park> {

     private final Context context;
     private final Park[] values;


    public NearByParksListAdapter(Context context, Park[] objects) {
        super(context,0,objects);
        this.values = objects;
        this.context = context;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        Park Yard=getItem(position);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.placedisplay_fragment,parent,false);

        convertView.setId(Yard.getID());

        TextView ParkName = (TextView) convertView.findViewById(R.id.parkName);
        ImageView ParkCaption = (ImageView) convertView.findViewById(R.id.parkCaption);
        TextView ParkLocation = (TextView) convertView.findViewById(R.id.parkLocstion);
        TextView UsedSpaceView=(TextView)convertView.findViewById(R.id.usedSpaceView);
        TextView LeftSpaceView=(TextView)convertView.findViewById(R.id.LeftSpaceView);

        ParkName.setText(Yard.getName());
        ParkLocation.setText(Yard.getLocation().getVicinity());
        ParkCaption.setImageBitmap(Yard.getImage());
        UsedSpaceView.setText(String.valueOf(Yard.getSpaces().getUsedSpaces()));
        LeftSpaceView.setText(String.valueOf((Yard.getSpaces().getSpaceCount())-(Yard.getSpaces().getUsedSpaces())));
        ParkCaption.setImageBitmap(Yard.getImage());

      //  Log.e("Image",Yard.getImage().toString());

        return  convertView;

    }
}
