package webservice;

/**
 * Created by Andreea on 24.11.2017.
 */

public interface UpdateDayDelegate {

    public void onUpdateDayDone(String result);

    void onUpdateDayError(String response);

}
