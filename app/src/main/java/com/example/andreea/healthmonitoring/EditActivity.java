package com.example.andreea.healthmonitoring;

import android.content.Intent;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.UnsupportedEncodingException;

import manager.DataManager;
import model.Food;
import model.User;
import webservice.LoginDelegate;
import webservice.LoginTask;
import webservice.SearchDayTask;
import webservice.UpdateAutentificationTask;
import webservice.UpdateUserDiaryEditDelegate;
import webservice.UpdateUserDiaryEditTask;

public class EditActivity extends AppCompatActivity implements UpdateUserDiaryEditDelegate, LoginDelegate {

    private User userAfterLogin;
    private Food foodReceived;
    private ImageView m_imageView;
    private TextView m_textViewNameFood;
    private TextView m_textViewCarbohydratesResult;
    private TextView m_textViewFatsResult;
    private TextView m_textViewProteinsResult;
    private TextView m_textViewCategoryResult;
    private TextView m_textViewQuatityResult;
    private TextView m_textViewError;
    private EditActivity editActivity;
    private double quantity;
    private Resources resources;
    private CardView m_buttonEditQuantity;
    private String stringDate;
    double currentQuantity = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        editActivity = this;
        resources = this.getResources();

        Intent intent = getIntent();
        userAfterLogin = (User) intent.getSerializableExtra("userAfterLogin");
        foodReceived = (Food) intent.getSerializableExtra("foodToSend");
        quantity = intent.getDoubleExtra("quantityFromHolderSelected", quantity);

        Bundle bundle = getIntent().getExtras();
        stringDate = bundle.getString("calendarString");

        m_textViewNameFood = (TextView) findViewById(R.id.textViewNameFood);
        m_imageView = (ImageView) findViewById(R.id.imageView);
        m_textViewCarbohydratesResult = (TextView) findViewById(R.id.textViewCarbohydratesResult);
        m_textViewFatsResult = (TextView) findViewById(R.id.textViewFatsResult);
        m_textViewProteinsResult = (TextView) findViewById(R.id.textViewProteinsResult);
        m_textViewCategoryResult = (TextView) findViewById(R.id.textViewCategoryResult);
        m_textViewQuatityResult = (TextView) findViewById(R.id.textViewQuatityResult);
        m_textViewError = (TextView) findViewById(R.id.textViewError);
        m_textViewError.setText("");
        m_buttonEditQuantity = (CardView) findViewById(R.id.buttonEditQuantity);

        m_textViewNameFood.setText(foodReceived.getFoodname());
        m_textViewCarbohydratesResult.setText(foodReceived.getCarbohydrates() + "");
        m_textViewFatsResult.setText(foodReceived.getFats() + "");
        m_textViewProteinsResult.setText(foodReceived.getProteins() + "");
        m_textViewCategoryResult.setText(foodReceived.getCategory() + "");
        m_textViewQuatityResult.setText(quantity + "");
        Integer idPicture = resources.getIdentifier(foodReceived.getPictureString(), "drawable", this.getPackageName());
        m_imageView.setImageResource(idPicture);

        m_textViewQuatityResult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                m_textViewError.setText("");
                m_textViewQuatityResult.setCursorVisible(true);
                m_textViewQuatityResult.setFocusableInTouchMode(true);
                m_textViewQuatityResult.setInputType(InputType.TYPE_CLASS_TEXT);
                m_textViewQuatityResult.requestFocus();

            }
        });

        m_buttonEditQuantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (validateTextFromQuantityTextView()) {
                    m_textViewError.setText("");
                    currentQuantity = Double.parseDouble(m_textViewQuatityResult.getText().toString());
                    UpdateUserDiaryEditTask updateUserDiaryEditTask = new UpdateUserDiaryEditTask(currentQuantity, stringDate, foodReceived.getFoodname(), userAfterLogin.getUsername());
                    updateUserDiaryEditTask.setUpdateUserDiaryEditDelegate(editActivity);

                    Toast.makeText(editActivity, "Quantity was updated with succes!", Toast.LENGTH_SHORT).show();
                } else
                    m_textViewError.setText("Quantity value contains letters! Try again with a numeric value");

            }
        });


    }

    private Boolean validateTextFromQuantityTextView() {
        String textFromQuantity = m_textViewQuatityResult.getText().toString();
        if (textFromQuantity.matches("[0-9]{1,13}(\\.[0-9]*)?")) {
            return true;
        }
        return false;
    }

    @Override
    public void onUpdateUserDiaryEditDone(String result) {
        LoginTask loginTask = new LoginTask(userAfterLogin.getUsername(), userAfterLogin.getPassword());
        loginTask.setLoginDelegate(editActivity);
    }

    @Override
    public void onUpdateUserDiaryEditError(String response) {
        Toast.makeText(EditActivity.this, response, Toast.LENGTH_SHORT).show();

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent = new Intent(this, FoodDiaryActivity.class);
            intent.putExtra("userAfterLogin", userAfterLogin);
//            intent.putExtra("foodToSend", foodReceived);
//            intent.putExtra("quantityFromHolderSelected", currentQuantity);
//            intent.putExtra("calendarString", stringDate);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, FoodDiaryActivity.class);
        intent.putExtra("userAfterLogin", userAfterLogin);
//        intent.putExtra("foodToSend", foodReceived);
//        intent.putExtra("quantityFromHolderSelected", currentQuantity);
//        intent.putExtra("calendarString", stringDate);
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
