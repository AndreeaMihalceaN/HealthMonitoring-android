package webservice;

import java.io.UnsupportedEncodingException;

/**
 * Created by Andreea on 18.11.2017.
 */

public interface GetFoodByNameDelegate {
    public void onGetFoodByNameDone(String result) throws UnsupportedEncodingException;
}
