package com.example.stek3.carparking.Repository;

import android.content.Context;
import android.util.Log;

import com.example.stek3.carparking.SQLite.Contract;
import com.example.stek3.carparking.SQLite.DbHelper;
import com.example.stek3.carparking.Users;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Stek3 on 05-Mar-18.
 */

public class UserRepo
{

    Context context;

    public UserRepo()
    {

    }

    public UserRepo(Context context){

        this.context=context;
    }
    public boolean SynchronizeUser(Users user)
    {
        DbHelper Database=new DbHelper(context);
        Database.InsertUser(user.getFirstName(),user.getLastName(),user.getMiddleName(),user.getUserName(),user.getEmail(),String.valueOf(user.getPhoneNumber()));


        return true;
    }

    public Users getUser(int ID) {

        Users user = new Users();

        String response=null;



            try {
                URL url = new URL("http://192.168.43.164/dcss/api/Parking/users/1");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                // read the response
                InputStream in = new BufferedInputStream(conn.getInputStream());
                response = in.toString();

                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                StringBuilder sb = new StringBuilder();

                System.out.println("Response: " + response);

                String output = null;

                while ((output = reader.readLine()) != null) {

                    sb.append(output);

                    System.out.println(sb);

                }

                JSONObject UserObject = new JSONObject(sb.toString());
                JSONObject NationalRegObject = UserObject.getJSONObject("NationalRegInfo");
                user.setFirstName(NationalRegObject.getString("FirstName"));
                user.setID(UserObject.getInt("ID"));
                user.setLastName(NationalRegObject.getString("LastName"));
                user.setEmail(UserObject.getString("Email"));
                user.setUserName(UserObject.getString("UserName"));


            } catch (Exception ex) {

                Log.e("Error For Me", ex.toString());
            }

        return  user;
    }
}
