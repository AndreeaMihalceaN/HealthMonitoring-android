package webservice;

import java.io.UnsupportedEncodingException;

/**
 * Created by Andreea on 27.12.2017.
 */

public interface GetUserDiaryDelegate {
    public void onGetUserDiaryDone(String result) throws UnsupportedEncodingException;
}
