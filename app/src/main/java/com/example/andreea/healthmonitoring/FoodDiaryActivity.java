package com.example.andreea.healthmonitoring;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import java.util.Calendar;
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

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import manager.DataManager;
import model.Food;
import model.User;
import model.UserDiary;
import webservice.AddUserDiaryTask;
import webservice.RegisterDelegate;
import webservice.RegisterFoodDelegate;
import webservice.RegisterFoodTask;
import webservice.RegisterTask;
import webservice.SearchDayTask;
import webservice.SelectFoodDelegate;
import webservice.SelectFoodFromCurrentDayDelegate;
import webservice.SelectFoodFromCurrentDayTask;
import webservice.SelectFoodTask;
import webservice.SelectUserDiaryDelegate;
import webservice.SelectUserDiaryTask;

public class FoodDiaryActivity extends AppCompatActivity implements RegisterFoodDelegate, SelectFoodDelegate, SelectFoodFromCurrentDayDelegate, SelectUserDiaryDelegate {

    private User userAfterLogin;
    private TextView m_textView;
    private Calendar m_currentDate;
    private int day, month, year;
    private ListView m_listView;
    private List<Food> foods;
    private List<UserDiary> userDiaryList;
    private CustomAdaptor customAdaptor;
    private boolean chooseAnotherDate;
    private TextView m_textViewTotalCarbohydrates;
    private TextView m_textViewTotalFats;
    private TextView m_textViewTotalProteins;

    private double sumCarbohydrates;
    private double sumFats;
    private double sumProteins;
    //private int[] images = {R.drawable.melon, R.drawable.cheese, R.drawable.milkshake, R.drawable.chocolate, R.drawable.vegetablesoup};
    //private String[] names = {"Melon", "Cheese", "Milkshake", "Chocolate", "Vegetable soup"};

    private ArrayList<Double> carbohydrates = new ArrayList<>();
    private ArrayList<Double> fats = new ArrayList<>();
    private ArrayList<Double> proteins = new ArrayList<>();
    private ArrayList<String> categories = new ArrayList<>();
    private ArrayList<String> names = new ArrayList<>();
    private ArrayList<Integer> idImages = new ArrayList<>();
    private ArrayList<Double> quantityList = new ArrayList<>();

    private ImageView mImageView;
    private TextView mTextView;
    private TextView m_textViewCarbohydratesResult;
    private TextView m_textViewFatsResult;
    private TextView m_textViewProteinsResult;
    private TextView m_textViewCategoryResult;
    private TextView m_textViewQuantityResult;
    private ArrayList<Food> foodList = new ArrayList<Food>();

