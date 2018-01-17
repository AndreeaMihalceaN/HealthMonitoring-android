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
 * Created by Andreea on 15.01.2018.
 */

public class UpdateUserDiaryEditTask extends AsyncTask<String, String, String> implements CredentialInterface {
    private UpdateUserDiaryEditDelegate updateUserDiaryEditDelegate;
    private double quantity;
    private String dateString;
    private String foodName;
    private String username;


    @Override
    protected String doInBackground(String... params) {
        try {
            return callUpdateUserDiaryEditService();
        } catch (IOException | JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    private String callUpdateUserDiaryEditService() throws IOException, JSONException {
        //String modelString = BASE_URL + "update/updateWeightHeight";
        String modelString = BASE_URL + "userdiary/updateUserDiaryQuantity?quantity=" + quantity + "&dateString=" + dateString + "&foodName=" + foodName + "&username=" + username;

        Uri uri = Uri.parse(modelString).buildUpon().build();

        HttpURLConnection connection = (HttpURLConnection) new URL(uri.toString()).openConnection();

        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("Accept", "application/json");
        connection.setRequestMethod("POST");
        connection.setConnectTimeout(1000000);
        connection.setReadTimeout(1000000);

        JSONObject object = new JSONObject();
        object.put("quantity", quantity);
        object.put("dateString", dateString);
        object.put("foodName", foodName);
        object.put("username", username);

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

    public UpdateUserDiaryEditTask(double quantity, String dateString, String foodName, String username) {

        this.quantity = quantity;
        this.dateString = dateString;
        this.foodName = foodName;
        this.username = username;

        String modelString = BASE_URL + "userdiary/updateUserDiaryQuantity?quantity=" + quantity + "&dateString=" + dateString + "&foodName=" + foodName + "&username=" + username;

        Uri uri = Uri.parse(modelString).buildUpon().build();
        this.execute(uri.toString());
    }

    @Override
    protected void onPostExecute(String o) {
        super.onPostExecute(o);
        String response = String.valueOf(o);

        if (updateUserDiaryEditDelegate != null) {
            updateUserDiaryEditDelegate.onUpdateUserDiaryEditDone(response);
        }
        if (response == null) {
            updateUserDiaryEditDelegate.onUpdateUserDiaryEditError(response);
        }
    }

    public UpdateUserDiaryEditDelegate getDelegate() {
        return updateUserDiaryEditDelegate;
    }


    public void setUpdateUserDiaryEditDelegate(UpdateUserDiaryEditDelegate updateUserDiaryEditDelegate) {
        this.updateUserDiaryEditDelegate = updateUserDiaryEditDelegate;
    }
}
