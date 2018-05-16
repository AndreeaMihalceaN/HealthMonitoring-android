package com.example.andreea.healthmonitoring.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.andreea.healthmonitoring.Encryption;
import com.example.andreea.healthmonitoring.R;

import java.io.UnsupportedEncodingException;

import manager.DataManager;
import model.User;
import webservice.LoginDelegate;
import webservice.LoginTask;
import webservice.SelectUserDelegate;
import webservice.SelectUserTask;
import webservice.UpdateAutentificationDelegate;
import webservice.UpdateAutentificationTask;

public class EditAccountActivity extends AppCompatActivity implements UpdateAutentificationDelegate, LoginDelegate, SelectUserDelegate {

    private EditText editTextUsername;
    private EditText editTextPassword;
    private EditText editTextRetypePassword;
    private CardView m_cardChangeAutentificationDates;
    private EditText m_errorInfo;
    private EditAccountActivity editAccountActivity;
    private User userAfterLogin;
    private static final String TAG = "EditAccountActivity";

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
//                    UpdateAutentificationTask updateAutentificationTask = new UpdateAutentificationTask(userAfterLogin.getUsername(), userAfterLogin.getPassword(), username, password);
//                    updateAutentificationTask.setUpdateAutentificationDelegate(editAccountActivity);
                    SelectUserTask selectUserTask = new SelectUserTask(username);
                    selectUserTask.setSelectUserDelegate(editAccountActivity);
//                    LoginTask loginTask = new LoginTask(username, password);
//                    loginTask.setLoginDelegate(editAccountActivity);
                    //Toast.makeText(editAccountActivity, "Succesful update!", Toast.LENGTH_SHORT).show();
                } else {
                    m_errorInfo.setText("These password don't match!! Try again!!");
                    m_errorInfo.setVisibility(View.VISIBLE);
                    editTextPassword.setText("");
                    editTextRetypePassword.setText("");

                }

            }
        });

        editTextPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                m_errorInfo.setText("");
                editTextPassword.setCursorVisible(true);
                editTextPassword.setFocusableInTouchMode(true);
//                editTextPassword.setInputType(InputType.TYPE_CLASS_TEXT);
                editTextPassword.requestFocus();

            }
        });

        editTextRetypePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                m_errorInfo.setText("");
                editTextRetypePassword.setCursorVisible(true);
                editTextRetypePassword.setFocusableInTouchMode(true);
//                editTextRetypePassword.setInputType(InputType.TYPE_CLASS_TEXT);
                editTextRetypePassword.requestFocus();

            }
        });


    }

    @Override
    public void onUpdateDone(String result) {
        Log.i(TAG, "UpdateDone here");
        Log.d(TAG, "UpdateDone here");
        Encryption sj = new Encryption();
        String hash = sj.MD5(editTextPassword.getText().toString());
        System.out.println("The MD5 (hexadecimal encoded) hash is:" + hash);
        LoginTask loginTask = new LoginTask(editTextUsername.getText().toString(), hash);
        loginTask.setLoginDelegate(editAccountActivity);

    }

    @Override
    public void onUpdateError(String response) {
        Toast.makeText(this, response, Toast.LENGTH_SHORT).show();

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent = new Intent(this, HomeActivity.class);
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
            Log.i(TAG, "User after login: "+user.getUsername());
            Log.d(TAG, "User after login: "+user.getUsername());
            userAfterLogin = user;
        }
    }

    @Override
    public void onSelectUserDone(String result) {

        Log.d(TAG, "SELECT USER DONE DELEGATE " + result);
        if (!result.isEmpty()) {
            m_errorInfo.setText("This username already exists!! Try again!");
            Log.i(TAG, "SelectUserDone; this username already exists");
            Log.d(TAG, "SelectUserDone; this username already exists");
            Toast.makeText(EditAccountActivity.this, "This username already exists. Please try with another username!", Toast.LENGTH_SHORT).show();
        } else {
            Log.i(TAG, "Update user with new credentials: " + editTextUsername.getText().toString() + ", " + userAfterLogin.getPassword().toString());

            Encryption sj = new Encryption();
            String hash = sj.MD5(editTextPassword.getText().toString());
            System.out.println("The MD5 (hexadecimal encoded) hash is:" + hash);


            UpdateAutentificationTask updateAutentificationTask = new UpdateAutentificationTask(userAfterLogin.getUsername(), userAfterLogin.getPassword(), editTextUsername.getText().toString(), hash);
            updateAutentificationTask.setUpdateAutentificationDelegate(editAccountActivity);
            Toast.makeText(EditAccountActivity.this, "Updated profile! ", Toast.LENGTH_SHORT).show();
        }

    }
}
