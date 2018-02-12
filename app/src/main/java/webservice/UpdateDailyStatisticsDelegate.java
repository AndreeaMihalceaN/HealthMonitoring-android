package webservice;

/**
 * Created by Andreea on 12.02.2018.
 */

public interface UpdateDailyStatisticsDelegate {
    public void onUpdateDailyStatisticsDone(String result);

    void onUpdateDailyStatisticsError(String response);

}
