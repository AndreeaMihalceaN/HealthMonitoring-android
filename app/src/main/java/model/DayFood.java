package model;

/**
 * Created by Andreea on 27.11.2017.
 */

public class DayFood {
    private Long id;

    private Long dayId;

    private Long foodId;


    public DayFood(Long id, Long dayId, Long foodId) {
        this.id = id;
        this.dayId = dayId;
        this.foodId = foodId;
    }

    public DayFood(Long dayId, Long foodId) {
        this.dayId = dayId;
        this.foodId = foodId;
    }

    public DayFood() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getDayId() {
        return dayId;
    }

    public void setDayId(Long dayId) {
        this.dayId = dayId;
    }

    public Long getFoodId() {
        return foodId;
    }

    public void setFoodId(Long foodId) {
        this.foodId = foodId;
    }
}
