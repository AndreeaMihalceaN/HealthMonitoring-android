package webservice;

/**
 * Created by Andreea on 25.02.2018.
 */

public interface UpdateStatisticsDailyStepsDelegate {
    public void onUpdateStatisticsDailyStepsDone(String result);

    void onUpdateStatisticsDailyStepsError(String response);
}
