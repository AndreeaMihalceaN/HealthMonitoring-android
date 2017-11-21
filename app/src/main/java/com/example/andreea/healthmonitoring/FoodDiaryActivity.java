package com.example.andreea.healthmonitoring;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.icu.util.Calendar;
import android.os.Build;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import model.Food;
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
    private ListView m_listView;
    private int[] images = {R.drawable.melon, R.drawable.cheese, R.drawable.milkshake, R.drawable.chocolate, R.drawable.vegetablesoup};
    //private String[] names = {"Melon", "Cheese", "Milkshake", "Chocolate", "Vegetable soup"};

    private ArrayList<Double>carbohydrates=new ArrayList<>();
    private ArrayList<Double>fats=new ArrayList<>();
    private ArrayList<Double>proteins=new ArrayList<>();
    private ArrayList<String>categories=new ArrayList<>();
    private ArrayList<String>names=new ArrayList<>();

    private ImageView mImageView;
    private TextView mTextView;
    private TextView m_textViewCarbohydratesResult;
    private TextView m_textViewFatsResult;
    private TextView m_textViewProteinsResult;
    private TextView m_textViewCategoryResult;
    private ArrayList<Food> foodList= new ArrayList<Food>();

    private FoodDiaryActivity foodDiaryActivity;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_diary);

        Food food1=new Food("Melon", 200, 150, 50, "Fruits");
        Food food2=new Food("Cheese", 200, 150, 50, "Dairy products");
        Food food3=new Food("Milkshake", 200, 150, 50, "Drink");
        Food food4=new Food("Chocolate", 200, 150, 50, "Sweets");
        Food food5=new Food("Vegetable soup", 200, 150, 50, "Soups");

        foodList.add(food1);
        foodList.add(food2);
        foodList.add(food3);
        foodList.add(food4);
        foodList.add(food5);

        putInArrayLists();

        m_listView = (ListView) findViewById(R.id.listView);
        m_textView = (TextView) findViewById(R.id.textView);
        m_currentDate = Calendar.getInstance();

        day = m_currentDate.get(Calendar.DAY_OF_MONTH);
        month = m_currentDate.get(Calendar.MONTH);
        year = m_currentDate.get(Calendar.YEAR);

        month = month + 1;

        m_textView.setText(day + "/" + month + "/" + year);

        CustomAdaptor customAdaptor= new CustomAdaptor();
        m_listView.setAdapter(customAdaptor);

        m_textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(FoodDiaryActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        month = month + 1;
                        m_textView.setText(dayOfMonth + "/" + month + "/" + year);

                    }
                }, year, month, day);
                datePickerDialog.show();
            }

        });

        Intent intent = getIntent();
        userAfterLogin = (User) intent.getSerializableExtra("userAfterLogin");
    }

    public void putInArrayLists()
    {
        for(Food food: foodList)
        {
            carbohydrates.add(food.getCarbohydrates());
            fats.add(food.getFats());
            proteins.add(food.getProteins());
            categories.add(food.getCategory());
            names.add(food.getName());
        }
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

    class CustomAdaptor extends BaseAdapter {

        @Override
        public int getCount() {
            return images.length;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = getLayoutInflater().inflate(R.layout.customlayout, null);

            mImageView = (ImageView) view.findViewById(R.id.imageView);
            mTextView = (TextView) view.findViewById(R.id.textView);
            m_textViewCarbohydratesResult=(TextView)view.findViewById(R.id.textViewCarbohydratesResult);
            m_textViewFatsResult=(TextView)view.findViewById(R.id.textViewFatsResult);
            m_textViewProteinsResult=(TextView)view.findViewById(R.id.textViewProteinsResult);
            m_textViewCategoryResult=(TextView)view.findViewById(R.id.textViewCategoryResult);
            mImageView.setImageResource(images[position]);
            mTextView.setText(names.get(position));
            m_textViewCarbohydratesResult.setText(carbohydrates.get(position)+"");
            m_textViewFatsResult.setText(fats.get(position)+"");
            m_textViewProteinsResult.setText(proteins.get(position)+"");
            m_textViewCategoryResult.setText(categories.get(position));


            return view;
        }
    }


}
