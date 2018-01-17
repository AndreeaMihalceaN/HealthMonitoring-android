package webservice;

import java.io.UnsupportedEncodingException;

/**
 * Created by Andreea on 13.01.2018.
 */

public interface SearchFoodByIdDelegate {
    public void onSearchFoodByIdDone(String result) throws UnsupportedEncodingException;
}
