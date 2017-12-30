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

public class SelectAnUserDiaryTask extends AsyncTask<String, String, String> implements CredentialInterface {

    private SelectAnUserDiaryDelegate selectAnUserDiaryDelegate;
    private Long idDayFood;
    private String username;

    @Override
    protected String doInBackground(String... params) {
        try {
            return callSelectAnUserDiaryService();
        } catch (IOException | JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    private String callSelectAnUserDiaryService() throws IOException, JSONException {
        String modelString = BASE_URL + "userdiary/searchUserDiary2?idDayFood=" + idDayFood + "&username=" + username;
        Uri uri = Uri.parse(modelString).buildUpon().build();
        //Uri uri = Uri.parse(BASE_URL).buildUpon().appendPath("food/all").build();
        HttpURLConnection connection = (HttpURLConnection) new URL(uri.toString()).openConnection();

        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("Accept", "application/json");
        connection.setRequestMethod("POST");
        connection.setConnectTimeout(1000000);
        connection.setReadTimeout(1000000);

        JSONObject object = new JSONObject();
        object.put("idDayFood", idDayFood);
        object.put("username", username);


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

    public SelectAnUserDiaryTask(Long idDayFood, String username) {

        this.idDayFood = idDayFood;
        this.username = username;

        String modelString = BASE_URL + "userdiary/searchUserDiary2?idDayFood=" + idDayFood + "&username=" + username;
        Uri uri = Uri.parse(modelString).buildUpon().build();
        //Uri uri = Uri.parse(BASE_URL).buildUpon().appendPath("food/all").build();
        this.execute(uri.toString());
    }

    @Override
    protected void onPostExecute(String o) {
        super.onPostExecute(o);
        String response = String.valueOf(o);

        if (selectAnUserDiaryDelegate != null) {
            try {
                selectAnUserDiaryDelegate.onSelectAnUserDiaryDone(response);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
    }

    public SelectAnUserDiaryDelegate getDelegate() {
        return selectAnUserDiaryDelegate;
    }

    public void setSelectAnUserDiaryDelegate(SelectAnUserDiaryDelegate selectAnUserDiaryDelegate) {
        this.selectAnUserDiaryDelegate = selectAnUserDiaryDelegate;
    }
}
