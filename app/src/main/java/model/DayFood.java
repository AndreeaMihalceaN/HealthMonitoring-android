package model;

/**
 * Created by Andreea on 27.11.2017.
 */

public class DayFood {
    private Long id;

    private Day day;

    private Food food;

    public Day getDay() {
        return day;
    }

    public void setDay(Day day) {
        this.day = day;
    }

    public Food getFood() {
        return food;
    }

    public void setFood(Food food) {
        this.food = food;
    }

    public DayFood(Day day, Food food) {
        this.day = day;
        this.food = food;
    }

    public DayFood() {

    }
}
