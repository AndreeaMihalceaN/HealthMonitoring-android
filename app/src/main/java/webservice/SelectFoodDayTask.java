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
 * Created by Andreea on 30.12.2017.
 */

public class SelectFoodDayTask extends AsyncTask<String, String, String> implements CredentialInterface {

    private SelectFoodDayDelegate selectFoodDayDelegate;
    private String dateString;
    private String foodname;

    @Override
    protected String doInBackground(String... params) {
        try {
            return callSelectFoodDayService();
        } catch (IOException | JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    private String callSelectFoodDayService() throws IOException, JSONException {
        String modelString = BASE_URL + "day_food/searchDayFood?dateString=" + dateString + "&foodname=" + foodname;
        Uri uri = Uri.parse(modelString).buildUpon().build();
        //Uri uri = Uri.parse(BASE_URL).buildUpon().appendPath("day/searchDay").build();
        HttpURLConnection connection = (HttpURLConnection) new URL(uri.toString()).openConnection();

        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("Accept", "application/json");
        connection.setRequestMethod("POST");
        connection.setConnectTimeout(1000000);
        connection.setReadTimeout(1000000);

        JSONObject object = new JSONObject();
        object.put("dateString", dateString);
        object.put("foodname", foodname);

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

    public SelectFoodDayTask(String dateString, String foodname) {

        this.dateString = dateString;
        this.foodname = foodname;

        String modelString = BASE_URL + "day_food/searchDayFood?dateString=" + dateString + "&foodname=" + foodname;
        Uri uri = Uri.parse(modelString).buildUpon().build();
        //Uri uri = Uri.parse(BASE_URL).buildUpon().appendPath("day/searchDay").build();
        this.execute(uri.toString());
    }

    @Override
    protected void onPostExecute(String o) {
        super.onPostExecute(o);
        String response = String.valueOf(o);

        if (selectFoodDayDelegate != null) {
            try {
                selectFoodDayDelegate.onSelectFoodDayDone(response);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
    }

    public SelectFoodDayDelegate getDelegate() {
        return selectFoodDayDelegate;
    }

    public void setSelectFoodDayDelegate(SelectFoodDayDelegate selectFoodDayDelegate) {
        this.selectFoodDayDelegate = selectFoodDayDelegate;
    }
}
