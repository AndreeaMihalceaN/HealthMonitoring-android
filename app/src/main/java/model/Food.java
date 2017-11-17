package model;

/**
 * Created by Andreea on 31.10.2017.
 */

public class Food {
    private String food_name;
    private double carbohydrates;
    private double proteins;
    private double fats;
    private String category;

    public Food(String food_name, double carbohydrates, double proteins, double fats, String category) {
        this.food_name = food_name;
        this.carbohydrates = carbohydrates;
        this.proteins = proteins;
        this.fats = fats;
        this.category = category;
    }

    public Food()
    {

    }

    public String getName() {
        return food_name;
    }

    public void setName(String food_name) {
        this.food_name = food_name;
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
}
