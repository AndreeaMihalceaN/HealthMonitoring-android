package com.example.andreea.healthmonitoring;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import manager.DataManager;
import model.DailyStatistics;
import model.Day;
import model.User;
import webservice.AddDailyStatisticsDelegate;
import webservice.AddDailyStatisticsTask;
import webservice.AddDayDelegate;
import webservice.AddDayTask;
import webservice.SearchDailyStatistics2Delegate;
import webservice.SearchDailyStatistics2Task;
import webservice.SearchDailyStatisticsByUserIdDelegate;
import webservice.SearchDailyStatisticsByUserIdTask;
import webservice.SearchDailyStatisticsDelegate;
import webservice.SearchDailyStatisticsTask;
import webservice.SearchDay2Delegate;
import webservice.SearchDay2Task;
import webservice.SearchDayByIdDelegate;
import webservice.SearchDayDelegate;
import webservice.SearchDayTask;
import webservice.UpdateDailyStatisticsDelegate;
import webservice.UpdateDailyStatisticsTask;
import webservice.UpdateStatisticsDailyStepsDelegate;
import webservice.UpdateStatisticsDailyStepsTask;

public class Walking2Activity extends AppCompatActivity implements SensorEventListener, UpdateDailyStatisticsDelegate, SearchDayDelegate, SearchDailyStatisticsDelegate, SearchDailyStatisticsByUserIdDelegate, AddDayDelegate, AddDailyStatisticsDelegate, UpdateStatisticsDailyStepsDelegate, SearchDayByIdDelegate, SearchDailyStatistics2Delegate, SearchDay2Delegate {

    private SensorManager sensorManager;
    private TextView count;
    boolean activityRunning;
    private Walking2Activity walking2Activity;
    private Day currentDay;
    private String calendarString;
    private User userAfterLogin;
    private Day day;
    private DailyStatistics dailyStatisticsObject;
    private TextView textViewResultDay1;
    private TextView textViewResultDay2;
    private TextView textViewResultDay3;
    private TextView textViewResultDay4;
    private TextView textViewResultToday;
    private TextView verif;
    boolean forFill = false;
    private List<DailyStatistics> dailyStatisticsListForThisUser = new ArrayList<>();
    int j = 1;

    private float numberOfSteps;
    private boolean ok = false;
    private boolean okNewDay = false;
    private double day1 = 0;
    private double day2 = 0;
    private double day3 = 0;
    private double day4 = 0;
    private double dayToday = 0;
    private GraphView graph;
    private Button personalRecord;
    private String dateForTask;
    private List<String> dates = new ArrayList<>();
    private double initialValueSensor;
    private boolean first;
    private double stepsObjective;
    private double eventValue;
    private double eventValuePrevious = 0;


//    private Sensor mStepCounterSensor;
//    private Sensor mStepDetectorSensor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_walking2);
        count = (TextView) findViewById(R.id.tv_steps);
        walking2Activity = this;
        ok = false;
        okNewDay = false;
        Intent intent = getIntent();
        first = false;

        textViewResultDay1 = (TextView) findViewById(R.id.textViewResultDay1);
        textViewResultDay2 = (TextView) findViewById(R.id.textViewResultDay2);
        textViewResultDay3 = (TextView) findViewById(R.id.textViewResultDay3);
        textViewResultDay4 = (TextView) findViewById(R.id.textViewResultDay4);
        textViewResultToday = (TextView) findViewById(R.id.textViewResultToday);
        verif = (TextView) findViewById(R.id.verif);
        personalRecord = (Button) findViewById(R.id.personalRecord);

        graph = (GraphView) findViewById(R.id.graph);

        userAfterLogin = (User) intent.getSerializableExtra("userAfterLogin");
        eventValuePrevious = intent.getDoubleExtra("eventValuePrevious", eventValuePrevious);

        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        calendarString = df.format(c.getTime());

        stepsObjective = userAfterLogin.getStepsObjective();


        SearchDayTask searchDayTask = new SearchDayTask(calendarString);
        searchDayTask.setSearchDayDelegate(walking2Activity);

//        SearchDailyStatisticsByUserIdTask searchDailyStatisticsByUserIdTask = new SearchDailyStatisticsByUserIdTask(userAfterLogin.getId());
//        searchDailyStatisticsByUserIdTask.setSearchDailyStatisticsByUserIdDelegate(walking2Activity);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
//        mStepCounterSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
//        mStepDetectorSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);
//        SearchDayTask searchDayTask = new SearchDayTask(calendarString);
//        searchDayTask.setSearchDayDelegate(walking2Activity);

        personalRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //android.os.Process.killProcess(android.os.Process.myPid());
                Intent intent = new Intent(Walking2Activity.this, PersonalRecordActivity.class);
                intent.putExtra("userAfterLogin", userAfterLogin);

                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                //Toast.makeText(getApplicationContext(), "Am intrat in actiune", Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_activity_record, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.settingOb_id:
                Intent intent = new Intent(Walking2Activity.this, SettingObjectiveActivity.class);
                intent.putExtra("userAfterLogin", userAfterLogin);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            default:
                return super.onOptionsItemSelected(item);

        }

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
        sensorManager.unregisterListener(this);
        // if you unregister the last listener, the hardware will stop detecting step events