    private FoodDiaryActivity foodDiaryActivity;
    private Resources resources;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_diary);

        foodDiaryActivity = this;
        chooseAnotherDate = false;
        resources = this.getResources();

        Intent intent = getIntent();
        userAfterLogin = (User) intent.getSerializableExtra("userAfterLogin");

        m_textViewTotalCarbohydrates = (TextView) findViewById(R.id.textViewTotalCarbohydrates);
        m_textViewTotalFats = (TextView) findViewById(R.id.textViewTotalFats);
        m_textViewTotalProteins = (TextView) findViewById(R.id.textViewTotalProteins);

        m_listView = (ListView) findViewById(R.id.listView);
        m_textView = (TextView) findViewById(R.id.textView);
        m_currentDate = Calendar.getInstance();

        day = m_currentDate.get(Calendar.DAY_OF_MONTH);
        month = m_currentDate.get(Calendar.MONTH);
        year = m_currentDate.get(Calendar.YEAR);

        month = month + 1;

        m_textView.setText(day + "-" + month + "-" + year);

        m_textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(FoodDiaryActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        month = month + 1;
                        m_textView.setText(dayOfMonth + "-" + month + "-" + year);
                        chooseAnotherDate = true;
                        customAdaptor = new CustomAdaptor();
                        userDiaryList.clear();
                        carbohydrates.clear();
                        fats.clear();
                        proteins.clear();
                        categories.clear();
                        names.clear();
                        idImages.clear();
                        quantityList.clear();
                        SelectUserDiaryTask selectUserDiaryTask = new SelectUserDiaryTask(m_textView.getText().toString(), userAfterLogin.getUsername());
                        selectUserDiaryTask.setSelectUserDiaryDelegate(foodDiaryActivity);


                    }
                }, year, month, day);
                datePickerDialog.show();
            }


        });

        if (!chooseAnotherDate) {
            customAdaptor = new CustomAdaptor();

//            SelectFoodFromCurrentDayTask selectFoodFromCurrentDayTask = new SelectFoodFromCurrentDayTask(m_textView.getText().toString());
//            selectFoodFromCurrentDayTask.setSelectFoodFromCurrentDayDelegate(foodDiaryActivity);
            SelectUserDiaryTask selectUserDiaryTask = new SelectUserDiaryTask(m_textView.getText().toString(), userAfterLogin.getUsername());
            selectUserDiaryTask.setSelectUserDiaryDelegate(foodDiaryActivity);

            //m_listView.setAdapter(customAdaptor);
        }

    }

    public void putInArrayLists() {
        sumCarbohydrates = 0;
        sumFats = 0;
        sumProteins = 0;
        int resourceId;
        for (UserDiary userDiary : userDiaryList) {
            carbohydrates.add(userDiary.getDayFood().getFood().getCarbohydrates());
            fats.add(userDiary.getDayFood().getFood().getFats());
            proteins.add(userDiary.getDayFood().getFood().getProteins());
            categories.add(userDiary.getDayFood().getFood().getCategory());
            names.add(userDiary.getDayFood().getFood().getFoodname());
            resourceId = resources.getIdentifier(userDiary.getDayFood().getFood().getPictureString().toString(), "drawable", this.getPackageName());
            idImages.add((int) resourceId);
            quantityList.add(userDiary.getQuantity());
            sumCarbohydrates += userDiary.getDayFood().getFood().getCarbohydrates() * userDiary.getQuantity() / 100;
            sumFats += userDiary.getDayFood().getFood().getFats() * userDiary.getQuantity() / 100;
            sumProteins += userDiary.getDayFood().getFood().getProteins() * userDiary.getQuantity() / 100;
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
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);

                Intent intentDataString = new Intent(FoodDiaryActivity.this, AddFoodActivity.class);
                intent.putExtra("calendarString", m_textView.getText());
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
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

    //in metoda aceasta nu va intra deoarece eu nu m nevoie sa aflu toata lista de food din bd, de aeea nu apelez SelectFoodTask
    @Override
    public void onSelectFoodDone(String result) throws UnsupportedEncodingException {

        if (!result.isEmpty()) {
            foods = DataManager.getInstance().parseFoods(result);
            DataManager.getInstance().setFoodsList(foods);

//            String baseAuthStr = username + ":" + password;
//            String str = "Basic " + Base64.encodeToString(baseAuthStr.getBytes("UTF-8"), Base64.DEFAULT);
//            DataManager.getInstance().setBaseAuthStr(str);

            //putNameFoodInVector();
            putInArrayLists();
            Toast.makeText(getApplicationContext(), "Get all foods from database", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onSelectFoodFromCurrentDayDone(String result) throws UnsupportedEncodingException {

        if (!result.isEmpty()) {
            foods = DataManager.getInstance().parseFoods(result);
            DataManager.getInstance().setFoodsList(foods);

//            String baseAuthStr = username + ":" + password;
//            String str = "Basic " + Base64.encodeToString(baseAuthStr.getBytes("UTF-8"), Base64.DEFAULT);
//            DataManager.getInstance().setBaseAuthStr(str);

            //putNameFoodInVector();
            putInArrayLists();
            m_listView.setAdapter(customAdaptor);
            Toast.makeText(getApplicationContext(), "Get all foods from database", Toast.LENGTH_SHORT).show();
        }
    }

    public void setTextViewWithTotalValues() {
        m_textViewTotalCarbohydrates.setText("Total carbohydrates: " + Math.floor(sumCarbohydrates * 100) / 100);
        m_textViewTotalFats.setText("Total fats: " + Math.floor(sumFats * 100) / 100);
        m_textViewTotalProteins.setText("Total proteins: " + Math.floor(sumProteins * 100) / 100);

    }

    @Override
    public void onSelectUserDiaryDone(String result) throws UnsupportedEncodingException {

        if (!result.isEmpty()) {
            userDiaryList = DataManager.getInstance().parseUserDiaryList(result);
            DataManager.getInstance().setUserDiaryList(userDiaryList);

//            String baseAuthStr = username + ":" + password;
//            String str = "Basic " + Base64.encodeToString(baseAuthStr.getBytes("UTF-8"), Base64.DEFAULT);
//            DataManager.getInstance().setBaseAuthStr(str);

            //putNameFoodInVector();
            putInArrayLists();
            setTextViewWithTotalValues();
            m_listView.setAdapter(customAdaptor);
            Toast.makeText(getApplicationContext(), "Get all foods from database day-user", Toast.LENGTH_SHORT).show();
        }
    }

    class CustomAdaptor extends BaseAdapter {

        @Override
        //public int getCount() {
//            return images.length;
//        }
        public int getCount() {
            return idImages.size();
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
            m_textViewCarbohydratesResult = (TextView) view.findViewById(R.id.textViewCarbohydratesResult);
            m_textViewFatsResult = (TextView) view.findViewById(R.id.textViewFatsResult);
            m_textViewProteinsResult = (TextView) view.findViewById(R.id.textViewProteinsResult);
            m_textViewCategoryResult = (TextView) view.findViewById(R.id.textViewCategoryResult);
            m_textViewQuantityResult =(TextView) view.findViewById(R.id.textViewQuantityResult);
            //mImageView.setImageResource(images[position]);
            mImageView.setImageResource(idImages.get(position));
            mTextView.setText(names.get(position));
            //Math.floor(currentWeight / s * 100) / 100;
            m_textViewCarbohydratesResult.setText(Math.floor(carbohydrates.get(position) * quantityList.get(position) / 100 * 100) / 100 + "");
            m_textViewFatsResult.setText(Math.floor(fats.get(position) * quantityList.get(position) / 100 * 100) / 100 + "");
            m_textViewProteinsResult.setText(Math.floor(proteins.get(position) * quantityList.get(position) / 100 * 100) / 100 + "");
            m_textViewCategoryResult.setText(categories.get(position));
            m_textViewQuantityResult.setText(quantityList.get(position)+"");

            return view;
        }

    }


}
