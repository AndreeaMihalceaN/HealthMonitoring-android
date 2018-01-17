package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Andreea on 31.10.2017.
 */

public class Food implements Serializable {
    private Long id;
    private String foodname;
    private double carbohydrates;
    private double proteins;
    private double fats;
    private String category;
    private String pictureString;
    private int stars;
    private String url;


    public Food(Long id, String foodname, double carbohydrates, double proteins, double fats, String category, String pictureString, int stars, String url) {
        this.id = id;
        this.foodname = foodname;
        this.carbohydrates = carbohydrates;
        this.proteins = proteins;
        this.fats = fats;
        this.category = category;
        this.pictureString = pictureString;
        this.stars = stars;
        this.url = url;
    }

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

    public Food() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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


}
