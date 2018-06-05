package com.example.stek3.carparking;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.example.stek3.carparking.SQLite.DbHelper;

/**
 * Created by Stek3 on 02-Mar-18.
 */

public class SplashActivity extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {


                DbHelper dbHelper=new DbHelper(getBaseContext());

                if(!dbHelper.UserLoggedIn()) {

                    Intent SignInView=new Intent(getBaseContext(),MainActivity.class);
                    startActivity(SignInView);
                }
                else {

                    Intent mainView=new Intent(getBaseContext(),home.class);
                    startActivity(mainView);
                }


                //was mainView
            }
        },3000);

    }
    }
