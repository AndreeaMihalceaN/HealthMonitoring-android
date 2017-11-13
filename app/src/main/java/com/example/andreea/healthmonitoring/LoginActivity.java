package com.example.andreea.healthmonitoring;

import android.content.SharedPreferences;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;

import manager.DataManager;
import model.User;
import webservice.LoginDelegate;
import webservice.LoginTask;

public class LoginActivity extends AppCompatActivity implements LoginDelegate {

    private ProgressBar progressBarSpinner;
    private CheckBox checkBox_RememberMe;
    private CardView btnSignIn;
    private EditText editTextUsername;
    private EditText editTextPassword;
    private SharedPreferences loginPreferences;
    private SharedPreferences.Editor loginPrefsEditor;
    private Boolean saveLogin;
    private LoginActivity loginActivity;
    private CardView m_cardSignUp;
    private String username;
    private String password;
    private User userAfterLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginActivity = this;

        getLoginPreferences();
        progressBarSpinner = (ProgressBar) findViewById(R.id.progressBar);
        btnSignIn = (CardView) findViewById(R.id.buttonSignIn);
        progressBarSpinner.setVisibility(View.GONE);
        m_cardSignUp = (CardView) findViewById(R.id.cardSignUp);


        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isNetworkConnected()) {
                    Toast.makeText(LoginActivity.this, "Please check your internet connection!", Toast.LENGTH_SHORT).show();
                } else {

                    username = editTextUsername.getText().toString();
                    password = editTextPassword.getText().toString();

                    progressBarSpinner.setVisibility(View.VISIBLE);

//                    if (checkBox_RememberMe.isChecked()) {
//                        // remember username and password
//                        loginPrefsEditor.putBoolean("saveLogin", true);
//                        loginPrefsEditor.putString("username", username);
//                        loginPrefsEditor.putString("password", password);
//                        loginPrefsEditor.commit();
//
//                    } else {
//                        loginPrefsEditor.clear();
//                        loginPrefsEditor.commit();
//                    }

                    LoginTask loginTask = new LoginTask(username, password);
                    loginTask.setLoginDelegate(loginActivity);
//
//                    Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
//                    intent.putExtra("Username", username);
//                    startActivity(intent);

                }
            }
        });


        m_cardSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);

                //Toast.makeText(getApplicationContext(), "Am intrat in actiune", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void startNewActivity(User user) {
        Intent myIntent;

        if (!user.getUsername().isEmpty() && !user.getFirstName().isEmpty()) {
//            myIntent = new Intent(LoginActivity.this, HomeActivity.class);
//            myIntent.putExtra("username", user.getUsername());
//            myIntent.putExtra("password", user.getPassword());
//
//            startActivity(myIntent);
            userAfterLogin = user;
            myIntent = new Intent(LoginActivity.this, HomeActivity.class);
            myIntent.putExtra("userAfterLogin", userAfterLogin);
            startActivity(myIntent);
        }
    }
//
//    public void serve(View v)
//    {
//        m_textViewSignUp = (TextView) findViewById(R.id.textViewSignUp);
//        Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
//        startActivity(intent);
//    }

    public void getLoginPreferences() {

        editTextUsername = (EditText) findViewById(R.id.editTextUsername);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        checkBox_RememberMe = (CheckBox) findViewById(R.id.checkBoxRememberMe);

        loginPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        loginPrefsEditor = loginPreferences.edit();

        saveLogin = loginPreferences.getBoolean("saveLogin", false);
        if (saveLogin == true) {
            editTextUsername.setText(loginPreferences.getString("username", ""));
            editTextPassword.setText(loginPreferences.getString("password", ""));
            checkBox_RememberMe.setChecked(true);
        }
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null;
    }

    @Override
    public void onLoginDone(String result) throws UnsupportedEncodingException {

        progressBarSpinner.setVisibility(View.INVISIBLE);
        Log.d("TAG", "LOGIN DONE DELEGATE " + result);

        if (!result.isEmpty()) {
            User user = DataManager.getInstance().parseUser(result);

            String baseAuthStr = username + ":" + password;
            String str = "Basic " + Base64.encodeToString(baseAuthStr.getBytes("UTF-8"), Base64.DEFAULT);
            DataManager.getInstance().setBaseAuthStr(str);

            if (checkBox_RememberMe.isChecked()) {
                // remember username and password
                loginPrefsEditor.putBoolean("saveLogin", true);
                loginPrefsEditor.putString("username", username);
                loginPrefsEditor.putString("password", password);
                loginPrefsEditor.commit();

            } else {
                loginPrefsEditor.clear();
                loginPrefsEditor.commit();
            }
            startNewActivity(user);
            Toast.makeText(getApplicationContext(), "Success login", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getApplicationContext(), "Fail login", Toast.LENGTH_SHORT).show();
        }
    }

}
