package com.example.smarthome;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationManagerCompat;

import android.app.NotificationChannel;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.Locale;

public class Heating extends AppCompatActivity {
    private EditText mEditTextInput;
    private TextView mTextViewCountDown,temperatureTV;
    private Button mButtonStartPause;
    private Button mButtonReset,back,help,btnUp,btnDown;
    private CountDownTimer mCountDownTimer;
    private boolean mTimerRunning;
    private long mStartTimeInMillis;
    private long mTimeLeftInMillis;
    private long mEndTime;
    private String input1;
    private Switch heatingSwitch;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_heating);
        getNav();
        back = (Button) findViewById(R.id.back);
        help = (Button) findViewById(R.id.help);
        mEditTextInput = findViewById(R.id.edit_text_input);
        mTextViewCountDown = findViewById(R.id.text_view_countdown);
        mButtonStartPause = findViewById(R.id.button_start_pause);
        mButtonReset = findViewById(R.id.button_reset);
        btnUp = (Button) findViewById(R.id.upbtn);
        btnDown = (Button) findViewById(R.id.downbtn);
        temperatureTV = (TextView) findViewById(R.id.textView);
        temperatureTV.setText(Data.getTemperature()+"\u2103");
        heatingSwitch = (Switch) findViewById(R.id.heatingSwitch);

        heatingSwitch.setChecked(Data.getHeatingState());

        heatingSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Data.setHeatingState(heatingSwitch.isChecked());
                mTimeLeftInMillis =0;
                mButtonStartPause.setBackgroundColor(Color.parseColor("#089508"));
                mTimerRunning = false;
                if(mCountDownTimer != null)
                    mCountDownTimer.cancel();
                updateCountDownText();
                updateWatchInterface();
                mButtonReset.setVisibility(View.INVISIBLE);
            }
        });
        mButtonStartPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mTimerRunning) {
                    pauseTimer();
                    mButtonStartPause.setBackgroundColor(Color.parseColor("#089508"));
                    heatingSwitch.setChecked(true);
                } else {

                    input1 = mEditTextInput.getText().toString().length()!=0 ?(Integer.parseInt(mEditTextInput.getText().toString())*60000)+"" : ((mTimeLeftInMillis)+"");
                    if (input1.length() == 0) {
                        Toast.makeText(getApplicationContext(), "Παρακαλώ συμπληρώστε τα λεπτά.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    long millisInput = Long.parseLong(input1) ;
                    if (millisInput == 0) {
                        Toast.makeText(getApplicationContext(), "Παρακαλώ δώστε μόνο θετικές τιμές.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    setTime(millisInput);
                    mEditTextInput.setText("");
                    startTimer();
                    mButtonStartPause.setBackgroundColor(Color.parseColor("#cc803d"));
                    heatingSwitch.setChecked(true);
                    Data.setHeatingState(true);
                }
            }
        });
        mButtonReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mStartTimeInMillis =0;
                resetTimer();
            }
        });

        btnUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Data.getTemperature()>=32){
                    Toast.makeText(getApplicationContext(),"Η μέγιστη θερμοκρασία είναι 32\u2103.",Toast.LENGTH_LONG).show();
                }
                else{
                    temperatureTV.setText((Data.getTemperature()+1)+"\u2103");
                    Data.setTemperature(Data.getTemperature()+1);
                }
            }
        });

        btnDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Data.getTemperature()<=0){
                    Toast.makeText(getApplicationContext(),"Η ελάχιστη θερμοκρασία είναι 0\u2103.",Toast.LENGTH_LONG).show();
                }
                else {
                    temperatureTV.setText((Data.getTemperature()-1)+"\u2103");
                    Data.setTemperature(Data.getTemperature()-1);
                }
            }
        });

    }
    private void getNav() {
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.action_bar_layout);
    }

    private void setTime(long milliseconds) {
        mStartTimeInMillis = milliseconds;
        resetTimer();
        closeKeyboard();
    }
    private void startTimer() {
        mEndTime = System.currentTimeMillis() + mTimeLeftInMillis;
        mCountDownTimer = new  CountDownTimer(mTimeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mTimeLeftInMillis = millisUntilFinished;
                updateCountDownText();
            }
            @Override
            public void onFinish() {
                mTimerRunning = false;
                updateWatchInterface();
                Data.builder.setContentTitle("Sm@rt Home Ειδοποίηση");
                Data.builder.setContentText("Η θέρμανση απενεργοποιήθηκε");
                Data.notificationManager.notify(100,Data.builder.build());
                heatingSwitch.setChecked(false);
                mButtonStartPause.setBackgroundColor(Color.parseColor("#089508"));
            }
        }.start();
        mTimerRunning = true;
        updateWatchInterface();
    }
    private void pauseTimer() {

        mCountDownTimer.cancel();
        mTimerRunning = false;
        updateWatchInterface();
    }
    private void resetTimer() {
        mTimeLeftInMillis = mStartTimeInMillis;
        updateCountDownText();
        updateWatchInterface();
    }
    private void updateCountDownText() {
        int hours = (int) (mTimeLeftInMillis / 1000) / 3600;
        int minutes = (int) ((mTimeLeftInMillis / 1000) % 3600) / 60;
        int seconds = (int) (mTimeLeftInMillis / 1000) % 60;
        String timeLeftFormatted;
        if (hours > 0) {
            timeLeftFormatted = String.format(Locale.getDefault(),
                    "%d:%02d:%02d", hours, minutes, seconds);
        } else {
            timeLeftFormatted = String.format(Locale.getDefault(),
                    "%02d:%02d", minutes, seconds);
        }
        mTextViewCountDown.setText(timeLeftFormatted);
    }
    private void updateWatchInterface() {
        if (mTimerRunning) {
            mEditTextInput.setVisibility(View.INVISIBLE);
            mButtonReset.setVisibility(View.INVISIBLE);
            mButtonStartPause.setText("ΠΑΥΣΗ");
        } else {
            mEditTextInput.setVisibility(View.VISIBLE);
            mButtonStartPause.setText("ΕΝΑΡΞΗ");
            mButtonStartPause.setVisibility(View.VISIBLE);
            if (mTimeLeftInMillis < mStartTimeInMillis) {
                mButtonReset.setVisibility(View.VISIBLE);
            } else {
                mButtonReset.setVisibility(View.INVISIBLE);
            }
        }
    }
    private void closeKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
    @Override
    protected void onStop() {
        super.onStop();
        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putLong("startTimeInMillis", mStartTimeInMillis);
        editor.putLong("millisLeft", mTimeLeftInMillis);
        editor.putBoolean("timerRunning", mTimerRunning);
        editor.putLong("endTime", mEndTime);
        editor.apply();
    }
    @Override
    protected void onStart() {
        super.onStart();



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
                Toast.makeText(getApplicationContext(), "Ενεργοποιήστε την θέρμανση ή/και ορίστε πότε θέλετε να κλείσει.", Toast.LENGTH_LONG).show();

            }
        });

        //Notificaiton
        Data.notificationManager = NotificationManagerCompat.from(this);


        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        mStartTimeInMillis = 0;
        mTimeLeftInMillis = 0;
        mTimerRunning = prefs.getBoolean("timerRunning", false);
        updateCountDownText();
        updateWatchInterface();
        if (mTimerRunning) {
            mEndTime = prefs.getLong("endTime", 0);

            mTimeLeftInMillis = mEndTime - System.currentTimeMillis();
            if (mTimeLeftInMillis <= 0) {
                heatingSwitch.setChecked(false);
                Data.setHeatingState(false);
                mTimeLeftInMillis = 0;
                mTimerRunning = false;
                updateCountDownText();
                updateWatchInterface();
            } else {
                startTimer();
            }
        }

    }
}