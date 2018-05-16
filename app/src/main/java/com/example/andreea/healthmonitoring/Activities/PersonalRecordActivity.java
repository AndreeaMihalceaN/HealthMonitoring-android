package com.example.andreea.healthmonitoring.Activities;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.andreea.healthmonitoring.R;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import manager.DataManager;
import model.DailyStatistics;
import model.Day;
import model.User;
import webservice.SearchDailyStatisticsByUserIdDelegate;
import webservice.SearchDailyStatisticsByUserIdTask;
import webservice.SearchDayByIdDelegate;
import webservice.SearchDayByIdTask;

public class PersonalRecordActivity extends AppCompatActivity implements SearchDailyStatisticsByUserIdDelegate, SearchDayByIdDelegate {
    private User userAfterLogin;
    private PersonalRecordActivity personalRecordActivity;
    private List<DailyStatistics> dailyStatisticsListForThisUser = new ArrayList<>();
    DailyStatistics dailyStatisticsObjectRecord = new DailyStatistics();
    DailyStatistics dailyStatisticsObjectPreviousRecord = new DailyStatistics();
    private Day day;
    private TextView textViewRecordDay;
    private TextView textViewNumberOfSteps;
    private GraphView graph;
    private double currentRecord = 0;
    private double previousRecord = 0;
    private TextView textViewPreviousRecord;
    private TextView textViewCurrentRecord;
    private static final String TAG = "PersonalRecordActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_record);
        personalRecordActivity = this;
        graph = (GraphView) findViewById(R.id.graph);

        textViewRecordDay = (TextView) findViewById(R.id.textViewRecordDay);
        textViewNumberOfSteps = (TextView) findViewById(R.id.textViewNumberOfSteps);
        textViewPreviousRecord = (TextView) findViewById(R.id.TextViewPreviousRecord);
        textViewCurrentRecord = (TextView) findViewById(R.id.TextViewCurrentRecord);


        Intent intent = getIntent();
        userAfterLogin = (User) intent.getSerializableExtra("userAfterLogin");

        SearchDailyStatisticsByUserIdTask searchDailyStatisticsByUserIdTask = new SearchDailyStatisticsByUserIdTask(userAfterLogin.getId());
        searchDailyStatisticsByUserIdTask.setSearchDailyStatisticsByUserIdDelegate(personalRecordActivity);
//
//        LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>(new DataPoint[]{
//                new DataPoint(0, previousRecord),
//                new DataPoint(1, currentRecord)
////                new DataPoint(2, 50)
////                new DataPoint(3, 78),
////                new DataPoint(4, 115)
//        });
//        graph.addSeries(series);
//        series.setColor(Color.YELLOW);
//        series.setDrawBackground(true);
//        series.setBackgroundColor(Color.rgb(51, 153, 255));
//        series.setDrawDataPoints(true);
//        series.setDataPointsRadius(10);


    }

    @Override
    public void onSearchDailyStatisticsByUserIdDone(String result) throws UnsupportedEncodingException {
        if (!result.isEmpty()) {
            dailyStatisticsListForThisUser = DataManager.getInstance().parseDailyStatisticsList(result);
            Log.i(TAG, "SearchDailyStatisticsByUserIdDone here");
            Log.d(TAG, "SearchDailyStatisticsByUserIdDone here");
            getObjectForRecord();
            getObjectForPreviousRecord();

            SearchDayByIdTask searchDayTask = new SearchDayByIdTask(dailyStatisticsObjectRecord.getDayId());
            searchDayTask.setSearchDayByIdDelegate(personalRecordActivity);

        }
    }

    private void getObjectForRecord() {
        double max = 0;
        for (DailyStatistics dailyStatisticsObject : dailyStatisticsListForThisUser) {
            if (max <= dailyStatisticsObject.getSteps()) {
                max = dailyStatisticsObject.getSteps();
                dailyStatisticsObjectRecord = dailyStatisticsObject;

            }
        }
    }

    private void getObjectForPreviousRecord() {
        double max = 0;
        for (DailyStatistics dailyStatisticsObject : dailyStatisticsListForThisUser) {
            if (!dailyStatisticsObject.equals(dailyStatisticsObjectRecord)) {
                if (max <= dailyStatisticsObject.getSteps()) {
                    max = dailyStatisticsObject.getSteps();
                    dailyStatisticsObjectPreviousRecord = dailyStatisticsObject;

                }
            }
        }
    }

    @Override
    public void onSearchDayByIdDone(String result) throws UnsupportedEncodingException {
        if (!result.isEmpty()) {
            day = DataManager.getInstance().parseDay(result);
            Log.i(TAG, "Day by id was found");
            Log.d(TAG, "Day by id was found");

            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
            Calendar cal = day.getDate();
            String text = sdf.format(cal.getTime());
            textViewRecordDay.setText(text);
            textViewNumberOfSteps.setText(dailyStatisticsObjectRecord.getSteps() + "");
            currentRecord = dailyStatisticsObjectRecord.getSteps();
            previousRecord = dailyStatisticsObjectPreviousRecord.getSteps();
            textViewPreviousRecord.setText("Previous record: " + previousRecord);
            textViewCurrentRecord.setText("Current record: " + currentRecord);


            LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>(new DataPoint[]{
                    new DataPoint(0, previousRecord),
                    new DataPoint(1, currentRecord)
//                new DataPoint(2, 50)
//                new DataPoint(3, 78),
//                new DataPoint(4, 115)
            });
            graph.removeAllSeries();
            graph.addSeries(series);
            graph.setScrollX(2);
            series.setColor(Color.YELLOW);
            series.setDrawBackground(true);
            series.setBackgroundColor(Color.rgb(51, 153, 255));
            series.setDrawDataPoints(true);
            series.setDataPointsRadius(10);

        }

    }
}