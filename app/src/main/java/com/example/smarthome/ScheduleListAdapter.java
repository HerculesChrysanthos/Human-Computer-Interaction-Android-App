package com.example.smarthome;

import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.text.style.IconMarginSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Switch;
import android.widget.TextView;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ScheduleListAdapter extends BaseAdapter {


    private Context mContext;
    private List<Schedule> mScheduleList;
    private String days[] = {"Sun","Mon","Tue","Wed","Thu","Fri","Sat"};
    private static boolean flag = true;


    //Constructor
    public ScheduleListAdapter(Context mContext, List<Schedule> mScheduleList) {
        this.mContext = mContext;
        this.mScheduleList = mScheduleList;
    }


    @Override
    public int getCount() {
        return mScheduleList.size();
    }

    @Override
    public Object getItem(int position) {
        return mScheduleList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = View.inflate(mContext,R.layout.schedule,null);
        TextView tvDay = (TextView) v.findViewById(R.id.day);
        TextView startTime = (TextView) v.findViewById(R.id.startTime);
        TextView stopTime = (TextView) v.findViewById(R.id.stopTime);
        Switch  aSwitch = (Switch) v.findViewById(R.id.switch1);

        //Set text for TextView
        tvDay.setText(mScheduleList.get(position).getDay());
        startTime.setText((mScheduleList.get(position).getStartTime()));
        stopTime.setText((mScheduleList.get(position).getStopTime()));

        aSwitch.setChecked(mScheduleList.get(position).isSwitchState());

        aSwitch.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                int dayPos = -1;
                Data.getScheduleList().get(position).setSwitchState(aSwitch.isChecked());
                if (aSwitch.isChecked()) {
                    Calendar calendar = Calendar.getInstance();
                    int currentDay = calendar.get(Calendar.DAY_OF_WEEK);


                    for (int i = 0; i < Data.getItems().length; i++) {
                        if (Data.getItems()[i].equalsIgnoreCase(mScheduleList.get(position).getDay())) {
                            dayPos = i;
                            break;
                        }
                    }


                    String day = days[dayPos];
                    String stime = mScheduleList.get(position).getStartTime();
                    String etime = mScheduleList.get(position).getStopTime();

                    Date currentTime = Calendar.getInstance().getTime();
                    String[] separated = currentTime.toString().split(" ");

                    String today = days[currentDay - 1] + " Jan 0" + (currentDay) + " " + separated[3] + " GMT+02:00 2023";
                    String start = day + " Jan 0" + (dayPos + 1) + " " + stime + ":00 GMT+02:00 2023";
                    String finish = day + " Jan 0" + (dayPos + 1) + " " + etime + ":00 GMT+02:00 2023";

                    //In case of  !currentDay<(dayPos+1)
                    int dayDiff = 7-currentDay;
                    int dayS = (dayPos + dayDiff+(currentDay-1))%6;

                    String dayName = days[dayS];

                    int dateFormat = (dayPos + 1+dayDiff+currentDay);

                    String startS="";
                    String finishS="";

                    if(dateFormat<10){
                        startS = dayName + " Jan 0" + (dayPos + 1+dayDiff+currentDay) + " " + stime + ":00 GMT+02:00 2023";
                        finishS = dayName + " Jan 0" + (dayPos + 1+dayDiff+currentDay) + " " + etime + ":00 GMT+02:00 2023";
                    }else{
                        startS = dayName + " Jan " + (dayPos + 1+dayDiff+currentDay) + " " + stime + ":00 GMT+02:00 2023";
                        finishS = dayName + " Jan " + (dayPos + 1+dayDiff+currentDay) + " " + etime + ":00 GMT+02:00 2023";
                    }


                    if (currentDay < (dayPos + 1)) {

                        startCounter(today,start,finish,position);

                    }else if (currentDay == (dayPos + 1)){

                        String todayTime[] = separated[3].split(":");
                        String startTime[] = stime.split(":");

                        int todayHour = Integer.parseInt(todayTime[0]);
                        int todayMin = Integer.parseInt(todayTime[1]);
                        int startHour = Integer.parseInt(startTime[0]);
                        int startMin = Integer.parseInt(startTime[1]);

                        if(todayHour < startHour){
                            startCounter(today,start,finish,position);
                        }else if (todayHour == startHour){
                            if(todayMin<startMin){
                                startCounter(today,start,finish,position);
                            }else if (todayMin > startMin){
                                startCounter(today,startS,finishS,position);
                            }
                        }else {
                            startCounter(today,startS,finishS,position);
                        }



                    }else{
                        startCounter(today,startS,finishS,position);
                    }
                }else {
                    Data.getScheduleList().get(position).setSwitchState(false);
                    Data.getScheduleList().get(position).getCountDownTimer().cancel();

                }
            }
        });

        v.setTag(mScheduleList.get(position).getDay());
        return v;
    }

    public void startCounter(String today, String start, String finish, int position){
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("EEE MMM dd HH:mm:ss z yyyy", Locale.ENGLISH);
            LocalDateTime localDate1 = LocalDateTime.parse(today, dateFormat);
            long timeInMilliseconds1 = localDate1.atOffset(ZoneOffset.UTC).toInstant().toEpochMilli();
            LocalDateTime localDate2 = LocalDateTime.parse(start, dateFormat);
            long timeInMilliseconds2 = localDate2.atOffset(ZoneOffset.UTC).toInstant().toEpochMilli();
            LocalDateTime localDate3 = LocalDateTime.parse(finish, dateFormat);
            long timeInMilliseconds3 = localDate3.atOffset(ZoneOffset.UTC).toInstant().toEpochMilli();

            long difference = timeInMilliseconds2 - timeInMilliseconds1;
            long difference2 = timeInMilliseconds3 - timeInMilliseconds1;

            Data.getScheduleList().get(position).setCountDownTimer(new CountDownTimer(difference2, 1000) {

                @Override
                public void onTick(long millisUntilFinished) {

                    if (millisUntilFinished <= (difference2 - difference) && flag) {
                        Data.setBoilerState(true);
                        Data.getScheduleList().get(position).setRunning(true);
                        Data.builder.setContentTitle("Sm@rt Home ειδοποίηση εβδομαδιαίου προγράμματος");
                        Data.builder.setContentText("Ο Θερμοσίφωνας ενεργοποιήθηκε");
                        Data.notificationManager.notify(102,Data.builder.build());
                        flag = false;
                    }
                }

                @Override
                public void onFinish() {
                    Data.getScheduleList().get(position).setSwitchState(false);
                    BoilerSchedule.getScheduleListAdapter().notifyDataSetChanged();
                    BoilerSchedule.getListView().setAdapter( BoilerSchedule.getScheduleListAdapter());
                    Data.setBoilerState(false);
                    Data.getScheduleList().get(position).setRunning(false);
                    Data.builder.setContentTitle("Sm@rt Home ειδοποίηση εβδοομαδιαίου προγράμματος");
                    Data.builder.setContentText("Ο θερμοσίφωνας απενεργοποιήθηκε");
                    Data.notificationManager.notify(101,Data.builder.build());
                }
            }.start()
            );


        }
    }




}
