package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Andreea on 31.10.2017.
 */

public class Food implements Serializable {
    private String foodname;
    private double carbohydrates;
    private double proteins;
    private double fats;
    private String category;
    private String pictureString;
    private int stars;
    private String url;
    //private List<Day> days;


    public Food(String foodname, double carbohydrates, double proteins, double fats, String category, String pictureString, int stars, String url) {
        this.foodname = foodname;
        this.carbohydrates = carbohydrates;
        this.proteins = proteins;
        this.fats = fats;
        this.category = category;
        this.pictureString = pictureString;
        this.stars = stars;
        this.url = url;
    }

    public Food(String foodname, double carbohydrates, double proteins, double fats, String category) {
        this.foodname = foodname;
        this.carbohydrates = carbohydrates;
        this.proteins = proteins;
        this.fats = fats;
        this.category = category;
        //this.days= new ArrayList<>();
    }

    public Food(String foodname, double carbohydrates, double proteins, double fats, String category, String pictureString) {
        this.foodname = foodname;
        this.carbohydrates = carbohydrates;
        this.proteins = proteins;
        this.fats = fats;
        this.category = category;
        this.pictureString = pictureString;
    }

    public Food(String foodname, double carbohydrates, double proteins, double fats, String category, String pictureString, int stars) {
        this.foodname = foodname;
        this.carbohydrates = carbohydrates;
        this.proteins = proteins;
        this.fats = fats;
        this.category = category;
        this.pictureString = pictureString;
        this.stars = stars;
    }

    public Food()
    {
        //this.days= new ArrayList<>();

    }

//    public Food(String food_name, double carbohydrates, double proteins, double fats, String category, List<Day> days) {
//        this.food_name = food_name;
//        this.carbohydrates = carbohydrates;
//        this.proteins = proteins;
//        this.fats = fats;
//        this.category = category;
//        this.days = days;
//    }


    public String getFoodname() {
        return foodname;
    }

    public void setFoodname(String foodname) {
        this.foodname = foodname;
    }

    public double getCarbohydrates() {
        return carbohydrates;
    }

    public void setCarbohydrates(double carbohydrates) {
        this.carbohydrates = carbohydrates;
    }

    public double getProteins() {
        return proteins;
    }

    public void setProteins(double proteins) {
        this.proteins = proteins;
    }

    public double getFats() {
        return fats;
    }

    public void setFats(double fats) {
        this.fats = fats;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }


    public String getPictureString() {
        return pictureString;
    }

    public void setPictureString(String pictureString) {
        this.pictureString = pictureString;
    }

    public int getStars() {
        return stars;
    }

    public void setStars(int stars) {
        this.stars = stars;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    //    public List<Day> getDays() {
//        return days;
//    }

//    public void setDays(List<Day> days) {
//        this.days = days;
//    }
}
