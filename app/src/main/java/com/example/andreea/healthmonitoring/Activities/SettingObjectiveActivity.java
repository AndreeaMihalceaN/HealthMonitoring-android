package com.example.andreea.healthmonitoring.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.andreea.healthmonitoring.R;

import java.io.UnsupportedEncodingException;

import manager.DataManager;
import model.User;
import webservice.LoginDelegate;
import webservice.LoginTask;
import webservice.UpdateStepsObjectiveDelegate;
import webservice.UpdateStepsObjectiveTask;

public class SettingObjectiveActivity extends AppCompatActivity implements LoginDelegate, UpdateStepsObjectiveDelegate {

    private User userAfterLogin;
    private SettingObjectiveActivity settingObjectiveActivity;
    private EditText textEditStepsObjective;
    private Button buttonStepObjective;
    private TextView errorTextView;
    private static final String TAG = "SettingObjectiveActivit";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_objective);
        settingObjectiveActivity = this;

        textEditStepsObjective = (EditText) findViewById(R.id.textEditStepsObjective);
        buttonStepObjective = (Button) findViewById(R.id.buttonStepObjective);
        errorTextView = (TextView) findViewById(R.id.errorTextView);
        //errorTextView.setText("");

        Intent intent = getIntent();
        userAfterLogin = (User) intent.getSerializableExtra("userAfterLogin");

        LoginTask loginTask = new LoginTask(userAfterLogin.getUsername(), userAfterLogin.getPassword());
        loginTask.setLoginDelegate(settingObjectiveActivity);

        buttonStepObjective.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateTextNumberOfSteps()) {
                    UpdateStepsObjectiveTask updateStepsObjectiveTask = new UpdateStepsObjectiveTask(userAfterLogin.getUsername(), userAfterLogin.getPassword(), Double.parseDouble(textEditStepsObjective.getText().toString()));
                    updateStepsObjectiveTask.setUpdateStepsObjectiveDelegate(settingObjectiveActivity);

                }
                else
                {
                    errorTextView.setText("This value contains letters! Try again with a numeric value!");
                }

            }
        });

        textEditStepsObjective.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                errorTextView.setText("");
                textEditStepsObjective.setCursorVisible(true);
                textEditStepsObjective.setFocusableInTouchMode(true);
                textEditStepsObjective.setInputType(InputType.TYPE_CLASS_TEXT);
                textEditStepsObjective.requestFocus();

            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent = new Intent(this, Walking2Activity.class);
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
        Intent intent = new Intent(this, Walking2Activity.class);
        intent.putExtra("userAfterLogin", userAfterLogin);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    @Override
    public void onLoginDone(String result) throws UnsupportedEncodingException {
        Log.i(TAG, "LoginDone");
        Log.d(TAG, "LoginDone");
        if (!result.isEmpty()) {
            User user = DataManager.getInstance().parseUser(result);
            userAfterLogin = user;
            textEditStepsObjective.setText(user.getStepsObjective() + "");
        }

    }

    private Boolean validateTextNumberOfSteps() {
        Log.i(TAG, "Check if number of steps are valid");
        Log.d(TAG, "Check if number of steps are valid");
        String textNumberOfStepsy = textEditStepsObjective.getText().toString();
        if (textNumberOfStepsy.matches("[0-9]{1,13}(\\.[0-9]*)?")) {
            return true;
        }
        return false;
    }

    @Override
    public void onUpdateStepsObjectiveDone(String result) {
        Log.i(TAG, "Update steps objective");
        Log.d(TAG, "Update steps objective");
        Toast.makeText(settingObjectiveActivity, result, Toast.LENGTH_SHORT).show();
        LoginTask loginTask = new LoginTask(userAfterLogin.getUsername(), userAfterLogin.getPassword());
        loginTask.setLoginDelegate(settingObjectiveActivity);

    }

    @Override
    public void onUpdateStepsObjectiveError(String response) {
        Toast.makeText(settingObjectiveActivity, response, Toast.LENGTH_SHORT).show();

    }
}
