package com.example.andreea.healthmonitoring;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.SeekBar;

import me.itangqi.waveloadingview.WaveLoadingView;

public class MonitoringWaterActivity extends AppCompatActivity {

    private WaveLoadingView m_waveWaveLoadingView;
    private SeekBar m_seekBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monitoring_water);

        m_waveWaveLoadingView = (WaveLoadingView) findViewById(R.id.waveLoadingView);
        m_seekBar = (SeekBar) findViewById(R.id.seekBar);
        m_waveWaveLoadingView.setProgressValue(0);

        m_seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
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

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

    }
}
