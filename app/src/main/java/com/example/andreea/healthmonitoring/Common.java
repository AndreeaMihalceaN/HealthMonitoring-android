package com.example.andreea.healthmonitoring;


import com.example.andreea.healthmonitoring.Remote.IGoogleAPIService;
import com.example.andreea.healthmonitoring.Remote.RetrofitClient;

/**
 * Created by Andreea on 14.02.2018.
 */

public class Common {
    private static final String GOOGLE_API_URL="https://maps.googleapis.com/";

    public static IGoogleAPIService getGoogleAPIService()
    {
        return RetrofitClient.getClient(GOOGLE_API_URL).create(IGoogleAPIService.class);
    }
}
