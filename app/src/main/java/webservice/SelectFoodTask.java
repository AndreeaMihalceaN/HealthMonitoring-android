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

import manager.DataManager;

/**
 * Created by Andreea on 14.11.2017.
 */

public class SelectFoodTask extends AsyncTask<String, String, String> implements CredentialInterface {

    private SelectFoodDelegate selectFoodDelegate;
    private String username;
    private String password;

    @Override
    protected String doInBackground(String... params) {
        try {
            return callSelectFoodService();
        } catch (IOException | JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    private String callSelectFoodService() throws IOException, JSONException {
        String modelString = BASE_URL + "food/all";
        Uri uri = Uri.parse(modelString).buildUpon().build();
        //Uri uri = Uri.parse(BASE_URL).buildUpon().appendPath("food/all").build();
        HttpURLConnection connection = (HttpURLConnection) new URL(uri.toString()).openConnection();

        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("Accept", "application/json");
        connection.setRequestMethod("GET");
        connection.setConnectTimeout(1000000);
        connection.setReadTimeout(1000000);

        connection.addRequestProperty("Authorization", DataManager.getInstance().getBaseAuthStr());


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

    public SelectFoodTask(String username, String password) {

        this.username = username;
        this.password = password;

        String modelString = BASE_URL + "food/all";
        Uri uri = Uri.parse(modelString).buildUpon().build();
        //Uri uri = Uri.parse(BASE_URL).buildUpon().appendPath("food/all").build();
        this.execute(uri.toString());
    }

    @Override
    protected void onPostExecute(String o) {
        super.onPostExecute(o);
        String response = String.valueOf(o);

        if (selectFoodDelegate != null) {
            try {
                selectFoodDelegate.onSelectFoodDone(response);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
    }

    public SelectFoodDelegate getDelegate() {
        return selectFoodDelegate;
    }

    public void setSelectFoodDelegate(SelectFoodDelegate selectFoodDelegate) {
        this.selectFoodDelegate = selectFoodDelegate;
    }
}
