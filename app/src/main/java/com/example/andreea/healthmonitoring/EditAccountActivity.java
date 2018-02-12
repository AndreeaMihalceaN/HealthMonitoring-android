package com.example.andreea.healthmonitoring;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;

import manager.DataManager;
import model.User;
import webservice.LoginDelegate;
import webservice.LoginTask;
import webservice.UpdateAutentificationDelegate;
import webservice.UpdateAutentificationTask;

public class EditAccountActivity extends AppCompatActivity implements UpdateAutentificationDelegate, LoginDelegate {

    private EditText editTextUsername;
    private EditText editTextPassword;
    private EditText editTextRetypePassword;
    private CardView m_cardChangeAutentificationDates;
    private EditText m_errorInfo;
    private EditAccountActivity editAccountActivity;
    private User userAfterLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_account);
        editAccountActivity = this;

        editTextUsername = (EditText) findViewById(R.id.editTextUsername);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        editTextRetypePassword = (EditText) findViewById(R.id.editTextRetypePassword);
        m_cardChangeAutentificationDates = (CardView) findViewById(R.id.buttonChageAutentificationDates);
        m_errorInfo = (EditText) findViewById(R.id.errorInfo);
        m_errorInfo.setVisibility(View.INVISIBLE);
        m_errorInfo.setEnabled(false);

        Intent intent = getIntent();
        userAfterLogin = (User) intent.getSerializableExtra("userAfterLogin");
        editTextUsername.setText(userAfterLogin.getUsername().toString());

        m_cardChangeAutentificationDates.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String password = editTextPassword.getText().toString();
                String rePassword = editTextRetypePassword.getText().toString();
                String username = editTextUsername.getText().toString();
                if (username.isEmpty() || password.isEmpty() || rePassword.isEmpty()) {
                    m_errorInfo.setText("Complete all fields!!");
                    m_errorInfo.setVisibility(View.VISIBLE);
                } else if (password.equals(rePassword)) {
                    m_errorInfo.setText("");
                    UpdateAutentificationTask updateAutentificationTask = new UpdateAutentificationTask(userAfterLogin.getUsername(), userAfterLogin.getPassword(), username, password);
                    updateAutentificationTask.setUpdateAutentificationDelegate(editAccountActivity);
                    LoginTask loginTask = new LoginTask(username, password);
                    loginTask.setLoginDelegate(editAccountActivity);
                    Toast.makeText(editAccountActivity, "Succesful update!", Toast.LENGTH_SHORT).show();
                } else {
                    m_errorInfo.setText("These password don't match!! Try again!!");
                    m_errorInfo.setVisibility(View.VISIBLE);
                    editTextPassword.setText("");
                    editTextRetypePassword.setText("");

                }

            }
        });


    }

    @Override
    public void onUpdateDone(String result) {

    }

    @Override
    public void onUpdateError(String response) {
        Toast.makeText(this, response, Toast.LENGTH_SHORT).show();

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent = new Intent(this,HomeActivity.class);
            intent.putExtra("userAfterLogin", userAfterLogin);
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
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    @Override
    public void onLoginDone(String result) throws UnsupportedEncodingException {
        if (!result.isEmpty()) {
            User user = DataManager.getInstance().parseUser(result);
            userAfterLogin = user;
        }
    }
}
