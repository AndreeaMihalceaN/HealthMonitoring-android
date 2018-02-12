package webservice;

/**
 * Created by Andreea on 25.01.2018.
 */

public interface AddDailyStatisticsDelegate {
    public void onAddDailyStatisticsDone(String result);

    void onAddDailyStatisticsError(String response);
}
