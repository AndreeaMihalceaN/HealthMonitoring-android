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
 * Created by Andreea on 08.11.2017.
 */

public class RegisterFoodTask extends AsyncTask<String, String, String> implements CredentialInterface {

    private RegisterFoodDelegate registerFoodDelegate;
    private String food_name;
    private double carbohydrates;
    private double proteins;
    private double fats;
    private String category;


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

        String modelString = BASE_URL + "food/addd?"+"food_name="+food_name+"&carbohydrates="+carbohydrates+"&proteins="+proteins+"&fats="+fats+"&category="+category;

        Uri uri = Uri.parse(modelString).buildUpon().build();

        HttpURLConnection connection = (HttpURLConnection) new URL(uri.toString()).openConnection();

        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("Accept", "application/json");
        connection.setRequestMethod("POST");
        connection.setConnectTimeout(1000000);
        connection.setReadTimeout(1000000);

        JSONObject object = new JSONObject();
        object.put("food_name", food_name);
        object.put("carbohydrates", carbohydrates);
        object.put("proteins", proteins);
        object.put("fats", fats);
        object.put("categoy", category);


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

    public RegisterFoodTask(String food_name, double carbohydrates, double proteins, double fats, String category) {

        this.food_name=food_name;
        this.carbohydrates=carbohydrates;
        this.proteins=proteins;
        this.fats=fats;
        this.category=category;


        String modelString = BASE_URL + "food/addd?"+"food_name="+food_name+"&carbohydrates="+carbohydrates+"&proteins="+proteins+"&fats="+fats+"&category="+category;

        Uri uri = Uri.parse(modelString).buildUpon().build();
        this.execute(uri.toString());
    }

    @Override
    protected void onPostExecute(String o) {
        super.onPostExecute(o);
        String response = String.valueOf(o);

        if (registerFoodDelegate != null) {
            registerFoodDelegate.onRegisterFoodDone(response);
        }
        if (response == null) {
            registerFoodDelegate.onRegisterFoodError(response);
        }
    }

    public RegisterFoodDelegate getDelegate() {
        return registerFoodDelegate;
    }


    public void setRegisterFoodDelegate(RegisterFoodDelegate registerFoodDelegate) {
        this.registerFoodDelegate = registerFoodDelegate;
    }
}
