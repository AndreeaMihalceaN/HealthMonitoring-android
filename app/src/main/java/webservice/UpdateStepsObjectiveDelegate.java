package webservice;

/**
 * Created by Andreea on 14.03.2018.
 */

public interface UpdateStepsObjectiveDelegate {
    public void onUpdateStepsObjectiveDone(String result);

    void onUpdateStepsObjectiveError(String response);
}
