package webservice;

import android.net.Uri;
import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Andreea on 12.02.2018.
 */

public class SearchDailyStatisticsTask extends AsyncTask<String, String, String> implements CredentialInterface {

    private SearchDailyStatisticsDelegate searchDailyStatisticsDelegate;
    private Long userId;
    private Long dayId;

    @Override
    protected String doInBackground(String... params) {
        try {
            return callSearchDailyStatisticsService();
        } catch (IOException | JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    private String callSearchDailyStatisticsService() throws IOException, JSONException {
        String modelString = BASE_URL + "dailyStatistics/searchDailyStatistics?userId=" + userId + "&dayId=" + dayId;
        Uri uri = Uri.parse(modelString).buildUpon().build();
        //Uri uri = Uri.parse(BASE_URL).buildUpon().appendPath("day/searchDay").build();
        HttpURLConnection connection = (HttpURLConnection) new URL(uri.toString()).openConnection();

        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("Accept", "application/json");
        connection.setRequestMethod("POST");
        connection.setConnectTimeout(1000000);
        connection.setReadTimeout(1000000);

        JSONObject object = new JSONObject();
        object.put("userId", userId);
        object.put("dayId", dayId);


        OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream());

        out.close();


        StringBuilder sb = new StringBuilder();
        int httpResult = connection.getResponseCode();
        if (httpResult == HttpURLConnection.HTTP_OK) {
            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"));
            String line = null;
            while ((line = br.readLine()) != null) {
                sb.append(line + "\n");
            }
            br.close();
            System.out.println("" + sb.toString());
        } else {
            return "";
        }
        return sb.toString();

    }

    public SearchDailyStatisticsTask(Long userId, Long dayId) {

        this.userId = userId;
        this.dayId = dayId;

        String modelString = BASE_URL + "dailyStatistics/searchDailyStatistics?userId=" + userId + "&dayId=" + dayId;
        Uri uri = Uri.parse(modelString).buildUpon().build();
        //Uri uri = Uri.parse(BASE_URL).buildUpon().appendPath("day/searchDay").build();
        this.execute(uri.toString());
    }

    @Override
    protected void onPostExecute(String o) {
        super.onPostExecute(o);
        String response = String.valueOf(o);

        if (searchDailyStatisticsDelegate != null) {
            try {
                searchDailyStatisticsDelegate.onSearchDailyStatisticsDone(response);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
    }

    public SearchDailyStatisticsDelegate getDelegate() {
        return searchDailyStatisticsDelegate;
    }

    public void setSearchDailyStatisticsDelegate(SearchDailyStatisticsDelegate searchDailyStatisticsDelegate) {
        this.searchDailyStatisticsDelegate = searchDailyStatisticsDelegate;
    }
}
