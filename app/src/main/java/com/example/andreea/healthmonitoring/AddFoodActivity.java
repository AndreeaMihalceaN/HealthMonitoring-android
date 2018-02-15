package com.example.andreea.healthmonitoring;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.text.InputType;
import android.util.Base64;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import manager.DataManager;
import model.Day;
import model.DayFood;
import model.Food;
import model.User;
import model.UserDiary;
import webservice.AddDayDelegate;
import webservice.AddDayTask;
import webservice.AddUserDiaryDelegate;
import webservice.AddUserDiaryTask;
import webservice.GetFoodByName2Delegate;
import webservice.GetFoodByName2Task;
import webservice.GetFoodByNameDelegate;
import webservice.GetFoodByNameTask;
import webservice.GetUserDiaryDelegate;
import webservice.GetUserDiaryTask;
import webservice.LoginDelegate;
import webservice.LoginTask;
import webservice.RegisterDayFoodDelegate;
import webservice.RegisterDayFoodTask;
import webservice.RegisterFoodDelegate;
import webservice.RegisterFoodTask;
import webservice.SearchDayDelegate;
import webservice.SearchDayTask;
import webservice.SelectFoodDayDelegate;
import webservice.SelectFoodDayTask;
import webservice.SelectFoodDelegate;
import webservice.SelectFoodTask;
import webservice.UpdateDayDelegate;
import webservice.UpdateDayTask;
import webservice.UpdateDelegate;
import webservice.UpdateTask;
import webservice.UpdateUserDiaryQuantityDelegate;
import webservice.UpdateUserDiaryQuantityTask;

public class AddFoodActivity extends AppCompatActivity implements SelectFoodDelegate, GetFoodByNameDelegate, GetFoodByName2Delegate, SearchDayDelegate, UpdateDayDelegate, RegisterDayFoodDelegate, AddDayDelegate, AddUserDiaryDelegate, GetUserDiaryDelegate, UpdateUserDiaryQuantityDelegate, SelectFoodDayDelegate, RegisterFoodDelegate {

    private AutoCompleteTextView m_autoCompleteTextView;
    private TextView m_textViewCarbohydratesQuantity;
    private EditText m_editTextCarbohydratesQuantity;
    private TextView m_textViewProteinQuantity;
    private EditText m_editTextProteinQuantity;
    private TextView m_textViewFatsQuantity;
    private EditText m_editTextFatsQuantity;
    private TextView m_textViewCategory;
    private EditText m_editTextCategory;
    private List<Food> foods;
    private User userAfterLogin;
    private Food receivedFood = new Food();
    private Button m_buttonMinus;
    private Button m_buttonPlus;
    private TextView m_textViewQuantity;
    private Button m_buttonRefresh;
    private double counter;
    private String calendarString;
    private Day currentDay;
    private Button m_buttonSubmit;
    private Button m_buttonAddNewFood;
    private String dateToday;
    private ImageView m_imageViewChooseHealth;

