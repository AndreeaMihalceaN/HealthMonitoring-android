package com.example.andreea.healthmonitoring;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;

import java.util.Calendar;

import android.os.Build;
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
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import manager.DataManager;
import model.DayFood;
import model.Food;
import model.User;
import model.UserDiary;
import webservice.DeleteUserDiaryDelegate;
import webservice.DeleteUserDiaryTask;
import webservice.RegisterFoodDelegate;
import webservice.SelectAnUserDiaryDelegate;
import webservice.SelectAnUserDiaryTask;
import webservice.SelectFoodDayDelegate;
import webservice.SelectFoodDayTask;
import webservice.SelectFoodDelegate;
import webservice.SelectFoodFromCurrentDayDelegate;
import webservice.SelectUserDiaryDelegate;
import webservice.SelectUserDiaryTask;

public class FoodDiaryActivity extends AppCompatActivity implements RegisterFoodDelegate, SelectFoodDelegate, SelectFoodFromCurrentDayDelegate, SelectUserDiaryDelegate, SelectFoodDayDelegate, SelectAnUserDiaryDelegate, DeleteUserDiaryDelegate {

    private User userAfterLogin;
    private TextView m_textView;
    private Calendar m_currentDate;
    private int day, month, year;
    private ListView m_listView;
    private List<Food> foods;
    private List<UserDiary> userDiaryList;
    private CustomAdaptor adapter;
    private boolean chooseAnotherDate;
    private TextView m_textViewTotalCarbohydrates;
    private TextView m_textViewTotalFats;
    private TextView m_textViewTotalProteins;

    private double sumCarbohydrates;
    private double sumFats;
    private double sumProteins;
    //private int[] images = {R.drawable.melon, R.drawable.cheese, R.drawable.milkshake, R.drawable.chocolate, R.drawable.vegetablesoup};
    //private String[] names = {"Melon", "Cheese", "Milkshake", "Chocolate", "Vegetable soup"};

    public ArrayList<Double> carbohydrates = new ArrayList<>();
    public ArrayList<Double> fats = new ArrayList<>();
    public ArrayList<Double> proteins = new ArrayList<>();
    public ArrayList<String> categories = new ArrayList<>();
    public ArrayList<String> names = new ArrayList<>();
    public ArrayList<Integer> idImages = new ArrayList<>();
    public ArrayList<Double> quantityList = new ArrayList<>();

    //    private ImageView mImageView;
//    private TextView mTextView;
//    private TextView m_textViewCarbohydratesResult;
//    private TextView m_textViewFatsResult;
//    private TextView m_textViewProteinsResult;
//    private TextView m_textViewCategoryResult;
//    private TextView m_textViewQuantityResult;
    private ArrayList<Food> foodList = new ArrayList<Food>();

    private FoodDiaryActivity foodDiaryActivity;
    private Resources resources;
    private DayFood dayFood = new DayFood();
    private UserDiary userDiaryForDelete = new UserDiary();

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

        m_listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //String item = ((TextView) view).getText().toString();

