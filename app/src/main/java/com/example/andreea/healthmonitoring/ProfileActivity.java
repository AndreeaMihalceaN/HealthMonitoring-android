package com.example.andreea.healthmonitoring;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class ProfileActivity extends AppCompatActivity {

    private EditText m_editTextFirstName;
    private EditText m_editTextLastName;
    private EditText m_editTextEmail;
    private EditText m_editTextContactNo;
    private EditText m_editTextAge;
    private EditText m_editTextHeight;
    private EditText m_editTextWeight;
    private Button m_buttonUpdate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        m_buttonUpdate = (Button) findViewById(R.id.buttonUpdate);
        m_editTextFirstName =(EditText) findViewById(R.id.editTextFirstName);
        m_editTextLastName =(EditText) findViewById(R.id.editTextLastName);
        m_editTextEmail =(EditText) findViewById(R.id.editTextEmail);
        m_editTextContactNo =(EditText) findViewById(R.id.editTextContactNo);
        m_editTextAge =(EditText) findViewById(R.id.editTextAge);
        m_editTextHeight =(EditText) findViewById(R.id.editTextHeight);
        m_editTextWeight =(EditText) findViewById(R.id.editTextWeight);

    }
}
