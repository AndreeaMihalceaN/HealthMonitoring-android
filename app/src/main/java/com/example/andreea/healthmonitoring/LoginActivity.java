package com.example.andreea.healthmonitoring;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class LoginActivity extends AppCompatActivity {

    private ProgressBar progressBarSpinner;
    private CheckBox checkBox_RememberMe;
    private Button btnSignIn;
    private EditText editTextUsername;
    private EditText editTextPassword;
    private SharedPreferences loginPreferences;
    private SharedPreferences.Editor loginPrefsEditor;
    private Boolean saveLogin;
    private LoginActivity loginActivity;
    private TextView m_textViewSignUp;
    private String username;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginActivity = this;

        getLoginPreferences();
        progressBarSpinner = (ProgressBar) findViewById(R.id.progressBar);
        btnSignIn = (Button) findViewById(R.id.buttonSignIn);
        progressBarSpinner.setVisibility(View.GONE);
        m_textViewSignUp = (TextView) findViewById(R.id.textViewSignUp);


        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isNetworkConnected()) {
                    Toast.makeText(LoginActivity.this, "Please check your internet connection!", Toast.LENGTH_SHORT).show();
                } else {

                    username = editTextUsername.getText().toString();
                    password = editTextPassword.getText().toString();

                    progressBarSpinner.setVisibility(View.VISIBLE);

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

                    Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                    intent.putExtra("Username", username);
                    startActivity(intent);

                }
            }
        });


        m_textViewSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);

                //Toast.makeText(getApplicationContext(), "Am intrat in actiune", Toast.LENGTH_SHORT).show();
            }
        });

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
}
