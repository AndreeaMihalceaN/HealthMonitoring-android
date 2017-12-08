package webservice;

/**
 * Created by Andreea on 04.12.2017.
 */

public interface AddUserDiaryDelegate {
    public void onAddUserDiaryDone(String result);

    void onAddUserDiaryError(String response);
}