                Toast.makeText(getBaseContext(), "Am dat click", Toast.LENGTH_LONG).show();
            }
        });

        m_textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(FoodDiaryActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        month = month + 1;
                        m_textView.setText(dayOfMonth + "-" + month + "-" + year);
                        chooseAnotherDate = true;
                        adapter = new CustomAdaptor(getApplicationContext(), carbohydrates, fats, proteins, categories, names, idImages, quantityList);
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
            adapter = new CustomAdaptor(getApplicationContext(), carbohydrates, fats, proteins, categories, names, idImages, quantityList);

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
        adapter.notifyDataSetChanged();
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
            m_listView.setAdapter(adapter);
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
            //CustomAdaptor adapter= new CustomAdaptor(this, carbohydrates, fats, proteins, categories, names, idImages, quantityList);
            m_listView.setAdapter(adapter);
            Toast.makeText(getApplicationContext(), "Get all foods from database day-user", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onSelectFoodDayDone(String result) throws UnsupportedEncodingException {
        if (!result.isEmpty()) {
            dayFood = DataManager.getInstance().parseDayFood(result);

            SelectAnUserDiaryTask selectAnUserDiaryTask = new SelectAnUserDiaryTask(dayFood.getId(), userAfterLogin.getUsername());
            selectAnUserDiaryTask.setSelectAnUserDiaryDelegate(foodDiaryActivity);
        }

    }

    @Override
    public void onSelectAnUserDiaryDone(String result) throws UnsupportedEncodingException {
        if (!result.isEmpty()) {
            userDiaryForDelete = DataManager.getInstance().parseUserDiary(result);

            DeleteUserDiaryTask deleteUserDiaryTask = new DeleteUserDiaryTask(userDiaryForDelete.getId());
            deleteUserDiaryTask.setDeleteUserDiaryDelegate(foodDiaryActivity);
        }
    }

    @Override
    public void onDeleteUserDiaryDone(String result) {
        Toast.makeText(FoodDiaryActivity.this, result, Toast.LENGTH_SHORT).show();
        //Refresh listView
        adapter = new CustomAdaptor(getApplicationContext(), carbohydrates, fats, proteins, categories, names, idImages, quantityList);
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

    @Override
    public void onDeleteUserDiaryError(String response) {

    }

    class CustomAdaptor extends BaseAdapter {

        Context context;
        ArrayList<Double> carbohydrates = new ArrayList<>();
        ArrayList<Double> fats = new ArrayList<>();
        ArrayList<Double> proteins = new ArrayList<>();
        ArrayList<String> categories = new ArrayList<>();
        ArrayList<String> names = new ArrayList<>();
        ArrayList<Integer> idImages = new ArrayList<>();
        ArrayList<Double> quantityList = new ArrayList<>();
        LayoutInflater inflater;


        public CustomAdaptor(@NonNull Context context, ArrayList<Double> carbohydrates, ArrayList<Double> fats, ArrayList<Double> proteins, ArrayList<String> categories, ArrayList<String> names, ArrayList<Integer> idImages, ArrayList<Double> quantityList) {
            this.context = context;
            this.carbohydrates = carbohydrates;
            this.fats = fats;
            this.proteins = proteins;
            this.categories = categories;
            this.names = names;
            this.idImages = idImages;
            this.quantityList = quantityList;

        }

        public class ViewHolder {
            ImageView mImageView;
            TextView mTextView;
            TextView m_textViewCarbohydratesResult;
            TextView m_textViewFatsResult;
            TextView m_textViewProteinsResult;
            TextView m_textViewCategoryResult;
            TextView m_textViewQuantityResult;
            ImageView m_imageViewEditQ;
            ImageView m_imageViewDelete;


        }

        @Override
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
        public View getView(final int position, View convertView, ViewGroup parent) {

            if (convertView == null) {
                inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.customlayout, null);
            }
//            View view = getLayoutInflater().inflate(R.layout.customlayout, null);
//
//            mImageView = (ImageView) view.findViewById(R.id.imageView);
//            mTextView = (TextView) view.findViewById(R.id.textView);
//            m_textViewCarbohydratesResult = (TextView) view.findViewById(R.id.textViewCarbohydratesResult);
//            m_textViewFatsResult = (TextView) view.findViewById(R.id.textViewFatsResult);
//            m_textViewProteinsResult = (TextView) view.findViewById(R.id.textViewProteinsResult);
//            m_textViewCategoryResult = (TextView) view.findViewById(R.id.textViewCategoryResult);
//            m_textViewQuantityResult = (TextView) view.findViewById(R.id.textViewQuantityResult);
//            //mImageView.setImageResource(images[position]);
//            mImageView.setImageResource(idImages.get(position));
//            mTextView.setText(names.get(position));
//            //Math.floor(currentWeight / s * 100) / 100;
//            m_textViewCarbohydratesResult.setText(Math.floor(carbohydrates.get(position) * quantityList.get(position) / 100 * 100) / 100 + "");
//            m_textViewFatsResult.setText(Math.floor(fats.get(position) * quantityList.get(position) / 100 * 100) / 100 + "");
//            m_textViewProteinsResult.setText(Math.floor(proteins.get(position) * quantityList.get(position) / 100 * 100) / 100 + "");
//            m_textViewCategoryResult.setText(categories.get(position));
//            m_textViewQuantityResult.setText(quantityList.get(position) + "");


            ViewHolder holder = new ViewHolder();
            holder.mImageView = (ImageView) convertView.findViewById(R.id.imageView);
            holder.mTextView = (TextView) convertView.findViewById(R.id.textView);
            holder.m_textViewCarbohydratesResult = (TextView) convertView.findViewById(R.id.textViewCarbohydratesResult);
            holder.m_textViewFatsResult = (TextView) convertView.findViewById(R.id.textViewFatsResult);
            holder.m_textViewProteinsResult = (TextView) convertView.findViewById(R.id.textViewProteinsResult);
            holder.m_textViewCategoryResult = (TextView) convertView.findViewById(R.id.textViewCategoryResult);
            holder.m_textViewQuantityResult = (TextView) convertView.findViewById(R.id.textViewQuantityResult);
            holder.m_imageViewEditQ = (ImageView) convertView.findViewById(R.id.imageViewEditQ);
            holder.m_imageViewDelete = (ImageView) convertView.findViewById(R.id.imageViewDelete);

            final String name = names.get(position);
            holder.mTextView.setText(names.get(position));
            holder.mImageView.setImageResource(idImages.get(position));
            holder.m_textViewCarbohydratesResult.setText(carbohydrates.get(position) + "");
            holder.m_textViewFatsResult.setText(fats.get(position) + "");
            holder.m_textViewProteinsResult.setText(proteins.get(position) + "");
            holder.m_textViewCategoryResult.setText(categories.get(position));
            holder.m_textViewQuantityResult.setText(quantityList.get(position) + "");

            holder.m_imageViewEditQ.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(context, name, Toast.LENGTH_SHORT).show();
                }
            });

            holder.m_imageViewDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(context, name, Toast.LENGTH_SHORT).show();

                    SelectFoodDayTask selectFoodDayTask = new SelectFoodDayTask(m_textView.getText().toString(), name);
                    selectFoodDayTask.setSelectFoodDayDelegate(foodDiaryActivity);

                }
            });

            return convertView;
        }

    }


}