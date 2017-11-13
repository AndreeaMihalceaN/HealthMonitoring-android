package com.example.andreea.healthmonitoring;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import model.User;
import webservice.RegisterDelegate;
import webservice.RegisterFoodDelegate;
import webservice.RegisterFoodTask;
import webservice.RegisterTask;

public class FoodDiaryActivity extends AppCompatActivity implements RegisterFoodDelegate {

    private User userAfterLogin;

    private FoodDiaryActivity foodDiaryActivity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_diary);

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
//                startActivity(intent);
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
