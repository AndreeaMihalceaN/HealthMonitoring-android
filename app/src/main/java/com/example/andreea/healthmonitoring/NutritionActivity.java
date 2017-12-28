package com.example.andreea.healthmonitoring;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nutrition);
        Intent intent = getIntent();
        food = (Food) intent.getSerializableExtra("selectedFood");
        textViewTitle = (TextView) findViewById(R.id.textViewTitle);
        textViewTitle.setText(food.getFoodname()+" nutrition statistics");
        Typeface font = Typeface.createFromAsset(getAssets(), "Analecta.ttf");
        textViewTitle.setTypeface(font);

//        Typeface font = Typeface.createFromAsset(getAssets(), "Analecta.ttf");
//        ((TextView) findViewById(R.id.textViewTitleNutrition)).setTypeface(font);

        mChart = (BarChart) findViewById(R.id.chart1);
        mChart.getDescription().setEnabled(false);


        setData(3);
        mChart.setFitBars(true);

    }

    private void setData(int count) {
//        ArrayList<BarEntry> entries = new ArrayList<>();
//        entries.add(new BarEntry((float) food.getCarbohydrates(), 0));
////        for (int i = 0; i < count; i++) {
////            float value = (float) (Math.random() * 100);
////            yVals.add(new BarEntry(i, (int) value));
////        }
//        entries.add(new BarEntry((float)food.getFats(), 1));
//        entries.add(new BarEntry((float)food.getProteins(), 2));
//
//
//        BarDataSet dataset = new BarDataSet(entries, "Carbohydrates-Fats-Proteins");
//        dataset.setColors(ColorTemplate.COLORFUL_COLORS);
//
//
//        ArrayList<String> labels = new ArrayList<String>();
//        labels.add("Carbohydrates");
//        labels.add("Fats");
//        labels.add("Proteins");
////
////        BarDataSet set = new BarDataSet(yVals, "Carbohydrates-Fats-Proteins");
////        set.setColors(ColorTemplate.MATERIAL_COLORS);
////        set.setDrawValues(true);
////
////        BarData data= new BarData(set);
////        mChart.setData(data);
////        mChart.invalidate();
////        mChart.animateY(700);
//
//
//
////        ArrayList<IBarDataSet> dataSets = new ArrayList<>();
////        dataSets.add((IBarDataSet) dataset);
//        //BarData Data = new BarData(Date, dataSets);
//        BarData data = new BarData(labels, dataset);
//        mChart.setData(data);
//        mChart.animateY(5000);

        List<BarEntry> entries = new ArrayList<>();

        entries.add(new BarEntry(0f, (float) food.getCarbohydrates()));
        entries.add(new BarEntry(1f, (float)food.getFats()));
        entries.add(new BarEntry(2f, (float)food.getProteins()));
//        entries.add(new BarEntry(3f, 50f));
//        // gap of 2f
//        entries.add(new BarEntry(5f, 70f));
//        entries.add(new BarEntry(6f, 60f));

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