    private AddFoodActivity addFoodActivity;
    private static ArrayList<String> Foods;
    private UserDiary userDiary;
    private TextView m_textViewError;
    private DayFood dayFoodSearch;
    private String caller;
    private Food foodReceived;
    private boolean ok = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_food);

        addFoodActivity = this;
        counter = 100;
        Intent intent = getIntent();
        userAfterLogin = (User) intent.getSerializableExtra("userAfterLogin");
        caller = getIntent().getStringExtra("caller");


        m_imageViewChooseHealth = (ImageView) findViewById(R.id.imageViewChooseHealth);
        m_buttonSubmit = (Button) findViewById(R.id.buttonSubmit);
        m_buttonAddNewFood = (Button) findViewById(R.id.buttonAddNewFood);
        m_buttonMinus = (Button) findViewById(R.id.buttonMinus);
        m_buttonPlus = (Button) findViewById(R.id.buttonPlus);
        m_textViewQuantity = (TextView) findViewById(R.id.textViewQuantity);
        m_buttonRefresh = (Button) findViewById(R.id.buttonRefresh);

        m_textViewCarbohydratesQuantity = (TextView) findViewById(R.id.textViewCarbohydratesQuantity);
        m_editTextCarbohydratesQuantity = (EditText) findViewById(R.id.editTextCarbohydratesQuantity);

        m_textViewProteinQuantity = (TextView) findViewById(R.id.textViewProteinQuantity);
        m_editTextProteinQuantity = (EditText) findViewById(R.id.editTextProteinQuantity);

        m_textViewFatsQuantity = (TextView) findViewById(R.id.textViewFatsQuantity);
        m_editTextFatsQuantity = (EditText) findViewById(R.id.editTextFatsQuantity);

        m_textViewCategory = (TextView) findViewById(R.id.textViewCategory);
        m_editTextCategory = (EditText) findViewById(R.id.editTextCategory);

        m_autoCompleteTextView = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView);
        m_textViewError = (TextView) findViewById(R.id.textViewError);

        if (caller.equals("AlbumsAdapter")) {
            foodReceived = (Food) intent.getSerializableExtra("food");
            Calendar c = Calendar.getInstance();
            SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
            calendarString = df.format(c.getTime());
            m_buttonAddNewFood.setVisibility(View.INVISIBLE);
            m_buttonAddNewFood.setEnabled(false);
            actionFromAlbumsAdapter();

            //Toast.makeText(addFoodActivity, formattedDate, Toast.LENGTH_SHORT).show();
        } else {
            doInvisibleAndEnable();

            Bundle bundle = getIntent().getExtras();
            calendarString = bundle.getString("calendarString");
        }

        SelectFoodTask selectFoodTask = new SelectFoodTask();
        selectFoodTask.setSelectFoodDelegate(addFoodActivity);

        m_autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                Toast.makeText(getBaseContext(), "MultiAutoComplete: " +
                                "you add color " + arg0.getItemAtPosition(arg2),
                        Toast.LENGTH_LONG).show();

                m_imageViewChooseHealth.setVisibility(View.INVISIBLE);
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
                m_textViewQuantity.setVisibility(View.VISIBLE);
                m_buttonSubmit.setVisibility(View.VISIBLE);

                m_buttonRefresh.setVisibility(View.VISIBLE);
                m_buttonAddNewFood.setVisibility(View.INVISIBLE);
            }

        });

        m_textViewQuantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                m_textViewError.setText("");
                m_textViewQuantity.setCursorVisible(true);
                m_textViewQuantity.setFocusableInTouchMode(true);
                m_textViewQuantity.setInputType(InputType.TYPE_CLASS_TEXT);
                m_textViewQuantity.requestFocus();

            }
        });


        m_buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ("Register new food AND Submit".equals(m_buttonSubmit.getText())) {
                    if (validateTextEditsForAdd()) {
                        ok = true;
                        m_textViewError.setText("");
                        counter = Double.parseDouble(m_textViewQuantity.getText().toString());
                        GetFoodByNameTask getFoodByNameTask = new GetFoodByNameTask(m_autoCompleteTextView.getText().toString());
                        getFoodByNameTask.setGetFoodByNameDelegate(addFoodActivity);
//                        RegisterFoodTask registerFood = new RegisterFoodTask(m_autoCompleteTextView.getText().toString(), Double.parseDouble(m_editTextCarbohydratesQuantity.getText().toString()), Double.parseDouble(m_editTextProteinQuantity.getText().toString()), Double.parseDouble(m_editTextFatsQuantity.getText().toString()), m_editTextCategory.getText().toString());
//                        registerFood.setRegisterFoodDelegate(addFoodActivity);
                       // Toast.makeText(addFoodActivity, "Da, e bine", Toast.LENGTH_SHORT).show();
                        m_buttonRefresh.setVisibility(View.VISIBLE);
                    } else
                        m_textViewError.setText("Quantity, Qarbohydrates, Fats, Proteins fields contains letters or field for new food is empty! Try again!");
                } else {
                    if (validateTextFromQuantityTextView()) {
                        ok = false;
                        m_textViewError.setText("");
                        counter = Double.parseDouble(m_textViewQuantity.getText().toString());
                        SearchDayTask searchDayTask = new SearchDayTask(calendarString);
                        searchDayTask.setSearchDayDelegate(addFoodActivity);
                    } else
                        m_textViewError.setText("Quantity value contains letters! Try again with a numeric value");
                }


            }
        });


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
                m_textViewError.setText("");
                counter = 100;
                m_textViewQuantity.setText(counter + "");
                m_autoCompleteTextView.setText("");
                doInvisibleAndEnable();
                if (ok) {
                    m_editTextCarbohydratesQuantity.setText("0");
                    m_editTextFatsQuantity.setText("0");
                    m_editTextProteinQuantity.setText("0");
                    m_editTextCategory.setText("Type a Category");

                }
                m_imageViewChooseHealth.setVisibility(View.VISIBLE);
                m_buttonAddNewFood.setVisibility(View.VISIBLE);

            }
        });

        m_buttonAddNewFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ok = true;
                m_textViewError.setText("");
                counter = 100;
                m_textViewQuantity.setText(counter + "");
                m_autoCompleteTextView.setText("");
                doVisibleAndEnableForAddNew();
                m_imageViewChooseHealth.setVisibility(View.INVISIBLE);
                m_buttonSubmit.setText("Register new food AND Submit");


            }
        });

    }

    //private void actionAutoCompleteTextView
    private void actionFromAlbumsAdapter() {
        m_autoCompleteTextView.setText(foodReceived.getFoodname().toString());
        //m_autoCompleteTextView.setImeActionLabel(foodReceived.getFoodname().toString(), KeyEvent.KEYCODE_ENTER);
        m_imageViewChooseHealth.setVisibility(View.INVISIBLE);
        GetFoodByNameTask getFoodByNameTask = new GetFoodByNameTask(m_autoCompleteTextView.getText().toString());
        getFoodByNameTask.setGetFoodByNameDelegate(addFoodActivity);
        m_editTextCarbohydratesQuantity.setEnabled(false);
        m_editTextProteinQuantity.setEnabled(false);
        m_editTextFatsQuantity.setEnabled(false);
        m_editTextCategory.setEnabled(false);


    }

    private Boolean validateTextFromQuantityTextView() {
        String textFromQuantity = m_textViewQuantity.getText().toString();
        if (textFromQuantity.matches("[0-9]{1,13}(\\.[0-9]*)?")) {
            return true;
        }
        return false;
    }

    private Boolean validateTextEditsForAdd() {
        String textFromQuantity = m_textViewQuantity.getText().toString();
        String textCarbohydrates = m_editTextCarbohydratesQuantity.getText().toString();
        String textProtein = m_editTextProteinQuantity.getText().toString();
        String textFats = m_editTextFatsQuantity.getText().toString();
        String stringFood = m_autoCompleteTextView.getText().toString();

        if (!stringFood.isEmpty() && textFromQuantity.matches("[0-9]{1,13}(\\.[0-9]*)?") &&
                textCarbohydrates.matches("[0-9]{1,13}(\\.[0-9]*)?") &&
                textProtein.matches("[0-9]{1,13}(\\.[0-9]*)?") &&
                textFats.matches("[0-9]{1,13}(\\.[0-9]*)?")) {
            return true;
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(AddFoodActivity.this, FoodDiaryActivity.class);
        intent.putExtra("userAfterLogin", userAfterLogin);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    void doInvisibleAndEnable() {

        m_textViewError.setText("");
        m_buttonMinus.setVisibility(View.INVISIBLE);
        m_buttonPlus.setVisibility(View.INVISIBLE);
        m_textViewQuantity.setVisibility(View.INVISIBLE);
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

        m_buttonSubmit.setVisibility(View.INVISIBLE);

    }

    void doVisibleAndEnableForAddNew() {

        m_textViewError.setText("");
        m_buttonMinus.setVisibility(View.VISIBLE);
        m_buttonPlus.setVisibility(View.VISIBLE);
        m_textViewQuantity.setVisibility(View.VISIBLE);
        m_buttonRefresh.setVisibility(View.INVISIBLE);

        m_textViewCarbohydratesQuantity.setVisibility(View.VISIBLE);
        m_editTextCarbohydratesQuantity.setVisibility(View.VISIBLE);
        m_editTextCarbohydratesQuantity.setEnabled(true);

        m_textViewProteinQuantity.setVisibility(View.VISIBLE);
        m_editTextProteinQuantity.setVisibility(View.VISIBLE);
        m_editTextProteinQuantity.setEnabled(true);

        m_textViewFatsQuantity.setVisibility(View.VISIBLE);
        m_editTextFatsQuantity.setVisibility(View.VISIBLE);
        m_editTextFatsQuantity.setEnabled(true);

        m_textViewCategory.setVisibility(View.VISIBLE);
        m_editTextCategory.setVisibility(View.VISIBLE);
        m_editTextCategory.setEnabled(true);

        m_buttonSubmit.setVisibility(View.VISIBLE);
        m_buttonAddNewFood.setVisibility(View.INVISIBLE);

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

            if (ok) {

                m_editTextCarbohydratesQuantity.setText(receivedFood.getCarbohydrates() + "");
                m_editTextCarbohydratesQuantity.setEnabled(false);
                m_editTextFatsQuantity.setText(receivedFood.getFats() + "");
                m_editTextFatsQuantity.setEnabled(false);
                m_editTextProteinQuantity.setText(receivedFood.getProteins() + "");
                m_editTextProteinQuantity.setEnabled(false);
                m_editTextCategory.setText(receivedFood.getCategory() + "");
                m_editTextCategory.setEnabled(false);

                SearchDayTask searchDayTask = new SearchDayTask(calendarString);
                searchDayTask.setSearchDayDelegate(addFoodActivity);
            }
            //Toast.makeText(getApplicationContext(), "Getfood selected from textEdit", Toast.LENGTH_SHORT).show();
        } else {
            if (ok) {
                RegisterFoodTask registerFood = new RegisterFoodTask(m_autoCompleteTextView.getText().toString(), Double.parseDouble(m_editTextCarbohydratesQuantity.getText().toString()), Double.parseDouble(m_editTextProteinQuantity.getText().toString()), Double.parseDouble(m_editTextFatsQuantity.getText().toString()), m_editTextCategory.getText().toString());
                registerFood.setRegisterFoodDelegate(addFoodActivity);
                Toast.makeText(addFoodActivity, "Doesn't exist this food", Toast.LENGTH_SHORT).show();
            }

        }
    }

    @Override
    public void onSearchDayDone(String result) throws UnsupportedEncodingException {
        if (!result.isEmpty()) {
            currentDay = DataManager.getInstance().parseDay(result);

            DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
            dateToday = formatter.format(currentDay.getDate().getTime());

//            RegisterDayFoodTask registerDayFoodTask = new RegisterDayFoodTask(dateToday, receivedFood.getFoodname());
//            registerDayFoodTask.setRegisterDayFoodDelegate(addFoodActivity);

            //Toast.makeText(getApplicationContext(), dateToday + " este data selectata", Toast.LENGTH_SHORT).show();
            //Toast.makeText(getApplicationContext(), "Get currentDay", Toast.LENGTH_SHORT).show();
        } else {
           // Toast .makeText(getApplicationContext(), "NU exista aceasta data", Toast.LENGTH_SHORT).show();
            AddDayTask addDayTask = new AddDayTask(calendarString);
            addDayTask.setAddDayDelegate(addFoodActivity);

            dateToday = calendarString;
//            RegisterDayFoodTask registerDayFoodTask = new RegisterDayFoodTask(calendarString, receivedFood.getFoodname());
//            registerDayFoodTask.setRegisterDayFoodDelegate(addFoodActivity);
        }
//verifica daca exista acest food in abza de date, corect dat dupa nume
        GetFoodByName2Task getFoodByName2Task = new GetFoodByName2Task(m_autoCompleteTextView.getText().toString());
        getFoodByName2Task.setGetFoodByName2Delegate(addFoodActivity);


    }

    public void resetParametersFromTextEdit() {
        m_editTextCarbohydratesQuantity.setText((receivedFood.getCarbohydrates() * counter) / 100 + "");
        m_editTextProteinQuantity.setText((receivedFood.getProteins() * counter) / 100 + "");
        m_editTextFatsQuantity.setText((receivedFood.getFats() * counter) / 100 + "");
        m_editTextCategory.setText(receivedFood.getCategory());
    }

    private void resetCounter() {
        counter = 100;
        m_textViewQuantity.setText(counter + "");
        resetParametersFromTextEdit();

    }

    private void plusCounter() {
        m_textViewError.setText("");
        counter += 0.25;
        m_textViewQuantity.setText(counter + "");
        if (!ok)
            resetParametersFromTextEdit();

    }

    private void minusCounter() {
        m_textViewError.setText("");
        counter -= 0.25;
        if (counter <= 0)
            counter = 0;
        m_textViewQuantity.setText(counter + "");
        if (!ok)
            resetParametersFromTextEdit();

    }

    @Override
    public void onSelectFoodDone(String result) throws UnsupportedEncodingException {
        if (!result.isEmpty()) {
            foods = DataManager.getInstance().parseFoods(result);
            DataManager.getInstance().setFoodsList(foods);
            putNameFoodInVector();
           // Toast.makeText(getApplicationContext(), "Get all foods from database", Toast.LENGTH_SHORT).show();
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
        SearchDayTask searchDayTask = new SearchDayTask(calendarString);
        searchDayTask.setSearchDayDelegate(addFoodActivity);

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

    @Override
    public void onAddUserDiaryDone(String result) {

    }

    @Override
    public void onAddUserDiaryError(String response) {

    }

    @Override
    public void onGetUserDiaryDone(String result) throws UnsupportedEncodingException {
        if (!result.isEmpty()) {
            userDiary = DataManager.getInstance().parseUserDiary(result);
            userDiary.setQuantity(userDiary.getQuantity() + counter);
            UpdateUserDiaryQuantityTask updateUserDiaryQuantityTask = new UpdateUserDiaryQuantityTask(userDiary.getQuantity(), userDiary.getId());
            updateUserDiaryQuantityTask.setUpdateUserDiaryQuantityDelegate(addFoodActivity);
            Toast.makeText(addFoodActivity, "Registered food today, just update the quantity", Toast.LENGTH_SHORT).show();
        } else {
            //Toast.makeText(addFoodActivity, "atentieee, nameFood introdus gresit", Toast.LENGTH_SHORT).show();
            SelectFoodDayTask selectFoodDayTask = new SelectFoodDayTask(calendarString, receivedFood.getFoodname());
            selectFoodDayTask.setSelectFoodDayDelegate(addFoodActivity);
        }
    }

    @Override
    public void onUpdateDone(String result) {

    }

    @Override
    public void onUpdateError(String response) {
        Toast.makeText(this, response, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onSelectFoodDayDone(String result) throws UnsupportedEncodingException {
        if (!result.isEmpty()) {
            //dayFoodSearch=DataManager.getInstance().parseDayFood(result);
            AddUserDiaryTask addUserDiaryTask = new AddUserDiaryTask(calendarString, receivedFood.getFoodname(), userAfterLogin.getUsername(), counter);
            addUserDiaryTask.setAddUserDiaryDelegate(addFoodActivity);
        } else {
            RegisterDayFoodTask registerDayFoodTask = new RegisterDayFoodTask(dateToday, receivedFood.getFoodname());
            registerDayFoodTask.setRegisterDayFoodDelegate(addFoodActivity);

            AddUserDiaryTask addUserDiaryTask = new AddUserDiaryTask(calendarString, receivedFood.getFoodname(), userAfterLogin.getUsername(), counter);
            addUserDiaryTask.setAddUserDiaryDelegate(addFoodActivity);
        }
    }

    @Override
    public void onGetFoodByName2Done(String result) throws UnsupportedEncodingException {
        if (!result.isEmpty()) {
            GetUserDiaryTask getUserDiaryTask = new GetUserDiaryTask(calendarString, receivedFood.getFoodname(), userAfterLogin.getUsername());
            getUserDiaryTask.setGetUserDiaryDelegate(addFoodActivity);

        } else {
            m_textViewError.setText("This food doesn't exist in dataBase!");

        }

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent = new Intent(this, FoodDiaryActivity.class);
            intent.putExtra("userAfterLogin", userAfterLogin);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    public void onRegisterFoodDone(String result) {
        GetFoodByNameTask getFoodByNameTask = new GetFoodByNameTask(m_autoCompleteTextView.getText().toString());
        getFoodByNameTask.setGetFoodByNameDelegate(addFoodActivity);

    }

    @Override
    public void onRegisterFoodError(String response) {
        Toast.makeText(AddFoodActivity.this, response, Toast.LENGTH_SHORT).show();
    }
}