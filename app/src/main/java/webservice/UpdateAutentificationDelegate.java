package webservice;

/**
 * Created by Andreea on 08.01.2018.
 */

public interface UpdateAutentificationDelegate {

    public void onUpdateDone(String result);

    void onUpdateError(String response);

}
