package webservice;

/**
 * Created by Andreea on 27.11.2017.
 */

public interface RegisterDayFoodDelegate {
    public void onRegisterDayFoodDone(String result);

    void onRegisterDayFoodError(String response);
}
