package com.example.stek3.carparking;

import android.location.Location;

/**
 * Created by Stek3 on 04-Mar-18.
 */

public class Users {

    private int ID;
    private String FirstName;
    private String LastName;
    private String MiddleName;
    private String UserName;
    private String Email;
    private int PhoneNumber;
    private String Location;

    public int getID() {
        return ID;
    }

    public String getEmail() {
        return Email;
    }

    public int getPhoneNumber() {
        return PhoneNumber;
    }

    public String getFirstName() {
        return FirstName;
    }

    public String getLastName() {
        return LastName;
    }

    public String getMiddleName() {
        return MiddleName;
    }

    public String getUserName() {
        return UserName;
    }

    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        Location = location;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public void setFirstName(String firstName) {
        FirstName = firstName;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public void setLastName(String lastName) {
        LastName = lastName;
    }

    public void setMiddleName(String middleName) {
        MiddleName = middleName;
    }

    public void setPhoneNumber(int phoneNumber) {
        PhoneNumber = phoneNumber;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }


}
