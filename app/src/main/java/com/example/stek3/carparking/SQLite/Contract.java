package com.example.stek3.carparking.SQLite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

/**
 * Created by Stek3 on 28-Apr-18.
 */

public class Contract {

    private Contract()
    {

    }

    public static class UserTable implements BaseColumns{

        public static final String TABLE_NAME = "Users";
        public static final String COLUMN_NAME_UserId="UserID";
        public static final String COLUMN_NAME_FirstName = "FirstName";
        public static final String COLUMN_NAME_LastName= "LastName";
        public static final String COLUMN_NAME_MiddleName="MiddleName";
        public static final String COLUMN_NAME_UserName="UserName";
        public static final String COLUMN_NAME_Email="Email";
        public static final String COLUMN_NAME_PhoneNumber="PhoneNumber";

    }

    private static class ParksTable implements BaseColumns{
        public static final String TABLE_NAME = "Parks";
        public static final String COLUMN_NAME_Name="ParkName";

    }

    public static final String SQL_CREATE_ENTRIES =

            "CREATE TABLE " + UserTable.TABLE_NAME + " (" +
                    UserTable._ID + " INTEGER PRIMARY KEY," +
                    UserTable.COLUMN_NAME_FirstName + " TEXT," +
                    UserTable.COLUMN_NAME_LastName + " TEXT," +
                    UserTable.COLUMN_NAME_MiddleName + " TEXT," +
                    UserTable.COLUMN_NAME_UserName + " TEXT," +
                    UserTable.COLUMN_NAME_Email + " TEXT," +
                    UserTable.COLUMN_NAME_PhoneNumber + " TEXT)";

    public static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + UserTable.TABLE_NAME;




}
