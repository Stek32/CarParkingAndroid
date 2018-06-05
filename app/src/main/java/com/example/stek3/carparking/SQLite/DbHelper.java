package com.example.stek3.carparking.SQLite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.inputmethodservice.Keyboard;

import com.example.stek3.carparking.Users;

/**
 * Created by Stek3 on 28-Apr-18.
 */
public class DbHelper extends SQLiteOpenHelper
{

    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "ParkingDB.db";

    public DbHelper(Context context)
    {
        super(context,DATABASE_NAME,null,DATABASE_VERSION);

    }

    @Override
    public void onCreate(android.database.sqlite.SQLiteDatabase db) {


        db.execSQL(Contract.SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(android.database.sqlite.SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL(Contract.SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    public void onDowngrade(android.database.sqlite.SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }



    public boolean InsertUser(String Firstname,String LastName,String MiddleName,String UserName,String Email,String PhoneNumber)
    {
        DeleteUsers();

        SQLiteDatabase MyDatabase=this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("FirstName",Firstname);
        contentValues.put("LastName",LastName);
        contentValues.put("MiddleName",MiddleName);
        contentValues.put("UserName",UserName);
        contentValues.put("Email",Email);
        contentValues.put("PhoneNumber",PhoneNumber);

        MyDatabase.insert(Contract.UserTable.TABLE_NAME,null,contentValues);

        return  true;
    }

    public Users getUser()
    {

        android.database.sqlite.SQLiteDatabase MyDatabase=getReadableDatabase();
        Cursor Res=MyDatabase.rawQuery("Select * from " + Contract.UserTable.TABLE_NAME+" order by " + Contract.UserTable._ID +" desc limit 1",null);
        Users user=null;
        if(Res.moveToFirst()){

            do{
              user =new Users();
                user.setFirstName(Res.getString(1));
                user.setLastName(Res.getString(2));
                user.setEmail(Res.getString(Res.getColumnIndex(Contract.UserTable.COLUMN_NAME_Email)));
                user.setUserName(Res.getString(Res.getColumnIndex(Contract.UserTable.COLUMN_NAME_UserName)));

            } while (Res.moveToNext());
        }

    return  user;
    }


    public boolean UserLoggedIn(){

        SQLiteDatabase db=getReadableDatabase();

        int Rows=(int) DatabaseUtils.queryNumEntries(db,Contract.UserTable.TABLE_NAME);

        if(Rows>0){
            return  true;
        }
        else {
            return  false;
        }
    }

    public boolean DeleteUsers(){
        SQLiteDatabase db=getWritableDatabase();
        //db.execSQL("Truncate table "+Contract.UserTable.TABLE_NAME);
        db.delete(Contract.UserTable.TABLE_NAME,null,null);

        return true;
    }
}
