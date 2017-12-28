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
 * Created by Andreea on 27.12.2017.
 */

public class SelectUserTask extends AsyncTask<String, String, String> implements CredentialInterface {
    private SelectUserDelegate selectUserDelegate;
    private String username;
    private String firstName;
    private String lastName;
    private String password;
    private String gender;
    private String email;


    @Override
    protected String doInBackground(String... params) {
        try {
            return callSelectUserService();
        } catch (IOException | JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    private String callSelectUserService() throws IOException, JSONException {
        String modelString = BASE_URL + "register/searchUser?username="+username+"&firstName="+firstName+"&lastName="+lastName+"&password="+password+"&gender="+gender+"&email="+email;
        Uri uri = Uri.parse(modelString).buildUpon().build();
        //Uri uri = Uri.parse(BASE_URL).buildUpon().appendPath("food/all").build();
        HttpURLConnection connection = (HttpURLConnection) new URL(uri.toString()).openConnection();

        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("Accept", "application/json");
        connection.setRequestMethod("POST");
        connection.setConnectTimeout(1000000);
        connection.setReadTimeout(1000000);

        JSONObject object = new JSONObject();
        object.put("username", username);
        object.put("firstName", firstName);
        object.put("lastName", lastName);
        object.put("password", password);
        object.put("gender", gender);
        object.put("email", email);


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

    public SelectUserTask(String username, String firstName, String lastName, String password, String gender, String email) {

        this.username=username;
        this.firstName=firstName;
        this.lastName=lastName;
        this.password=password;
        this.gender=gender;
        this.email=email;
        String modelString = BASE_URL + "register/searchUser?username="+username+"&firstName="+firstName+"&lastName="+lastName+"&password="+password+"&gender="+gender+"&email="+email;
        Uri uri = Uri.parse(modelString).buildUpon().build();
        //Uri uri = Uri.parse(BASE_URL).buildUpon().appendPath("food/all").build();
        this.execute(uri.toString());
    }

    @Override
    protected void onPostExecute(String o) {
        super.onPostExecute(o);
        String response = String.valueOf(o);

        if (selectUserDelegate != null) {
            try {
                selectUserDelegate.onSelectUserDone(response);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
    }

    public SelectUserDelegate getDelegate() {
        return selectUserDelegate;
    }

    public void setSelectUserDelegate(SelectUserDelegate selectUserDelegate) {
        this.selectUserDelegate = selectUserDelegate;
    }
}
