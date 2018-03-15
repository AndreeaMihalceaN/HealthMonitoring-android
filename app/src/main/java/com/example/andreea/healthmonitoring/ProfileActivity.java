package com.example.andreea.healthmonitoring;

import android.content.Intent;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import manager.DataManager;
import model.User;
import webservice.LoginDelegate;
import webservice.LoginTask;
import webservice.RegisterDelegate;
import webservice.RegisterTask;
import webservice.UpdateDelegate;
import webservice.UpdateTask;

import static java.lang.Integer.parseInt;

public class ProfileActivity extends AppCompatActivity implements UpdateDelegate, LoginDelegate {

    private EditText m_editTextFirstName;
    private EditText m_editTextLastName;
    private EditText m_editTextEmail;
    private EditText m_editTextContactNo;
    private EditText m_editTextAge;
    private EditText m_editTextHeight;
    private EditText m_editTextWeight;
    private CardView m_cardViewUpdate;
    private User userAfterLogin;
    private ProfileActivity profileActivity;
    private EditText m_errorInfo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        profileActivity = this;
        m_cardViewUpdate = (CardView) findViewById(R.id.cardViewUpdate);
        m_editTextFirstName = (EditText) findViewById(R.id.editTextFirstName);
        m_editTextLastName = (EditText) findViewById(R.id.editTextLastName);
        m_editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        m_editTextContactNo = (EditText) findViewById(R.id.editTextContactNo);
        m_editTextAge = (EditText) findViewById(R.id.editTextAge);
        m_editTextHeight = (EditText) findViewById(R.id.editTextHeight);
        m_editTextWeight = (EditText) findViewById(R.id.editTextWeight);
        m_errorInfo = (EditText) findViewById(R.id.errorInfo);
        m_errorInfo.setVisibility(View.INVISIBLE);

        Intent intent = getIntent();
        userAfterLogin = (User) intent.getSerializableExtra("userAfterLogin");
        m_editTextFirstName.setText(userAfterLogin.getFirstName());
        m_editTextLastName.setText(userAfterLogin.getLastName());
        m_editTextEmail.setText(userAfterLogin.getEmail());
        m_editTextContactNo.setText(userAfterLogin.getContactNo());
        m_editTextAge.setText(userAfterLogin.getAge() + "");
        m_editTextHeight.setText(userAfterLogin.getHeight() + "");
        m_editTextWeight.setText(userAfterLogin.getWeight() + "");


