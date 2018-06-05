package com.example.stek3.carparking.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringDef;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.example.stek3.carparking.Parks.Park;
import com.example.stek3.carparking.R;

import java.util.List;
import java.util.Objects;

/**
 * Created by Stek3 on 07-Mar-18.
 */



public class NearByParksAdapter extends ArrayAdapter<Park> {
    private final Context context;
    private final Park[] values;

    public NearByParksAdapter(Context context, Park[] objects) {
        super(context,0,objects);
        this.context = context;
        this.values = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        Park pyard=getItem(position);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.nearbyparksview, parent, false);


        convertView.setId(pyard.getID());

        TextView ParkName = (TextView) convertView.findViewById(R.id.parkName);
        ImageView ParkCaption = (ImageView) convertView.findViewById(R.id.parkCaption);
        TextView ParkLocation = (TextView) convertView.findViewById(R.id.parkLocstion);

        ParkName.setText(pyard.getName());
        ParkLocation.setText(pyard.getLocation().getVicinity());
        ParkCaption.setImageBitmap(pyard.getImage());
        return  convertView;

    }
}
