package com.ian.examapp.services;

import com.ian.examapp.model.Item;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ItemService
{
    @GET("robots/{type}")
    @Headers("Content-Type: application/json")
    Call<List<Item>> getRobotsByType(@Path("type") String type);

    @POST("robot")
    @Headers("Content-Type: application/json")
    Call<Item> insertItem(@Body Item item);

    @GET("old")
    @Headers("Content-Type: application/json")
    Call<List<Item>> getAllItems();

    @POST("age")
    @Headers("Content-Type: application/json")
    Call<Item> updateAge(@Body Item item);

    @POST("height")
    @Headers("Content-Type: application/json")
    Call<Item> updateHeight(@Body Item item);

//    @GET("products")
//    @Headers("Content-Type: application/json")
//    Call<List<Item>> getAvailableItems();
//
//    @GET("all")
//    @Headers("Content-Type: application/json")
//    Call<List<Item>> getAllItems();
//
//    @POST("product")
//    @Headers("Content-Type: application/json")
//    Call<Item> insertItem(@Body Item item);
//
//    @DELETE("product/{id}")
//    @Headers("Content-Type: application/json")
//    Call<Item> deleteItem(@Path("id") int id);
}
