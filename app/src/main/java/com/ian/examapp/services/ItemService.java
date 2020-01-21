package com.ian.examapp.services;

import com.ian.examapp.model.Item;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;

public interface ItemService
{
    @GET("products")
    @Headers("Content-Type: application/json")
    Call<List<Item>> getAvailableItems();
}
