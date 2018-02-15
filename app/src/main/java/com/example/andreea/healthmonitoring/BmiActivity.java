package com.example.andreea.healthmonitoring;

import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import manager.DataManager;
import model.Day;
import model.DayWeight;
import model.MonthWeight;
import model.User;
import model.WeightStatistics;
import webservice.AddDayDelegate;
import webservice.AddDayTask;
import webservice.AddWeightStatisticsDelegate;
import webservice.AddWeightStatisticsTask;
import webservice.LoginDelegate;
import webservice.LoginTask;
import webservice.SearchDayDelegate;
import webservice.SearchDayTask;
import webservice.SelectDayWeightObjectsForUserDelegate;
import webservice.SelectDayWeightObjectsForUserTask;
import webservice.UpdateWHDelegate;
import webservice.UpdateWHTask;

public class BmiActivity extends AppCompatActivity implements UpdateWHDelegate, LoginDelegate, AddWeightStatisticsDelegate, SearchDayDelegate, AddDayDelegate, SelectDayWeightObjectsForUserDelegate {

    private SeekBar m_seekBarWeight;
    private SeekBar m_seekBarHeight;
    private Button m_buttonWeight;
    private Button m_buttonHeight;
    private User userAfterLogin;
    private int currentWeight;
    private int currentHeight;
    private TextView m_result;
    private double BMI;
    private String state = "";
    private Button m_buttonUpdateWH;
    private BmiActivity bmiActivity;
    private Day currentDay;
    private String calendarString;
    private Button m_buttonViewProgress;
    boolean wantToSeeProgress = false;
    private Set<DayWeight> dayWeightList;//= new ArrayList<>();
    List<Double> ian = new ArrayList<>();
    List<Double> feb = new ArrayList<>();
    List<Double> mar = new ArrayList<>();
    List<Double> apr = new ArrayList<>();
    List<Double> may = new ArrayList<>();
    List<Double> jun = new ArrayList<>();
    List<Double> jul = new ArrayList<>();
    List<Double> aug = new ArrayList<>();
    List<Double> sep = new ArrayList<>();
    List<Double> oct = new ArrayList<>();
    List<Double> nov = new ArrayList<>();
    List<Double> dec = new ArrayList<>();
    List<MonthWeight> weightValuesForProgress= new ArrayList<MonthWeight>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bmi);
        bmiActivity = this;

        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        calendarString = df.format(c.getTime());

        m_seekBarWeight = (SeekBar) findViewById(R.id.seekBarWeight);
        m_seekBarHeight = (SeekBar) findViewById(R.id.seekBarHeight);
        m_buttonWeight = (Button) findViewById(R.id.buttonWeight);
        m_buttonHeight = (Button) findViewById(R.id.buttonHeight);
        m_result = (TextView) findViewById(R.id.textView4);
        m_buttonUpdateWH = (Button) findViewById(R.id.buttonUpdateWH);
        m_buttonViewProgress = (Button) findViewById(R.id.buttonViewProgress);
        m_seekBarWeight.setMax(350);
        m_seekBarHeight.setMax(130);


        m_buttonUpdateWH.setVisibility(View.INVISIBLE);

        Intent intent = getIntent();
        userAfterLogin = (User) intent.getSerializableExtra("userAfterLogin");
        currentWeight = userAfterLogin.getWeight();
        currentHeight = userAfterLogin.getHeight();

        m_buttonWeight.setText("Weight: " + currentWeight + " kg");
        m_buttonHeight.setText("Height: " + currentHeight + " cm");
        m_seekBarWeight.setProgress(currentWeight);
        m_seekBarHeight.setProgress(currentHeight);


        state();

        m_seekBarWeight.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // TODO Auto-generated method stub

                progress += 30;

                m_buttonWeight.setText("Weight: " + progress + " kg");
                currentWeight = progress;
                state();

            }
        });

        m_seekBarHeight.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // TODO Auto-generated method stub

                progress += 100;

                m_buttonHeight.setText("Height: " + progress + " cm");
                currentHeight = progress;
                state();
            }
        });

        m_buttonUpdateWH.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                //Toast.makeText(getApplicationContext(), "Heeeeeeeeei", Toast.LENGTH_SHORT).show();
                SearchDayTask searchDayTask = new SearchDayTask(calendarString);
                searchDayTask.setSearchDayDelegate(bmiActivity);
