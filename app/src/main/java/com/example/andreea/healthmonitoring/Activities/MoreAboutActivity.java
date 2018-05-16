package com.example.andreea.healthmonitoring.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;

import com.example.andreea.healthmonitoring.R;

import model.Food;

public class MoreAboutActivity extends AppCompatActivity {

    private WebView webView;
    private Food food;
    private static final String TAG = "LoginActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_about);
        Intent intent = getIntent();
        food = (Food) intent.getSerializableExtra("selectedFood");

        Log.i(TAG, "Create MoreAboutActivity");
        Log.d(TAG, "Create MoreAboutActivity");

        webView= (WebView)findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(food.getUrl());



    }
}
