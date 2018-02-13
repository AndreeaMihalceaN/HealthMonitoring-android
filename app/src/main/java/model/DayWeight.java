package model;

/**
 * Created by Andreea on 13.02.2018.
 */

public class DayWeight {
    private Day day;
    private double currentWeight;

    public DayWeight(Day day, double currentWeight) {
        this.day = day;
        this.currentWeight = currentWeight;
    }

    public DayWeight() {
    }

    public Day getDay() {
        return day;
    }

    public void setDay(Day day) {
        this.day = day;
    }

    public double getCurrentWeight() {
        return currentWeight;
    }

    public void setCurrentWeight(double currentWeight) {
        this.currentWeight = currentWeight;
    }
}
