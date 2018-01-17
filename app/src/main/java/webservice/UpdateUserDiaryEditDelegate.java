package webservice;

/**
 * Created by Andreea on 15.01.2018.
 */

public interface UpdateUserDiaryEditDelegate {

    public void onUpdateUserDiaryEditDone(String result);

    void onUpdateUserDiaryEditError(String response);
}
