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
 * Created by Andreea on 08.01.2018.
 */

public class UpdateAutentificationTask extends AsyncTask<String, String, String> implements CredentialInterface {
    private UpdateAutentificationDelegate updateAutentificationDelegate;
    private String username;
    private String password;
    private String newPassword;
    private String newUsername;

    @Override
    protected String doInBackground(String... params) {
        try {
            return callUpdateAutentificationService();
        } catch (IOException | JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    private String callUpdateAutentificationService() throws IOException, JSONException {
        String modelString = BASE_URL + "update/updateAutentificationDates?username=" + username + "&password=" + password + "&newUsername=" + newUsername + "&newPassword=" + newPassword;

        Uri uri = Uri.parse(modelString).buildUpon().build();

        HttpURLConnection connection = (HttpURLConnection) new URL(uri.toString()).openConnection();

        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("Accept", "application/json");
        connection.setRequestMethod("POST");
        connection.setConnectTimeout(1000000);
        connection.setReadTimeout(1000000);

        JSONObject object = new JSONObject();
        object.put("username", username);
        object.put("password", password);
        object.put("newUsername", newUsername);
        object.put("newPassword", newPassword);


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

    public UpdateAutentificationTask(String username, String password, String newUsername, String newPassword) {

        this.username = username;
        this.password = password;
        this.newUsername = newUsername;
        this.newPassword = newPassword;

        String modelString = BASE_URL + "update/updateAutentificationDates?username=" + username + "&password=" + password + "&newUsername=" + newUsername + "&newPassword=" + newPassword;

        Uri uri = Uri.parse(modelString).buildUpon().build();
        this.execute(uri.toString());
    }

    @Override
    protected void onPostExecute(String o) {
        super.onPostExecute(o);
        String response = String.valueOf(o);

        if (updateAutentificationDelegate != null) {
            updateAutentificationDelegate.onUpdateDone(response);
        }
        if (response == null) {
            updateAutentificationDelegate.onUpdateError(response);
        }
    }

    public UpdateAutentificationDelegate getDelegate() {
        return updateAutentificationDelegate;
    }


    public void setUpdateAutentificationDelegate(UpdateAutentificationDelegate updateAutentificationDelegate) {
        this.updateAutentificationDelegate = updateAutentificationDelegate;
    }
}
