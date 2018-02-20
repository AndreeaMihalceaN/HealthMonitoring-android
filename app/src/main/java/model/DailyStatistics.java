package model;

/**
 * Created by Andreea on 25.01.2018.
 */

public class DailyStatistics {
    private Long id;
    private Long userId;
    private Long dayId;
    private double totalCalories;
    private double steps;

    public DailyStatistics(Long id, Long userId, Long dayId, double totalCalories) {
        this.id = id;
        this.userId = userId;
        this.dayId = dayId;
        this.totalCalories = totalCalories;
    }

    public DailyStatistics(Long id, Long userId, Long dayId, double totalCalories, double steps) {
        this.id = id;
        this.userId = userId;
        this.dayId = dayId;
        this.totalCalories = totalCalories;
        this.steps = steps;
    }

    public DailyStatistics() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getDayId() {
        return dayId;
    }

    public void setDayId(Long dayId) {
        this.dayId = dayId;
    }

    public double getTotalCalories() {
        return totalCalories;
    }

    public void setTotalCalories(double totalCalories) {
        this.totalCalories = totalCalories;
    }

    public double getSteps() {
        return steps;
    }

    public void setSteps(double steps) {
        this.steps = steps;
    }
}
