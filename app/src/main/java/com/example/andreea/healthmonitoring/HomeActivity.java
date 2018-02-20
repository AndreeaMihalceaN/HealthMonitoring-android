package com.example.andreea.healthmonitoring;

import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import manager.DataManager;
import model.DailyStatistics;
import model.Day;
import model.QuantityFood;
import model.User;
import webservice.AddDailyStatisticsDelegate;
import webservice.AddDailyStatisticsTask;
import webservice.GetAllFoodsFromThisDayTask;
import webservice.GetQuantityFoodDelegate;
import webservice.GetQuantityFoodTask;
import webservice.SearchDailyStatisticsDelegate;
import webservice.SearchDailyStatisticsTask;
import webservice.SearchDayDelegate;
import webservice.SearchDayTask;
import webservice.UpdateDailyStatisticsDelegate;
import webservice.UpdateDailyStatisticsTask;

public class HomeActivity extends AppCompatActivity implements GetQuantityFoodDelegate, SearchDayDelegate, AddDailyStatisticsDelegate, SearchDailyStatisticsDelegate, UpdateDailyStatisticsDelegate {
    private TextView textEdit;
    //    private Button m_buttonFoodDiary;
//    private Button m_buttonFoodSuggestion;
    private CardView m_cardViewConsulting;
    //    private Button m_buttonStartWalking;
    private CardView m_cardViewHospital;
    //    private Button m_buttonML;
    private User userAfterLogin;
    private CardView m_cardViewFoodDiary;
    private CardView m_cardViewStartWalking;
    private CardView m_cardViewFoodSugestion;
    private Day currentDay;
    private String calendarString;
    List<QuantityFood> quantityFoodList = new ArrayList<>();
    private HomeActivity homeActivity;
    private double totalCalories = 0;
    private Day day;
    private DailyStatistics dailyStatisticsObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        homeActivity = this;

        //textEdit = (TextView) findViewById(R.id.textViewH);
//        PieChart pieChart = (PieChart) findViewById(R.id.piechart);
        m_cardViewFoodDiary = (CardView) findViewById(R.id.cardViewFoodDiary);
        m_cardViewStartWalking = (CardView) findViewById(R.id.cardViewStartWalking);
        m_cardViewHospital = (CardView) findViewById(R.id.cardViewHospital);
        m_cardViewFoodSugestion = (CardView) findViewById(R.id.cardViewFoodSugestion);
//        m_buttonFoodSuggestion = (Button) findViewById(R.id.buttonFoodSuggestion);
        m_cardViewConsulting = (CardView) findViewById(R.id.cardViewConsulting);
//        m_buttonStartWalking = (Button) findViewById(R.id.buttonStartWalking);
//        m_buttonNeareastHospital = (Button) findViewById(R.id.buttonNeareastHospital);
//        m_buttonML = (Button) findViewById(R.id.buttonML);

        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        calendarString = df.format(c.getTime());


//        pieChart.setUsePercentValues(true);
        Intent intent = getIntent();
        //Bundle bundle = intent.getExtras();

        //if (bundle != null) {
        //String username = (String) bundle.get("username");
        //User userAfterLogin= (User) bundle.get("userAfterLogin");

        userAfterLogin = (User) intent.getSerializableExtra("userAfterLogin");

        SearchDayTask searchDayTask = new SearchDayTask(calendarString);
        searchDayTask.setSearchDayDelegate(homeActivity);
//        GetQuantityFoodTask getQuantityFoodTask = new GetQuantityFoodTask(calendarString, userAfterLogin.getUsername());
//        getQuantityFoodTask.setGetQuantityFoodDelegate(homeActivity);
        //textEdit.setText("Hello " + userAfterLogin.getUsername() + "!");
        //}

//        ArrayList<Entry> yvalues = new ArrayList<Entry>();
//        yvalues.add(new Entry(8f, 0));
//        yvalues.add(new Entry(15f, 1));
//        yvalues.add(new Entry(12f, 2));
////        yvalues.add(new Entry(25f, 3));
////        yvalues.add(new Entry(23f, 4));
////        yvalues.add(new Entry(17f, 5));
//
//        PieDataSet dataSet = new PieDataSet(yvalues, "Election Results");
//
//        ArrayList<String> xVals = new ArrayList<String>();
//
//        xVals.add("January");
//        xVals.add("February");
//        xVals.add("March");
////        xVals.add("April");
////        xVals.add("May");
////        xVals.add("June");
//
//        PieData data = new PieData(xVals, dataSet);
//
//        // In percentage Term
//        data.setValueFormatter(new PercentFormatter());
//// Default value
////data.setValueFormatter(new DefaultValueFormatter(0));
//
//        pieChart.setData(data);
//        dataSet.setColors(ColorTemplate.JOYFUL_COLORS);


