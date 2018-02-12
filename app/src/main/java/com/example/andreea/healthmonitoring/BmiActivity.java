package com.example.andreea.healthmonitoring;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.UnsupportedEncodingException;

import manager.DataManager;
import model.User;
import webservice.AddWeightStatisticsDelegate;
import webservice.AddWeightStatisticsTask;
import webservice.LoginDelegate;
import webservice.LoginTask;
import webservice.UpdateTask;
import webservice.UpdateWHDelegate;
import webservice.UpdateWHTask;

public class BmiActivity extends AppCompatActivity implements UpdateWHDelegate, LoginDelegate, AddWeightStatisticsDelegate {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bmi);

        bmiActivity = this;
        m_seekBarWeight = (SeekBar) findViewById(R.id.seekBarWeight);
        m_seekBarHeight = (SeekBar) findViewById(R.id.seekBarHeight);
        m_buttonWeight = (Button) findViewById(R.id.buttonWeight);
        m_buttonHeight = (Button) findViewById(R.id.buttonHeight);
        m_result = (TextView) findViewById(R.id.textView4);
        m_buttonUpdateWH = (Button) findViewById(R.id.buttonUpdateWH);
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


                Toast.makeText(getApplicationContext(), "Heeeeeeeeei", Toast.LENGTH_SHORT).show();
                UpdateWHTask updateWHTask = new UpdateWHTask(userAfterLogin.getUsername(), userAfterLogin.getPassword(), currentHeight, currentWeight);
                updateWHTask.setUpdateWHDelegate(bmiActivity);
                //Toast.makeText(bmiActivity, "S-a realizat update", Toast.LENGTH_SHORT).show();

                LoginTask loginTask = new LoginTask(userAfterLogin.getUsername(), userAfterLogin.getPassword());
                loginTask.setLoginDelegate(bmiActivity);

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
        AddWeightStatisticsTask addWeightStatisticsTask = new AddWeightStatisticsTask(userAfterLogin.getId(), currentWeight);
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

    }

    @Override
    public void onAddWeightStatisticsError(String response) {

    }
}
