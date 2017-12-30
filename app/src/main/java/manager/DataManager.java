package manager;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import model.Day;
import model.DayFood;
import model.Food;
import model.User;
import model.UserDiary;

/**
 * Created by Andreea on 30.10.2017.
 */

public class DataManager {

    private static DataManager instance = new DataManager();

    public static DataManager getInstance() {
        return instance;
    }

    private String baseAuthStr;

    public List<Day> daysList;
    public List<Food> foodsList;
    public List<UserDiary> userDiaryList;
    public List<Food> foodsListFromDay;

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

    public List<Food> getFoodsListFromDay() {
        return foodsListFromDay;
    }

    public void setFoodsListFromDay(List<Food> foodsListFromDay) {
        this.foodsListFromDay = foodsListFromDay;
    }

    public List<UserDiary> getUserDiaryList() {
        return userDiaryList;
    }

    public void setUserDiaryList(List<UserDiary> userDiaryList) {
        this.userDiaryList = userDiaryList;
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
            user.setEmail(jsonObject.getString("email"));
            user.setContactNo(jsonObject.getString("contactNo"));
            user.setAge(jsonObject.getInt("age"));

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return user;
    }

    public List<Food> parseFoods(String inputJSON) {

        foodsList = new ArrayList<Food>();
        Day day;

        try {
            JSONArray jsonArray = new JSONArray(inputJSON);
            Log.d("TAG", "JSONArray - " + String.valueOf(jsonArray));

            for (int i = 0; i < jsonArray.length(); i++) {
                //Food food = new Food();

                JSONObject jsonObject = jsonArray.getJSONObject(i);
                ///JSONObject foodObject = jsonObject.getJSONObject("foodname");
                //(String food_name, double carbohydrates, double proteins, double fats, String category)
//                JSONArray jsonArrayDays = jsonObject.getJSONArray("days");
//                for (int j = 0; j < jsonArrayDays.length(); j++) {
//                    JSONObject jsonObject2 = jsonArray.getJSONObject(j);
//                    ///JSONObject foodObject = jsonObject.getJSONObject("foodname");
//                    //(String food_name, double carbohydrates, double proteins, double fats, String category)
//                    day = new Day(jsonObject2.getString("date"));
//
//                    daysList.add(day);
//                }
                Food food = new Food(jsonObject.getString("foodname"), jsonObject.getDouble("carbohydrates"), jsonObject.getDouble("proteins"), jsonObject.getDouble("fats"), jsonObject.getString("category"), jsonObject.getString("pictureString"), jsonObject.getInt("stars"), jsonObject.getString("url"));

                foodsList.add(food);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return foodsList;
    }

    public List<UserDiary> parseUserDiaryList(String inputJSON) {

        userDiaryList = new ArrayList<UserDiary>();
        Day day;

        try {
            JSONArray jsonArray = new JSONArray(inputJSON);
            Log.d("TAG", "JSONArray - " + String.valueOf(jsonArray));

            for (int i = 0; i < jsonArray.length(); i++) {
                //Food food = new Food();

                JSONObject jsonObject = jsonArray.getJSONObject(i);
                ///JSONObject foodObject = jsonObject.getJSONObject("foodname");
                //(String food_name, double carbohydrates, double proteins, double fats, String category)
//                JSONArray jsonArrayDays = jsonObject.getJSONArray("days");
//                for (int j = 0; j < jsonArrayDays.length(); j++) {
//                    JSONObject jsonObject2 = jsonArray.getJSONObject(j);
//                    ///JSONObject foodObject = jsonObject.getJSONObject("foodname");
//                    //(String food_name, double carbohydrates, double proteins, double fats, String category)
//                    day = new Day(jsonObject2.getString("date"));
//
//                    daysList.add(day);
//                }
                JSONObject foodDayJSON = jsonObject.getJSONObject("dayFood");
                JSONObject userJSON = jsonObject.getJSONObject("user");
                JSONObject dayJSON = foodDayJSON.getJSONObject("day");
                JSONObject foodJSON = foodDayJSON.getJSONObject("food");
                String dateString = dayJSON.getString("date");
                Day dayAfterJson = new Day(dateString);
                Food foodAfterJson = new Food(foodJSON.getString("foodname"), foodJSON.getDouble("carbohydrates"), foodJSON.getDouble("proteins"), foodJSON.getDouble("fats"), foodJSON.getString("category"), foodJSON.getString("pictureString"), foodJSON.getInt("stars"), foodJSON.getString("url"));
                DayFood dayFood = new DayFood(dayAfterJson, foodAfterJson);

                User user = new User();
                user.setUsername(userJSON.getString("username"));

                user.setFirstName(userJSON.getString("firstName"));
                user.setLastName(userJSON.getString("lastName"));
                user.setPassword(userJSON.getString("password"));
                user.setGender(userJSON.getString("gender"));
                user.setHeight(userJSON.getInt("height"));
                user.setWeight(userJSON.getInt("weight"));
                user.setEmail(userJSON.getString("email"));

                UserDiary userDiary = new UserDiary(dayFood, user, jsonObject.getDouble("quantity"));

                userDiaryList.add(userDiary);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return userDiaryList;
    }

    public Food parseFood(String inputJSON) {


        Food food = new Food();
        Day day;
        try {
            JSONObject jsonObject = new JSONObject(inputJSON);
            Log.d("TAG", "jsonObject - " + String.valueOf(jsonObject));


//            JSONArray jsonArray = jsonObject.getJSONArray("days");
//            for (int i = 0; i < jsonArray.length(); i++) {
//                JSONObject jsonObject2 = jsonArray.getJSONObject(i);
//                ///JSONObject foodObject = jsonObject.getJSONObject("foodname");
//                //(String food_name, double carbohydrates, double proteins, double fats, String category)
//                day = new Day(jsonObject2.getString("date"));
//
//                daysList.add(day);
//            }

            food = new Food(jsonObject.getString("foodname"), jsonObject.getDouble("carbohydrates"), jsonObject.getDouble("proteins"), jsonObject.getDouble("fats"), jsonObject.getString("category"), jsonObject.getString("pictureString"), jsonObject.getInt("stars"), jsonObject.getString("url"));

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return food;
    }

    public Day parseDay(String inputJSON) {


        foodsListFromDay = new ArrayList<Food>();
        Day day = new Day();
        try {
            JSONObject jsonObject = new JSONObject(inputJSON);
            Log.d("TAG", "jsonObject - " + String.valueOf(jsonObject));


//            JSONArray jsonArray = jsonObject.getJSONArray("foods");
//            for (int i = 0; i < jsonArray.length(); i++) {
//                jsonObject = jsonArray.getJSONObject(i);
//                ///JSONObject foodObject = jsonObject.getJSONObject("foodname");
//                //(String food_name, double carbohydrates, double proteins, double fats, String category)
//                Food food = new Food(jsonObject.getString("foodname"), jsonObject.getDouble("carbohydrates"), jsonObject.getDouble("proteins"), jsonObject.getDouble("fats"), jsonObject.getString("category"));
//
//                foodsListFromDay.add(food);
//            }
            day = new Day(jsonObject.getString("date"));
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return day;
    }

    public UserDiary parseUserDiary(String inputJSON) {

        UserDiary userDiary = new UserDiary();
        Day day;

        try {
            JSONObject jsonObject = new JSONObject(inputJSON);

            Long idfoodDayJSON = jsonObject.getLong("id");
            JSONObject foodDayJSON = jsonObject.getJSONObject("dayFood");
            JSONObject userJSON = jsonObject.getJSONObject("user");
            JSONObject dayJSON = foodDayJSON.getJSONObject("day");
            JSONObject foodJSON = foodDayJSON.getJSONObject("food");
            String dateString = dayJSON.getString("date");
            Day dayAfterJson = new Day(dateString);
            Food foodAfterJson = new Food(foodJSON.getString("foodname"), foodJSON.getDouble("carbohydrates"), foodJSON.getDouble("proteins"), foodJSON.getDouble("fats"), foodJSON.getString("category"), foodJSON.getString("pictureString"), foodJSON.getInt("stars"), foodJSON.getString("url"));
            DayFood dayFood = new DayFood(dayAfterJson, foodAfterJson);

            User user = new User();
            user.setUsername(userJSON.getString("username"));

            user.setFirstName(userJSON.getString("firstName"));
            user.setLastName(userJSON.getString("lastName"));
            user.setPassword(userJSON.getString("password"));
            user.setGender(userJSON.getString("gender"));
            user.setHeight(userJSON.getInt("height"));
            user.setWeight(userJSON.getInt("weight"));
            user.setEmail(userJSON.getString("email"));

            userDiary = new UserDiary(idfoodDayJSON, dayFood, user, jsonObject.getDouble("quantity"));

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return userDiary;
    }

    public DayFood parseDayFood(String inputJSON) {

        DayFood dayFood = new DayFood();

        try {
            JSONObject jsonObject = new JSONObject(inputJSON);

            Long idDayFoodJSON = jsonObject.getLong("id");
            JSONObject dayJSON = jsonObject.getJSONObject("day");
            JSONObject foodJSON = jsonObject.getJSONObject("food");
            Day day = new Day(dayJSON.getString("date"));
            Food food = new Food(foodJSON.getString("foodname"), foodJSON.getDouble("carbohydrates"), foodJSON.getDouble("proteins"), foodJSON.getDouble("fats"), foodJSON.getString("category"), foodJSON.getString("pictureString"), foodJSON.getInt("stars"), foodJSON.getString("url"));

            dayFood = new DayFood(idDayFoodJSON, day, food);

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dayFood;
    }

}



