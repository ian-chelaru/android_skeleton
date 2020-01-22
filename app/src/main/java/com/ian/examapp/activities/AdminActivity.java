package com.ian.examapp.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.ian.examapp.R;
import com.ian.examapp.adapters.AdminItemListAdapter;
import com.ian.examapp.adapters.ItemListAdapter;
import com.ian.examapp.model.Item;
import com.ian.examapp.services.ItemService;

import org.jetbrains.annotations.NotNull;

import java.util.Comparator;
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

public class AdminActivity extends AppCompatActivity
{
    private static final int CREATE_ITEM_REQUEST = 1;

    private ItemService itemService;
    private AdminItemListAdapter itemListAdapter;
    private ACProgressFlower progressIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        RecyclerView itemsRecyclerView = findViewById(R.id.items_recycler_view);
        itemListAdapter = new AdminItemListAdapter(this);
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

        getAllServerItems();
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item)
    {
        Item itemSelected = itemListAdapter.getItemByPosition(item.getGroupId());
        switch (item.getItemId())
        {
            case R.id.delete_item:
                deleteServerItem(itemSelected);
                break;
        }

        return super.onContextItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CREATE_ITEM_REQUEST)
        {
            if (resultCode == Activity.RESULT_OK)
            {
                assert data != null;
                Item newItem = (Item) data.getSerializableExtra("new_item");
                insertServerItem(newItem);
            }
        }
    }

    private void insertServerItem(Item newItem)
    {
        progressIndicator.show();

        itemService.insertItem(newItem).enqueue(new Callback<Item>()
        {
            @Override
            public void onResponse(@NotNull Call<Item> call, @NotNull Response<Item> response)
            {
                progressIndicator.dismiss();
                if (response.isSuccessful())
                {
                    Item item = response.body();
                    assert item != null;
                    itemListAdapter.insertItem(item);
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
            public void onFailure(@NotNull Call<Item> call, @NotNull Throwable t)
            {
                progressIndicator.dismiss();
                displayToastMessage("Network error. Products can not be loaded");
            }
        });
    }

    private void getAllServerItems()
    {
        progressIndicator.show();

        itemService.getAllItems().enqueue(new Callback<List<Item>>()
        {
            @Override
            public void onResponse(@NonNull Call<List<Item>> call, @NonNull Response<List<Item>> response)
            {
                progressIndicator.dismiss();
                if (response.isSuccessful())
                {
                    List<Item> items = response.body();
                    assert items != null;
                    items.sort(Comparator.comparing(Item::getQuantity));
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
            }
        });

    }

    private void deleteServerItem(Item item)
    {
        progressIndicator.show();

        itemService.deleteItem(item.getId()).enqueue(new Callback<Item>()
        {
            @Override
            public void onResponse(@NotNull Call<Item> call, @NotNull Response<Item> response)
            {
                if (response.isSuccessful())
                {
                    progressIndicator.dismiss();
                    itemListAdapter.deleteItem(item);
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
            public void onFailure(@NotNull Call<Item> call, @NotNull Throwable t)
            {
                progressIndicator.dismiss();
                displayToastMessage("No internet connection. Delete operation not available.");
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

    public void createItem(View view)
    {
        Intent intent = new Intent(this, CreateItemActivity.class);
        startActivityForResult(intent, CREATE_ITEM_REQUEST);
    }

}
