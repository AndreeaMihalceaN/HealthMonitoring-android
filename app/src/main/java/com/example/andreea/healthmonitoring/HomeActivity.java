package com.example.andreea.healthmonitoring;

import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

import java.util.ArrayList;

import model.User;

public class HomeActivity extends AppCompatActivity {
    private TextView textEdit;
    private Button m_buttonFoodDiary;
    private Button m_buttonFoodSuggestion;
    private Button m_buttonConsulting;
    private Button m_buttonStartWalking;
    private Button m_buttonNeareastHospital;
    private Button m_buttonML;
    private User userAfterLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        textEdit = (TextView) findViewById(R.id.textViewH);
        PieChart pieChart = (PieChart) findViewById(R.id.piechart);
        m_buttonFoodDiary = (Button) findViewById(R.id.buttonFoodDiary);
        m_buttonFoodSuggestion = (Button) findViewById(R.id.buttonFoodSuggestion);
        m_buttonConsulting = (Button) findViewById(R.id.buttonConsulting);
        m_buttonStartWalking = (Button) findViewById(R.id.buttonStartWalking);
        m_buttonNeareastHospital = (Button) findViewById(R.id.buttonNeareastHospital);
        m_buttonML = (Button) findViewById(R.id.buttonML);


        pieChart.setUsePercentValues(true);
        Intent intent = getIntent();
        //Bundle bundle = intent.getExtras();

        //if (bundle != null) {
        //String username = (String) bundle.get("username");
        //User userAfterLogin= (User) bundle.get("userAfterLogin");

        userAfterLogin = (User) intent.getSerializableExtra("userAfterLogin");
        textEdit.setText("Hello " + userAfterLogin.getUsername() + "!");
        //}

        ArrayList<Entry> yvalues = new ArrayList<Entry>();
        yvalues.add(new Entry(8f, 0));
        yvalues.add(new Entry(15f, 1));
        yvalues.add(new Entry(12f, 2));
//        yvalues.add(new Entry(25f, 3));
//        yvalues.add(new Entry(23f, 4));
//        yvalues.add(new Entry(17f, 5));

        PieDataSet dataSet = new PieDataSet(yvalues, "Election Results");

        ArrayList<String> xVals = new ArrayList<String>();

        xVals.add("January");
        xVals.add("February");
        xVals.add("March");
//        xVals.add("April");
//        xVals.add("May");
//        xVals.add("June");

        PieData data = new PieData(xVals, dataSet);

        // In percentage Term
        data.setValueFormatter(new PercentFormatter());
// Default value
//data.setValueFormatter(new DefaultValueFormatter(0));

        pieChart.setData(data);
        dataSet.setColors(ColorTemplate.JOYFUL_COLORS);


        m_buttonFoodDiary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, FoodDiaryActivity.class);
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
                Intent intent = new Intent(HomeActivity.this, ProfileActivity.class);
                intent = new Intent(HomeActivity.this, ProfileActivity.class);
                intent.putExtra("userAfterLogin", userAfterLogin);
                startActivity(intent);
                startActivity(intent);
                //break;
                return true;
            case R.id.LogOut_id:
                //Toast.makeText(getApplicationContext(), "Log Out icon is selected", Toast.LENGTH_SHORT).show();
                Intent intent2 = new Intent(HomeActivity.this, LoginActivity.class);
                startActivity(intent2);
                //break;
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }

    }
}