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
import java.text.ParseException;

/**
 * Created by Andreea on 09.03.2018.
 */

public class SearchDailyStatistics2Task extends AsyncTask<String, String, String> implements CredentialInterface {

    private SearchDailyStatistics2Delegate searchDailyStatistics2Delegate;
    private Long userId;
    private Long dayId;

    @Override
    protected String doInBackground(String... params) {
        try {
            return callSearchDailyStatistics2Service();
        } catch (IOException | JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    private String callSearchDailyStatistics2Service() throws IOException, JSONException {
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

    public SearchDailyStatistics2Task(Long userId, Long dayId) {

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

        if (searchDailyStatistics2Delegate != null) {
            try {
                searchDailyStatistics2Delegate.onSearchDailyStatistics2Done(response);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    public SearchDailyStatistics2Delegate getDelegate() {
        return searchDailyStatistics2Delegate;
    }

    public void setSearchDailyStatistics2Delegate(SearchDailyStatistics2Delegate searchDailyStatistics2Delegate) {
        this.searchDailyStatistics2Delegate = searchDailyStatistics2Delegate;
    }
}