//        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        eventValue = event.values[0];
        if (activityRunning) {
            if (!first) {
                SearchDay2Task searchDay2Task = new SearchDay2Task(calendarString);
                searchDay2Task.setSearchDay2Delegate(walking2Activity);
//                if (!first) {
//                    initialValueSensor = event.values[0];
//                    first = true;
//                }
//
//                verif.setText(String.valueOf(initialValueSensor));
            } else {
                double differenceValue = 0;
                if (eventValuePrevious == 0) {
                    //differenceValue = eventValue - initialValueSensor + eventValuePrevious;
                    //differenceValue = eventValue - initialValueSensor;
                    differenceValue = eventValue + initialValueSensor - eventValuePrevious;
                    //verif.setText(String.valueOf(eventValue + " - " + initialValueSensor));
                } else {
                    differenceValue = eventValue + initialValueSensor - eventValuePrevious;
                    //verif.setText(String.valueOf(eventValue + " + " + initialValueSensor + " - " + eventValuePrevious));
                }
                count.setText(String.valueOf(differenceValue));
                textViewResultToday.setText(String.valueOf(differenceValue));
                //verif.setText(String.valueOf(eventValue + " - " + initialValueSensor + "+ " + eventValuePrevious));
                //dayToday = event.values[0];
                dayToday = differenceValue;

                //pentru refresh graph, trebuie sa verific pe tableta
                LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>(new DataPoint[]{
                        new DataPoint(0, day1),
                        new DataPoint(1, day2),
                        new DataPoint(2, day3),
                        new DataPoint(3, day4),
                        new DataPoint(4, dayToday)
                });
                graph.removeAllSeries();
                graph.addSeries(series);


                UpdateStatisticsDailyStepsTask updateStatisticsDailyStepsTask = new UpdateStatisticsDailyStepsTask(dailyStatisticsObject.getUserId(), dailyStatisticsObject.getDayId(), Double.parseDouble(count.getText().toString()));
                updateStatisticsDailyStepsTask.setUpdateStatisticsDailyStepsDelegate(walking2Activity);
                //am adaugat aici
                if (event.values[0] > stepsObjective) {
                    addNotification();
                    //Toast.makeText(this, "", Toast.LENGTH_SHORT).show();
                }
                float numberOfSteps = event.values[0];
                changeCaloriesInDatabase();
            }

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
                        .setContentTitle("Excellent work!")
                        .setContentText("You've met the step goal! Congratulations!");

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
    public void onSearchDailyStatisticsDone(String result) throws UnsupportedEncodingException, ParseException {
        if (!result.isEmpty()) {
            dailyStatisticsObject = DataManager.getInstance().parseDailyStatistics(result);
            textViewResultToday.setText(dailyStatisticsObject.getSteps() + "");


            int numberMonth = day.getDate().get(Calendar.MONTH) + 1;
            DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
            Calendar cal = day.getDate();
            for (int i = 1; i <= 4; i++) {
                cal.setTime(new Date());
                cal.add(Calendar.DAY_OF_YEAR, -i);
                Date dayPrev = cal.getTime();
                String dateString = dayPrev.toString();
                dateForTask = df.format(dayPrev);
                dates.add(dateString);

                //fillAllDays();

                //j = 4 - i + 1;
                //}
//            for (int i = 0; i <= 4; i++) {
                okNewDay = true;
                SearchDayTask searchDayTask = new SearchDayTask(dateForTask);
                searchDayTask.setSearchDayDelegate(walking2Activity);
            }
        } else {
            AddDailyStatisticsTask addDailyStatisticsTask = new AddDailyStatisticsTask(day.getId(), 0, userAfterLogin.getId(), 0);
            addDailyStatisticsTask.setAddDailyStatisticsDelegate(walking2Activity);
        }
    }

//    public void fillAllDays()
//    {
//        for(String stringDate: dates) {
//            forFill=true;
//            SearchDayTask searchDayTask = new SearchDayTask(stringDate);
//            searchDayTask.setSearchDayDelegate(walking2Activity);
//        }
//    }

    @Override
    public void onSearchDayDone(String result) throws UnsupportedEncodingException {
        if (!result.isEmpty()) {
            day = DataManager.getInstance().parseDay(result);

            if (okNewDay) {
                for (DailyStatistics dayObject : dailyStatisticsListForThisUser) {
                    if (dayObject.getDayId().equals(day.getId())) {
                        switch (j) {
                            case 1:
                                textViewResultDay4.setText(dayObject.getSteps() + "");
                                day4 = dayObject.getSteps();
                                //okNewDay = false;
                                j++;
                                refreshGraph();
                                break;
                            case 2:
                                textViewResultDay3.setText(dayObject.getSteps() + "");
                                day3 = dayObject.getSteps();
                                //okNewDay = false;
                                j++;
                                refreshGraph();
                                break;
                            case 3:
                                textViewResultDay2.setText(dayObject.getSteps() + "");
                                day2 = dayObject.getSteps();
                                //okNewDay = false;
                                j++;
                                refreshGraph();
                                break;
                            case 4:
                                textViewResultDay1.setText(dayObject.getSteps() + "");
                                day1 = dayObject.getSteps();
                                //okNewDay = false;
                                j++;
                                refreshGraph();
//                                LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>(new DataPoint[]{
//                                        new DataPoint(0, day1),
//                                        new DataPoint(1, day2),
//                                        new DataPoint(2, day3),
//                                        new DataPoint(3, day4),
//                                        new DataPoint(4, dayToday)
//                                });
//                                graph.addSeries(series);
                                break;

                        }
                    }
                }
            } else {
                SearchDailyStatisticsByUserIdTask searchDailyStatisticsByUserIdTask = new SearchDailyStatisticsByUserIdTask(userAfterLogin.getId());
                searchDailyStatisticsByUserIdTask.setSearchDailyStatisticsByUserIdDelegate(walking2Activity);
            }
        }
//        } else {
//            AddDayTask addDayTask = new AddDayTask(dateForTask);
//            addDayTask.setAddDayDelegate(walking2Activity);
//
//        }
    }


    public void refreshGraph() {
        LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>(new DataPoint[]{
                new DataPoint(0, day1),
                new DataPoint(1, day2),
                new DataPoint(2, day3),
                new DataPoint(3, day4),
                new DataPoint(4, dayToday)
        });
        graph.removeAllSeries();
        graph.addSeries(series);
    }

    @Override
    public void onSearchDailyStatisticsByUserIdDone(String result) throws UnsupportedEncodingException {
        if (!result.isEmpty()) {
            dailyStatisticsListForThisUser = DataManager.getInstance().parseDailyStatisticsList(result);

            SearchDailyStatisticsTask searchDailyStatisticsTask = new SearchDailyStatisticsTask(userAfterLogin.getId(), day.getId());
            searchDailyStatisticsTask.setSearchDailyStatisticsDelegate(walking2Activity);

        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            first = false;
            eventValuePrevious = eventValue;
            Intent intent = new Intent(this, HomeActivity.class);
            intent.putExtra("userAfterLogin", userAfterLogin);
            intent.putExtra("eventValuePrevious", eventValuePrevious);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        first = false;
        eventValuePrevious = eventValue;
        Intent intent = new Intent(this, HomeActivity.class);
        intent.putExtra("userAfterLogin", userAfterLogin);
        intent.putExtra("eventValuePrevious", eventValuePrevious);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    @Override
    public void onAddDayDone(String result) {
        //okNewDay = true;
//        SearchDayTask searchDayTask = new SearchDayTask(calendarString);
//        searchDayTask.setSearchDayDelegate(walking2Activity);
        SearchDailyStatisticsTask searchDailyStatisticsTask = new SearchDailyStatisticsTask(userAfterLogin.getId(), day.getId());
        searchDailyStatisticsTask.setSearchDailyStatisticsDelegate(walking2Activity);

    }

    @Override
    public void onAddDayError(String response) {

    }

    @Override
    public void onAddDailyStatisticsDone(String result) {
        SearchDailyStatisticsTask searchDailyStatisticsTask = new SearchDailyStatisticsTask(userAfterLogin.getId(), day.getId());
        searchDailyStatisticsTask.setSearchDailyStatisticsDelegate(walking2Activity);

    }

    @Override
    public void onAddDailyStatisticsError(String response) {

    }

    @Override
    public void onUpdateStatisticsDailyStepsDone(String result) {

    }

    @Override
    public void onUpdateStatisticsDailyStepsError(String response) {

    }

    @Override
    public void onSearchDayByIdDone(String result) throws UnsupportedEncodingException {
        if (!result.isEmpty()) {
            day = DataManager.getInstance().parseDay(result);
        }
    }

    @Override
    public void onSearchDailyStatistics2Done(String result) throws UnsupportedEncodingException, ParseException {
        if (!result.isEmpty()) {
            dailyStatisticsObject = DataManager.getInstance().parseDailyStatistics(result);
            initialValueSensor = dailyStatisticsObject.getSteps();
            eventValuePrevious = eventValue;
            first = true;
//            if (!first) {
//                initialValueSensor = eventValue;
//                first = true;
//            }

            //verif.setText(String.valueOf(initialValueSensor));
        }

    }

    @Override
    public void onSearchDay2Done(String result) throws UnsupportedEncodingException {
        if (!result.isEmpty()) {
            day = DataManager.getInstance().parseDay(result);

            SearchDailyStatistics2Task searchDailyStatistics2Task = new SearchDailyStatistics2Task(userAfterLogin.getId(), day.getId());
            searchDailyStatistics2Task.setSearchDailyStatistics2Delegate(walking2Activity);
        }

    }
}
