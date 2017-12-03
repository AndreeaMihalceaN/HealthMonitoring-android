package model;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Set;

/**
 * Created by Andreea on 21.11.2017.
 */

public class Day {

    private Calendar date;
    private Set<DayFood> listDayFoods;

    public Set<DayFood> getListDayFoods() {
        return listDayFoods;
    }

    //public List<Food> foods;

    public Day() {
        //this.foods= new ArrayList<>();
    }

//    public Day(Calendar date, List<Food> foods) {
//        this.date = date;
//        this.foods = foods;
//    }

    public Day(String date) throws ParseException {
        DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        Calendar cal = Calendar.getInstance();
        cal.setTime(df.parse(date));
        this.date = cal;
        //this.foods= new ArrayList<>();
    }

//    public Day(String date, List<Food> foods) throws ParseException {
//        DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
//        Calendar cal  = Calendar.getInstance();
//        cal.setTime(df.parse(date));
//        this.date=cal;
//        //this.foods = foods;
//    }

//    public void addFoodInFoodList(Food newFood)
//    {
//        foods.add(newFood);
//    }

    public Calendar getDate() {
        return date;
    }

    public void setDate(Calendar date) {
        this.date = date;
    }

//    public List<Food> getFoods() {
//        return foods;
//    }
//
//    public void setFoods(List<Food> foods) {
//        this.foods = foods;
//    }
}
