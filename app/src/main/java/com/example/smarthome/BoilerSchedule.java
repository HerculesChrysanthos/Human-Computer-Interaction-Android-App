package com.example.smarthome;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class BoilerSchedule extends AppCompatActivity {

    private Button back,help,addSchedule,delete;

    private int startHour=-1;
    private int startMinute=-1;

    private int endHour=-1;
    private int endMinute=-1;
    int Position=-1,deletePosition=-1;
    private static ListView listView;
    private static ScheduleListAdapter scheduleListAdapter;
    private List<Schedule> mScheduleList;
    AlertDialog.Builder mode;
    private String Shour="",Smin="",Ehour="",Emin="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_boiler_schedule);
        getNav();
        back = (Button) findViewById(R.id.back);
        addSchedule = (Button) findViewById(R.id.add);
        help = (Button) findViewById(R.id.help);
        delete = (Button) findViewById(R.id.delete);
        mode = new AlertDialog.Builder(BoilerSchedule.this);
        addSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onButtonShowPopupWindowClick(v);
            }
        });

        listView = (ListView) findViewById(R.id.listview);

        mScheduleList = Data.getScheduleList();

        scheduleListAdapter = new ScheduleListAdapter(getApplicationContext(),mScheduleList);
        listView.setAdapter(scheduleListAdapter);
        listView.requestFocus();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                listView.setSelection(position);
                deletePosition = position;
            }
        });
    }

    public static ListView getListView() {
        return listView;
    }

    public static ScheduleListAdapter getScheduleListAdapter() {
        return scheduleListAdapter;
    }

    public void onButtonShowPopupWindowClick(View view) {
        // inflate the layout of the popup window
        LayoutInflater inflater = (LayoutInflater)
                getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.popup_window, null);
        // create the popup window
        int width = LinearLayout.LayoutParams.MATCH_PARENT;
        int height = LinearLayout.LayoutParams.MATCH_PARENT;
        boolean focusable = false;
        // lets taps outside the popup also dismiss it
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);
        // show the popup window
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

        Spinner dropdown = (Spinner) popupView.findViewById(R.id.spinner1);
        Button startTime = (Button) popupView.findViewById(R.id.startTime);
        Button endTime = (Button) popupView.findViewById(R.id.endTime);
        Button cancel = (Button) popupView.findViewById(R.id.cancelButton);
        Button save = (Button) popupView.findViewById(R.id.saveButton);
        String[] items = Data.getItems();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        dropdown.setAdapter(adapter);



        dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Position = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startHour=-1;
                endHour=-1;
                Ehour="";
                Emin="";
                Shour="";
                Smin="";
                popupWindow.dismiss();
            }
        });

        startTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                TimePickerDialog timePickerDialog = new TimePickerDialog(BoilerSchedule.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int pHour,int pMinute) {
                                startHour = pHour;
                                startMinute = pMinute;
                                Shour=Integer.toString(pHour);
                                Smin=Integer.toString(pMinute);
                                if(startHour<10){
                                    Shour="0"+startHour;
                                }
                                if(startMinute<10){
                                    Smin="0"+startMinute;
                                }


                                if(Ehour!="" && Emin!=""){
                                    if(Integer.valueOf(Ehour) == Integer.valueOf(Shour) && Integer.valueOf(Emin) <= Integer.valueOf(Smin)){
                                        Shour = "";
                                        Smin = "";
                                        startHour=-1;
                                        startMinute=-1;
                                        Toast.makeText(getApplicationContext(), "Ο χρόνος έναρξης πρέπει να είναι νωρίτερα από τον χρόνο λήξης.", Toast.LENGTH_LONG).show();
                                    }
                                    else if(Integer.parseInt(Ehour) < Integer.valueOf(Shour)){
                                        Shour = "";
                                        Smin = "";
                                        startHour=-1;
                                        startMinute=-1;
                                        Toast.makeText(getApplicationContext(), "Ο χρόνος έναρξης πρέπει να είναι νωρίτερα από τον χρόνο λήξης.", Toast.LENGTH_LONG).show();
                                    }
                                    else{
                                        startTime.setText(Shour+":"+Smin);
                                    }
                                }
                                else{
                                    startTime.setText(Shour+":"+Smin);
                                }
                            }
                        },startHour, startMinute, true);
                timePickerDialog.show();
            }
        });

        endTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TimePickerDialog timePickerDialog = new TimePickerDialog(BoilerSchedule.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int pHour,int pMinute) {
                                endHour = pHour;
                                endMinute = pMinute;
                                Ehour=Integer.toString(pHour);
                                Emin=Integer.toString(pMinute);
                                if(endHour<10){
                                    Ehour="0"+endHour;
                                }
                                if(endMinute<10){
                                    Emin="0"+endMinute;
                                }
                                if(Shour!="" && Smin!="") {
                                    if (Integer.valueOf(Ehour) == Integer.valueOf(Shour) && Integer.valueOf(Emin) <= Integer.valueOf(Smin)) {
                                        Ehour = "";
                                        Emin = "";
                                        endHour = -1;
                                        endMinute = -1;
                                        Toast.makeText(getApplicationContext(), "Ο χρόνος λήξης πρέπει να είναι αργότερα από τον χρόνο έναρξης.", Toast.LENGTH_LONG).show();
                                    } else if (Integer.parseInt(Ehour) < Integer.valueOf(Shour)) {
                                        Ehour = "";
                                        Emin = "";
                                        endHour = -1;
                                        endMinute = -1;
                                        Toast.makeText(getApplicationContext(), "Ο χρόνος λήξης πρέπει να είναι αργότερα από τον χρόνο έναρξης.", Toast.LENGTH_LONG).show();
                                    } else {
                                        endTime.setText(Ehour + ":" + Emin);
                                    }
                                }
                                else{
                                    endTime.setText(Ehour + ":" + Emin);
                                }
                            }
                        },endHour, endMinute, true);
                timePickerDialog.show();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(startHour==-1 || startMinute==-1){
                    Toast.makeText(BoilerSchedule.this, "Παρακαλώ ορίστε χρόνο έναρξης.", Toast.LENGTH_SHORT).show();
                }
                else if(endHour==-1 || endMinute==-1){
                    Toast.makeText(BoilerSchedule.this, "Παρακαλώ ορίστε χρόνο λήξης.", Toast.LENGTH_SHORT).show();
                }
                else{
                    Schedule schedule = new Schedule(Data.getItems()[Position],Shour+":"+Smin,Ehour+":"+Emin,false);
                    Data.setScheduleList(schedule);
                    scheduleListAdapter.notifyDataSetChanged();
                    listView.setAdapter(scheduleListAdapter);
                    startHour=-1;
                    endHour=-1;
                    Ehour="";
                    Emin="";
                    Shour="";
                    Smin="";
                    popupWindow.dismiss();
                }


            }
        });




    }

    private void getNav() {
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.action_bar_layout);
    }

    protected void onStart() {
        super.onStart();

        //Notificaiton
        Data.notificationManager = NotificationManagerCompat.from(this);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent firstPage = new Intent(getApplicationContext(), MainActivity.class);
                firstPage.putExtra("switch", "yes");
                startActivity(firstPage);
            }
        });

        help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Πατήστε στο + για να προσθέσετε προφίλ.", Toast.LENGTH_LONG).show();

            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(deletePosition==-1){
                    Toast.makeText(getApplicationContext(), "Επιλέξτε πρώτα ένα προφίλ.", Toast.LENGTH_SHORT).show();
                }
                else{
                    mode.setTitle("Είστε σίγουροι ότι θέλετε να διαγραφεί;");
                    mode.setPositiveButton("ΝΑΙ", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                            if(Data.getScheduleList().get(deletePosition).getCountDownTimer() != null){
                                Data.getScheduleList().get(deletePosition).getCountDownTimer().cancel();
                            }

                            Data.getScheduleList().remove(deletePosition);
                            scheduleListAdapter.notifyDataSetChanged();
                            listView.setAdapter(scheduleListAdapter);
                            deletePosition = -1;
                            dialog.dismiss();
                        }
                    });
                    mode.setNegativeButton("ΟΧΙ", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            scheduleListAdapter.notifyDataSetChanged();
                            listView.setAdapter(scheduleListAdapter);
                            deletePosition = -1;
                            dialog.dismiss();

                        }
                    });
                    mode.show();
                }

            }
        });


    }
}