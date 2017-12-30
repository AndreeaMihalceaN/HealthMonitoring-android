package webservice;

/**
 * Created by Andreea on 30.12.2017.
 */

public interface DeleteUserDiaryDelegate {
    public void onDeleteUserDiaryDone(String result);

    void onDeleteUserDiaryError(String response);
}
