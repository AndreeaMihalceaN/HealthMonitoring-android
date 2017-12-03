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
 * Created by Andreea on 29.11.2017.
 */

public class SelectFoodFromCurrentDayTask extends AsyncTask<String, String, String> implements CredentialInterface  {

    private SelectFoodFromCurrentDayDelegate selectFoodFromCurrentDayDelegate;
    private String dateString;

    @Override
    protected String doInBackground(String... params) {
        try {
            return callSelectFoodFromCurrentDayService();
        } catch (IOException | JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    private String callSelectFoodFromCurrentDayService() throws IOException, JSONException {
        String modelString = BASE_URL + "day_food/allFoodFromThisDay?dateString="+dateString;
        Uri uri = Uri.parse(modelString).buildUpon().build();
        //Uri uri = Uri.parse(BASE_URL).buildUpon().appendPath("food/all").build();
        HttpURLConnection connection = (HttpURLConnection) new URL(uri.toString()).openConnection();

        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("Accept", "application/json");
        connection.setRequestMethod("POST");
        connection.setConnectTimeout(1000000);
        connection.setReadTimeout(1000000);

        JSONObject object = new JSONObject();
        object.put("dateString", dateString);


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

    public SelectFoodFromCurrentDayTask(String dateString) {

        this.dateString=dateString;

        String modelString = BASE_URL + "day_food/allFoodFromThisDay?dateString="+dateString;
        Uri uri = Uri.parse(modelString).buildUpon().build();
        //Uri uri = Uri.parse(BASE_URL).buildUpon().appendPath("food/all").build();
        this.execute(uri.toString());
    }

    @Override
    protected void onPostExecute(String o) {
        super.onPostExecute(o);
        String response = String.valueOf(o);

        if (selectFoodFromCurrentDayDelegate != null) {
            try {
                selectFoodFromCurrentDayDelegate.onSelectFoodFromCurrentDayDone(response);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
    }

    public SelectFoodFromCurrentDayDelegate getDelegate() {
        return selectFoodFromCurrentDayDelegate;
    }

    public void setSelectFoodFromCurrentDayDelegate(SelectFoodFromCurrentDayDelegate selectFoodFromCurrentDayDelegate) {
        this.selectFoodFromCurrentDayDelegate = selectFoodFromCurrentDayDelegate;
    }
}
