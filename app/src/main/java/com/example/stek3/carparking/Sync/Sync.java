package com.example.stek3.carparking.Sync;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.example.stek3.carparking.Repository.UserRepo;
import com.example.stek3.carparking.SQLite.DbHelper;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Stek3 on 02-Apr-18.
 */

public class Sync extends AsyncTask<Void,Void,Boolean> {

Context context;
    public Sync(){

    }

    public Sync(Context context){
        this.context=context;
    }

    @Override
    protected Boolean doInBackground(Void... params)
    {

        Timer tm=new Timer();
        tm.schedule(new TimerTask() {
            int count=0;

            @Override
            public void run() {

                UserRepo Repo=new UserRepo(context);
                Repo.SynchronizeUser(Repo.getUser(1));

                Log.e("Synchronise User","Success");

                Log.e("Timer",String.valueOf(count+=1));

                DbHelper helper=new DbHelper(context);

                Log.e("Saved User 3",helper.getUser().getFirstName());

            }
        },5000,2000);

        return null;
    }

}
