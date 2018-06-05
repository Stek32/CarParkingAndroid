package com.example.stek3.carparking.Parks;

import android.graphics.Bitmap;

import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Stek3 on 13-Mar-18.
 */

public class Park implements Serializable {

    private int ID;
    private String Name;
    private Location location;
    private String Reference;
    private Spaces spaces;
    private String PlaceID;
    private Bitmap Image;


    public void setImage(Bitmap image) {
        Image = image;
    }

    public Bitmap getImage() {
        return Image;
    }

    public String getPlaceID() {
        return PlaceID;
    }

    public void setPlaceID(String placeID) {
        PlaceID = placeID;
    }

    public Spaces getSpaces() {
        return spaces;
    }

    public String getReference() {
        return Reference;
    }

    public void setReference(String reference) {
        Reference = reference;
    }

    public void setSpaces(Spaces spaces) {
        this.spaces = spaces;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public void setName(String name) {
        Name = name;
    }

    public int getID() {
        return ID;
    }

    public Location getLocation() {
        return location;
    }

    public String getName() {
        return Name;
    }


    public static class Location
    {

        private String Vicinity;
        private int LocationID;
        private Double Latitude;
        private Double Longitude;
        private List<String> Directions;
        private PolylineOptions Route;

        public void setDirections(List<String> directions) {
            Directions = directions;
        }

        public List<String> getDirections() {
            return Directions;
        }

        public void setRoute(PolylineOptions route) {
            Route = route;
        }

        public PolylineOptions getRoute() {
            return Route;
        }

        public void setLongitude(Double longitude) {
            Longitude = longitude;
        }

        public void setLatitude(Double latitude) {
            Latitude = latitude;
        }

        public void setLocationID(int locationID) {
            LocationID = locationID;
        }

        public void setVicinity(String vicinity) {
            Vicinity = vicinity;
        }

        public Double getLatitude() {
            return Latitude;
        }

        public Double getLongitude() {
            return Longitude;
        }

        public int getLocationID() {
            return LocationID;
        }

        public String getVicinity() {
            return Vicinity;
        }
    }

public static class Spaces{
    private int SpaceID;
    private int SpaceCount;
    private int UsedSpaces;

    public void setSpaceCount(int spaceCount) {
        SpaceCount = spaceCount;
    }

    public void setSpaceID(int spaceID) {
        SpaceID = spaceID;
    }

    public void setUsedSpaces(int usedSpaces) {
        UsedSpaces = usedSpaces;
    }

    public int getUsedSpaces() {
        return UsedSpaces;
    }

    public int getSpaceCount() {
        return SpaceCount;
    }

    public int getSpaceID() {
        return SpaceID;
    }

}
}
