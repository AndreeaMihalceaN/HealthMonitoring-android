package com.example.andreea.healthmonitoring;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.icu.util.Calendar;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import model.User;
import webservice.RegisterDelegate;
import webservice.RegisterFoodDelegate;
import webservice.RegisterFoodTask;
import webservice.RegisterTask;

public class FoodDiaryActivity extends AppCompatActivity implements RegisterFoodDelegate {

    private User userAfterLogin;
    private TextView m_textView;
    private Calendar m_currentDate;
    private int day, month, year;

    private FoodDiaryActivity foodDiaryActivity;
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_diary);

        m_textView=(TextView)findViewById(R.id.textView);
        m_currentDate=Calendar.getInstance();

        day=m_currentDate.get(Calendar.DAY_OF_MONTH);
        month=m_currentDate.get(Calendar.MONTH);
        year=m_currentDate.get(Calendar.YEAR);

        month=month+1;

        m_textView.setText(day+"/"+month+"/"+year);

        m_textView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view)
            {
                DatePickerDialog datePickerDialog= new DatePickerDialog(FoodDiaryActivity.this, new DatePickerDialog.OnDateSetListener(){
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        month=month+1;
                        m_textView.setText(dayOfMonth+"/"+month+"/"+year);

                    }
                }, year, month, day);
                datePickerDialog.show();
            }

        });

        Intent intent = getIntent();
        userAfterLogin = (User) intent.getSerializableExtra("userAfterLogin");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.food_activity_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.addFood_id:
                //RegisterFoodTask registerFoodTask = new RegisterFoodTask(food_name, carbohydrates, proteins, fats, category);
                //registerFoodTask.setRegisterFoodDelegate(foodDiaryActivity);
                //Toast.makeText(FoodDiaryActivity.this, "Your have registered! ", Toast.LENGTH_SHORT).show();
                Toast.makeText(getApplicationContext(), "Add Food icon is selected", Toast.LENGTH_SHORT).show();
                //Intent intent = new Intent(FoodDiaryActivity.this, ProfileActivity.class);
                Intent intent = new Intent(FoodDiaryActivity.this, AddFoodActivity.class);
                intent.putExtra("userAfterLogin", userAfterLogin);
                startActivity(intent);

                //startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }

    }

    @Override
    public void onRegisterFoodDone(String result) {

    }

    @Override
    public void onRegisterFoodError(String errorMsg) {
        Toast.makeText(FoodDiaryActivity.this, errorMsg, Toast.LENGTH_SHORT).show();

    }
}
