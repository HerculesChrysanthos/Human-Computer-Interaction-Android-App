package com.example.smarthome;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class ControlRoom extends AppCompatActivity {

    private Button back,help,saloni,kouzina,ypnodwmatio,mpanio;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control_room);

        getNav();
        initialize();
    }

    @Override
    protected void onStart() {
        super.onStart();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent firstPage = new Intent(getApplicationContext(),MainActivity.class);
                firstPage.putExtra("switch","yes");
                startActivity(firstPage);
            }
        });

        help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Επιλέξτε ένα δωμάτιο για να δείτε την κατάσταση λειτουργίας του.", Toast.LENGTH_LONG).show();
            }
        });

        saloni.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent firstPage = new Intent(getApplicationContext(), Room.class);
                firstPage.putExtra("id",0);
                startActivity(firstPage);
            }
        });

        kouzina.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent firstPage = new Intent(getApplicationContext(),Room.class);
                firstPage.putExtra("id",1);
                startActivity(firstPage);
            }
        });

        ypnodwmatio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent firstPage = new Intent(getApplicationContext(),Room.class);
                firstPage.putExtra("id",2);
                startActivity(firstPage);
            }
        });

        mpanio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent firstPage = new Intent(getApplicationContext(),Room.class);
                firstPage.putExtra("id",3);
                startActivity(firstPage);
            }
        });
    }

    private void getNav() {
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.action_bar_layout);
    }

    private void initialize(){
        back = (Button) findViewById(R.id.back);
        help = (Button) findViewById(R.id.help);
        saloni = (Button) findViewById(R.id.saloni);
        kouzina = (Button) findViewById(R.id.kouzina);
        ypnodwmatio = (Button) findViewById(R.id.ypnodwmatio);
        mpanio = (Button) findViewById(R.id.mpanio);
    }
}