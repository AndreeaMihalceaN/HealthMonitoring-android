package model;

/**
 * Created by Andreea on 12.02.2018.
 */

public class WeightStatistics {
    private Long id;
    private Long userId;
    private double currentWeight;

    public WeightStatistics(Long id, Long userId, double currentWeight) {
        this.id = id;
        this.userId = userId;
        this.currentWeight = currentWeight;
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

    public double getCurrentWeight() {
        return currentWeight;
    }

    public void setCurrentWeight(double currentWeight) {
        this.currentWeight = currentWeight;
    }
}
