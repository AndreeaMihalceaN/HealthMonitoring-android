package webservice;

import android.os.AsyncTask;

import java.io.UnsupportedEncodingException;

/**
 * Created by Andreea on 14.11.2017.
 */

public interface SelectFoodDelegate {
    public void onSelectFoodDone(String result) throws UnsupportedEncodingException;
}