//
//                UpdateWHTask updateWHTask = new UpdateWHTask(userAfterLogin.getUsername(), userAfterLogin.getPassword(), currentHeight, currentWeight);
//                updateWHTask.setUpdateWHDelegate(bmiActivity);
//                //Toast.makeText(bmiActivity, "S-a realizat update", Toast.LENGTH_SHORT).show();
//
//                LoginTask loginTask = new LoginTask(userAfterLogin.getUsername(), userAfterLogin.getPassword());
//                loginTask.setLoginDelegate(bmiActivity);

            }
        });

        m_buttonViewProgress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wantToSeeProgress = true;
                SelectDayWeightObjectsForUserTask selectWeightStatisticsUserDayTask = new SelectDayWeightObjectsForUserTask(userAfterLogin.getId());
                selectWeightStatisticsUserDayTask.setSelectDayWeightObjectsForUserDelegate(bmiActivity);


            }
        });


    }

    public void state() {
        double s = (currentHeight / 100.0) * (currentHeight / 100.0);
        BMI = Math.floor(currentWeight / s * 100) / 100;
        if (currentHeight == 0)
            BMI = 0;
        if (BMI < 18.5)
            state = "Underweight";
        else if (BMI >= 18.5 && BMI < 25)
            state = "Normal or Healthy Weight";
        else if (BMI >= 25.0 && BMI < 30.0)
            state = "Overweight";
        else if (BMI >= 30.0)
            state = "Obese";
        m_result.setText(BMI + "\n" + state);

        if (currentHeight == userAfterLogin.getHeight() && currentWeight == userAfterLogin.getWeight())
            m_buttonUpdateWH.setVisibility(View.INVISIBLE);
        else m_buttonUpdateWH.setVisibility(View.VISIBLE);

    }

    @Override
    public void onUpdateDone(String result) {
        AddWeightStatisticsTask addWeightStatisticsTask = new AddWeightStatisticsTask(userAfterLogin.getId(), currentDay.getId(), currentWeight);
        addWeightStatisticsTask.setAddWeightStatisticsDelegate(bmiActivity);

    }

    @Override
    public void onUpdateError(String response) {
        Toast.makeText(this, response, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLoginDone(String result) throws UnsupportedEncodingException {
        if (!result.isEmpty()) {
            User user = DataManager.getInstance().parseUser(result);
            userAfterLogin = user;
            state();
        }
    }

    @Override
    public void onAddWeightStatisticsDone(String result) {
        LoginTask loginTask = new LoginTask(userAfterLogin.getUsername(), userAfterLogin.getPassword());
        loginTask.setLoginDelegate(bmiActivity);

    }

    @Override
    public void onAddWeightStatisticsError(String response) {

    }

    @Override
    public void onSearchDayDone(String result) throws UnsupportedEncodingException {
        if (!result.isEmpty()) {
            currentDay = DataManager.getInstance().parseDay(result);

            if (!wantToSeeProgress) {
                UpdateWHTask updateWHTask = new UpdateWHTask(userAfterLogin.getUsername(), userAfterLogin.getPassword(), currentHeight, currentWeight);
                updateWHTask.setUpdateWHDelegate(bmiActivity);
            } //else {
//                SelectWeightStatisticsUserDayTask selectWeightStatisticsUserDayTask = new SelectWeightStatisticsUserDayTask(userAfterLogin.getId(), currentDay.getId());
//                selectWeightStatisticsUserDayTask.setSelectWeightStatisticsUserDayDelegate(bmiActivity);
//            }
//            AddDailyStatisticsTask addDailyStatisticsTask = new AddDailyStatisticsTask(day.getId(), totalCalories, userAfterLogin.getId());
//            addDailyStatisticsTask.setAddDailyStatisticsDelegate(homeActivity);

        } else {
            if (!wantToSeeProgress) {
                AddDayTask addDayTask = new AddDayTask(calendarString);
                addDayTask.setAddDayDelegate(bmiActivity);
            }

        }
    }

    @Override
    public void onAddDayDone(String result) {
        SearchDayTask searchDayTask = new SearchDayTask(calendarString);
        searchDayTask.setSearchDayDelegate(bmiActivity);

    }

    @Override
    public void onAddDayError(String response) {

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent = new Intent(this, HomeActivity.class);
            intent.putExtra("userAfterLogin", userAfterLogin);
//            intent.putExtra("foodToSend", foodReceived);
//            intent.putExtra("quantityFromHolderSelected", currentQuantity);
//            intent.putExtra("calendarString", stringDate);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, HomeActivity.class);
        intent.putExtra("userAfterLogin", userAfterLogin);
//        intent.putExtra("foodToSend", foodReceived);
//        intent.putExtra("quantityFromHolderSelected", currentQuantity);
//        intent.putExtra("calendarString", stringDate);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }


    @Override
    public void onSelectDayWeightObjectsForUserDone(String result) throws UnsupportedEncodingException {

        if (!result.isEmpty()) {
            dayWeightList = DataManager.getInstance().parseDayWeightList(result);
            //Toast.makeText(bmiActivity, dayWeightList.size()+"", Toast.LENGTH_SHORT).show();

            for (DayWeight dayWeight : dayWeightList) {
                int numberMonth = dayWeight.getDay().getDate().get(Calendar.MONTH)+1;
                switch (numberMonth) {
                    case 1:
                        ian.add(dayWeight.getCurrentWeight());
                        break;
                    case 2:
                        feb.add(dayWeight.getCurrentWeight());
                        break;
                    case 3:
                        mar.add(dayWeight.getCurrentWeight());
                        break;
                    case 4:
                        apr.add(dayWeight.getCurrentWeight());
                        break;
                    case 5:
                        may.add(dayWeight.getCurrentWeight());
                        break;
                    case 6:
                        jun.add(dayWeight.getCurrentWeight());
                        break;
                    case 7:
                        jul.add(dayWeight.getCurrentWeight());
                        break;
                    case 8:
                        aug.add(dayWeight.getCurrentWeight());
                        break;
                    case 9:
                        sep.add(dayWeight.getCurrentWeight());
                        break;
                    case 10:
                        oct.add(dayWeight.getCurrentWeight());
                        break;
                    case 11:
                        nov.add(dayWeight.getCurrentWeight());
                        break;
                    case 12:
                        dec.add(dayWeight.getCurrentWeight());
                        break;

                }

            }

            for(int i=1; i<=12; i++)
            {
                switch (i)
                {
                    case 1:
                        int sizeIan=ian.size();
                        if(sizeIan>=2)
                        {
                            weightValuesForProgress.add(new MonthWeight(0, ian.get(sizeIan-1)));
                            weightValuesForProgress.add(new MonthWeight(0, ian.get(sizeIan-2)));
                        }
                        else if(sizeIan==1)
                        {
                            weightValuesForProgress.add(new MonthWeight(0, ian.get(0)));
                        }

                        break;
                    case 2:
                        int sizeFeb=feb.size();
                        if(sizeFeb>=2)
                        {
                            weightValuesForProgress.add(new MonthWeight(1, feb.get(sizeFeb-1)));
                            weightValuesForProgress.add(new MonthWeight(1, feb.get(sizeFeb-2)));
                        }
                        else if(sizeFeb==1)
                        {
                            weightValuesForProgress.add(new MonthWeight(1, feb.get(0)));
                        }


                        break;
                    case 3:
                        int sizeMar=mar.size();
                        if(sizeMar>=2)
                        {
                            weightValuesForProgress.add(new MonthWeight(2, mar.get(sizeMar-1)));
                            weightValuesForProgress.add(new MonthWeight(2, mar.get(sizeMar-2)));
                        }
                        else if(sizeMar==1)
                        {
                            weightValuesForProgress.add(new MonthWeight(2, mar.get(0)));
                        }


                        break;
                    case 4:
                        int sizeApr=apr.size();
                        if(sizeApr>=2)
                        {
                            weightValuesForProgress.add(new MonthWeight(3, apr.get(sizeApr-1)));
                            weightValuesForProgress.add(new MonthWeight(3, apr.get(sizeApr-2)));
                        }
                        else if(sizeApr==1)
                        {
                            weightValuesForProgress.add(new MonthWeight(3, apr.get(0)));
                        }


                        break;
                    case 5:
                        int sizeMay=may.size();
                        if(sizeMay>=2)
                        {
                            weightValuesForProgress.add(new MonthWeight(4, may.get(sizeMay-1)));
                            weightValuesForProgress.add(new MonthWeight(4, may.get(sizeMay-2)));
                        }
                        else if(sizeMay==1)
                        {
                            weightValuesForProgress.add(new MonthWeight(4, may.get(0)));
                        }


                        break;
                    case 6:
                        int sizeJun=jun.size();
                        if(sizeJun>=2)
                        {
                            weightValuesForProgress.add(new MonthWeight(5, jun.get(sizeJun-1)));
                            weightValuesForProgress.add(new MonthWeight(5, jun.get(sizeJun-2)));
                        }
                        else if(sizeJun==1)
                        {
                            weightValuesForProgress.add(new MonthWeight(5, jun.get(0)));
                        }


                        break;
                    case 7:
                        int sizeJul=jul.size();
                        if(sizeJul>=2)
                        {
                            weightValuesForProgress.add(new MonthWeight(6, jul.get(sizeJul-1)));
                            weightValuesForProgress.add(new MonthWeight(6, jul.get(sizeJul-2)));
                        }
                        else if(sizeJul==1)
                        {
                            weightValuesForProgress.add(new MonthWeight(6, jul.get(0)));
                        }


                        break;
                    case 8:
                        int sizeAug=aug.size();
                        if(sizeAug>=2)
                        {
                            weightValuesForProgress.add(new MonthWeight(7, aug.get(sizeAug-1)));
                            weightValuesForProgress.add(new MonthWeight(7, aug.get(sizeAug-2)));
                        }
                        else if(sizeAug==1)
                        {
                            weightValuesForProgress.add(new MonthWeight(7, aug.get(0)));
                        }


                        break;
                    case 9:
                        int sizeSep=sep.size();
                        if(sizeSep>=2)
                        {
                            weightValuesForProgress.add(new MonthWeight(8, sep.get(sizeSep-1)));
                            weightValuesForProgress.add(new MonthWeight(8, sep.get(sizeSep-2)));
                        }
                        else if(sizeSep==1)
                        {
                            weightValuesForProgress.add(new MonthWeight(8, sep.get(0)));
                        }


                        break;
                    case 10:
                        int sizeOct=oct.size();
                        if(sizeOct>=2)
                        {
                            weightValuesForProgress.add(new MonthWeight(9, oct.get(sizeOct-1)));
                            weightValuesForProgress.add(new MonthWeight(9, oct.get(sizeOct-2)));
                        }
                        else if(sizeOct==1)
                        {
                            weightValuesForProgress.add(new MonthWeight(9, oct.get(0)));
                        }


                        break;
                    case 11:
                        int sizeNov=nov.size();
                        if(sizeNov>=2)
                        {
                            weightValuesForProgress.add(new MonthWeight(10, nov.get(sizeNov-1)));
                            weightValuesForProgress.add(new MonthWeight(10, nov.get(sizeNov-2)));
                        }
                        else if(sizeNov==1)
                        {
                            weightValuesForProgress.add(new MonthWeight(10, nov.get(0)));
                        }


                        break;
                    case 12:
                        int sizeDec=dec.size();
                        if(sizeDec>=2)
                        {
                            weightValuesForProgress.add(new MonthWeight(11, dec.get(sizeDec-1)));
                            weightValuesForProgress.add(new MonthWeight(11, dec.get(sizeDec-2)));
                        }
                        else if(sizeDec==1)
                        {
                            weightValuesForProgress.add(new MonthWeight(11, dec.get(0)));
                        }

                        break;
                }
            }


            Intent intent = new Intent(BmiActivity.this, ProgressActivity.class);
            intent.putExtra("weightValuesForProgress", (Serializable) weightValuesForProgress);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }

    }
}
