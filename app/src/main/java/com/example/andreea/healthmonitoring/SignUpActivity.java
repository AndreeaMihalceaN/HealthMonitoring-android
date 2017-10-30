package com.example.andreea.healthmonitoring;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import webservice.RegisterDelegate;
import webservice.RegisterTask;

public class SignUpActivity extends AppCompatActivity implements RegisterDelegate {

    private EditText m_editTextUsername;
    private EditText m_editTextFirstName;
    private EditText m_editTextLastName;
    private EditText m_editTextPassword;
    private EditText m_editTextRetypePassword;
    private EditText m_editTextHeight;
    private EditText m_editTextWeight;
    private Button m_buttonSubmit;
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private String gender;
    private int height;
    private int weight;
    private RadioGroup radioGroup;
    private RadioButton radioButton;
    private SignUpActivity signUpActivity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        m_editTextUsername = (EditText) findViewById(R.id.editTextUsername);
        m_editTextFirstName = (EditText) findViewById(R.id.editTextFirstName);
        m_editTextLastName = (EditText) findViewById(R.id.editTextLastName);
        m_editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        m_editTextRetypePassword = (EditText) findViewById(R.id.editTextRetypePassword);
        m_editTextHeight = (EditText) findViewById(R.id.editTextHeight);
        m_editTextWeight = (EditText) findViewById(R.id.editTextWeight);
        m_buttonSubmit = (Button) findViewById(R.id.buttonSubmit);
        radioGroup = (RadioGroup) findViewById(R.id.rgroup);

        m_buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username = m_editTextUsername.getText().toString();
                password = m_editTextPassword.getText().toString();
                firstName = m_editTextFirstName.getText().toString();
                lastName = m_editTextLastName.getText().toString();
                height = Integer.parseInt(m_editTextHeight.getText().toString());
                weight = Integer.parseInt(m_editTextWeight.getText().toString());
                gender = radioButton.getText().toString();
                //gender="F";


                RegisterTask loginTask = new RegisterTask(username, password, firstName, lastName, gender, height, weight);
                loginTask.setRegisterDelegate(signUpActivity);
                Toast.makeText(SignUpActivity.this, "Your have registered! ", Toast.LENGTH_SHORT).show();

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
}