        m_editTextFirstName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                m_errorInfo.setText("");
                m_errorInfo.setVisibility(View.INVISIBLE);
            }
        });
        m_editTextLastName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                m_errorInfo.setText("");
                m_errorInfo.setVisibility(View.INVISIBLE);
            }
        });
        m_editTextEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                m_errorInfo.setText("");
                m_errorInfo.setVisibility(View.INVISIBLE);
            }
        });
        m_editTextContactNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                m_errorInfo.setText("");
                m_errorInfo.setVisibility(View.INVISIBLE);
            }
        });
        m_editTextAge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                m_errorInfo.setText("");
                m_errorInfo.setVisibility(View.INVISIBLE);
            }
        });
        m_editTextHeight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                m_errorInfo.setText("");
                m_errorInfo.setVisibility(View.INVISIBLE);
            }
        });
        m_editTextWeight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                m_errorInfo.setText("");
                m_errorInfo.setVisibility(View.INVISIBLE);
            }
        });


        m_cardViewUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String firstName = m_editTextFirstName.getText().toString();
                String lastName = m_editTextLastName.getText().toString();
                String email = m_editTextEmail.getText().toString();
                String contactNo = m_editTextContactNo.getText().toString();
                String age = m_editTextAge.getText().toString();
                String height = m_editTextHeight.getText().toString();
                String weight = m_editTextWeight.getText().toString();
                m_errorInfo.setText("");

                if (!firstName.isEmpty() && !lastName.isEmpty() && !email.isEmpty() && !contactNo.isEmpty() && !age.isEmpty() && !height.isEmpty() && !weight.isEmpty()) {
                    if (emailValid(email) && contactNumberValid(contactNo) && isNumericValue(age) && isNumericValue(height) && isNumericValue(weight)) {
                        UpdateTask updateTask = new UpdateTask(userAfterLogin.getUsername(), userAfterLogin.getPassword(), m_editTextFirstName.getText().toString(), m_editTextLastName.getText().toString(), userAfterLogin.getGender(), Integer.parseInt(m_editTextHeight.getText().toString()), Integer.parseInt(m_editTextWeight.getText().toString()), Integer.parseInt(m_editTextAge.getText().toString()), m_editTextEmail.getText().toString(), m_editTextContactNo.getText().toString());
                        updateTask.setUpdateDelegate(profileActivity);
                        Toast.makeText(ProfileActivity.this, "Updated profile! ", Toast.LENGTH_SHORT).show();
                    } else {
                        m_errorInfo.setVisibility(View.VISIBLE);
                        if (!emailValid(email))
                            m_errorInfo.setText(m_errorInfo.getText().toString() + "Invalid email. ");
                        if (!contactNumberValid(contactNo))
                            m_errorInfo.setText(m_errorInfo.getText().toString() + "Invalid phone number. ");
                        if (!isNumericValue(age))
                            m_errorInfo.setText(m_errorInfo.getText().toString() + "Age contains letter, invalid age. ");
                        if (!isNumericValue(height))
                            m_errorInfo.setText(m_errorInfo.getText().toString() + "Height contains letter, invalid height. ");
                        if (!isNumericValue(weight))
                            m_errorInfo.setText(m_errorInfo.getText().toString() + "Weight contains letter, invalid weight. ");

                    }

                } else {
                    m_errorInfo.setVisibility(View.VISIBLE);
                    m_errorInfo.setText("Complete all fields! ");
                }


                LoginTask loginTask = new LoginTask(userAfterLogin.getUsername(), userAfterLogin.getPassword());
                loginTask.setLoginDelegate(profileActivity);

            }
        });


    }

    private Boolean isNumericValue(String value) {

        if (value.matches("[0-9]+") && value.charAt(0) != '0' && value.length() > 0 && value.length() < 4) {
            return true;
        }
        return false;
    }

    private Boolean emailValid(String email) {
        if (email.endsWith("@yahoo.com") || email.endsWith("@gmail.com"))
            return true;
        return false;
    }

    private Boolean contactNumberValid(String contactNo) {
        //if (contactNo.matches("\\d{10}")) return true;
//        if (contactNo.matches("^(?:(?:\\\\+?1\\\\s*(?:[.-]\\\\s*)?)?(?:\\\\(\\\\s*([2-9]1[02-9]|[2-9][02-8]1|[2-9][02-8][02-9])\\\\s*\\\\)|([2-9]1[02-9]|[2-9][02-8]1|[2-9][02-8][02-9]))\\\\s*(?:[.-]\\\\s*)?)?([2-9]1[02-9]|[2-9][02-9]1|[2-9][02-9]{2})\\\\s*(?:[.-]\\\\s*)?([0-9]{4})(?:\\\\s*(?:#|x\\\\.?|ext\\\\.?|extension)\\\\s*(\\\\d+))?$")) return true;
//        return false;

        String expression = "^([0-9\\+]|\\(\\d{1,3}\\))[0-9\\-\\. ]{3,15}$";
        CharSequence inputString = contactNo;
        Pattern pattern = Pattern.compile(expression);
        Matcher matcher = pattern.matcher(inputString);
        if (matcher.matches())
        {
            return true;
        }
        else{
            return false;
        }
    }

    @Override
    public void onUpdateDone(String result) {
        m_errorInfo.setVisibility(View.INVISIBLE);

    }

    @Override
    public void onUpdateError(String errorMsg) {

        Toast.makeText(ProfileActivity.this, errorMsg, Toast.LENGTH_SHORT).show();
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
            userAfterLogin = user;
        }
    }
}
