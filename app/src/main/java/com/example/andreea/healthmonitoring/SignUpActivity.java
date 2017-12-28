package com.example.andreea.healthmonitoring;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;

import manager.DataManager;
import model.User;
import webservice.RegisterDelegate;
import webservice.RegisterTask;
import webservice.SelectUserDelegate;
import webservice.SelectUserTask;

public class SignUpActivity extends AppCompatActivity implements RegisterDelegate, SelectUserDelegate {

    private EditText m_editTextUsername;
    private EditText m_editTextFirstName;
    private EditText m_editTextLastName;
    private EditText m_editTextPassword;
    private EditText m_editTextRetypePassword;
    private EditText m_editTextHeight;
    private EditText m_editTextWeight;
    private EditText m_editTextEmail;
    private EditText m_errorInfo;
    private CardView m_cardViewSubmit;
    private String username;
    private String password;
    private String repeatPassword;
    private String firstName;
    private String lastName;
    private String gender;
    private String email;
    private int height;
    private int weight;
    private RadioGroup radioGroup;
    private RadioButton radioButton;
    private SignUpActivity signUpActivity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        this.signUpActivity = this;

        m_editTextUsername = (EditText) findViewById(R.id.editTextUsername);
        m_editTextFirstName = (EditText) findViewById(R.id.editTextFirstName);
        m_editTextLastName = (EditText) findViewById(R.id.editTextLastName);
        m_editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        m_editTextRetypePassword = (EditText) findViewById(R.id.editTextRetypePassword);
        m_editTextHeight = (EditText) findViewById(R.id.editTextHeight);
        m_editTextWeight = (EditText) findViewById(R.id.editTextWeight);
        m_editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        m_errorInfo = (EditText) findViewById(R.id.errorInfo);
        m_errorInfo.setVisibility(View.INVISIBLE);
        m_errorInfo.setEnabled(false);
        m_cardViewSubmit = (CardView) findViewById(R.id.cardViewSubmit);
        radioGroup = (RadioGroup) findViewById(R.id.rgroup);

        m_cardViewSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username = m_editTextUsername.getText().toString();
                password = m_editTextPassword.getText().toString();
                repeatPassword = m_editTextRetypePassword.getText().toString();
                firstName = m_editTextFirstName.getText().toString();
                lastName = m_editTextLastName.getText().toString();

                if (radioGroup.getCheckedRadioButtonId() == -1)
                    gender = "";
                else
                    gender = radioButton.getText().toString();

                email = m_editTextEmail.getText().toString();

                if (username.isEmpty() || password.isEmpty() || repeatPassword.isEmpty() || firstName.isEmpty() || lastName.isEmpty() || gender.isEmpty() || email.isEmpty()) {
                    m_errorInfo.setText("Complete all fields!!");
                    m_errorInfo.setVisibility(View.VISIBLE);

                } else {

                    if (password.equals(repeatPassword)) {
                        m_errorInfo.setVisibility(View.INVISIBLE);

                        String heightString = m_editTextHeight.getText().toString();
                        String weightString = m_editTextWeight.getText().toString();
                        if (heightString.matches("[0-9]+") && heightString.charAt(0) != '0' && heightString.length() > 0 && heightString.length() < 4)
                            height = Integer.parseInt(m_editTextHeight.getText().toString());
                        else height = 0;
                        if (weightString.matches("[0-9]+") && weightString.charAt(0) != '0' && weightString.length() > 0 && weightString.length() < 4)
                            weight = Integer.parseInt(m_editTextWeight.getText().toString());
                        else weight = 0;
                        if (height == 0 || weight == 0) {
                            m_errorInfo.setText("Height or weight field contains letters or contains a too big value or start with 0");
                            m_errorInfo.setVisibility(View.VISIBLE);
                            m_editTextHeight.setText("");
                            m_editTextWeight.setText("");
                        } else {
                            if (email.endsWith("@yahoo.com") || email.endsWith("@gmail.com")) {
                                SelectUserTask loginTask = new SelectUserTask(username, firstName, lastName, password, gender, email);
                                loginTask.setSelectUserDelegate(signUpActivity);

                            } else {
                                m_errorInfo.setText("Please write a yahoo or gmail account! Try again!");
                                m_errorInfo.setVisibility(View.VISIBLE);
                                m_editTextEmail.setText("");
                            }
                        }
                    } else {
                        m_errorInfo.setText("These password don't match!! Try again!!");
                        m_errorInfo.setVisibility(View.VISIBLE);
                        m_editTextPassword.setText("");
                        m_editTextRetypePassword.setText("");
                    }
                }

            }


        });
    }


    public void rbClick(View view) {
        int idRadioButton = radioGroup.getCheckedRadioButtonId();
        radioButton = (RadioButton) findViewById(idRadioButton);
    }

    @Override
    public void onRegisterDone(String result) {

    }

    @Override
    public void onRegisterError(String errorMsg) {
        Toast.makeText(SignUpActivity.this, errorMsg, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onSelectUserDone(String result) throws UnsupportedEncodingException {
        if (!result.isEmpty()) {
            //User user = DataManager.getInstance().parseUser(result);
            Toast.makeText(signUpActivity, "This user already exists!", Toast.LENGTH_SHORT).show();

        } else {
            RegisterTask loginTask = new RegisterTask(username, password, firstName, lastName, gender, height, weight, 0, email, " ");
            loginTask.setRegisterDelegate(signUpActivity);
            Toast.makeText(SignUpActivity.this, "Your have registered! ", Toast.LENGTH_SHORT).show();
        }

    }
}