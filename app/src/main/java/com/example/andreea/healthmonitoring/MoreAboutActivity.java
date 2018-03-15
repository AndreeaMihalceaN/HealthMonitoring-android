package com.example.andreea.healthmonitoring;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;

import model.Food;

public class MoreAboutActivity extends AppCompatActivity {

    private WebView webView;
    private Food food;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_about);
        Intent intent = getIntent();
        food = (Food) intent.getSerializableExtra("selectedFood");

        webView= (WebView)findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(food.getUrl());



    }
}
