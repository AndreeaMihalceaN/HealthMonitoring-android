package webservice;

/**
 * Created by Andreea on 30.10.2017.
 */

public interface RegisterDelegate {
    public void onRegisterDone(String result);

    void onRegisterError(String response);
}
