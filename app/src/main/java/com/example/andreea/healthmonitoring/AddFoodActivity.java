package com.example.andreea.healthmonitoring;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Base64;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import manager.DataManager;
import model.Day;
import model.Food;
import model.User;
import webservice.AddDayDelegate;
import webservice.AddDayTask;
import webservice.GetFoodByNameDelegate;
import webservice.GetFoodByNameTask;
import webservice.LoginDelegate;
import webservice.LoginTask;
import webservice.RegisterDayFoodDelegate;
import webservice.RegisterDayFoodTask;
import webservice.SearchDayDelegate;
import webservice.SearchDayTask;
import webservice.SelectFoodDelegate;
import webservice.SelectFoodTask;
import webservice.UpdateDayDelegate;
import webservice.UpdateDayTask;
import webservice.UpdateDelegate;
import webservice.UpdateTask;

public class AddFoodActivity extends AppCompatActivity implements SelectFoodDelegate, GetFoodByNameDelegate, SearchDayDelegate, UpdateDayDelegate, RegisterDayFoodDelegate, AddDayDelegate {

    private AutoCompleteTextView m_autoCompleteTextView;
    private TextView m_textViewCarbohydratesQuantity;
    private EditText m_editTextCarbohydratesQuantity;
    private TextView m_textViewProteinQuantity;
    private EditText m_editTextProteinQuantity;
    private TextView m_textViewFatsQuantity;
    private EditText m_editTextFatsQuantity;
    private TextView m_textViewCategory;
    private EditText m_editTextCategory;
    //private CardView m_cardSubmit;
    private List<Food> foods;
    private User userAfterLogin;
    private Food receivedFood = new Food();
    private Button m_buttonMinus;
    private Button m_buttonPlus;
    private Button m_buttonQuantity;
    private Button m_buttonRefresh;
    private double counter;
    private String calendarString;
    private Day currentDay;
    private Button m_buttonSubmit;
    private String dateToday;
//    private TextView m_textViewForData;

    private AddFoodActivity addFoodActivity;
    //private static String[] Foods = new String[]{"Yoghurt", "Cheese", "Milk", "Spaghetti", "Steak", "Soup", "Asparagus", "Chicken", "Fish", "Baked fish with vegetables"};
    private static ArrayList<String> Foods;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_food);

        addFoodActivity = this;
        counter=100;
        Intent intent = getIntent();
        userAfterLogin = (User) intent.getSerializableExtra("userAfterLogin");

        m_buttonSubmit = (Button) findViewById(R.id.buttonSubmit);
//        m_textViewForData=(TextView) findViewById(R.id.textViewForData);
        m_buttonMinus = (Button) findViewById(R.id.buttonMinus);
        m_buttonPlus = (Button) findViewById(R.id.buttonPlus);
        m_buttonQuantity = (Button) findViewById(R.id.buttonQuantity);
        m_buttonRefresh = (Button) findViewById(R.id.buttonRefresh);

        m_textViewCarbohydratesQuantity = (TextView) findViewById(R.id.textViewCarbohydratesQuantity);
        m_editTextCarbohydratesQuantity = (EditText) findViewById(R.id.editTextCarbohydratesQuantity);

        m_textViewProteinQuantity = (TextView) findViewById(R.id.textViewProteinQuantity);
        m_editTextProteinQuantity = (EditText) findViewById(R.id.editTextProteinQuantity);

        m_textViewFatsQuantity = (TextView) findViewById(R.id.textViewFatsQuantity);
        m_editTextFatsQuantity = (EditText) findViewById(R.id.editTextFatsQuantity);

        m_textViewCategory = (TextView) findViewById(R.id.textViewCategory);
        m_editTextCategory = (EditText) findViewById(R.id.editTextCategory);

        //m_cardSubmit = (CardView) findViewById(R.id.cardSubmit);

        m_autoCompleteTextView = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView);

        doInvisibleAndEnable();

        Bundle bundle = getIntent().getExtras();
        calendarString = bundle.getString("calendarString");

        //userAfterLogin.getUsername(), userAfterLogin.getPassword() in SelectFoodTask() mai jos

        SelectFoodTask selectFoodTask = new SelectFoodTask();
        selectFoodTask.setSelectFoodDelegate(addFoodActivity);

        //ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, Foods);
