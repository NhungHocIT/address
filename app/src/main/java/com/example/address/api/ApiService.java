package com.example.address.api;



import com.example.address.model.LocationResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {
    // https://us1.locationiq.com/v1/search.php?key=KEY&q=Hanoi&format=json
    @GET("v1/search.php")
    Call<List<LocationResponse>> searchLocation(
            @Query("key") String apiKey,
            @Query("q") String query,
            @Query("format") String format // e.g. "json"
    );
}

