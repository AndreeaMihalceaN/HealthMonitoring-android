package com.example.andreea.healthmonitoring.Activities;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.andreea.healthmonitoring.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

import model.Food;
import model.User;

public class NutritionActivity extends AppCompatActivity {

    private BarChart mChart;
    private Food food;
    private TextView textViewTitle;
    private static final String TAG = "NutritionActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nutrition);
        Intent intent = getIntent();
        food = (Food) intent.getSerializableExtra("selectedFood");
        textViewTitle = (TextView) findViewById(R.id.textViewTitle);
        textViewTitle.setText(food.getFoodname() + " nutrition statistics");

        Typeface font = Typeface.createFromAsset(getAssets(), "Analecta.ttf");
        textViewTitle.setTypeface(font);

        mChart = (BarChart) findViewById(R.id.chart1);
        mChart.getDescription().setEnabled(false);
        setData(3);
        mChart.setFitBars(true);

    }
    private void setData(int count) {
        Log.i(TAG, "Set values on chart");
        Log.d(TAG, "Set values on chart");
        List<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(0f, (float) food.getCarbohydrates()));
        entries.add(new BarEntry(1f, (float) food.getFats()));
        entries.add(new BarEntry(2f, (float) food.getProteins()));
        BarDataSet set = new BarDataSet(entries, "Carbohydrates-Fats-Proteins");
        set.setColors(ColorTemplate.MATERIAL_COLORS);
        BarData data = new BarData(set);
        data.setBarWidth(0.9f); // set custom bar width
        mChart.setData(data);
        mChart.setFitBars(true); // make the x-axis fit exactly all bars
        mChart.invalidate(); // refresh
        mChart.animateY(5000);

    }
}