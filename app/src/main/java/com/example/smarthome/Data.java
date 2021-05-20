package com.example.smarthome;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class Data extends Application {

    private static ArrayList<ArrayList<plug>> rooms=new ArrayList<ArrayList<plug>>();
    private static boolean[] lightState;
    private static boolean[] widnowsState;
    //Notification
    public static NotificationManagerCompat notificationManager;
    public static NotificationCompat.Builder builder;
    private static int temperature;
    private static boolean heatingState;
    private static boolean boilerState;
    private static String[] items;
    private static List<Schedule> ScheduleList;

    @Override
    public void onCreate() {
        super.onCreate();

        for(int i=0; i<4;i++){
            rooms.add(new ArrayList<plug>());
        }

        for(int j=1; j<=6;j++){
            rooms.get(0).add(new plug(("Πρίζα "+(j)),false));
        }
        for(int j=1; j<=5;j++){
            rooms.get(1).add(new plug(("Πρίζα "+(j)),false));
        }
        for(int j=1; j<=4;j++){
            rooms.get(2).add(new plug(("Πρίζα "+(j)),false));
        }
        rooms.get(3).add(new plug(("Πρίζα "+(1)),false));
        heatingState = false;
        lightState = new boolean[]{false, false, false, false};
        widnowsState = new boolean[]{false, false, false, false};

        createNotificationChannel();

        Data.builder = new NotificationCompat.Builder(this,"sm@rtHome")
                .setSmallIcon(R.drawable.ic_baseline_alarm_24)
                .setContentTitle("Sm@rt Home Notification")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        temperature = 25;

        items = new String[]{"Κυριακή","Δευτέρα","Τρίτη","Τετάρτη","Πέμπτη","Παρασκευή","Σάββατο"};
        ScheduleList = new ArrayList<>();

    }

    public static List<Schedule> getScheduleList() {
        return ScheduleList;
    }

    public static void setScheduleList(Schedule schedule) {
        ScheduleList.add(schedule);
    }

    public static String[] getItems() {
        return items;
    }

    public static boolean getBoilerState() {
        return boilerState;
    }

    public static void setBoilerState(boolean boilerState) {
        Data.boilerState = boilerState;
    }

    public static boolean getHeatingState() {
        return heatingState;
    }

    public static void setHeatingState(boolean heatingState) {
        Data.heatingState = heatingState;
    }

    public static int getTemperature() {
        return temperature;
    }

    public static void setTemperature(int temperature) {
        Data.temperature = temperature;
    }

    private void createNotificationChannel(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            CharSequence name = "smartHomeChannel";
            String description = "Channel for smartHome notifications";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("sm@rtHome",name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    public static ArrayList<plug> getRoom(int roomId) {
        return rooms.get(roomId);
    }

    public static String getRoomName(int id){
        String[] rooms  = {"ΣΑΛΟΝΙ","ΚΟΥΖΙΝΑ","ΥΠΝΟΔΩΜΑΤΙΟ","ΜΠΑΝΙΟ"};
        return rooms[id];
    }

    public static boolean getLightState(int index) {
        return lightState[index];
    }

    public static boolean getWidnowsState(int index) {
        return widnowsState[index];
    }

    public static void setLightState(int index,boolean state){
        lightState[index] = state;
    }

    public static void setWindowsState(int index,boolean state){
        widnowsState[index] = state;
    }
}
