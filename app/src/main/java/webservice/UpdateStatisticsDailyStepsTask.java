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
 * Created by Andreea on 25.02.2018.
 */

public class UpdateStatisticsDailyStepsTask extends AsyncTask<String, String, String> implements CredentialInterface {
    private UpdateStatisticsDailyStepsDelegate updateStatisticsDailyStepsDelegate;
    private Long userId;
    private Long dayId;
    private double steps;

    @Override
    protected String doInBackground(String... params) {
        try {
            return callUpdateStatisticsDailyStepsService();
        } catch (IOException | JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    private String callUpdateStatisticsDailyStepsService() throws IOException, JSONException {
        String modelString = BASE_URL + "dailyStatistics/updateDailyStatisticsSteps?steps=" + steps + "&userId=" + userId + "&dayId=" + dayId;

        Uri uri = Uri.parse(modelString).buildUpon().build();

        HttpURLConnection connection = (HttpURLConnection) new URL(uri.toString()).openConnection();

        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("Accept", "application/json");
        connection.setRequestMethod("POST");
        connection.setConnectTimeout(1000000);
        connection.setReadTimeout(1000000);

        JSONObject object = new JSONObject();
        object.put("userId", userId);
        object.put("dayId", dayId);
        object.put("steps", steps);


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

    public UpdateStatisticsDailyStepsTask(Long userId, Long dayId, double steps) {

        this.userId = userId;
        this.dayId = dayId;
        this.steps = steps;
        String modelString = BASE_URL + "dailyStatistics/updateDailyStatisticsSteps?steps=" + steps + "&userId=" + userId + "&dayId=" + dayId;
        Uri uri = Uri.parse(modelString).buildUpon().build();
        this.execute(uri.toString());
    }

    @Override
    protected void onPostExecute(String o) {
        super.onPostExecute(o);
        String response = String.valueOf(o);

        if (updateStatisticsDailyStepsDelegate != null) {
            updateStatisticsDailyStepsDelegate.onUpdateStatisticsDailyStepsDone(response);
        }
        if (response == null) {
            updateStatisticsDailyStepsDelegate.onUpdateStatisticsDailyStepsError(response);
        }
    }

    public UpdateStatisticsDailyStepsDelegate getDelegate() {
        return updateStatisticsDailyStepsDelegate;
    }


    public void setUpdateStatisticsDailyStepsDelegate(UpdateStatisticsDailyStepsDelegate updateStatisticsDailyStepsDelegate) {
        this.updateStatisticsDailyStepsDelegate = updateStatisticsDailyStepsDelegate;
    }

}
