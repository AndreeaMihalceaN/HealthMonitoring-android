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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import model.DailyStatistics;
import model.Day;
import model.DayFood;
import model.DayWeight;
import model.Food;
import model.QuantityFood;
import model.User;
import model.UserDiary;
import model.WeightStatistics;

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

            user.setId(jsonObject.getLong("id"));
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

                JSONObject jsonObject = jsonArray.getJSONObject(i);
                Food food = new Food(jsonObject.getLong("id"), jsonObject.getString("foodname"), jsonObject.getDouble("carbohydrates"), jsonObject.getDouble("proteins"), jsonObject.getDouble("fats"), jsonObject.getString("category"), jsonObject.getString("pictureString"), jsonObject.getInt("stars"), jsonObject.getString("url"));

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

                JSONObject jsonObject = jsonArray.getJSONObject(i);

                JSONObject foodDayJSON = jsonObject.getJSONObject("dayFood");
                JSONObject userJSON = jsonObject.getJSONObject("user");
                long dayJSON = foodDayJSON.getLong("dayId");
                long foodJSON = foodDayJSON.getLong("foodId");
                double quantity = jsonObject.getDouble("quantity");
//                String dateString = dayJSON.getString("date");
//                Day dayAfterJson = new Day(dateString);
                //Food foodAfterJson = new Food(foodJSON.getString("foodname"), foodJSON.getDouble("carbohydrates"), foodJSON.getDouble("proteins"), foodJSON.getDouble("fats"), foodJSON.getString("category"), foodJSON.getString("pictureString"), foodJSON.getInt("stars"), foodJSON.getString("url"));
                DayFood dayFood = new DayFood(dayJSON, foodJSON);

                User user = new User();
                user.setUsername(userJSON.getString("username"));

                user.setFirstName(userJSON.getString("firstName"));
                user.setLastName(userJSON.getString("lastName"));
                user.setPassword(userJSON.getString("password"));
                user.setGender(userJSON.getString("gender"));
                user.setHeight(userJSON.getInt("height"));
                user.setWeight(userJSON.getInt("weight"));
                user.setEmail(userJSON.getString("email"));

                UserDiary userDiary = new UserDiary(dayFood, user, quantity);

                userDiaryList.add(userDiary);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return userDiaryList;
    }

    public Food parseFood(String inputJSON) {


        Food food = new Food();
        try {
            JSONObject jsonObject = new JSONObject(inputJSON);
            Log.d("TAG", "jsonObject - " + String.valueOf(jsonObject));

            food = new Food(jsonObject.getString("foodname"), jsonObject.getDouble("carbohydrates"), jsonObject.getDouble("proteins"), jsonObject.getDouble("fats"), jsonObject.getString("category"), jsonObject.getString("pictureString"), jsonObject.getInt("stars"), jsonObject.getString("url"));

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return food;
    }

    public Day parseDay(String inputJSON) {

        Day day = new Day();
        try {
            JSONObject jsonObject = new JSONObject(inputJSON);
            Log.d("TAG", "jsonObject - " + String.valueOf(jsonObject));

            String date = jsonObject.getString("date");
            DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
            Calendar cal = Calendar.getInstance();
            cal.setTime(df.parse(date));
            day = new Day(jsonObject.getLong("id"), cal);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return day;
    }

    public UserDiary parseUserDiary(String inputJSON) {

        UserDiary userDiary = new UserDiary();
        try {
            JSONObject jsonObject = new JSONObject(inputJSON);

            Long idfoodDayJSON = jsonObject.getLong("id");
            JSONObject foodDayJSON = jsonObject.getJSONObject("dayFood");
            JSONObject userJSON = jsonObject.getJSONObject("user");
            Long dayJSON = foodDayJSON.getLong("dayId");
            Long foodJSON = foodDayJSON.getLong("foodId");
            //String dateString = dayJSON.getString("date");
            //Day dayAfterJson = new Day(dateString);
            //Food foodAfterJson = new Food(foodJSON.getString("foodname"), foodJSON.getDouble("carbohydrates"), foodJSON.getDouble("proteins"), foodJSON.getDouble("fats"), foodJSON.getString("category"), foodJSON.getString("pictureString"), foodJSON.getInt("stars"), foodJSON.getString("url"));
            DayFood dayFood = new DayFood(dayJSON, foodJSON);

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
        }
        return userDiary;
    }

    public DayFood parseDayFood(String inputJSON) {

        DayFood dayFood = new DayFood();

        try {
            JSONObject jsonObject = new JSONObject(inputJSON);

            Long idDayFoodJSON = jsonObject.getLong("id");
            Long dayJSON = jsonObject.getLong("dayId");
            Long foodJSON = jsonObject.getLong("foodId");
            //Day day = new Day(dayJSON.getString("date"));
            //Food food = new Food(foodJSON.getString("foodname"), foodJSON.getDouble("carbohydrates"), foodJSON.getDouble("proteins"), foodJSON.getDouble("fats"), foodJSON.getString("category"), foodJSON.getString("pictureString"), foodJSON.getInt("stars"), foodJSON.getString("url"));

            dayFood = new DayFood(idDayFoodJSON, dayJSON, foodJSON);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return dayFood;
    }

    public List<QuantityFood> parseQuantityFoodList(String inputJSON) {

        List<QuantityFood> quantityFoodList = new ArrayList<QuantityFood>();

        try {
            JSONArray jsonArray = new JSONArray(inputJSON);
            Log.d("TAG", "JSONArray - " + String.valueOf(jsonArray));

            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject jsonObject = jsonArray.getJSONObject(i);
                JSONObject foodJSON = jsonObject.getJSONObject("food");
                double quantity = jsonObject.getDouble("quantity");
                Food food = new Food(foodJSON.getLong("id"), foodJSON.getString("foodname"), foodJSON.getDouble("carbohydrates"), foodJSON.getDouble("proteins"), foodJSON.getDouble("fats"), foodJSON.getString("category"), foodJSON.getString("pictureString"), foodJSON.getInt("stars"), foodJSON.getString("url"));

                quantityFoodList.add(new QuantityFood(food, quantity));

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return quantityFoodList;
    }

    public DailyStatistics parseDailyStatistics(String inputJSON) {

        DailyStatistics dailyStatistics = new DailyStatistics();

        try {
            JSONObject jsonObject = new JSONObject(inputJSON);

            Long idJSON = jsonObject.getLong("id");
            Long dayIdJSON = jsonObject.getLong("dayId");
            double totalCalories = jsonObject.getDouble("totalCalories");
            Long userIdJSON = jsonObject.getLong("userId");
            //Day day = new Day(dayJSON.getString("date"));
            //Food food = new Food(foodJSON.getString("foodname"), foodJSON.getDouble("carbohydrates"), foodJSON.getDouble("proteins"), foodJSON.getDouble("fats"), foodJSON.getString("category"), foodJSON.getString("pictureString"), foodJSON.getInt("stars"), foodJSON.getString("url"));

            dailyStatistics = new DailyStatistics(idJSON, userIdJSON, dayIdJSON, totalCalories);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return dailyStatistics;
    }

    public WeightStatistics parseWeightStatistics(String inputJSON) {

        WeightStatistics weightStatistics = new WeightStatistics();

        try {
            JSONObject jsonObject = new JSONObject(inputJSON);

            Long idJSON = jsonObject.getLong("id");
            Long userIdJSON = jsonObject.getLong("userId");
            Long dayIdJSON = jsonObject.getLong("dayId");
            double currentWeight = jsonObject.getDouble("currentWeight");

            weightStatistics = new WeightStatistics(idJSON, userIdJSON, dayIdJSON, currentWeight);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return weightStatistics;
    }

    public List<WeightStatistics> parseWeightStatisticsList(String inputJSON) {

        List<WeightStatistics> weightStatisticsList = new ArrayList<WeightStatistics>();


        try {
            JSONArray jsonArray = new JSONArray(inputJSON);
            Log.d("TAG", "JSONArray - " + String.valueOf(jsonArray));

            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject jsonObject = jsonArray.getJSONObject(i);

                Long id = jsonObject.getLong("id");
                double currentWeight = jsonObject.getDouble("currentWeight");
                Long userId = jsonObject.getLong("userId");
                Long dayId = jsonObject.getLong("dayId");
                WeightStatistics weightStatistics = new WeightStatistics(id, userId, dayId, currentWeight);


                weightStatisticsList.add(weightStatistics);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return weightStatisticsList;
    }

    public Set<DayWeight> parseDayWeightList(String inputJSON) {

        Set<DayWeight> dayWeightList = new HashSet<>();


        try {
            JSONArray jsonArray = new JSONArray(inputJSON);
            Log.d("TAG", "JSONArray - " + String.valueOf(jsonArray));

            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject jsonObject = jsonArray.getJSONObject(i);

                JSONObject dayJson = jsonObject.getJSONObject("day");
                double currentWeight = jsonObject.getDouble("currentWeight");
                Long id = dayJson.getLong("id");
                String date = dayJson.getString("date");
                DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
                Calendar cal = Calendar.getInstance();
                cal.setTime(df.parse(date));
                Day myDay = new Day(id, cal);

                DayWeight dayWeight = new DayWeight(myDay, currentWeight);

                dayWeightList.add(dayWeight);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dayWeightList;
    }


}



