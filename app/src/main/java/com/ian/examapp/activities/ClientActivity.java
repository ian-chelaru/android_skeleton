package com.ian.examapp.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.Toast;

import com.ian.examapp.R;
import com.ian.examapp.adapters.ItemListAdapter;
import com.ian.examapp.model.Item;
import com.ian.examapp.services.ItemService;

import java.util.List;

import cc.cloudist.acplibrary.ACProgressConstant;
import cc.cloudist.acplibrary.ACProgressFlower;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ClientActivity extends AppCompatActivity
{

    private ItemService itemService;
    private ItemListAdapter itemListAdapter;
    private ACProgressFlower progressIndicator;
    private AlertDialog retryDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client);

        RecyclerView itemsRecyclerView = findViewById(R.id.items_recycler_view);
        itemListAdapter = new ItemListAdapter(this);
        itemsRecyclerView.setAdapter(itemListAdapter);
        itemsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

        // TODO change here the port
        Retrofit retrofit = new Retrofit.Builder().baseUrl("http://10.0.2.2:2024/")
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        itemService = retrofit.create(ItemService.class);

        progressIndicator = getProgressIndicator();
        retryDialog = getRetryDialog();

        getServerItems();
    }

    private void getServerItems()
    {
        progressIndicator.show();

        itemService.getAvailableItems().enqueue(new Callback<List<Item>>()
        {
            @Override
            public void onResponse(@NonNull Call<List<Item>> call, @NonNull Response<List<Item>> response)
            {
                progressIndicator.dismiss();
                if (response.isSuccessful())
                {
                    List<Item> items = response.body();
                    itemListAdapter.setItems(items);
                }
                else
                {
                    int errorStatusCode = response.code();
                    assert response.body() != null;
                    String errorMessage = response.body().toString();
                    displayToastMessage(errorStatusCode + " " + errorMessage);
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Item>> call, @NonNull Throwable t)
            {
                progressIndicator.dismiss();
                displayToastMessage("Network error. Products can not be loaded");
                retryDialog.show();
            }
        });

    }

    private void displayToastMessage(String message)
    {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    private ACProgressFlower getProgressIndicator()
    {
        return new ACProgressFlower.Builder(this)
                .direction(ACProgressConstant.DIRECT_CLOCKWISE)
                .themeColor(Color.WHITE)
                .text("Loading...")
                .fadeColor(Color.DKGRAY).build();
    }

    private AlertDialog getRetryDialog()
    {
        AlertDialog.Builder retryDialog = new AlertDialog.Builder(this);
        return retryDialog.setMessage("Please check your internet connection")
                .setPositiveButton("Retry", ((dialog, which) -> {
                    dialog.cancel();
                    getServerItems();
                }))
                .create();
    }
}
