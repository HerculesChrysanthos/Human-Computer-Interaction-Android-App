package com.example.smarthome;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.NotificationChannel;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity {


    private Switch aSwitch;
    private Button waterHeater,heating,controlRoom,help;
    AlertDialog.Builder mode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getNav();
        initialize();
        if(getIntent().hasExtra("switch")){
            aSwitch.setChecked(true);
        }



    }

    @Override
    protected void onStart() {
        super.onStart();

        aSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!aSwitch.isChecked()){
                    Data.setHeatingState(false);
                    Data.setBoilerState(false);
                    for(int i=0;i<4;i++){
                        Data.setLightState(i,false);
                        Data.setWindowsState(i,false);
                        for(int j=0;j<Data.getRoom(i).size();j++){
                            Data.getRoom(i).get(j).setState(false);
                        }
                    }
                    for(int i=0;i<Data.getScheduleList().size();i++){
                        Data.getScheduleList().get(i).setSwitchState(false);
                        Data.getScheduleList().get(i).getCountDownTimer().cancel();
                    }
                }
            }
        });


        controlRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(aSwitch.isChecked()){
                    Intent controlR = new Intent(getApplicationContext(),ControlRoom.class);
                    startActivity(controlR);
                }else{
                    Toast.makeText(getApplicationContext(), "Παρακαλώ ενεργοποιήστε τον γενικό διακόπτη.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        heating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(aSwitch.isChecked()){
                    Intent heat = new Intent(getApplicationContext(),Heating.class);
                    startActivity(heat);
                }else{
                    Toast.makeText(getApplicationContext(), "Παρακαλώ ενεργοποιήστε τον γενικό διακόπτη.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        waterHeater.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(aSwitch.isChecked()){

                    mode.setTitle("Επιλέξτε λειτουργία για το θερμοσίφωνα: ");
                    mode.setPositiveButton("ΡΥΘΜΙΣΗ ΧΡΟΝΟΔΙΑΚΟΠΤΗ", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();
                            Intent boiler = new Intent(getApplicationContext(),Boiler.class);
                            startActivity(boiler);
                        }
                    });
                    mode.setNegativeButton("ΕΒΔΟΜΑΔΙΑΙΟ ΠΡΟΓΡΑΜΜΑ", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();
                            Intent program = new Intent(getApplicationContext(),BoilerSchedule.class);
                            startActivity(program);
                        }
                    });
                    mode.show();


                }else{
                    Toast.makeText(getApplicationContext(), "Παρακαλώ ενεργοποιήστε τον γενικό διακόπτη.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Παρακαλώ ενεργοποιήστε τον γενικό διακόπτη για να χρησιμοποιήσετε τις υπόλοιπες λειτουργίες", Toast.LENGTH_LONG).show();
            }
        });

    }

    private void initialize() {
        aSwitch = (Switch) findViewById(R.id.switch1);
        waterHeater = (Button) findViewById(R.id.waterHeater);
        heating = (Button) findViewById(R.id.heating);
        controlRoom = (Button) findViewById(R.id.controlRoom);
        help = (Button) findViewById(R.id.help);
        mode = new AlertDialog.Builder(MainActivity.this);
    }

    private void getNav() {
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.action_bar_layout);
    }
}