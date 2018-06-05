package com.example.stek3.carparking;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView NewUserButton=(TextView)findViewById(R.id.newusrlink);

        NewUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent RegistrationIntent=new Intent(getBaseContext(),registration.class);

                startActivity(RegistrationIntent );
            }
        });

        Button LoginButton=(Button) findViewById(R.id.loginbtn);

        LoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent HomeIntent=new Intent(getBaseContext(),home.class);

                startActivity(HomeIntent);
            }
        });

    }
}
