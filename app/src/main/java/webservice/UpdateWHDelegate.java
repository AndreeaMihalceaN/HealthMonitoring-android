package webservice;

/**
 * Created by Andreea on 08.12.2017.
 */

public interface UpdateWHDelegate {
    public void onUpdateDone(String result);

    void onUpdateError(String response);
}
