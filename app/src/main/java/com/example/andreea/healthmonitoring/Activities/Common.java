package com.example.andreea.healthmonitoring.Activities;


import com.example.andreea.healthmonitoring.Model.Results;
import com.example.andreea.healthmonitoring.Remote.IGoogleAPIService;
import com.example.andreea.healthmonitoring.Remote.RetrofitClient;

/**
 * Created by Andreea on 14.02.2018.
 */

public class Common {
    public static Results currentResult;

    private static final String GOOGLE_API_URL="https://maps.googleapis.com/";

    public static IGoogleAPIService getGoogleAPIService()
    {
        return RetrofitClient.getClient(GOOGLE_API_URL).create(IGoogleAPIService.class);
    }
}
