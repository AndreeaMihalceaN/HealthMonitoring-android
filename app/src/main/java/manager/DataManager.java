package manager;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import model.Food;
import model.User;

/**
 * Created by Andreea on 30.10.2017.
 */

public class DataManager {

    private static DataManager instance = new DataManager();

    public static DataManager getInstance() {
        return instance;
    }

    private String baseAuthStr;

    public List<Food>foodsList;

    private DataManager() {
        Log.d("TAG", "DataManager()");
    }

    public DataManager(String baseAuthStr) {
        this.baseAuthStr = baseAuthStr;
    }

    public static void setInstance(DataManager instance) {
        DataManager.instance = instance;
    }

    public String getBaseAuthStr() {
        return baseAuthStr;
    }

    public void setBaseAuthStr(String baseAuthStr) {
        this.baseAuthStr = baseAuthStr;
    }

    public List<Food> getFoodsList() {
        return foodsList;
    }

    public void setFoodsList(List<Food> foodsList) {
        this.foodsList = foodsList;
    }

    public User parseUser(String inputJSON) {

        User user = new User();

        try {
            JSONObject jsonObject = new JSONObject(inputJSON);
            Log.d("TAG", "jsonObject - " + String.valueOf(jsonObject));

            user.setUsername(jsonObject.getString("username"));

            user.setFirstName(jsonObject.getString("firstName"));
            user.setLastName(jsonObject.getString("lastName"));
            user.setPassword(jsonObject.getString("password"));
            user.setGender(jsonObject.getString("gender"));
            user.setHeight(jsonObject.getInt("height"));
            user.setWeight(jsonObject.getInt("weight"));

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return user;
    }

    public List<Food> parseFoods(String inputJSON) {

        foodsList = new ArrayList<Food>();

        try {
            JSONArray jsonArray = new JSONArray(inputJSON);
            Log.d("TAG", "JSONArray - " + String.valueOf(jsonArray));

            for (int i = 0; i < jsonArray.length(); i++) {
                //Food food = new Food();

                JSONObject jsonObject = jsonArray.getJSONObject(i);
                ///JSONObject foodObject = jsonObject.getJSONObject("foodname");
                //(String food_name, double carbohydrates, double proteins, double fats, String category)
                Food food = new Food(jsonObject.getString("foodname"), jsonObject.getDouble("carbohydrates"), jsonObject.getDouble("proteins"), jsonObject.getDouble("fats"), jsonObject.getString("category"));

                foodsList.add(food);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return foodsList;
    }
}