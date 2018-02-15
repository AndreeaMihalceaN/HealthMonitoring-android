package com.example.andreea.healthmonitoring;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import model.MonthWeight;

public class ProgressActivity extends AppCompatActivity {

    BarChart barChart;
    List<MonthWeight> weightValuesForProgress = new ArrayList<>();
    ArrayList<BarEntry> barEntries = new ArrayList<BarEntry>();
    ArrayList<BarEntry> barEntries1 = new ArrayList<BarEntry>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress);

        Intent intent = getIntent();
        List<MonthWeight> weightValuesForProgress = (List<MonthWeight>) intent.getSerializableExtra("weightValuesForProgress");

        barEntries.clear();
        barEntries1.clear();
        barChart = (BarChart) findViewById(R.id.barchart);
        barChart.clear();
        barChart.setFitBars(true);
//        barChart.setDrawBarShadow(false);
//        barChart.setDrawValueAboveBar(true);
//        barChart.setMaxVisibleValueCount(50);
//        barChart.setPinchZoom(false);
//        barChart.setDrawGridBackground(true);

        //ArrayList<BarEntry> barEntries = new ArrayList<BarEntry>();
//        barEntries.add(new BarEntry(1, 40f));
//        barEntries.add(new BarEntry(2, 44f));
//        barEntries.add(new BarEntry(3, 30f));
//        barEntries.add(new BarEntry(4, 30f));
//        barEntries.add(new BarEntry(5, 30f));
//        barEntries.add(new BarEntry(6, 46f));
//        barEntries.add(new BarEntry(7, 20f));
        //barEntries.add(new BarEntry(0, 60f));

        //ArrayList<BarEntry> barEntries1 = new ArrayList<BarEntry>();
//        barEntries1.add(new BarEntry(1, 44f));
//        barEntries1.add(new BarEntry(2, 54f));
//        barEntries1.add(new BarEntry(3, 60f));
//        barEntries1.add(new BarEntry(4, 31f));
//        barEntries1.add(new BarEntry(5, 31f));
//        barEntries1.add(new BarEntry(6, 46f));
//        barEntries1.add(new BarEntry(7, 46f));
        for (MonthWeight monthWeight : weightValuesForProgress) {
            if (checkInbarEntries(monthWeight.getMonth())) {
                barEntries1.add(new BarEntry(monthWeight.getMonth(), (float) monthWeight.getWeight()));
            } else {
                barEntries.add(new BarEntry(monthWeight.getMonth(), (float) monthWeight.getWeight()));
            }

        }

        BarDataSet barDataSet = new BarDataSet(barEntries, "Weight1");
        barDataSet.setColors(ColorTemplate.COLORFUL_COLORS);

        BarDataSet barDataSet1 = new BarDataSet(barEntries1, "Weight2");
        barDataSet1.setColors(ColorTemplate.COLORFUL_COLORS);

        BarData data = new BarData(barDataSet, barDataSet1);
        //BarData data = new BarData(barDataSet);

        float groupSpace = 0.1f;
        float barSpace = 0.01f;
        float barWidth = 0.43f;

        barChart.setData(data);

        data.setBarWidth(barWidth);
        barChart.groupBars(1, groupSpace, barSpace);


        String[] months = new String[]{"Jan", "Feb", "Mar", "April", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
        Toast.makeText(this, months.length + "", Toast.LENGTH_SHORT).show();
        XAxis xAxis = barChart.getXAxis();
        xAxis.setValueFormatter(new MyXAxisValueFormatter(months));
        xAxis.setPosition(XAxis.XAxisPosition.BOTH_SIDED);
        xAxis.setGranularity(1);
        xAxis.setCenterAxisLabels(true);
        xAxis.setAxisMinimum(1);
        barChart.setFitBars(true); // make the x-axis fit exactly all bars
//        barChart.invalidate();
        //xAxis.setAxisMaximum(0.01f);
        //data.setBarWidth(0.9f); // set custom bar width
        //barChart.setData(data);
        //barChart.setFitBars(true); // make the x-axis fit exactly all bars
        barChart.invalidate(); // refresh
        barChart.animateY(5000);


    }

    public boolean checkInbarEntries(int month) {
        for (BarEntry barEntryItem : barEntries) {
            if (month == barEntryItem.getX())
                return true;
        }
        return false;
    }

    public class MyXAxisValueFormatter implements IAxisValueFormatter {
        private String[] mValues;

        public MyXAxisValueFormatter(String[] values) {
            this.mValues = values;

        }

        @Override
        public String getFormattedValue(float value, AxisBase axis) {
            return mValues[(int) value];
        }

    }
}
