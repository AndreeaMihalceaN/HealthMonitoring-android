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
 * Created by Andreea on 30.10.2017.
 */

public class RegisterTask extends AsyncTask<String, String, String> implements CredentialInterface {

    private RegisterDelegate registerDelegate;
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private String gender;
    private int height;
    private int weight;
    private int age;
    private String email;
    private String contactNo;
    private double stepsObjective;


    @Override
    protected String doInBackground(String... params) {
        try {
            return callRegisterService();
        } catch (IOException | JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    private String callRegisterService() throws IOException, JSONException {

        String modelString = BASE_URL + "register/add?username=" + username + "&password=" + password + "&firstName=" + firstName + "&lastName=" + lastName + "&gender=" + gender + "&height=" + height + "&weight=" + weight + "&age=" + 0 + "&email=" + "" + "&contactNo=" + ""+"$stepsObjective="+0;

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
        object.put("firstName", firstName);
        object.put("lastName", lastName);
        object.put("gender", gender);
        object.put("height", height);
        object.put("weight", weight);
        object.put("age", age);
        object.put("email", email);
        object.put("contactNo", contactNo);
        object.put("stepsObjective", stepsObjective);


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

    public RegisterTask(String username, String password, String firstName, String lastName, String gender, int height, int weight, int age, String email, String contactNo, double stepsObjective) {

        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.height = height;
        this.weight = weight;
        this.age = age;
        this.email = email;
        this.contactNo = contactNo;
        this.stepsObjective=stepsObjective;


        String modelString = BASE_URL + "register/add?username=" + username + "&password=" + password + "&firstName=" + firstName + "&lastName=" + lastName + "&gender=" + gender + "&height=" + height + "&weight=" + weight + "&age=" + 0 + "&email=" + "" + "&contactNo=" + ""+"&stepsObjective="+stepsObjective;

        Uri uri = Uri.parse(modelString).buildUpon().build();
        this.execute(uri.toString());
    }

    @Override
    protected void onPostExecute(String o) {
        super.onPostExecute(o);
        String response = String.valueOf(o);

        if (registerDelegate != null) {
            registerDelegate.onRegisterDone(response);
        }
        if (response == null) {
            registerDelegate.onRegisterError(response);
        }
    }

    public RegisterDelegate getDelegate() {
        return registerDelegate;
    }


    public void setRegisterDelegate(RegisterDelegate registerDelegate) {
        this.registerDelegate = registerDelegate;
    }
}
