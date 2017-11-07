package com.example.andreea.healthmonitoring;

import android.content.Intent;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import model.User;
import webservice.RegisterDelegate;
import webservice.RegisterTask;
import webservice.UpdateDelegate;
import webservice.UpdateTask;

import static java.lang.Integer.parseInt;

public class ProfileActivity extends AppCompatActivity implements UpdateDelegate {

    private EditText m_editTextFirstName;
    private EditText m_editTextLastName;
    private EditText m_editTextEmail;
    private EditText m_editTextContactNo;
    private EditText m_editTextAge;
    private EditText m_editTextHeight;
    private EditText m_editTextWeight;
    private Button m_buttonUpdate;
    private User userAfterLogin;
    private ProfileActivity profileActivity;


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

        Intent intent = getIntent();
        userAfterLogin = (User) intent.getSerializableExtra("userAfterLogin");
        m_editTextFirstName.setText(userAfterLogin.getFirstName());
        m_editTextLastName.setText(userAfterLogin.getLastName());
        m_editTextEmail.setText(userAfterLogin.getEmail());
        m_editTextContactNo.setText(userAfterLogin.getContactNo());
        m_editTextAge.setText(userAfterLogin.getAge()+"");
        m_editTextHeight.setText(userAfterLogin.getHeight()+"");
        m_editTextWeight.setText(userAfterLogin.getWeight()+"");


        m_buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                username = m_editTextUsername.getText().toString();
//                password = m_editTextPassword.getText().toString();
//                firstName = m_editTextFirstName.getText().toString();
//                lastName = m_editTextLastName.getText().toString();
//                height = Integer.parseInt(m_editTextHeight.getText().toString());
//                weight = Integer.parseInt(m_editTextWeight.getText().toString());
//                gender = radioButton.getText().toString();
//                //gender="F";


                UpdateTask updateTask = new UpdateTask(userAfterLogin.getUsername(), userAfterLogin.getPassword(), m_editTextFirstName.getText().toString(), m_editTextLastName.getText().toString(), userAfterLogin.getGender(), Integer.parseInt(m_editTextHeight.getText().toString()), Integer.parseInt(m_editTextWeight.getText().toString()), Integer.parseInt(m_editTextAge.getText().toString()), m_editTextEmail.getText().toString(), m_editTextContactNo.getText().toString());
                updateTask.setUpdateDelegate(profileActivity);
                Toast.makeText(ProfileActivity.this, "Updated profile! ", Toast.LENGTH_SHORT).show();

            }
        });


    }

    @Override
    public void onUpdateDone(String result) {

    }

    @Override
    public void onUpdateError(String errorMsg) {

        Toast.makeText(ProfileActivity.this, errorMsg, Toast.LENGTH_SHORT).show();
    }
}