        m_cardViewFoodDiary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, FoodDiaryActivity.class);
                //startActivity(intent);

                intent.putExtra("userAfterLogin", userAfterLogin);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);

                //Toast.makeText(getApplicationContext(), "Am intrat in actiune", Toast.LENGTH_SHORT).show();
            }
        });

        m_cardViewStartWalking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, Walking2Activity.class);

                intent.putExtra("userAfterLogin", userAfterLogin);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                //Toast.makeText(getApplicationContext(), "Am intrat in actiune", Toast.LENGTH_SHORT).show();
            }
        });

        m_cardViewHospital.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, BmiActivity.class);
                intent.putExtra("userAfterLogin", userAfterLogin);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                //Toast.makeText(getApplicationContext(), "Am intrat in actiune", Toast.LENGTH_SHORT).show();
            }
        });

        m_cardViewFoodSugestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, FoodSugestionActivity.class);
                intent.putExtra("userAfterLogin", userAfterLogin);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                //Toast.makeText(getApplicationContext(), "Am intrat in actiune", Toast.LENGTH_SHORT).show();
            }
        });

        m_cardViewConsulting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, MapsActivity.class);
//                intent.putExtra("userAfterLogin", userAfterLogin);
//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                //Toast.makeText(getApplicationContext(), "Am intrat in actiune", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_activity_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.profile_id:
                //Toast.makeText(getApplicationContext(), "Profile icon is selected", Toast.LENGTH_SHORT).show();
                //Intent intent = new Intent(HomeActivity.this, ProfileActivity.class);
                Intent intent = new Intent(HomeActivity.this, ProfileActivity.class);
                intent.putExtra("userAfterLogin", userAfterLogin);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                //startActivity(intent);
                return true;
            case R.id.LogOut_id:
                //Toast.makeText(getApplicationContext(), "Log Out icon is selected", Toast.LENGTH_SHORT).show();
                Intent intent2 = new Intent(HomeActivity.this, LoginActivity.class);
                intent2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent2);
                return true;
            case R.id.setting_id:
                intent = new Intent(HomeActivity.this, EditAccountActivity.class);
                intent.putExtra("userAfterLogin", userAfterLogin);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            default:
                return super.onOptionsItemSelected(item);

        }

    }

    @Override
    public void onGetQuantityFoodDone(String result) throws UnsupportedEncodingException {
        if (!result.isEmpty()) {
            quantityFoodList = DataManager.getInstance().parseQuantityFoodList(result);
            for (QuantityFood qFood : quantityFoodList) {
                totalCalories += qFood.getQuantity() * (qFood.getFood().getCarbohydrates() * 4 + qFood.getFood().getProteins() * 4 + qFood.getFood().getFats() * 9);
            }

            SearchDailyStatisticsTask searchDailyStatisticsTask = new SearchDailyStatisticsTask(userAfterLogin.getId(), day.getId());
            searchDailyStatisticsTask.setSearchDailyStatisticsDelegate(homeActivity);
//            SearchDayTask searchDayTask = new SearchDayTask(calendarString);
//            searchDayTask.setSearchDayDelegate(homeActivity);
        }

    }

    @Override
    public void onSearchDayDone(String result) throws UnsupportedEncodingException {
        if (!result.isEmpty()) {
            day = DataManager.getInstance().parseDay(result);

            GetQuantityFoodTask getQuantityFoodTask = new GetQuantityFoodTask(calendarString, userAfterLogin.getUsername());
            getQuantityFoodTask.setGetQuantityFoodDelegate(homeActivity);
//            SearchDailyStatisticsTask searchDailyStatisticsTask = new SearchDailyStatisticsTask(userAfterLogin.getId(),day.getId());
//            searchDailyStatisticsTask.setSearchDailyStatisticsDelegate(homeActivity);
//            AddDailyStatisticsTask addDailyStatisticsTask = new AddDailyStatisticsTask(day.getId(), totalCalories, userAfterLogin.getId());
//            addDailyStatisticsTask.setAddDailyStatisticsDelegate(homeActivity);


        }
    }

    @Override
    public void onAddDailyStatisticsDone(String result) {

    }

    @Override
    public void onAddDailyStatisticsError(String response) {
        Toast.makeText(HomeActivity.this, response, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onSearchDailyStatisticsDone(String result) throws UnsupportedEncodingException {
        if (!result.isEmpty()) {
            dailyStatisticsObject = DataManager.getInstance().parseDailyStatistics(result);
            UpdateDailyStatisticsTask updateDailyStatisticsTask = new UpdateDailyStatisticsTask(dailyStatisticsObject.getUserId(), dailyStatisticsObject.getDayId(), totalCalories);
            updateDailyStatisticsTask.setUpdateDailyStatisticsDelegate(homeActivity);


        } else {
            AddDailyStatisticsTask addDailyStatisticsTask = new AddDailyStatisticsTask(day.getId(), totalCalories, userAfterLogin.getId(), 0);
            addDailyStatisticsTask.setAddDailyStatisticsDelegate(homeActivity);
        }

    }

    @Override
    public void onUpdateDailyStatisticsDone(String result) {

    }

    @Override
    public void onUpdateDailyStatisticsError(String response) {

    }
}