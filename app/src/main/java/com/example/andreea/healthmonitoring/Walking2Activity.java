package com.example.andreea.healthmonitoring;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import manager.DataManager;
import model.DailyStatistics;
import model.Day;
import model.User;
import webservice.SearchDailyStatisticsDelegate;
import webservice.SearchDailyStatisticsTask;
import webservice.SearchDayDelegate;
import webservice.SearchDayTask;
import webservice.UpdateDailyStatisticsDelegate;
import webservice.UpdateDailyStatisticsTask;

public class Walking2Activity extends AppCompatActivity implements SensorEventListener, UpdateDailyStatisticsDelegate, SearchDayDelegate, SearchDailyStatisticsDelegate {

    private SensorManager sensorManager;
    private TextView count;
    boolean activityRunning;
    private Walking2Activity walking2Activity;
    private Day currentDay;
    private String calendarString;
    private User userAfterLogin;
    private Day day;
    private DailyStatistics dailyStatisticsObject;
    private float numberOfSteps;
//    private Sensor mStepCounterSensor;
//    private Sensor mStepDetectorSensor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_walking2);
        count = (TextView) findViewById(R.id.tv_steps);
        walking2Activity = this;
        Intent intent = getIntent();

        userAfterLogin = (User) intent.getSerializableExtra("userAfterLogin");

        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        calendarString = df.format(c.getTime());
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
//        mStepCounterSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
//        mStepDetectorSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);
        SearchDayTask searchDayTask = new SearchDayTask(calendarString);
        searchDayTask.setSearchDayDelegate(walking2Activity);
    }

    @Override
    protected void onResume() {
        super.onResume();
        activityRunning = true;
        Sensor countSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
//        PackageManager pm = getPackageManager();
//        if (pm.hasSystemFeature(PackageManager.FEATURE_SENSOR_STEP_COUNTER)) {
//            Toast.makeText(this, "Da, are sensor", Toast.LENGTH_SHORT).show();
//        }
        if (countSensor != null) {
            sensorManager.registerListener(this, countSensor, SensorManager.SENSOR_DELAY_UI);
            //addNotification();
        } else {
            Toast.makeText(this, "Count sensor not available!", Toast.LENGTH_LONG).show();
            //am pus aici dar doar sa vad ca se activeaza notificarea
            //addNotification();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        activityRunning = false;
        // if you unregister the last listener, the hardware will stop detecting step events
//        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (activityRunning) {
            count.setText(String.valueOf(event.values[0]));
            //am adaugat aici
            if (event.values[0] > 50) {
                addNotification();
                //Toast.makeText(this, "", Toast.LENGTH_SHORT).show();
            }
            float numberOfSteps=event.values[0];
            changeCaloriesInDatabase();

        }
    }

    public void changeCaloriesInDatabase() {
        int quantity = 0;
        if (numberOfSteps % 2000 == 0) {
            quantity += 100;
        }
        if (quantity != 0) {
            UpdateDailyStatisticsTask updateDailyStatisticsTask = new UpdateDailyStatisticsTask(dailyStatisticsObject.getUserId(), dailyStatisticsObject.getDayId(), dailyStatisticsObject.getTotalCalories() - quantity);
            updateDailyStatisticsTask.setUpdateDailyStatisticsDelegate(walking2Activity);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    private void addNotification() {
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.notification)
                        .setContentTitle("Notifications Example")
                        .setContentText("Ai indeplinit obiectivul de pasi! Felicitari! Ai obtinut recompensa.");

        Intent notificationIntent = new Intent(this, Walking2Activity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(contentIntent);

        // Add as notification
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(0, builder.build());
    }

    @Override
    public void onUpdateDailyStatisticsDone(String result) {

    }

    @Override
    public void onUpdateDailyStatisticsError(String response) {

    }

    @Override
    public void onSearchDayDone(String result) throws UnsupportedEncodingException {
        if (!result.isEmpty()) {
            day = DataManager.getInstance().parseDay(result);

            SearchDailyStatisticsTask searchDailyStatisticsTask = new SearchDailyStatisticsTask(userAfterLogin.getId(), day.getId());
            searchDailyStatisticsTask.setSearchDailyStatisticsDelegate(walking2Activity);
//            AddDailyStatisticsTask addDailyStatisticsTask = new AddDailyStatisticsTask(day.getId(), totalCalories, userAfterLogin.getId());
//            addDailyStatisticsTask.setAddDailyStatisticsDelegate(homeActivity);


        }
    }

    @Override
    public void onSearchDailyStatisticsDone(String result) throws UnsupportedEncodingException {
        if (!result.isEmpty()) {
            dailyStatisticsObject = DataManager.getInstance().parseDailyStatistics(result);
        }
    }
}
