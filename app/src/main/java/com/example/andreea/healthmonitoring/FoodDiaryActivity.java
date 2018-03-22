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
import android.view.KeyEvent;
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
import webservice.DeleteDayFoodDelegate;
import webservice.DeleteDayFoodTask;
import webservice.DeleteUserDiaryDelegate;
import webservice.DeleteUserDiaryTask;
import webservice.GetAllFoodsFromThisDayDelegate;
import webservice.GetAllFoodsFromThisDayTask;
import webservice.RegisterFoodDelegate;
import webservice.SearchFoodByIdDelegate;
import webservice.SearchFoodByIdTask;
import webservice.SelectAnUserDiaryDelegate;
import webservice.SelectAnUserDiaryTask;
import webservice.SelectFoodDayDelegate;
import webservice.SelectFoodDayTask;
import webservice.SelectFoodDelegate;
import webservice.SelectFoodFromCurrentDayDelegate;
import webservice.SelectUserDiaryDelegate;
import webservice.SelectUserDiaryTask;

public class FoodDiaryActivity extends AppCompatActivity implements /*RegisterFoodDelegate, SelectFoodDelegate, SelectFoodFromCurrentDayDelegate,*/ SelectUserDiaryDelegate, SelectFoodDayDelegate, SelectAnUserDiaryDelegate, DeleteUserDiaryDelegate, GetAllFoodsFromThisDayDelegate, DeleteDayFoodDelegate {

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

    public ArrayList<Double> carbohydrates = new ArrayList<>();
    public ArrayList<Double> fats = new ArrayList<>();
    public ArrayList<Double> proteins = new ArrayList<>();
    public ArrayList<String> categories = new ArrayList<>();
    public ArrayList<String> names = new ArrayList<>();
    public ArrayList<Integer> idImages = new ArrayList<>();
    public ArrayList<Double> quantityList = new ArrayList<>();

    private List<Food> foodList = new ArrayList<Food>();

    private FoodDiaryActivity foodDiaryActivity;
    private Resources resources;
    private DayFood dayFood = new DayFood();
    private UserDiary userDiaryForDelete = new UserDiary();
    String name = "";
    Food foodToSend = new Food();
    double quatityToSend = 0;
    double quantityValue = 0;

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

