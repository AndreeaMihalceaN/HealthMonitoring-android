package webservice;

/**
 * Created by Andreea on 14.01.2018.
 */

public interface DeleteDayFoodDelegate {
    public void onDeleteDayFoodDone(String result);

    void onDeleteDayFoodError(String response);
}
