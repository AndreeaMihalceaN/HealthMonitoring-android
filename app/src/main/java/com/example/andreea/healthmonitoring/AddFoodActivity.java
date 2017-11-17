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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import manager.DataManager;
import model.Food;
import model.User;
import webservice.LoginDelegate;
import webservice.LoginTask;
import webservice.SelectFoodDelegate;
import webservice.SelectFoodTask;

public class AddFoodActivity extends AppCompatActivity implements SelectFoodDelegate {

    private AutoCompleteTextView m_autoCompleteTextView;
    private TextView m_textViewCarbohydratesQuantity;
    private EditText m_editTextCarbohydratesQuantity;
    private TextView m_textViewProteinQuantity;
    private EditText m_editTextProteinQuantity;
    private TextView m_textViewFatsQuantity;
    private EditText m_editTextFatsQuantity;
    private TextView m_textViewCategory;
    private EditText m_editTextCategory;
    private CardView m_cardSubmit;
    private List<Food> foods;
    private User userAfterLogin;

    private AddFoodActivity addFoodActivity;
    //private static String[] Foods = new String[]{"Yoghurt", "Cheese", "Milk", "Spaghetti", "Steak", "Soup", "Asparagus", "Chicken", "Fish", "Baked fish with vegetables"};
    private static ArrayList<String> Foods;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_food);

        addFoodActivity=this;

        m_textViewCarbohydratesQuantity = (TextView) findViewById(R.id.textViewCarbohydratesQuantity);
        m_textViewCarbohydratesQuantity.setVisibility(View.INVISIBLE);
        m_editTextCarbohydratesQuantity = (EditText) findViewById(R.id.editTextCarbohydratesQuantity);
        m_editTextCarbohydratesQuantity.setVisibility(View.INVISIBLE);
        m_editTextCarbohydratesQuantity.setEnabled(false);

        m_textViewProteinQuantity = (TextView) findViewById(R.id.textViewProteinQuantity);
        m_textViewProteinQuantity.setVisibility(View.INVISIBLE);
        m_editTextProteinQuantity = (EditText) findViewById(R.id.editTextProteinQuantity);
        m_editTextProteinQuantity.setVisibility(View.INVISIBLE);
        m_editTextProteinQuantity.setEnabled(false);

        m_textViewFatsQuantity = (TextView) findViewById(R.id.textViewFatsQuantity);
        m_textViewFatsQuantity.setVisibility(View.INVISIBLE);
        m_editTextFatsQuantity = (EditText) findViewById(R.id.editTextFatsQuantity);
        m_editTextFatsQuantity.setVisibility(View.INVISIBLE);
        m_editTextFatsQuantity.setEnabled(false);

        m_textViewCategory = (TextView) findViewById(R.id.textViewCategory);
        m_textViewCategory.setVisibility(View.INVISIBLE);
        m_editTextCategory = (EditText) findViewById(R.id.editTextCategory);
        m_editTextCategory.setVisibility(View.INVISIBLE);
        m_editTextCategory.setEnabled(false);

        m_cardSubmit = (CardView) findViewById(R.id.cardSubmit);

        m_autoCompleteTextView = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView);

        Intent intent = getIntent();
        userAfterLogin = (User) intent.getSerializableExtra("userAfterLogin");

        SelectFoodTask selectFoodTask = new SelectFoodTask(userAfterLogin.getUsername(), userAfterLogin.getPassword());
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
                m_textViewCarbohydratesQuantity.setVisibility(View.VISIBLE);
                m_editTextCarbohydratesQuantity.setVisibility(View.VISIBLE);

                m_textViewProteinQuantity.setVisibility(View.VISIBLE);
                m_editTextProteinQuantity.setVisibility(View.VISIBLE);

                m_textViewFatsQuantity.setVisibility(View.VISIBLE);
                m_editTextFatsQuantity.setVisibility(View.VISIBLE);

                m_textViewCategory.setVisibility(View.VISIBLE);
                m_editTextCategory.setVisibility(View.VISIBLE);
            }
        });

        m_cardSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(AddFoodActivity.this, SignUpActivity.class);
//                startActivity(intent);

                Toast.makeText(getApplicationContext(), "Am intrat in actiune add food", Toast.LENGTH_SHORT).show();
            }
        });

    }

    void putNameFoodInVector() {
        Foods = new ArrayList<>();
        for (Food food : foods) {
            Foods.add(food.getName());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, Foods);
        m_autoCompleteTextView.setAdapter(adapter);
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
}
