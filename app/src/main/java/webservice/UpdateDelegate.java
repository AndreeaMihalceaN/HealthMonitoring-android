package webservice;

/**
 * Created by Andreea on 07.11.2017.
 */

public interface UpdateDelegate {

    public void onUpdateDone(String result);

    void onUpdateError(String response);


}
