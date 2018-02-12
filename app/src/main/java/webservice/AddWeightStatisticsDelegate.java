package webservice;

/**
 * Created by Andreea on 12.02.2018.
 */

public interface AddWeightStatisticsDelegate {
    public void onAddWeightStatisticsDone(String result);

    void onAddWeightStatisticsError(String response);
}
