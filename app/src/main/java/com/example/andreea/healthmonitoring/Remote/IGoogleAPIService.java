package com.example.andreea.healthmonitoring.Remote;


import com.example.andreea.healthmonitoring.Model.MyPlaces;
import com.example.andreea.healthmonitoring.Model.PlaceDetail;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

/**
 * Created by Andreea on 14.02.2018.
 */

public interface IGoogleAPIService {
    @GET
    Call<MyPlaces> getNearByPlaces(@Url String url);
    @GET
    Call<PlaceDetail> getDetailPlace(@Url String url);
}
