package webservice;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;

/**
 * Created by Andreea on 09.03.2018.
 */

public interface SearchDailyStatistics2Delegate {
    public void onSearchDailyStatistics2Done(String result) throws UnsupportedEncodingException, ParseException;
}
