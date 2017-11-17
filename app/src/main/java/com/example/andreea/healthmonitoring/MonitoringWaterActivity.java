package com.example.andreea.healthmonitoring;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Toast;

import me.itangqi.waveloadingview.WaveLoadingView;

public class MonitoringWaterActivity extends AppCompatActivity {

    private WaveLoadingView m_waveWaveLoadingView;
    //private SeekBar m_seekBar;
    //Button m_buttonMinus;
    //Button m_buttonPlus;
    Button m_buttonContent;
    private int counter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monitoring_water);

        m_waveWaveLoadingView = (WaveLoadingView) findViewById(R.id.waveLoadingView);
        //m_seekBar = (SeekBar) findViewById(R.id.seekBar);
        //m_buttonMinus = (Button) findViewById(R.id.buttonMinus);
        //m_buttonPlus = (Button) findViewById(R.id.buttonPlus);
        m_buttonContent = (Button) findViewById(R.id.buttonContent);
        m_waveWaveLoadingView.setProgressValue(0);

//        m_seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
//
//            @Override
//            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//                m_waveWaveLoadingView.setProgressValue(progress);
//                if (progress < 50) {
//                    m_waveWaveLoadingView.setBottomTitle(String.format("%d%%", progress));
//                    m_waveWaveLoadingView.setCenterTitle("");
//                    m_waveWaveLoadingView.setTopTitle("");
//                } else if (progress < 80) {
//                    m_waveWaveLoadingView.setBottomTitle("");
//                    m_waveWaveLoadingView.setCenterTitle(String.format("%d%%", progress));
//                    m_waveWaveLoadingView.setTopTitle("");
//                } else {
//                    m_waveWaveLoadingView.setBottomTitle("");
//                    m_waveWaveLoadingView.setCenterTitle("");
//                    m_waveWaveLoadingView.setTopTitle(String.format("%d%%", progress));
//                }
//            }
//
//            @Override
//            public void onStartTrackingTouch(SeekBar seekBar) {
//
//            }
//
//            @Override
//            public void onStopTrackingTouch(SeekBar seekBar) {
//
//            }
//        });

//        m_buttonMinus.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                minusConter();
//                changeWave();
//            }
//        });

//        m_buttonPlus.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                plusConter();
//                changeWave();
//
//            }
//        });

        m_buttonContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                plusConter();
                changeWave();

            }
        });

    }

    private void changeWave() {
        int progress = counter * 100 / 8;
        if (progress == 100) {
            Toast.makeText(getApplicationContext(), "Felicitari!! Cantitatea necesara de apa pe zi a fost atinsa!", Toast.LENGTH_SHORT).show();
        } else if (progress > 100)
            progress = 100;

        m_waveWaveLoadingView.setProgressValue(progress);
        if (progress < 50) {
            m_waveWaveLoadingView.setBottomTitle(String.format("%d%%", progress));
            m_waveWaveLoadingView.setCenterTitle("");
            m_waveWaveLoadingView.setTopTitle("");
        } else if (progress < 80) {
            m_waveWaveLoadingView.setBottomTitle("");
            m_waveWaveLoadingView.setCenterTitle(String.format("%d%%", progress));
            m_waveWaveLoadingView.setTopTitle("");
        } else {
            m_waveWaveLoadingView.setBottomTitle("");
            m_waveWaveLoadingView.setCenterTitle("");
            m_waveWaveLoadingView.setTopTitle(String.format("%d%%", progress));
        }
    }


    private void resetCounter() {
        counter = 0;
        m_buttonContent.setText(counter + "");

    }

    private void plusConter() {
        counter++;
        m_buttonContent.setText(counter + "");

    }

//    private void minusConter() {
//        counter--;
//        validate();
//        m_buttonContent.setText(counter + "");
//
//    }
}
