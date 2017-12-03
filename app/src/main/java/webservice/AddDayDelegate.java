package webservice;

/**
 * Created by Andreea on 29.11.2017.
 */

public interface AddDayDelegate {
    public void onAddDayDone(String result);

    void onAddDayError(String response);
}
