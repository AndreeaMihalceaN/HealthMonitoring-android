package model;

/**
 * Created by Andreea on 04.12.2017.
 */

public class UserDiary {
    private Long id;

    private DayFood dayFood;

    private User user;

    private double quantity;

    public UserDiary(){

    }

    public UserDiary(Long id, DayFood dayFood, User user, double quantity) {
        this.id = id;
        this.dayFood = dayFood;
        this.user = user;
        this.quantity = quantity;
    }

    public UserDiary(DayFood dayFood, User user, double quantity) {
        this.dayFood = dayFood;
        this.user = user;
        this.quantity = quantity;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public DayFood getDayFood() {
        return dayFood;
    }

    public void setDayFood(DayFood dayFood) {
        this.dayFood = dayFood;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }
}
