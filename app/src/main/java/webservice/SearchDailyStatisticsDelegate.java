package webservice;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;

/**
 * Created by Andreea on 12.02.2018.
 */

public interface SearchDailyStatisticsDelegate {
    public void onSearchDailyStatisticsDone(String result) throws UnsupportedEncodingException, ParseException;
}
