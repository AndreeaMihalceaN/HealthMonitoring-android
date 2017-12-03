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
 * Created by Andreea on 27.11.2017.
 */

public class RegisterDayFoodTask extends AsyncTask<String, String, String> implements CredentialInterface {
    private RegisterDayFoodDelegate registerDayFoodDelegate;
    private String dateString;
    private String nameFood;

    @Override
    protected String doInBackground(String... params) {
        try {
            return callRegisterDayFoodService();
        } catch (IOException | JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    private String callRegisterDayFoodService() throws IOException, JSONException {

        String modelString = BASE_URL + "day_food/addByDateStringAndFoodName?"+"dateString="+dateString+"&nameFood="+nameFood;

        Uri uri = Uri.parse(modelString).buildUpon().build();

        HttpURLConnection connection = (HttpURLConnection) new URL(uri.toString()).openConnection();

        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("Accept", "application/json");
        connection.setRequestMethod("POST");
        connection.setConnectTimeout(1000000);
        connection.setReadTimeout(1000000);

        JSONObject object = new JSONObject();
        object.put("dateString", dateString);
        object.put("nameFood", nameFood);

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

    public RegisterDayFoodTask(String dateString, String nameFood) {

        this.dateString=dateString;
        this.nameFood=nameFood;

        String modelString = BASE_URL + "day_food/addByDateStringAndFoodName?"+"dateString="+dateString+"&nameFood="+nameFood;

        Uri uri = Uri.parse(modelString).buildUpon().build();
        this.execute(uri.toString());
    }

    @Override
    protected void onPostExecute(String o) {
        super.onPostExecute(o);
        String response = String.valueOf(o);

        if (registerDayFoodDelegate != null) {
            registerDayFoodDelegate.onRegisterDayFoodDone(response);
        }
        if (response == null) {
            registerDayFoodDelegate.onRegisterDayFoodError(response);
        }
    }

    public RegisterDayFoodDelegate getDelegate() {
        return registerDayFoodDelegate;
    }


    public void setRegisterDayFoodDelegate(RegisterDayFoodDelegate registerDayFoodDelegate) {
        this.registerDayFoodDelegate = registerDayFoodDelegate;
    }
}
