package com.example.smarthome;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.service.controls.Control;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

public class Room extends AppCompatActivity {
    private Button back,help;
    int roomId,plugs;
    TextView tv;
    ArrayList<String> plugsArray;
    ArrayList<Switch> plugSwitch;
    String name;
    private Switch plug1switch,plug2switch,plug3switch,plug4switch,plug5switch,plug6switch,lightsSwitch, windowsSwitch;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room);
        getNav();
        initialize();
        roomId = getIntent().getExtras().getInt("id");
        name = ((Data)this.getApplication()).getRoomName(roomId);

        tv.setText(name);
        plugs = Data.getRoom(roomId).size();
        for(int i=0;i<6;i++){
            plugSwitch.get(i).setVisibility(View.INVISIBLE);
        }
        for(int i=0;i<plugs;i++){
             plugsArray.add(Data.getRoom(roomId).get(i).getName());
             plugSwitch.get(i).setVisibility(View.VISIBLE);
             plugSwitch.get(i).setChecked(Data.getRoom(roomId).get(i).isState());
        }
        lightsSwitch.setChecked(Data.getLightState(roomId));
        windowsSwitch.setChecked(Data.getWidnowsState(roomId));
    }

    @Override
    protected void onStart() {
        super.onStart();
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent prevPage = new Intent(getApplicationContext(), ControlRoom.class);
                startActivity(prevPage);
            }
        });

        help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "hint: πατήστε πάνω στους διακόπτες για να αλλάξετε την κατάσταση λειτουργίας του.", Toast.LENGTH_LONG).show();
            }
        });
        plug1switch.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Data.getRoom(roomId).get(0).setState(plug1switch.isChecked());
            }
        });

        plug2switch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Data.getRoom(roomId).get(1).setState(plug2switch.isChecked());
            }
        });

        plug3switch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Data.getRoom(roomId).get(2).setState(plug3switch.isChecked());
            }
        });

        plug4switch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Data.getRoom(roomId).get(3).setState(plug4switch.isChecked());
            }
        });
        plug5switch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Data.getRoom(roomId).get(4).setState(plug5switch.isChecked());
            }
        });
        plug6switch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Data.getRoom(roomId).get(5).setState(plug6switch.isChecked());
            }
        });
        lightsSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Data.setLightState(roomId,lightsSwitch.isChecked());
            }
        });

        windowsSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Data.setWindowsState(roomId,windowsSwitch.isChecked());
            }
        });
    }

    private void getNav() {
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.action_bar_layout);
    }

    private void initialize(){
        tv = (TextView)  findViewById(R.id.nameRoom);
        back = (Button) findViewById(R.id.back);
        help = (Button) findViewById(R.id.help);
        plugsArray = new ArrayList<>();
        plugSwitch = new ArrayList<>();
        lightsSwitch = (Switch) findViewById(R.id.lights);
        windowsSwitch = (Switch) findViewById(R.id.windows);

        plug1switch = (Switch) findViewById(R.id.plug1switch);
        plugSwitch.add(plug1switch);
        plug2switch = (Switch) findViewById(R.id.plug2switch);
        plugSwitch.add(plug2switch);
        plug3switch = (Switch) findViewById(R.id.plug3switch);
        plugSwitch.add(plug3switch);
        plug4switch = (Switch) findViewById(R.id.plug4switch);
        plugSwitch.add(plug4switch);
        plug5switch = (Switch) findViewById(R.id.plug5switch);
        plugSwitch.add(plug5switch);
        plug6switch = (Switch) findViewById(R.id.plug6switch);
        plugSwitch.add(plug6switch);
    }
}