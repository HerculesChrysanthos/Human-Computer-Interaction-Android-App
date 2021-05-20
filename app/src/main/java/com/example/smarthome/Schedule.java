package com.example.smarthome;

import android.os.CountDownTimer;

import java.util.concurrent.CountDownLatch;

public class Schedule {

    private String day,startTime,stopTime;
    private boolean switchState,running;
    private CountDownTimer countDownTimer;

    public Schedule(String day, String startTime, String stopTime, boolean switchState) {
        this.day = day;
        this.startTime = startTime;
        this.stopTime = stopTime;
        this.switchState = switchState;
        this.running = false;
    }

    public CountDownTimer getCountDownTimer() {
        return countDownTimer;
    }

    public void setCountDownTimer(CountDownTimer countDownTimer) {
        this.countDownTimer = countDownTimer;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getStopTime() {
        return stopTime;
    }

    public void setStopTime(String stopTime) {
        this.stopTime = stopTime;
    }

    public boolean isSwitchState() {
        return switchState;
    }

    public void setSwitchState(boolean switchState) {
        this.switchState = switchState;
    }

    public boolean isRunning() {
        return running;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }
}
