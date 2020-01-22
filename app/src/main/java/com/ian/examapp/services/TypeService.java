package com.ian.examapp.services;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;

public interface TypeService
{
    @GET("types")
    @Headers("Content-Type: application/json")
    Call<List<String>> getTypes();
}