                //Toast.makeText(getBaseContext(), "Am dat click", Toast.LENGTH_LONG).show();
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
                        if (userDiaryList != null)
                            userDiaryList.clear();
                        carbohydrates.clear();
                        fats.clear();
                        proteins.clear();
                        categories.clear();
                        names.clear();
                        idImages.clear();
                        quantityList.clear();
                        GetAllFoodsFromThisDayTask getAllFoodsFromThisDayTask = new GetAllFoodsFromThisDayTask(m_textView.getText().toString(), userAfterLogin.getUsername());
                        getAllFoodsFromThisDayTask.setGetAllFoodsFromThisDayDelegate(foodDiaryActivity);


                    }
                }, year, month, day);
                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                datePickerDialog.show();
            }


        });

        if (!chooseAnotherDate) {
            adapter = new CustomAdaptor(getApplicationContext(), carbohydrates, fats, proteins, categories, names, idImages, quantityList);

            GetAllFoodsFromThisDayTask getAllFoodsFromThisDayTask = new GetAllFoodsFromThisDayTask(m_textView.getText().toString(), userAfterLogin.getUsername());
            getAllFoodsFromThisDayTask.setGetAllFoodsFromThisDayDelegate(foodDiaryActivity);

        }

    }


    public void putInArrayLists() {
        sumCarbohydrates = 0;
        sumFats = 0;
        sumProteins = 0;
        int resourceId;
        for (Food food : foodList) {
            for (UserDiary userDiaryObj : userDiaryList)
                if (userDiaryObj.getDayFood().getFoodId().equals(food.getId())) {
                    quantityValue = userDiaryObj.getQuantity();

                    carbohydrates.add(food.getCarbohydrates());
                    fats.add(food.getFats());
                    proteins.add(food.getProteins());
                    categories.add(food.getCategory());
                    names.add(food.getFoodname());
                    resourceId = resources.getIdentifier(food.getPictureString().toString(), "drawable", this.getPackageName());
                    idImages.add((int) resourceId);
                    quantityList.add(quantityValue);
                    sumCarbohydrates += food.getCarbohydrates() * quantityValue / 100;
                    sumFats += food.getFats() * quantityValue / 100;
                    sumProteins += food.getProteins() * quantityValue / 100;
                }
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
                Toast.makeText(getApplicationContext(), "Add Food icon is selected", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(FoodDiaryActivity.this, AddFoodActivity.class);
                intent.putExtra("userAfterLogin", userAfterLogin);
                intent.putExtra("caller", "FoodDiaryActivity");
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);

                Intent intentDataString = new Intent(FoodDiaryActivity.this, AddFoodActivity.class);
                intent.putExtra("calendarString", m_textView.getText());
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
//
//    @Override
//    public void onRegisterFoodDone(String result) {
//
//    }
//
//    @Override
//    public void onRegisterFoodError(String errorMsg) {
//        Toast.makeText(FoodDiaryActivity.this, errorMsg, Toast.LENGTH_SHORT).show();
//
//    }

    //in metoda aceasta nu va intra deoarece eu nu m nevoie sa aflu toata lista de food din bd, de aeea nu apelez SelectFoodTask
//    @Override
//    public void onSelectFoodDone(String result) throws UnsupportedEncodingException {
//
//        if (!result.isEmpty()) {
//            foods = DataManager.getInstance().parseFoods(result);
//            DataManager.getInstance().setFoodsList(foods);
//            putInArrayLists();
//            //Toast.makeText(getApplicationContext(), "Get all foods from database", Toast.LENGTH_SHORT).show();
//        }
//    }

//    @Override
//    public void onSelectFoodFromCurrentDayDone(String result) throws UnsupportedEncodingException {
//
//        if (!result.isEmpty()) {
//            foods = DataManager.getInstance().parseFoods(result);
//            DataManager.getInstance().setFoodsList(foods);
//            putInArrayLists();
//            m_listView.setAdapter(adapter);
//            //Toast.makeText(getApplicationContext(), "Get all foods from database", Toast.LENGTH_SHORT).show();
//        }
//    }

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

            putInArrayLists();
            setTextViewWithTotalValues();
            adapter.notifyDataSetChanged();
            m_listView.setAdapter(adapter);
            // Toast.makeText(getApplicationContext(), "Get all foods from database day-user", Toast.LENGTH_SHORT).show();
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
//        DeleteDayFoodTask deleteDayFoodTask = new DeleteDayFoodTask(m_textView.getText().toString(), name);
//        deleteDayFoodTask.setDeleteDayFoodDelegate(foodDiaryActivity);

        //Refresh listView
        adapter = new CustomAdaptor(getApplicationContext(), carbohydrates, fats, proteins, categories, names, idImages, quantityList);
        if (userDiaryList != null)
            userDiaryList.clear();
        carbohydrates.clear();
        fats.clear();
        proteins.clear();
        categories.clear();
        names.clear();
        idImages.clear();
        quantityList.clear();
        GetAllFoodsFromThisDayTask getAllFoodsFromThisDayTask = new GetAllFoodsFromThisDayTask(m_textView.getText().toString(), userAfterLogin.getUsername());
        getAllFoodsFromThisDayTask.setGetAllFoodsFromThisDayDelegate(foodDiaryActivity);

    }

    @Override
    public void onDeleteUserDiaryError(String response) {
        Toast.makeText(FoodDiaryActivity.this, response, Toast.LENGTH_SHORT).show();

    }


    @Override
    public void onGetAllFoodsFromThisDayDone(String result) throws UnsupportedEncodingException {
        if (!result.isEmpty()) {
            foodList = DataManager.getInstance().parseFoods(result);


            if (foodList.size() >= 1) {
                SelectUserDiaryTask selectUserDiaryTask = new SelectUserDiaryTask(m_textView.getText().toString(), userAfterLogin.getUsername());
                selectUserDiaryTask.setSelectUserDiaryDelegate(foodDiaryActivity);
            } else {
                adapter.notifyDataSetChanged();
                m_listView.setAdapter(adapter);

                m_textViewTotalCarbohydrates.setText("Total carbohydrates: 0");
                m_textViewTotalFats.setText("Total fats: 0");
                m_textViewTotalProteins.setText("Total proteins: 0");

            }
        }

    }

    @Override
    public void onDeleteDayFoodDone(String result) {

        Toast.makeText(FoodDiaryActivity.this, result, Toast.LENGTH_SHORT).show();
//        //Refresh listView
//        adapter = new CustomAdaptor(getApplicationContext(), carbohydrates, fats, proteins, categories, names, idImages, quantityList);
//        if (userDiaryList != null)
//            userDiaryList.clear();
//        carbohydrates.clear();
//        fats.clear();
//        proteins.clear();
//        categories.clear();
//        names.clear();
//        idImages.clear();
//        quantityList.clear();
//        GetAllFoodsFromThisDayTask getAllFoodsFromThisDayTask = new GetAllFoodsFromThisDayTask(m_textView.getText().toString(), userAfterLogin.getUsername());
//        getAllFoodsFromThisDayTask.setGetAllFoodsFromThisDayDelegate(foodDiaryActivity);
    }

    @Override
    public void onDeleteDayFoodError(String response) {

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

            final ViewHolder holder = new ViewHolder();
            holder.mImageView = (ImageView) convertView.findViewById(R.id.imageView);
            holder.mTextView = (TextView) convertView.findViewById(R.id.textView);
            holder.m_textViewCarbohydratesResult = (TextView) convertView.findViewById(R.id.textViewCarbohydratesResult);
            holder.m_textViewFatsResult = (TextView) convertView.findViewById(R.id.textViewFatsResult);
            holder.m_textViewProteinsResult = (TextView) convertView.findViewById(R.id.textViewProteinsResult);
            holder.m_textViewCategoryResult = (TextView) convertView.findViewById(R.id.textViewCategoryResult);
            holder.m_textViewQuantityResult = (TextView) convertView.findViewById(R.id.textViewQuantityResult);
            holder.m_imageViewEditQ = (ImageView) convertView.findViewById(R.id.imageViewEditQ);
            holder.m_imageViewDelete = (ImageView) convertView.findViewById(R.id.imageViewDelete);

            name = names.get(position);
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
                    for (Food selectedfood : foodList)
                        if (selectedfood.getFoodname().equals(holder.mTextView.getText().toString()))
                            name = selectedfood.getFoodname();

                    SelectFoodDayTask selectFoodDayTask = new SelectFoodDayTask(m_textView.getText().toString(), name);
                    selectFoodDayTask.setSelectFoodDayDelegate(foodDiaryActivity);

                }
            });
            holder.m_imageViewEditQ.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(context, " edit", Toast.LENGTH_SHORT).show();

                    for (Food selectedfood : foodList)
                        if (selectedfood.getFoodname().equals(holder.mTextView.getText().toString()))
                            foodToSend = selectedfood;

                    quatityToSend = Double.parseDouble(holder.m_textViewQuantityResult.getText().toString());

                    Intent intent = new Intent(FoodDiaryActivity.this, EditActivity.class);

                    intent.putExtra("userAfterLogin", userAfterLogin);
                    intent.putExtra("foodToSend", foodToSend);
                    intent.putExtra("quantityFromHolderSelected", quatityToSend);
                    intent.putExtra("calendarString", m_textView.getText());
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);

                }
            });

            return convertView;
        }

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


}