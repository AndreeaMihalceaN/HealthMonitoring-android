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
 * Created by Andreea on 27.12.2017.
 */

public class UpdateUserDiaryQuantityTask extends AsyncTask<String, String, String> implements CredentialInterface {
    private UpdateUserDiaryQuantityDelegate updateUserDiaryQuantityDelegate;
    private double quantity;
    private Long id;

    @Override
    protected String doInBackground(String... params) {
        try {
            return callUpdateUserDiaryQuantityService();
        } catch (IOException | JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    private String callUpdateUserDiaryQuantityService() throws IOException, JSONException {
        //String modelString = BASE_URL + "update/updateWeightHeight";
        String modelString = BASE_URL + "userdiary/updateUserDiary?quantity=" + quantity + "&id=" + id;

        Uri uri = Uri.parse(modelString).buildUpon().build();

        HttpURLConnection connection = (HttpURLConnection) new URL(uri.toString()).openConnection();

        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("Accept", "application/json");
        connection.setRequestMethod("POST");
        connection.setConnectTimeout(1000000);
        connection.setReadTimeout(1000000);

        JSONObject object = new JSONObject();
        object.put("quantity", quantity);
        object.put("id", id);
        ;


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

    public UpdateUserDiaryQuantityTask(double quantity, Long id) {

        this.quantity = quantity;
        this.id = id;

        String modelString = BASE_URL + "userdiary/updateUserDiary?quantity=" + quantity + "&id=" + id;

        Uri uri = Uri.parse(modelString).buildUpon().build();
        this.execute(uri.toString());
    }

    @Override
    protected void onPostExecute(String o) {
        super.onPostExecute(o);
        String response = String.valueOf(o);

        if (updateUserDiaryQuantityDelegate != null) {
            updateUserDiaryQuantityDelegate.onUpdateDone(response);
        }
        if (response == null) {
            updateUserDiaryQuantityDelegate.onUpdateError(response);
        }
    }

    public UpdateUserDiaryQuantityDelegate getDelegate() {
        return updateUserDiaryQuantityDelegate;
    }


    public void setUpdateUserDiaryQuantityDelegate(UpdateUserDiaryQuantityDelegate updateUserDiaryQuantityDelegate) {
        this.updateUserDiaryQuantityDelegate = updateUserDiaryQuantityDelegate;
    }
}
