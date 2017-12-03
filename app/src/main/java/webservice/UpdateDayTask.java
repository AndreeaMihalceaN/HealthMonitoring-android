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
import java.util.List;

import manager.DataManager;
import model.Day;
import model.Food;

/**
 * Created by Andreea on 24.11.2017.
 */

public class UpdateDayTask extends AsyncTask<String, String, String> implements CredentialInterface {
    private UpdateDayDelegate updateDayDelegate;
    private String date;
    private List<Food> foods;

    @Override
    protected String doInBackground(String... params) {
        try {
            return callUpdateDayService();
        } catch (IOException | JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    private String callUpdateDayService() throws IOException, JSONException {
        String modelString = BASE_URL + "day/updateDay2?date=" + date + "&foods=" + foods;

        Uri uri = Uri.parse(modelString).buildUpon().build();

        HttpURLConnection connection = (HttpURLConnection) new URL(uri.toString()).openConnection();

        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("Accept", "application/json");
        connection.setRequestMethod("POST");
        connection.setConnectTimeout(1000000);
        connection.setReadTimeout(1000000);

        JSONObject object = new JSONObject();
        object.put("date", date);
        object.put("foods", foods);


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

    public UpdateDayTask(String date, List<Food>foods) {

        this.date = date;
        this.foods=foods;
        String modelString = BASE_URL + "day/updateDay2?date=" + date + "&foods=" + foods;
        Uri uri = Uri.parse(modelString).buildUpon().build();
        this.execute(uri.toString());
    }

    @Override
    protected void onPostExecute(String o) {
        super.onPostExecute(o);
        String response = String.valueOf(o);

        if (updateDayDelegate != null) {
            updateDayDelegate.onUpdateDayDone(response);
        }
        if (response == null) {
            updateDayDelegate.onUpdateDayError(response);
        }
    }

    public UpdateDayDelegate getDelegate() {
        return updateDayDelegate;
    }


    public void setUpdateDayDelegate(UpdateDayDelegate updateDayDelegate) {
        this.updateDayDelegate = updateDayDelegate;
    }
}
