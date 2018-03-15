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
 * Created by Andreea on 09.03.2018.
 */

public class SearchDay2Task extends AsyncTask<String, String, String> implements CredentialInterface {

    private SearchDay2Delegate searchDay2Delegate;
    private String dateString;

    @Override
    protected String doInBackground(String... params) {
        try {
            return callSerachDay2Service();
        } catch (IOException | JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    private String callSerachDay2Service() throws IOException, JSONException {
        String modelString = BASE_URL + "day/searchDayString?dateString=" + dateString;
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

    public SearchDay2Task(String dateString) {

        this.dateString = dateString;

        String modelString = BASE_URL + "day/searchDayString?dateString=" + dateString;
        Uri uri = Uri.parse(modelString).buildUpon().build();
        //Uri uri = Uri.parse(BASE_URL).buildUpon().appendPath("day/searchDay").build();
        this.execute(uri.toString());
    }

    @Override
    protected void onPostExecute(String o) {
        super.onPostExecute(o);
        String response = String.valueOf(o);

        if (searchDay2Delegate != null) {
            try {
                searchDay2Delegate.onSearchDay2Done(response);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
    }

    public SearchDay2Delegate getDelegate() {
        return searchDay2Delegate;
    }

    public void setSearchDay2Delegate(SearchDay2Delegate searchDay2Delegate) {
        this.searchDay2Delegate = searchDay2Delegate;
    }
}