//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, Foods);
//        m_autoCompleteTextView.setAdapter(adapter);


        m_autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                Toast.makeText(getBaseContext(), "MultiAutoComplete: " +
                                "you add color " + arg0.getItemAtPosition(arg2),
                        Toast.LENGTH_LONG).show();


                GetFoodByNameTask getFoodByNameTask = new GetFoodByNameTask(m_autoCompleteTextView.getText().toString());
                getFoodByNameTask.setGetFoodByNameDelegate(addFoodActivity);


                m_textViewCarbohydratesQuantity.setVisibility(View.VISIBLE);
                m_editTextCarbohydratesQuantity.setVisibility(View.VISIBLE);

                m_textViewProteinQuantity.setVisibility(View.VISIBLE);
                m_editTextProteinQuantity.setVisibility(View.VISIBLE);

                m_textViewFatsQuantity.setVisibility(View.VISIBLE);
                m_editTextFatsQuantity.setVisibility(View.VISIBLE);

                m_textViewCategory.setVisibility(View.VISIBLE);
                m_editTextCategory.setVisibility(View.VISIBLE);

                m_buttonMinus.setVisibility(View.VISIBLE);
                m_buttonPlus.setVisibility(View.VISIBLE);
                m_buttonQuantity.setVisibility(View.VISIBLE);

//                m_cardSubmit.setEnabled(false);
//                m_cardSubmit.setVisibility(View.VISIBLE);

                m_buttonRefresh.setVisibility(View.VISIBLE);
            }

        });

        m_buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(AddFoodActivity.this, SignUpActivity.class);
//                startActivity(intent);
                SearchDayTask searchDayTask = new SearchDayTask(calendarString);
                searchDayTask.setSearchDayDelegate(addFoodActivity);

//                AddDayTask addDayTask = new AddDayTask(calendarString);
//                addDayTask.setAddDayDelegate(addFoodActivity);
//
//                RegisterDayFoodTask registerDayFoodTask = new RegisterDayFoodTask(dateToday, receivedFood.getName());
//                registerDayFoodTask.setRegisterDayFoodDelegate(addFoodActivity);

                //Update day

                //Toast.makeText(getApplicationContext(), "Am intrat in actiune testSUbmit", Toast.LENGTH_SHORT).show();

            }
        });

