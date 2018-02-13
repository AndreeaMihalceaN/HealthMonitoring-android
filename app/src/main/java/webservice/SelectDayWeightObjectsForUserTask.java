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
 * Created by Andreea on 13.02.2018.
 */

public class SelectDayWeightObjectsForUserTask extends AsyncTask<String, String, String> implements CredentialInterface {

    private SelectDayWeightObjectsForUserDelegate selectDayWeightObjectsForUserDelegate;
    private Long userId;

    @Override
    protected String doInBackground(String... params) {
        try {
            return callSelectDayWeightObjectsForUserService();
        } catch (IOException | JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    private String callSelectDayWeightObjectsForUserService() throws IOException, JSONException {
        String modelString = BASE_URL + "weightStatistics/dayWeightForUser?userId=" + userId;
        Uri uri = Uri.parse(modelString).buildUpon().build();
        //Uri uri = Uri.parse(BASE_URL).buildUpon().appendPath("food/all").build();
        HttpURLConnection connection = (HttpURLConnection) new URL(uri.toString()).openConnection();

        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("Accept", "application/json");
        connection.setRequestMethod("POST");
        connection.setConnectTimeout(1000000);
        connection.setReadTimeout(1000000);

        JSONObject object = new JSONObject();
        object.put("userId", userId);


        OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream());
        out.write(object.toString());
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

    public SelectDayWeightObjectsForUserTask(Long userId) {

        this.userId = userId;

        String modelString = BASE_URL + "weightStatistics/dayWeightForUser?userId=" + userId;
        Uri uri = Uri.parse(modelString).buildUpon().build();
        //Uri uri = Uri.parse(BASE_URL).buildUpon().appendPath("food/all").build();
        this.execute(uri.toString());
    }

    @Override
    protected void onPostExecute(String o) {
        super.onPostExecute(o);
        String response = String.valueOf(o);

        if (selectDayWeightObjectsForUserDelegate != null) {
            try {
                selectDayWeightObjectsForUserDelegate.onSelectDayWeightObjectsForUserDone(response);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
    }

    public SelectDayWeightObjectsForUserDelegate getDelegate() {
        return selectDayWeightObjectsForUserDelegate;
    }

    public void setSelectDayWeightObjectsForUserDelegate(SelectDayWeightObjectsForUserDelegate selectDayWeightObjectsForUserDelegate) {
        this.selectDayWeightObjectsForUserDelegate = selectDayWeightObjectsForUserDelegate;
    }
}
