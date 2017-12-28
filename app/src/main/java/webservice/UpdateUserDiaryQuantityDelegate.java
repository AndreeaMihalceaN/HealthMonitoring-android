package webservice;

/**
 * Created by Andreea on 27.12.2017.
 */

public interface UpdateUserDiaryQuantityDelegate {
    public void onUpdateDone(String result);

    void onUpdateError(String response);
}