//        m_cardSubmit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                Intent intent = new Intent(AddFoodActivity.this, SignUpActivity.class);
////                startActivity(intent);
//                SearchDayTask searchDayTask = new SearchDayTask(calendarString);
//                searchDayTask.setSearchDayDelegate(addFoodActivity);
//
//                Toast.makeText(getApplicationContext(), "Am intrat in actiune testSUbmit", Toast.LENGTH_SHORT).show();
//
//            }
//        });
//
//        m_cardSubmit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                Intent intent = new Intent(AddFoodActivity.this, SignUpActivity.class);
////                startActivity(intent);
//                SearchDayTask searchDayTask = new SearchDayTask(calendarString);
//                searchDayTask.setSearchDayDelegate(addFoodActivity);
//
//
//                Toast.makeText(getApplicationContext(), "Am intrat in actiune add food", Toast.LENGTH_SHORT).show();
//                Toast.makeText(getApplicationContext(), currentDay.getDate().toString()+" este data selectata", Toast.LENGTH_SHORT).show();
//            }
//        });

        m_buttonPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                plusCounter();

            }
        });

        m_buttonMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                minusCounter();

            }
        });

        m_buttonRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                counter = 100;
                m_autoCompleteTextView.setText("");
                doInvisibleAndEnable();

            }
        });

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(AddFoodActivity.this, FoodDiaryActivity.class);
        intent.putExtra("userAfterLogin", userAfterLogin);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    void doInvisibleAndEnable() {
        m_buttonMinus.setVisibility(View.INVISIBLE);
        m_buttonPlus.setVisibility(View.INVISIBLE);
        m_buttonQuantity.setVisibility(View.INVISIBLE);
        m_buttonRefresh.setVisibility(View.INVISIBLE);

        m_textViewCarbohydratesQuantity.setVisibility(View.INVISIBLE);
        m_editTextCarbohydratesQuantity.setVisibility(View.INVISIBLE);
        m_editTextCarbohydratesQuantity.setEnabled(false);

        m_textViewProteinQuantity.setVisibility(View.INVISIBLE);
        m_editTextProteinQuantity.setVisibility(View.INVISIBLE);
        m_editTextProteinQuantity.setEnabled(false);

        m_textViewFatsQuantity.setVisibility(View.INVISIBLE);
        m_editTextFatsQuantity.setVisibility(View.INVISIBLE);
        m_editTextFatsQuantity.setEnabled(false);

        m_textViewCategory.setVisibility(View.INVISIBLE);
        m_editTextCategory.setVisibility(View.INVISIBLE);
        m_editTextCategory.setEnabled(false);

//        m_cardSubmit.setEnabled(false);
//        m_cardSubmit.setVisibility(View.INVISIBLE);


    }

    void putNameFoodInVector() {
        Foods = new ArrayList<>();
        for (Food food : foods) {
            Foods.add(food.getFoodname());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, Foods);
        m_autoCompleteTextView.setAdapter(adapter);
    }

    @Override
    public void onGetFoodByNameDone(String result) throws UnsupportedEncodingException {

        if (!result.isEmpty()) {
            receivedFood = DataManager.getInstance().parseFood(result);
            m_editTextCarbohydratesQuantity.setText(receivedFood.getCarbohydrates() + "");
            m_editTextProteinQuantity.setText(receivedFood.getProteins() + "");
            m_editTextFatsQuantity.setText(receivedFood.getFats() + "");
            m_editTextCategory.setText(receivedFood.getCategory());
            //DataManager.getInstance().setFoodsList(foods);

//            String baseAuthStr = username + ":" + password;
//            String str = "Basic " + Base64.encodeToString(baseAuthStr.getBytes("UTF-8"), Base64.DEFAULT);
//            DataManager.getInstance().setBaseAuthStr(str);

            //putNameFoodInVector();
            Toast.makeText(getApplicationContext(), "Getfood selected from textEdit", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onSearchDayDone(String result) throws UnsupportedEncodingException {
        if (!result.isEmpty()) {
            currentDay = DataManager.getInstance().parseDay(result);
            //currentDay.addFoodInFoodList(receivedFood);


            DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
            dateToday = formatter.format(currentDay.getDate().getTime());

//            AddDayTask addDayTask = new AddDayTask(calendarString);
//            addDayTask.setAddDayDelegate(addFoodActivity);

            RegisterDayFoodTask registerDayFoodTask = new RegisterDayFoodTask(dateToday, receivedFood.getFoodname());
            registerDayFoodTask.setRegisterDayFoodDelegate(addFoodActivity);

//            UpdateDayTask updateDayTask = new UpdateDayTask(dateToday, currentDay.getFoods());
//            updateDayTask.setUpdateDayDelegate(addFoodActivity);
//            Toast.makeText(AddFoodActivity.this, "Updated day! ", Toast.LENGTH_SHORT).show();
            //DataManager.getInstance().setFoodsListFromDay(currentDay.getFoods());

//            SearchDayTask searchDayTask = new SearchDayTask(calendarString);
//            searchDayTask.setSearchDayDelegate(addFoodActivity);

//            String baseAuthStr = username + ":" + password;
//            String str = "Basic " + Base64.encodeToString(baseAuthStr.getBytes("UTF-8"), Base64.DEFAULT);
//            DataManager.getInstance().setBaseAuthStr(str);

            //putNameFoodInVector();
            //m_textViewForData.setText(currentDay.getDate().toString());
            Toast.makeText(getApplicationContext(), dateToday + " este data selectata", Toast.LENGTH_SHORT).show();
            Toast.makeText(getApplicationContext(), "Get currentDay", Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(getApplicationContext(), "NU exista aceasta data", Toast.LENGTH_SHORT).show();
            AddDayTask addDayTask = new AddDayTask(calendarString);
            addDayTask.setAddDayDelegate(addFoodActivity);

            RegisterDayFoodTask registerDayFoodTask = new RegisterDayFoodTask(calendarString, receivedFood.getFoodname());
            registerDayFoodTask.setRegisterDayFoodDelegate(addFoodActivity);
        }

    }

    public void resetParametersFromTextEdit()
    {
        m_editTextCarbohydratesQuantity.setText((receivedFood.getCarbohydrates()*counter)/100 + "");
        m_editTextProteinQuantity.setText((receivedFood.getProteins()*counter)/100 + "");
        m_editTextFatsQuantity.setText((receivedFood.getFats()*counter)/100 + "");
        m_editTextCategory.setText(receivedFood.getCategory());
    }

    private void resetCounter() {
        counter = 100;
        m_buttonQuantity.setText(counter + "");
        resetParametersFromTextEdit();

    }

    private void plusCounter() {
        counter+=0.25;
        m_buttonQuantity.setText(counter + "");
        resetParametersFromTextEdit();

    }

    private void minusCounter() {
        counter -= 0.25;
        if(counter<=0)
            counter=0;
        m_buttonQuantity.setText(counter + "");
        resetParametersFromTextEdit();

    }

    @Override
    public void onSelectFoodDone(String result) throws UnsupportedEncodingException {
        if (!result.isEmpty()) {
            foods = DataManager.getInstance().parseFoods(result);
            DataManager.getInstance().setFoodsList(foods);

//            String baseAuthStr = username + ":" + password;
//            String str = "Basic " + Base64.encodeToString(baseAuthStr.getBytes("UTF-8"), Base64.DEFAULT);
//            DataManager.getInstance().setBaseAuthStr(str);

            putNameFoodInVector();
            Toast.makeText(getApplicationContext(), "Get all foods from database", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onUpdateDayDone(String result) {

    }

    @Override
    public void onUpdateDayError(String response) {

        Toast.makeText(AddFoodActivity.this, response, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRegisterDayFoodDone(String result) {

    }

    @Override
    public void onRegisterDayFoodError(String response) {

        Toast.makeText(AddFoodActivity.this, response, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onAddDayDone(String result) {

    }

    @Override
    public void onAddDayError(String response) {
        Toast.makeText(AddFoodActivity.this, response, Toast.LENGTH_SHORT).show();

    }
}
