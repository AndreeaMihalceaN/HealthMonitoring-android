package model;

import java.io.Serializable;

/**
 * Created by Andreea on 13.02.2018.
 */

public class MonthWeight implements Serializable {
    private int month;
    private double weight;

    public MonthWeight(int month, double weight) {
        this.month = month;
        this.weight = weight;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }
}
