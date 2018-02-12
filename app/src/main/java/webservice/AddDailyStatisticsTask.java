package webservice;

import android.net.Uri;
import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import manager.DataManager;

/**
 * Created by Andreea on 25.01.2018.
 */

public class AddDailyStatisticsTask extends AsyncTask<String, String, String> implements CredentialInterface {

    private AddDailyStatisticsDelegate addDailyStatisticsDelegate;
    private Long dayId;
    private double totalCalories;
    private Long userId;

    @Override
    protected String doInBackground(String... params) {
        try {
            return callAddDailyStatisticsService();
        } catch (IOException | JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    private String callAddDailyStatisticsService() throws IOException, JSONException {

        String modelString = BASE_URL + "dailyStatistics/add2?" + "dayId=" + dayId + "&totalCalories=" + totalCalories + "&userId=" + userId;

        Uri uri = Uri.parse(modelString).buildUpon().build();

        HttpURLConnection connection = (HttpURLConnection) new URL(uri.toString()).openConnection();

        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("Accept", "application/json");
        connection.setRequestMethod("POST");
        connection.setConnectTimeout(1000000);
        connection.setReadTimeout(1000000);

        JSONObject object = new JSONObject();
        object.put("dayId", dayId);
        object.put("totalCalories", totalCalories);
        object.put("userId", userId);

        connection.addRequestProperty("Authorization", DataManager.getInstance().getBaseAuthStr());

        OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream());
        out.write(object.toString());
        out.close();

        StringBuilder sb = new StringBuilder();
        int httpResult = connection.getResponseCode();
        if (httpResult == HttpURLConnection.HTTP_OK || httpResult == HttpURLConnection.HTTP_CREATED || httpResult == HttpURLConnection.HTTP_CLIENT_TIMEOUT) {
            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"));
            String line = null;
            while ((line = br.readLine()) != null) {
                sb.append(line + "\n");
            }
            br.close();
            System.out.println("" + sb.toString());
        } else {
            System.out.println(connection.getResponseMessage());
        }
        return sb.toString();
    }

    public AddDailyStatisticsTask(Long dayId, double totalCalories, Long userId) {

        this.dayId = dayId;
        this.totalCalories = totalCalories;
        this.userId = userId;

        String modelString = BASE_URL + "dailyStatistics/add2?" + "dayId=" + dayId + "&totalCalories=" + totalCalories + "&userId=" + userId;

        Uri uri = Uri.parse(modelString).buildUpon().build();
        this.execute(uri.toString());
    }

    @Override
    protected void onPostExecute(String o) {
        super.onPostExecute(o);
        String response = String.valueOf(o);

        if (addDailyStatisticsDelegate != null) {
            addDailyStatisticsDelegate.onAddDailyStatisticsDone(response);
        }
        if (response == null) {
            addDailyStatisticsDelegate.onAddDailyStatisticsError(response);
        }
    }

    public AddDailyStatisticsDelegate getDelegate() {
        return addDailyStatisticsDelegate;
    }


    public void setAddDailyStatisticsDelegate(AddDailyStatisticsDelegate addDailyStatisticsDelegate) {
        this.addDailyStatisticsDelegate = addDailyStatisticsDelegate;
    }

}
