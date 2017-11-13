package webservice;

/**
 * Created by Andreea on 08.11.2017.
 */

public interface RegisterFoodDelegate {
    public void onRegisterFoodDone(String result);

    void onRegisterFoodError(String response);
}
