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
 * Created by Andreea on 13.01.2018.
 */

public class SearchFoodByIdTask extends AsyncTask<String, String, String> implements CredentialInterface {

    private SearchFoodByIdDelegate searchFoodByIdDelegate;
    private Long id;

    @Override
    protected String doInBackground(String... params) {
        try {
            return callSearchFoodByIdService();
        } catch (IOException | JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    private String callSearchFoodByIdService() throws IOException, JSONException {
        String modelString = BASE_URL + "food/searchFoodById?id="+id;
        Uri uri = Uri.parse(modelString).buildUpon().build();
        //Uri uri = Uri.parse(BASE_URL).buildUpon().appendPath("food/all").build();
        HttpURLConnection connection = (HttpURLConnection) new URL(uri.toString()).openConnection();

        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("Accept", "application/json");
        connection.setRequestMethod("POST");
        connection.setConnectTimeout(1000000);
        connection.setReadTimeout(1000000);

        JSONObject object = new JSONObject();
        object.put("id", id);


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

    public SearchFoodByIdTask(Long id) {

        this.id=id;
        String modelString = BASE_URL + "food/searchFoodById?id="+id;
        Uri uri = Uri.parse(modelString).buildUpon().build();
        //Uri uri = Uri.parse(BASE_URL).buildUpon().appendPath("food/all").build();
        this.execute(uri.toString());
    }

    @Override
    protected void onPostExecute(String o) {
        super.onPostExecute(o);
        String response = String.valueOf(o);

        if (searchFoodByIdDelegate != null) {
            try {
                searchFoodByIdDelegate.onSearchFoodByIdDone(response);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
    }

    public SearchFoodByIdDelegate getDelegate() {
        return searchFoodByIdDelegate;
    }

    public void setSearchFoodByIdDelegate(SearchFoodByIdDelegate searchFoodByIdDelegate) {
        this.searchFoodByIdDelegate = searchFoodByIdDelegate;
    }
}
